package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.TokenParenLeft;
import com.funwithbasic.basic.token.TokenParenRight;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

// This function is not intended to be called by the user's code.
// It is a helper for evaluating multiple comma-separated arguments passed as the parameters to statements.
public abstract class TokenFunctionParams extends TokenFunction {

    @Override
    public Token evaluate(List<Token> args) throws BasicException {
        verifyNumArguments(args);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            Token arg = args.get(i);
            if (arg instanceof TokenValueString) {
                builder.append(((TokenValueString) arg).getValue().getValueWithQuotes());
            }
            else {
                builder.append(arg.getValueAsString().getValue());
            }
            if (i != args.size() - 1) {
                builder.append(TokenFunctionComma.SYMBOL);
            }
        }
        return new TokenValueString(new ValueString(builder.toString(), basicRunner.getKeyboardInput()), basicRunner);
    }

    protected static String enclose(String symbol, String unevaluatedExpressionWithCommas) {
        StringBuilder builder = new StringBuilder();
        builder.append(symbol);
        builder.append(TokenParenLeft.SYMBOL);
        builder.append(unevaluatedExpressionWithCommas);
        builder.append(TokenParenRight.SYMBOL);
        return builder.toString();
    }

}
