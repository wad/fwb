package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.ValueNumber;

public class StatementIf extends Statement {
    // The IF statement can have the following forms:
    // IF (condition) THEN [GOTO|GOSUB|blank] (label)
    // IF (condition) THEN [GOTO|GOSUB|blank] (label) ELSE [GOTO|GOSUB|blank] label

    private static final String COMMAND_THEN = "THEN";
    private static final String COMMAND_ELSE = "ELSE";
    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 3 + COMMAND_THEN.length() + 2;

    private String labelIfTrue;
    private boolean trueIsGosub;
    private String labelIfFalse;
    private boolean falseIsGosub;

    private String expression;
    private boolean isFirstExecution;
    private Statement initialSubsequentStatement;

    public StatementIf(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        int numPieces = pieces.length;
        if (numPieces < 4) {
            invalid(me(), code);
        }

        // find first THEN statement
        int indexOfThen = -1;
        for (int i = 0; i < numPieces; i++) {
            if (COMMAND_THEN.equalsIgnoreCase(pieces[i])) {
                indexOfThen = i;
                break;
            }
        }
        if (indexOfThen == -1) {
            invalid(me(), code, "missing " + COMMAND_THEN + ".");
        }
        if (indexOfThen >= numPieces - 1) {
            invalid(me(), code, "The " + COMMAND_THEN + " is at the end.");
        }

        // learn what to do if the condition evaluates to TRUE or FALSE.
        int numWordsAfterThen = numPieces - indexOfThen - 1;
        switch(numWordsAfterThen) {

            // IF (condition) THEN (label)
            case 1:
                labelIfTrue = pieces[indexOfThen + 1];
                labelIfFalse = null;
                trueIsGosub = false;
                falseIsGosub = false;
                break;

            // IF (condition) THEN GOTO (label)
            // IF (condition) THEN GOSUB (label)
            case 2:
                trueIsGosub = isGosubAndVerifyGosubOrGoto(pieces[indexOfThen + 1], code);
                labelIfTrue = pieces[indexOfThen + 2];
                labelIfFalse = null;
                falseIsGosub = false;
                break;

            // IF (condition) THEN (label) ELSE (label)
            case 3:
                labelIfTrue = pieces[indexOfThen + 1];
                verifyElse(pieces[indexOfThen + 2], code);
                labelIfFalse = pieces[indexOfThen + 3];
                trueIsGosub = false;
                falseIsGosub = false;
                break;

            // IF (condition) THEN GOTO (label) ELSE (label)
            // IF (condition) THEN GOSUB (label) ELSE (label)
            // IF (condition) THEN (label) ELSE GOTO (label)
            // IF (condition) THEN (label) ELSE GOSUB (label)
            case 4:
                if (isGosubOrGoto(pieces[indexOfThen + 1])) {
                    // IF (condition) THEN GOTO (label) ELSE (label)
                    // IF (condition) THEN GOSUB (label) ELSE (label)
                    trueIsGosub = isGosubAndVerifyGosubOrGoto(pieces[indexOfThen + 1], code);
                    labelIfTrue = pieces[indexOfThen + 2];
                    verifyElse(pieces[indexOfThen + 3], code);
                    labelIfFalse = pieces[indexOfThen + 4];
                    falseIsGosub = false;
                }
                else {
                    // IF (condition) THEN (label) ELSE GOTO (label)
                    // IF (condition) THEN (label) ELSE GOSUB (label)
                    labelIfTrue = pieces[indexOfThen + 1];
                    verifyElse(pieces[indexOfThen + 2], code);
                    falseIsGosub = isGosubAndVerifyGosubOrGoto(pieces[indexOfThen + 3], code);
                    labelIfFalse = pieces[indexOfThen + 4];
                    trueIsGosub = false;
                }
                break;

            // IF (condition) THEN GOTO (label) ELSE GOTO (label)
            // IF (condition) THEN GOSUB (label) ELSE GOTO (label)
            // IF (condition) THEN GOTO (label) ELSE GOSUB (label)
            // IF (condition) THEN GOSUB (label) ELSE GOSUB (label)
            case 5:
                trueIsGosub = isGosubAndVerifyGosubOrGoto(pieces[indexOfThen + 1], code);
                labelIfTrue = pieces[indexOfThen + 2];
                verifyElse(pieces[indexOfThen + 3], code);
                falseIsGosub = isGosubAndVerifyGosubOrGoto(pieces[indexOfThen + 4], code);
                labelIfFalse = pieces[indexOfThen + 5];
                break;
            
            default:
                invalid(me(), code, "The " + COMMAND_THEN + " is at the end.");
        }

        // verify labels
        if (!ExpressionEvaluator.isValidLabelName(labelIfTrue)) {
            invalid(me(), code, "Invalid label in if: [" + labelIfTrue + "]");
        }
        if (labelIfFalse != null && !ExpressionEvaluator.isValidLabelName(labelIfFalse)) {
            invalid(me(), code, "Invalid label in if (for " + COMMAND_ELSE + "): [" + labelIfFalse + "]");
        }

        // grab the expression
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < indexOfThen; i++) {
            builder.append(pieces[i]);
            builder.append(' ');
        }
        expression = builder.toString().trim();

        isFirstExecution = true;
    }

    boolean isGosubAndVerifyGosubOrGoto(String word, String code) throws BasicException {
        if (StatementGosub.me().getCmd().equalsIgnoreCase(word)) {
            return true;
        }
        else if (StatementGoto.me().getCmd().equalsIgnoreCase(word)) {
            return false;
        }
        // throw this exception
        invalid(me(), code, "expected " + StatementGosub.me().getCmd() + " or " + StatementGoto.me().getCmd());
        return false;
    }

    boolean isGosubOrGoto(String word) {
        return StatementGosub.me().getCmd().equalsIgnoreCase(word) || StatementGoto.me().getCmd().equalsIgnoreCase(word);
    }

    void verifyElse(String word, String code) throws BasicException {
        if (!COMMAND_ELSE.equalsIgnoreCase(word)) {
            invalid(me(), code, "expected " + COMMAND_ELSE + ".");
        }
    }

    public static StatementType me() {
        return StatementType.IF;
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

        ValueNumber result = ExpressionEvaluator.evaluateExpression(expression, getBasicRunner()).asNumber();
        if (result.isTrue()) {
            Statement statement = getBasicRunner().lookupLabel(labelIfTrue);
            if (statement == null) {
                invalid(me(), expression, "label does not exist: [" + labelIfTrue + "]");
            }
            if (trueIsGosub) {
                getBasicRunner().stackPush(initialSubsequentStatement);
            }
            setSubsequentStatement(statement);
        }
        else {
            if (labelIfFalse == null) {
                setSubsequentStatement(initialSubsequentStatement);
            }
            else {
                Statement statement = getBasicRunner().lookupLabel(labelIfFalse);
                if (statement == null) {
                    invalid(me(), expression, "label does not exist (for " + COMMAND_ELSE + "): [" + labelIfFalse + "]");
                }
                if (falseIsGosub) {
                    getBasicRunner().stackPush(initialSubsequentStatement);
                }
                setSubsequentStatement(statement);
            }
        }
    }

}
