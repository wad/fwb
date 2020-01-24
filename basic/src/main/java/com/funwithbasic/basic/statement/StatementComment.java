package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;

public class StatementComment extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = 1;

    public StatementComment(BasicRunner basicRunner, int lineNumberInFile) {
        super(basicRunner, lineNumberInFile);
    }

    public static StatementType me() {
        return StatementType.COMMENT;
    }

    public static boolean isItMine(String code) {
        return code.length() > MINIMUM_NUM_CHARACTERS && code.charAt(0) == me().getCmd().charAt(0);
    }

    @Override
    public void execute() throws BasicException {
    }

}
