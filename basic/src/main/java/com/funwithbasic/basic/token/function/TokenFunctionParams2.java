package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicRunner;

public class TokenFunctionParams2 extends TokenFunctionParams {

    public static final String SYMBOL = TokenFunction.Function.PARAMS2.getText();

    public TokenFunctionParams2(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

    public static String wrapMeAround(String unevaluatedExpression) {
        return enclose(SYMBOL, unevaluatedExpression);
    }

}
