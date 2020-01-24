package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;

public class StatementReturn extends Statement {

    public StatementReturn(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        if (pieces.length != 1) {
            invalid(me(), code);
        }
    }

    public static StatementType me() {
        return StatementType.RETURN;
    }

    public static boolean isItMine(String code) {
        return me().getCmd().equalsIgnoreCase(code);
    }

    @Override
    public void execute() throws BasicException {
        setSubsequentStatement(getBasicRunner().stackPop());
    }

}
