package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.value.ValueString;

public class TokenFunctionComma extends Token {

    public static final char SYMBOL = ',';

    public TokenFunctionComma(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public Type getType() {
        return Type.COMMA;
    }

    @Override
    public String getSymbol() {
        return String.valueOf(SYMBOL);
    }

    @Override
    public ValueString getValueAsString() throws BasicException {
        return new ValueString(String.valueOf(SYMBOL), basicRunner.getKeyboardInput());
    }

}
