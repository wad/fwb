package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.value.Value;

public class StatementPrint extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length();

    public static final char OMIT_NEWLINE_INDICATOR = ';';

    private String printSpecification;
    private boolean omitNewline;

    public StatementPrint(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        omitNewline = false;
        String[] pieces = code.split("\\s+");

        // a print command by itself prints a blank line
        if (pieces.length == 1) {
            printSpecification = "";
        }
        else {
            // identify what the user is wanting to print
            printSpecification = code.substring(me().getCmd().length() + 1).trim();

            // take care of the trailing ';' character, which indicates that a newline is to be omitted.
            omitNewline = printSpecification.charAt(printSpecification.length() - 1) == OMIT_NEWLINE_INDICATOR;
            if (omitNewline) {
                printSpecification = printSpecification.substring(0, printSpecification.length() - 1).trim();
            }
        }
    }

    public static StatementType me() {
        return StatementType.PRINT;
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
        if (printSpecification.length() == 0) {
            if (!omitNewline) {
                getBasicRunner().getTextTerminal().print(getBasicRunner().getKeyboardInput(), "", true);
            }
        }
        else {
            Value value = ExpressionEvaluator.evaluateExpression(printSpecification, getBasicRunner());
            getBasicRunner().getTextTerminal().print(getBasicRunner().getKeyboardInput(), value.print(), !omitNewline);
        }
    }

}
