package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

// Returns the ASCII value of the first character in the supplied string argument
public class TokenFunctionAsc extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.ASC.getText();

    public TokenFunctionAsc(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() {
        return 1;
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        int resultInt = 0;
        verifyNumArguments(arguments);
        Token firstArgument = arguments.get(0);
        verifyArgumentIsString(firstArgument);
        String argString = ((TokenValueString)(firstArgument)).getValue().getValue();
        if (argString.length() > 0) {
            resultInt = argString.charAt(0);
        }
        return new TokenValueNumber(new ValueNumber(resultInt), basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
