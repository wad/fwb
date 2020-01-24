package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.TokenSquareBracketLeft;
import com.funwithbasic.basic.token.function.TokenFunctionArrayVariable;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.Value;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.ArrayList;
import java.util.List;

public class StatementLet extends Statement {

    public static final String ASSIGNMENT_OPERATOR = "=";

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 3 + ASSIGNMENT_OPERATOR.length();

    private String statementContent;

    public StatementLet(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        statementContent = code.substring(me().getCmd().length() + 1).trim();
    }

    public static StatementType me() {
        return StatementType.LET;
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
        String[] pieces = statementContent.split(ASSIGNMENT_OPERATOR);
        if (pieces.length < 2) {
            invalid(me(), statementContent, "Missing " + ASSIGNMENT_OPERATOR);
        }
        String leftHandSide = pieces[0].trim();
        String rightHandSide = statementContent.substring(statementContent.indexOf('=') + 1).trim();

        Value value = ExpressionEvaluator.evaluateExpression(rightHandSide, getBasicRunner());

        if (leftHandSide.contains(String.valueOf(TokenSquareBracketLeft.SYMBOL))) {
            // array variable

            //get the variable name
            String[] leftSplit = leftHandSide.split("\\" + TokenSquareBracketLeft.SYMBOL);
            String variableName = leftSplit[0].trim();

            // get the index into the hash for array values
            String dimensionIndexes = leftHandSide.substring(leftSplit[0].length());
            List<Token> indexes = ExpressionEvaluator.tokenize(dimensionIndexes, getBasicRunner());
            String index = TokenFunctionArrayVariable.convertToIndex(filterForNumbers(indexes));

            if (ExpressionEvaluator.isStringVariableName(variableName)) {
                if (!(value instanceof ValueString)) {
                    invalid(me(), statementContent, "Expression evaluates to a number, not a string");
                }
                String varName = ExpressionEvaluator.removeStringIndicator(variableName);
                getBasicRunner().setStringArrayVariable(varName, index, (ValueString)value);
            }
            else {
                if (!(value instanceof ValueNumber)) {
                    invalid(me(), statementContent, "Expression evaluates to a string, not a number");
                }
                getBasicRunner().setNumberArrayVariable(variableName, index, (ValueNumber)value);
            }
        }
        else if (ExpressionEvaluator.isStringVariableName(leftHandSide)) {
            // string variable

            if (!(value instanceof ValueString)) {
                invalid(me(), statementContent, "Expression evaluates to a number, not a string");
            }

            leftHandSide = ExpressionEvaluator.removeStringIndicator(leftHandSide);
            if (!ExpressionEvaluator.isValidVariableName(leftHandSide)) {
                invalid(me(), statementContent, "Invalid string variable name: [" + leftHandSide + "]");
            }

            getBasicRunner().setStringVariable(leftHandSide, (ValueString)value);
        }
        else {
            // numeric variable

            if (!(value instanceof ValueNumber)) {
                invalid(me(), statementContent, "Expression evaluates to a string, not a number");
            }

            if (!ExpressionEvaluator.isValidVariableName(leftHandSide)) {
                invalid(me(), statementContent, "Invalid variable name: [" + leftHandSide + "]");
            }
            getBasicRunner().setNumberVariable(leftHandSide, (ValueNumber)value);
        }
    }

    List<Token> filterForNumbers(List<Token> in) {
        List<Token> result = new ArrayList<Token>();
        for (Token token : in) {
            if (token instanceof TokenValueNumber) {
                long num = ((TokenValueNumber) token).getValue().toLong();
                result.add(new TokenValueNumber(new ValueNumber(num), getBasicRunner()));
            }
        }
        return result;
    }

}
