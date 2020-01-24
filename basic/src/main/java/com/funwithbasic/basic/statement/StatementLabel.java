package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;

public class StatementLabel extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = me().getCmd().length() + 2;

    private String label;

    public StatementLabel(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        if (pieces.length != 2) {
            invalid(me(), code);
        }

        label = pieces[1];
        if (!ExpressionEvaluator.isValidLabelName(label)) {
            invalid(me(), code, "Invalid label [" + label + "]");
        }

        // check to see if this label already exists
        if (getBasicRunner().lookupLabel(label) != null) {
            invalid(me(), code, "Duplicate label found: [" + label + "]");
        }

        // link my label with myself, in the BasicRunner.
        getBasicRunner().addLabel(label, this);
    }

    public static StatementType me() {
        return StatementType.LABEL;
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
        // do nothing
    }

    public String getLabel() {
        return label;
    }
}
