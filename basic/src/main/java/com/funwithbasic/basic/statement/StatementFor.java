package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.Value;
import com.funwithbasic.basic.value.ValueNumber;

// Note that you can't use arrays as the index in a for statement
public class StatementFor extends Statement {

    private static final String COMMAND_TO = "TO";
    private static final String COMMAND_STEP = "STEP";
    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 3 + COMMAND_TO.length() + 2;

    private String variableName;
    private String assignment;
    private String endloopComparisonExpression;
    private String stepExpression;

    public StatementFor(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        // Statement is in this form: FOR (variable name) = (expression) TO (expression)
        // It can also be in this form: FOR (variable name) = (expression) TO (expression) STEP (expression)
        String[] pieces = code.split("\\s+");
        int numPieces = pieces.length;
        if (numPieces < 4) {
            invalid(me(), code);
        }

        // find first TO statement
        int indexOfTo = -1;
        for (int i = 0; i < numPieces; i++) {
            if (COMMAND_TO.equalsIgnoreCase(pieces[i])) {
                indexOfTo = i;
                break;
            }
        }
        if (indexOfTo == -1) {
            invalid(me(), code, "missing " + COMMAND_TO + ".");
        }
        if (indexOfTo >= numPieces - 1) {
            invalid(me(), code, "The " + COMMAND_TO + " is at the end.");
        }

        // find the first STEP statement, if it exists
        int indexOfStep = -1;
        for (int i = 0; i < numPieces; i++) {
            if (COMMAND_STEP.equalsIgnoreCase(pieces[i])) {
                indexOfStep = i;
                break;
            }
        }
        stepExpression = null;
        if (indexOfStep == -1) {
            stepExpression = "1";
        }
        if (indexOfStep >= numPieces - 1) {
            invalid(me(), code, "The " + COMMAND_STEP + " is at the end.");
        }

        // check for validity between the TO and STEP
        if (indexOfStep != -1) {
            if (indexOfStep < indexOfTo) {
                invalid(me(), code, "The " + COMMAND_STEP + " is before the " + COMMAND_TO + ".");
            }
            if (indexOfStep == indexOfTo + 1) {
                invalid(me(), code, "The " + COMMAND_STEP + " is right after the " + COMMAND_TO + ".");

            }
        }

        variableName = null;

        // capture the assignment portion
        StringBuilder builderForAssignment = new StringBuilder();
        for (int i = 1; i < indexOfTo; i++) {
            builderForAssignment.append(pieces[i]);
            builderForAssignment.append(' ');
        }
        assignment = builderForAssignment.toString().trim();

        if (stepExpression == null) {
            // there is a STEP to consider
            // capture the end loop expression
            StringBuilder builderForEndLoop = new StringBuilder();
            for (int i = indexOfTo + 1; i < indexOfStep; i++) {
                builderForEndLoop.append(pieces[i]);
                builderForEndLoop.append(' ');
            }
            endloopComparisonExpression = builderForEndLoop.toString().trim();

            // capture the step expression
            StringBuilder builderForStep = new StringBuilder();
            for (int i = indexOfStep + 1; i < pieces.length; i++) {
                builderForStep.append(pieces[i]);
                builderForStep.append(' ');
            }
            stepExpression = builderForStep.toString().trim();
        }
        else {
            // there is no STEP (it's already been set to "1")
            // capture the end loop expression
            StringBuilder builder = new StringBuilder();
            for (int i = indexOfTo + 1; i < pieces.length; i++) {
                builder.append(pieces[i]);
                builder.append(' ');
            }
            endloopComparisonExpression = builder.toString().trim();
        }
    }

    public static StatementType me() {
        return StatementType.FOR;
    }

    public static boolean isItMine(String code) {
        if (code.length() < MINIMUM_NUM_CHARACTERS) {
            return false;
        }
        String[] pieces = code.split("\\s+");
        return pieces.length > 0 && me().getCmd().equalsIgnoreCase(pieces[0]);
    }

    @Override
    public void execute() throws BasicException {
        // do the variable assignment
        String[] pieces = assignment.split(StatementLet.ASSIGNMENT_OPERATOR);
        if (pieces.length < 2) {
            invalid(me(), assignment, "Missing " + StatementLet.ASSIGNMENT_OPERATOR);
        }
        variableName = pieces[0].trim();
        String rightHandSide = assignment.substring(assignment.indexOf('=') + 1).trim();

        // check to make sure we've got a valid numeric variable name
        if (ExpressionEvaluator.isStringVariableName(variableName)) {
            invalid(me(), assignment, "Variable name must not be a string variable name");
        }

        // evaluate and set the value of the index variable
        Value value = ExpressionEvaluator.evaluateExpression(rightHandSide, getBasicRunner());
        getBasicRunner().setNumberVariable(variableName, value.asNumber());

        // store this statement so that a NEXT statement can find it
        getBasicRunner().storeForStatement(variableName, this);
    }

    public boolean isEndConditionMet() throws BasicException {
        ValueNumber endCondition = ExpressionEvaluator.evaluateExpression(endloopComparisonExpression, getBasicRunner()).asNumber();
        ValueNumber variableValue = getBasicRunner().lookupNumberVariable(variableName);
        return ValueNumber.isEqualTo(endCondition, variableValue);
    }

    public ValueNumber evaluateStepValue() throws BasicException {
        return ExpressionEvaluator.evaluateExpression(stepExpression, getBasicRunner()).asNumber();
    }

}
