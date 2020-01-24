package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.function.TokenFunctionComma;
import com.funwithbasic.basic.token.function.TokenFunctionParams3;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

public class StatementPuts extends Statement {

    private String unevaluatedArguments;

    public StatementPuts(String code, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        super(basicRunner, lineNumberInFile);

        String[] pieces = code.split("\\s+");
        unevaluatedArguments = code.substring(pieces[0].length()).trim();
    }

    public static StatementType me() {
        return StatementType.PUTS;
    }

    public static boolean isItMine(String code) {
        String[] pieces = code.split("\\s+");
        return me().getCmd().equalsIgnoreCase(pieces[0]);
    }

    @Override
    public void execute() throws BasicException {
        if (unevaluatedArguments.length() == 0) {
            getBasicRunner().getTextTerminal().create(getBasicRunner().getKeyboardInput());
        }
        else {
            String wrappedExpression = TokenFunctionParams3.wrapMeAround(unevaluatedArguments);

            // The Evaluator knows what to do with a function that has comma-delimited arguments.
            // The result of the evaluation of that PARAMS3 function is a string with the results of
            // the evaluation of the arguments, separated by commas.
            ValueString valueString = (ValueString) ExpressionEvaluator.evaluateExpression(wrappedExpression, getBasicRunner());
            String[] arguments = valueString.getValue().split(String.valueOf(TokenFunctionComma.SYMBOL));
            if (arguments.length != 3) {
                invalid(me(), unevaluatedArguments, "expected 3 arguments, but found " + arguments.length);
            }
            int row = (int) new ValueNumber(arguments[0]).toLong();
            int column = (int) new ValueNumber(arguments[1]).toLong();
            String characters = ValueString.convertFromLiteral(arguments[2]);

            getBasicRunner().getTextTerminal().putStringAtPosition(getBasicRunner().getKeyboardInput(), row, column, characters);
        }
    }

}
