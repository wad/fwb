package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.ValueNumber;

public class StatementGetkey extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String variableName;

    public StatementGetkey(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        variableName = pieces[1].trim();
    }

    public static StatementType me() {
        return StatementType.GETKEY;
    }

    public static boolean isItMine(String code) {
        if (code.length() < MINIMUM_NUM_CHARACTERS) {
            return false;
        }
        String[] pieces = code.split("\\s+");
        return pieces.length == 2 && me().getCmd().equalsIgnoreCase(pieces[0]);
    }

    @Override
    public void execute() throws BasicException {
        int keyRead = getBasicRunner().getKeyboardInput().readKeypress();

        boolean isStringVariable = ExpressionEvaluator.isStringVariableName(variableName);
        if (isStringVariable) {
            throw new BasicException("Invalid variable type, require numeric variable");
        }

        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            invalid(me(), variableName, "Invalid variable name: [" + variableName + "]");
        }

        getBasicRunner().setNumberVariable(variableName, new ValueNumber(keyRead));
    }

}
