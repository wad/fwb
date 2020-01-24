package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.function.TokenFunctionComma;
import com.funwithbasic.basic.value.Value;

public class StatementDim extends Statement {

    private String unevaluatedArguments;

    public StatementDim(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        unevaluatedArguments = code.substring(pieces[0].length()).trim();
    }

    public static StatementType me() {
        return StatementType.DIM;
    }

    public static boolean isItMine(String code) {
        String[] pieces = code.split("\\s+");
        return me().getCmd().equalsIgnoreCase(pieces[0]);
    }

    @Override
    public void execute() throws BasicException {

        // get the variable name
        String[] pieces = unevaluatedArguments.split(String.valueOf(TokenFunctionComma.SYMBOL));
        if (pieces.length != 2) {
            invalid(me(), unevaluatedArguments, "expected 2 arguments, but found " + pieces.length);
        }
        String variableName = pieces[0].trim();

        // get the number of dimensions
        Value value = ExpressionEvaluator.evaluateExpression(unevaluatedArguments.substring(pieces[0].length() + 1), getBasicRunner());
        int numDimensions = (int)value.asNumber().toLong();

        if (numDimensions < 1) {
            invalid(me(), unevaluatedArguments, "invalid array size specified");
        }
        if (ExpressionEvaluator.isStringVariableName(variableName)) {
            String v = ExpressionEvaluator.removeStringIndicator(variableName);
            if (!ExpressionEvaluator.isValidVariableName(v)) {
                invalid(me(), unevaluatedArguments, "invalid string array variable name");
            }
        }
        else {
            if (!ExpressionEvaluator.isValidVariableName(variableName)) {
                invalid(me(), unevaluatedArguments, "invalid numeric array variable name");
            }
        }

        // we intentionally leave the '$' on string array variable names, to distinguish them from numeric.
        getBasicRunner().defineArrayVariable(variableName, numDimensions);
    }

}
