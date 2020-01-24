package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;

public class StatementFail extends Statement {

    public StatementFail(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);
    }

    public static Statement.StatementType me() {
        return Statement.StatementType.FAIL;
    }

    public static boolean isItMine(String code) {
        return me().getCmd().equalsIgnoreCase(code);
    }

    @Override
    public void execute() throws BasicException {
        throw new BasicException("Intentional FAIL encountered");
    }

}
