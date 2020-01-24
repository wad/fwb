package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.value.ValueString;

// This one is somewhat special, as it's never actually tokenized. Since array operations are
// converted into functions internally, this is just a placeholder for the symbol.
public class TokenArrayDimensionSeparator extends Token {

    public static final String SYMBOL = "][";

    public TokenArrayDimensionSeparator(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public Token.Type getType() {
        return Token.Type.ARRAY_DIMENSION_SEPARATOR;
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
