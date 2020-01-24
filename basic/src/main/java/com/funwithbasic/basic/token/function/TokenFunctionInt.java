package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

// Strip any fractional component from the number
public class TokenFunctionInt extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.INT.getText();

    public TokenFunctionInt(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 1;
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        verifyNumArguments(arguments);
        Token firstToken = arguments.get(0);
        verifyArgumentIsNumber(firstToken);
        return new TokenValueNumber(new ValueNumber(((TokenValueNumber)firstToken).getValue().toLong()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
