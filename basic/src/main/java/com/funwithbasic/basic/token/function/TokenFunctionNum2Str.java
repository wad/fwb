package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public class TokenFunctionNum2Str extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.NUM2STR.getText();

    public TokenFunctionNum2Str(BasicRunner basicRunner) {
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
        ValueNumber a = ((TokenValueNumber)firstToken).getValue();
        return new TokenValueString(new ValueString(String.valueOf(a), basicRunner.getKeyboardInput()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
