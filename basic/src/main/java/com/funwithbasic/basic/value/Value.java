package com.funwithbasic.basic.value;

import com.funwithbasic.basic.BasicException;

public abstract class Value {

    public ValueNumber asNumber() throws BasicException {
        if (!(this instanceof ValueNumber)) {
            throw new BasicException("Type mismatch, expected a number");
        }
        return (ValueNumber)this;
    }

    public ValueString asString() throws BasicException {
        if (!(this instanceof ValueString)) {
            throw new BasicException("Type mismatch, expected a string");
        }
        return (ValueString)this;
    }

    public abstract String print() throws BasicException;

}
