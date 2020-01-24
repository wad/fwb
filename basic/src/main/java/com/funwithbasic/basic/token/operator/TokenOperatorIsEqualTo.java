package com.funwithbasic.basic.token.operator;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.TokenWithArguments;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public class TokenOperatorIsEqualTo extends TokenOperator implements TokenWithArguments {

    public static final char SYMBOL = '=';

    public TokenOperatorIsEqualTo(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }

    @Override
    public boolean isRightAssociative() {
        return false;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_ORDER_COMPARATORS;
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

        boolean firstTokenIsNumber = firstToken instanceof TokenValueNumber;
        boolean secondTokenIsNumber = secondToken instanceof TokenValueNumber;

        if (firstTokenIsNumber != secondTokenIsNumber) {
            throw new BasicException("Type mismatch");
        }

        if (firstTokenIsNumber) {
            // they are both numbers
            ValueNumber a = ((TokenValueNumber)firstToken).getValue();
            ValueNumber b = ((TokenValueNumber)secondToken).getValue();
            return new TokenValueNumber(new ValueNumber(ValueNumber.isEqualTo(a, b)), basicRunner);
        }
        else {
            // they are both strings
            ValueString a = ((TokenValueString)firstToken).getValue();
            ValueString b = ((TokenValueString)secondToken).getValue();
            return new TokenValueNumber(new ValueNumber(ValueString.isEqualTo(a, b)), basicRunner);
        }
    }

    @Override
    public String getSymbol() {
        return String.valueOf(SYMBOL);
    }

}
