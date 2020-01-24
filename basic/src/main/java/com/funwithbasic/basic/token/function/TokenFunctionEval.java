package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.Value;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

// This function accepts a string, which represents a valid BASIC expression. That expression is evaluated, and the
// result is returned. The expression can include variable names, etc.
public class TokenFunctionEval extends TokenFunction {

    public static final String SYMBOL = TokenFunction.Function.EVAL.getText();

    public TokenFunctionEval(BasicRunner basicRunner) {
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
        Value resultValue = ExpressionEvaluator.evaluateExpression(a.getValue(), basicRunner);
        String resultString;
        if (resultValue instanceof ValueString) {
            resultString = ((ValueString)resultValue).getValue();
        }
        else {
            resultString = String.valueOf(((ValueNumber)resultValue).toDouble());
        }
        return Token.create(resultString, basicRunner);
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }

}
