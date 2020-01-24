package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.value.ValueNumber;

public class StatementNext extends Statement {

    // look up the for statement in the forHash for this variable name.
    // the step value is added to the variable
    // the comparison is made in the for statement
    // if true, remove the for statement from the hash, and execution proceeds with the line after the next statement
    // if false, execution proceeds with the line after the for statement

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String variableName;
    private boolean isFirstExecution;
    private Statement initialSubsequentStatement;

    public StatementNext(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        int numPieces = pieces.length;
        if (numPieces != 2) {
            invalid(me(), code);
        }

        variableName = pieces[1].trim();
        isFirstExecution = true;
    }

    public static StatementType me() {
        return StatementType.NEXT;
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
        if (isFirstExecution) {
            isFirstExecution = false;
            initialSubsequentStatement = getSubsequentStatement();
        }

        // get the FOR statement
        StatementFor forStatement = getBasicRunner().retrieveForStatement(variableName);

        // check to see if we're at the end of our loop
        if (forStatement == null) {
            setSubsequentStatement(initialSubsequentStatement);
        }
        else {
            // adjust the index variable's value
            ValueNumber variableValue = getBasicRunner().lookupNumberVariable(variableName);
            ValueNumber stepValue = forStatement.evaluateStepValue();
            ValueNumber newValue = ValueNumber.add(variableValue, stepValue);
            getBasicRunner().setNumberVariable(variableName, newValue);

            // check the comparison in the FOR statement
            if (forStatement.isEndConditionMet()) {
                getBasicRunner().removeCompletedForStatement(variableName);
            }
            else {
                setSubsequentStatement(forStatement.getSubsequentStatement());
            }
        }
    }

}
