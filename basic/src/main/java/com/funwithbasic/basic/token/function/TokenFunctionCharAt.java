package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

// Returns the character at the specified zero-based position of the specified string.
// First parameter is the string, the second is the position, rounded to the nearest integer.
public class TokenFunctionCharAt extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.CHARAT.getText();

    public TokenFunctionCharAt(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 2;
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        verifyNumArguments(arguments);
        Token firstToken = arguments.get(0);
        Token secondToken = arguments.get(1);
        verifyArgumentIsString(firstToken);
        verifyArgumentIsNumber(secondToken);
        ValueString a = ((TokenValueString)firstToken).getValue();
        ValueNumber b = ((TokenValueNumber)secondToken).getValue();
        return new TokenValueString(ValueString.charAt(a, (int)b.toLong()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
