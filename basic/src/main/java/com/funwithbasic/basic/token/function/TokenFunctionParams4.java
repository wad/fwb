package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicRunner;

public class TokenFunctionParams4 extends TokenFunctionParams {

    public static final String SYMBOL = TokenFunction.Function.PARAMS4.getText();

    public TokenFunctionParams4(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 4;
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

    public static String wrapMeAround(String unevaluatedExpression) {
        return enclose(SYMBOL, unevaluatedExpression);
    }

}
