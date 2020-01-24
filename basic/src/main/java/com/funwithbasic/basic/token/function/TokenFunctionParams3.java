package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicRunner;

public class TokenFunctionParams3 extends TokenFunctionParams {

    public static final String SYMBOL = TokenFunction.Function.PARAMS3.getText();

    public TokenFunctionParams3(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 3;
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

    public static String wrapMeAround(String unevaluatedExpression) {
        return enclose(SYMBOL, unevaluatedExpression);
    }
    
}
