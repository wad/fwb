package com.funwithbasic.basic.token.operator;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

public class TokenOperatorExponent extends TokenOperator {

    public static final char SYMBOL = '^';

    public TokenOperatorExponent(BasicRunner basicRunner) {
        this.basicRunner = basicRunner;
    }

    @Override
    public boolean isLeftAssociative() {
        return false;
    }

    @Override
    public boolean isRightAssociative() {
        return true;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_ORDER_EXPONENTIATION;
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
        verifyArgumentIsNumber(firstToken);
        verifyArgumentIsNumber(secondToken);
        ValueNumber a = ((TokenValueNumber)firstToken).getValue();
        ValueNumber b = ((TokenValueNumber)secondToken).getValue();
        return new TokenValueNumber(ValueNumber.exponent(a, b), basicRunner);
    }

    @Override
    public String getSymbol() {
        return String.valueOf(SYMBOL);
    }

}
