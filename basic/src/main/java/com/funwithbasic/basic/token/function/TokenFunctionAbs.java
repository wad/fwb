package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

public class TokenFunctionAbs extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.ABS.getText();

    public TokenFunctionAbs(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 1;
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        verifyNumArguments(arguments);
        Token firstArgument = arguments.get(0);
        verifyArgumentIsNumber(firstArgument);
        double a = ((TokenValueNumber)arguments.get(0)).getValue().toDouble();
        return new TokenValueNumber(new ValueNumber(Math.abs(a)), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
