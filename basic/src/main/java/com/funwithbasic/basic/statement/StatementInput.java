package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

public class StatementInput extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String variableName;

    public StatementInput(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        variableName = pieces[1].trim();
    }

    public static StatementType me() {
        return StatementType.INPUT;
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
        String userInput = getBasicRunner().getKeyboardInput().readLine();

        boolean isStringVariable = ExpressionEvaluator.isStringVariableName(variableName);
        String varName = variableName;
        if (isStringVariable) {
            varName = ExpressionEvaluator.removeStringIndicator(varName);
        }

        if (!ExpressionEvaluator.isValidVariableName(varName)) {
            invalid(me(), varName, "Invalid variable name: [" + varName + "]");
        }

        if (isStringVariable) {
            getBasicRunner().setStringVariable(varName, new ValueString(userInput, getBasicRunner().getKeyboardInput()));
        }
        else {
            getBasicRunner().setNumberVariable(varName, new ValueNumber(userInput));
        }
    }

}
