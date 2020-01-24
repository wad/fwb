package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;

public class StatementGosub extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String destinationLabel;
    private boolean isFirstExecution;
    private Statement initialSubsequentStatement;

    public StatementGosub(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        if (pieces.length != 2) {
            invalid(me(), code);
        }

        destinationLabel = pieces[1];
        if (!ExpressionEvaluator.isValidLabelName(destinationLabel)) {
            invalid(me(), code, "Invalid label: [" + destinationLabel + "]");
        }

        isFirstExecution = true;
    }

    public static StatementType me() {
        return StatementType.GOSUB;
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

        Statement statement = getBasicRunner().lookupLabel(destinationLabel);
        if (statement == null) {
            invalid(me(), me().getCmd(), "label does not exist: [" + destinationLabel + "]");
        }
        getBasicRunner().stackPush(initialSubsequentStatement);
        setSubsequentStatement(statement);
    }

}
