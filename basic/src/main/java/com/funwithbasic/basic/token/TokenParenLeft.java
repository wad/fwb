package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.value.ValueString;

public class TokenParenLeft extends TokenNonValue {

    public static final char SYMBOL = '(';

    public TokenParenLeft(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public Type getType() {
        return Type.PAREN_LEFT;
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
