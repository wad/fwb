package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.value.ValueString;

// This one is somewhat special, as it's never actually tokenized. Since array operations are
// converted into functions internally, this is just a placeholder for the symbol.
public class TokenSquareBracketLeft extends TokenNonValue {

    public static final char SYMBOL = '[';

    public TokenSquareBracketLeft(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public Type getType() {
        return Type.SQUARE_BRACKET_LEFT;
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
