package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;

public class StatementNoGraphics extends Statement {

    public StatementNoGraphics(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        if (pieces.length != 1) {
            invalid(me(), code);
        }
    }

    public static StatementType me() {
        return StatementType.NOGRAPHICS;
    }

    public static boolean isItMine(String code) {
        return me().getCmd().equalsIgnoreCase(code);
    }

    @Override
    public void execute() throws BasicException {
        getBasicRunner().getGraphicsTerminal().destroy();
    }

}
