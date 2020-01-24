package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public class TokenFunctionStrlen extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.STRLEN.getText();

    public TokenFunctionStrlen(BasicRunner basicRunner) {
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
        verifyArgumentIsString(firstToken);
        ValueString a = ((TokenValueString)firstToken).getValue();
        return new TokenValueNumber(new ValueNumber(ValueString.strlen(a)), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
