package com.funwithbasic.basic.token.value;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

public class TokenValueNumber extends TokenValue {

    private ValueNumber value;

    public TokenValueNumber(String valueAsString, BasicRunner basicRunner) throws BasicException {
        this(new ValueNumber(valueAsString), basicRunner);
    }

    public TokenValueNumber(ValueNumber value, BasicRunner basicRunner) {
        this.value = value;
        this.basicRunner = basicRunner;
    }

    public ValueNumber getValue() {
        return value;
    }

    @Override
    public ValueString getValueAsString() throws BasicException {
        return new ValueString(value.print(), basicRunner.getKeyboardInput());
    }

    @Override
    public Token.Type getType() {
        return Token.Type.NUMBER;
    }
    
    @Override
    public String getSymbol() {
        try {
            return getValueAsString().getValue();
        }
        catch (BasicException e) {
            return value.toString();
        }
    }

}
