package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;
import java.util.Random;

public class TokenFunctionRnd extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.RND.getText();

    public TokenFunctionRnd(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 0;
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        verifyNumArguments(arguments);
        Random random = new Random(System.nanoTime());
        return new TokenValueNumber(new ValueNumber(random.nextDouble()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
