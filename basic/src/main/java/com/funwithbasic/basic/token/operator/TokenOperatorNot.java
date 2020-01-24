package com.funwithbasic.basic.token.operator;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;

import java.util.List;

public class TokenOperatorNot extends TokenOperator {

    public static final char SYMBOL = '!';

    public TokenOperatorNot(BasicRunner basicRunner) {
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
        return PRECEDENCE_ORDER_LOGICAL_NOT;
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
        return new TokenValueNumber(new ValueNumber(!a.isTrue()), basicRunner);
    }

    @Override
    public String getSymbol() {
        return String.valueOf(SYMBOL);
    }

}
