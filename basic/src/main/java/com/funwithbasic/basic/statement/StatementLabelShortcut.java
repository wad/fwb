package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;

public class StatementLabelShortcut extends Statement {

    private static final int MINIMUM_NUM_CHARACTERS = 2;

    private String label;

    public StatementLabelShortcut(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        if (code.length() < MINIMUM_NUM_CHARACTERS) {
            invalid(me(), code, "no label");
        }

        label = code.substring(1);
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
        return StatementType.LABEL_;
    }

    public static boolean isItMine(String code) {
        return code.length() >= MINIMUM_NUM_CHARACTERS && code.charAt(0) == me().getCmd().charAt(0);
    }

    @Override
    public void execute() throws BasicException {
        // does nothing
    }

    public String getLabel() {
        return label;
    }

}
