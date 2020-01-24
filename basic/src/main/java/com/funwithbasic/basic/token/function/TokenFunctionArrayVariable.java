package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;

import java.util.List;

public class TokenFunctionArrayVariable extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.ARRAY_VARIABLE.getText();

    private String variableName;

    public TokenFunctionArrayVariable(BasicRunner basicRunner, String variableName) {
        this.variableName = variableName;
        this.basicRunner = basicRunner;
    }

    @Override
    public int getNumArguments() throws BasicException {
        return basicRunner.getNumDimensionsForArray(variableName);
    }

    @Override
    public Token evaluate(List<Token> arguments) throws BasicException {
        verifyNumArguments(arguments);
        String arrayKey = convertToIndex(arguments);
        if (ExpressionEvaluator.isStringVariableName(variableName)) {
            return new TokenValueString(basicRunner.lookupStringArrayVariable(ExpressionEvaluator.removeStringIndicator(variableName), arrayKey), basicRunner);
        }
        else {
            return new TokenValueNumber(basicRunner.lookupNumberArrayVariable(variableName, arrayKey), basicRunner);
        }
    }

    @Override
    public String getSymbol() {
        return variableName;
    }

    public static String convertToIndex(List<Token> indexes) throws BasicException {
        StringBuilder builder = new StringBuilder();
        for (Token token: indexes) {
            verifyArgumentIsNumber(token);
            int index = (int)((TokenValueNumber)token).getValue().toLong();
            builder.append(String.valueOf(index));
            builder.append(" ");
        }
        return builder.toString().trim();
    }

}
