package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.function.TokenFunctionComma;
import com.funwithbasic.basic.token.function.TokenFunctionParams4;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

public class StatementRect extends Statement {

    private String unevaluatedArguments;

    public StatementRect(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        unevaluatedArguments = code.substring(pieces[0].length()).trim();
    }

    public static StatementType me() {
        return StatementType.RECT;
    }

    public static boolean isItMine(String code) {
        String[] pieces = code.split("\\s+");
        return me().getCmd().equalsIgnoreCase(pieces[0]);
    }

    @Override
    public void execute() throws BasicException {
        String wrappedExpression = TokenFunctionParams4.wrapMeAround(unevaluatedArguments);

        // The Evaluator knows what to do with a function that has comma-delimited arguments.
        // The result of the evaluation of that PARAMS4 function is a string with the results of
        // the evaluation of the arguments, separated by commas.
        ValueString valueString = (ValueString) ExpressionEvaluator.evaluateExpression(wrappedExpression, getBasicRunner());
        String[] arguments = valueString.getValue().split(String.valueOf(TokenFunctionComma.SYMBOL));
        if (arguments.length != 4) {
            invalid(me(), unevaluatedArguments, "expected 4 arguments, but found " + arguments.length);
        }
        int x1 = (int) new ValueNumber(arguments[0]).toLong();
        int y1 = (int) new ValueNumber(arguments[1]).toLong();
        int x2 = (int) new ValueNumber(arguments[2]).toLong();
        int y2 = (int) new ValueNumber(arguments[3]).toLong();

        getBasicRunner().getGraphicsTerminal().rect(x1, y1, x2, y2);
    }

}
