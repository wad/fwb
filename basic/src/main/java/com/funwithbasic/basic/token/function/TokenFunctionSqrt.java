package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

public class TokenFunctionSqrt extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.SQRT.getText();

    public TokenFunctionSqrt(BasicRunner basicRunner) {
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
        double a = ((TokenValueNumber)firstToken).getValue().toDouble();
        if (a < 0.0D) {
            throw new BasicException("Cannot take square root of negative number");
        }
        return new TokenValueNumber(new ValueNumber(Math.sqrt(a)), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
