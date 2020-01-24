package com.funwithbasic.basic.token.operator;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.TokenNonValue;
import com.funwithbasic.basic.token.TokenWithArguments;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public abstract class TokenOperator extends TokenNonValue implements TokenWithArguments {

    public static final int PRECEDENCE_ORDER_EXPONENTIATION = 10;
    public static final int PRECEDENCE_ORDER_MULTIPLICATION_AND_DIVISION = 9;
    public static final int PRECEDENCE_ORDER_MODULUS = 8;
    public static final int PRECEDENCE_ORDER_ADDITION_AND_SUBTRACTION = 7;
    public static final int PRECEDENCE_ORDER_LOGICAL_NOT = 6;
    public static final int PRECEDENCE_ORDER_LOGICAL_AND = 5;
    public static final int PRECEDENCE_ORDER_LOGICAL_OR = 4;
    public static final int PRECEDENCE_ORDER_COMPARATORS = 3;

    public static TokenOperator create(String value, BasicRunner basicRunner) throws BasicException {
        if (value.length() == 1) {
            char c = value.charAt(0);
            if (c == TokenOperatorAddition.SYMBOL) {
                return new TokenOperatorAddition(basicRunner);
            }
            if (c == TokenOperatorSubtraction.SYMBOL) {
                return new TokenOperatorSubtraction(basicRunner);
            }
            if (c == TokenOperatorMultiplication.SYMBOL) {
                return new TokenOperatorMultiplication(basicRunner);
            }
            if (c == TokenOperatorDivision.SYMBOL) {
                return new TokenOperatorDivision(basicRunner);
            }
            if (c == TokenOperatorModulus.SYMBOL) {
                return new TokenOperatorModulus(basicRunner);
            }
            if (c == TokenOperatorExponent.SYMBOL) {
                return new TokenOperatorExponent(basicRunner);
            }
            if (c == TokenOperatorIsEqualTo.SYMBOL) {
                return new TokenOperatorIsEqualTo(basicRunner);
            }
            if (c == TokenOperatorIsLessThan.SYMBOL) {
                return new TokenOperatorIsLessThan(basicRunner);
            }
            if (c == TokenOperatorIsGreaterThan.SYMBOL) {
                return new TokenOperatorIsGreaterThan(basicRunner);
            }
            if (c == TokenOperatorAnd.SYMBOL) {
                return new TokenOperatorAnd(basicRunner);
            }
            if (c == TokenOperatorOr.SYMBOL) {
                return new TokenOperatorOr(basicRunner);
            }
            if (c == TokenOperatorNot.SYMBOL) {
                return new TokenOperatorNot(basicRunner);
            }
        }
        else {
            if (TokenOperatorIsNotEqualTo.SYMBOL.equals(value)) {
                return new TokenOperatorIsNotEqualTo(basicRunner);
            }
            if (TokenOperatorIsLessThanOrEqualTo.SYMBOL.equals(value)) {
                return new TokenOperatorIsLessThanOrEqualTo(basicRunner);
            }
            if (TokenOperatorIsGreaterThanOrEqualTo.SYMBOL.equals(value)) {
                return new TokenOperatorIsGreaterThanOrEqualTo(basicRunner);
            }
        }
        throw new BasicException("Unknown operator: [" + value + "]");
    }

    @Override
    public Type getType() {
        return Type.OPERATOR;
    }

    public abstract boolean isLeftAssociative();

    public abstract boolean isRightAssociative();

    public abstract int getPrecedence();

    void verifyNumArguments(List<Token> arguments) throws BasicException {
        if (arguments.size() != getNumArguments()) {
            throw new BasicException("Expected " + getNumArguments() + " arguments but received " + arguments.size());
        }
    }

    @Override
    public ValueString getValueAsString() throws BasicException {
        return new ValueString(getSymbol(), basicRunner.getKeyboardInput());
    }
    
}
