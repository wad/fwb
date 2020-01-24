package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.Value;
import com.funwithbasic.basic.value.ValueNumber;

// Cause the program to delay. Note that you may not be able to break execution while it's sleeping.
public class StatementSleep extends Statement {

    public static final int MAX_SLEEP_TIME = 60000;

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String argument;

    public StatementSleep(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        if (pieces.length < 2) {
            invalid(me(), code);
        }

        argument = code.substring(pieces[0].length()).trim();
    }

    public static StatementType me() {
        return StatementType.SLEEP;
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
        if (argument.length() == 0) {
            invalid(me(), "", "How many milliseconds to sleep?");
        }
        Value value = ExpressionEvaluator.evaluateExpression(argument, getBasicRunner());
        if (!(value instanceof ValueNumber)) {
            invalid(me(), argument, "Number required");
        }
        int milliseconds = (int)((ValueNumber)value).toLong();
        if (milliseconds > MAX_SLEEP_TIME) {
            invalid(me(), String.valueOf(milliseconds), "Sleep time cannot be greater than " + MAX_SLEEP_TIME + ".");
        }
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ie) {
            throw new BasicException("Sleep interrupted");
        }
    }

}
