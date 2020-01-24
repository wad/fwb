package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public class TokenFunctionConcat extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.CONCAT.getText();

    public TokenFunctionConcat(BasicRunner basicRunner) {
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
        verifyArgumentIsString(secondToken);
        ValueString a = ((TokenValueString)firstToken).getValue();
        ValueString b = ((TokenValueString)secondToken).getValue();
        return new TokenValueString(ValueString.concat(a, b), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
