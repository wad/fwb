package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.value.ValueString;

public class TokenParenRight extends TokenNonValue {

    public static final char SYMBOL = ')';

    public TokenParenRight(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public Token.Type getType() {
        return Token.Type.PAREN_RIGHT;
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
