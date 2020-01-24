package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

// Tangent, needs argument in radians
public class TokenFunctionTan extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.TAN.getText();

    public TokenFunctionTan(BasicRunner basicRunner) {
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
        return new TokenValueNumber(new ValueNumber(Math.tan(a)), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
