package com.funwithbasic.basic.token.value;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.value.ValueString;

public class TokenValueString extends TokenValue {

    private ValueString valueString;

    public TokenValueString(ValueString valueString, BasicRunner basicRunner) {
        this.valueString = valueString;
        this.basicRunner = basicRunner;
    }

    public ValueString getValue() {
        return valueString;
    }

    @Override
    public Token.Type getType() {
        return Token.Type.STRING_LITERAL;
    }

    @Override
    public String toString() {
        return valueString.getValueWithQuotes();
    }

    @Override
    public String getSymbol() {
        return getValue().getValue();
    }

    @Override
    public ValueString getValueAsString() throws BasicException {
        return getValue();
    }

}
