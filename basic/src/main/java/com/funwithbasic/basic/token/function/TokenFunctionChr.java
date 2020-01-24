package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

// Produce a string with one character in it of the supplied ASCII value
public class TokenFunctionChr extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.CHR.getText();

    public TokenFunctionChr(BasicRunner basicRunner) {
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
        int a = (int)(((TokenValueNumber)arguments.get(0)).getValue().toLong());
        return new TokenValueString(new ValueString(String.valueOf((char)a), basicRunner.getKeyboardInput()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
