package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public class TokenFunctionIsNum extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.ISNUM.getText();

    public TokenFunctionIsNum(BasicRunner basicRunner) {
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
        boolean isNumber;

        // todo: Using exception handling for non-exceptional situations is ugly.
        // todo: But down below, it's catching a NumberFormatException anyway, and
        // todo: I can't think of another way to do it. Figure this out later.
        try {
            new ValueNumber(a.getValue());
            isNumber = true;
        }
        catch (BasicException e) {
            isNumber = false;
        }
        return new TokenValueNumber(new ValueNumber(isNumber), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
