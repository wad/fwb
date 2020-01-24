package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;
import com.funwithbasic.basic.token.function.TokenFunction;
import com.funwithbasic.basic.token.function.TokenFunctionComma;
import com.funwithbasic.basic.token.operator.*;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

public abstract class Token {

    public enum Type {
        NUMBER,
        STRING_LITERAL,
        OPERATOR,
        PAREN_LEFT,
        PAREN_RIGHT,
        SQUARE_BRACKET_LEFT,
        SQUARE_BRACKET_RIGHT,
        FUNCTION,
        COMMA,
        ARRAY_DIMENSION_SEPARATOR
    }

    protected BasicRunner basicRunner;

    public static Token create(String value, BasicRunner basicRunner) throws BasicException {
        if (value == null || value.length() == 0) {
            throw new BasicException("Invalid token, it's empty.");
        }

        char c = value.charAt(0);

        if (value.length() == 1) {
            if (c == TokenParenLeft.SYMBOL) {
                return new TokenParenLeft(basicRunner);
            }
            if (c == TokenParenRight.SYMBOL) {
                return new TokenParenRight(basicRunner);
            }
            if (c == TokenSquareBracketLeft.SYMBOL) {
                // since we're converting array variables to functions, '[' turns into '('
                return new TokenParenLeft(basicRunner);
            }
            if (c == TokenSquareBracketRight.SYMBOL) {
                // since we're converting array variables to functions, ']' turns into ')'
                return new TokenParenRight(basicRunner);
            }
            if (c == TokenFunctionComma.SYMBOL) {
                return new TokenFunctionComma(basicRunner);
            }
            if (c == TokenOperatorAddition.SYMBOL ||
                    c == TokenOperatorSubtraction.SYMBOL ||
                    c == TokenOperatorMultiplication.SYMBOL ||
                    c == TokenOperatorDivision.SYMBOL ||
                    c == TokenOperatorModulus.SYMBOL ||
                    c == TokenOperatorExponent.SYMBOL ||
                    c == TokenOperatorIsEqualTo.SYMBOL ||
                    c == TokenOperatorIsLessThan.SYMBOL ||
                    c == TokenOperatorIsGreaterThan.SYMBOL ||
                    c == TokenOperatorAnd.SYMBOL ||
                    c == TokenOperatorOr.SYMBOL ||
                    c == TokenOperatorNot.SYMBOL) {
                return TokenOperator.create(value, basicRunner);
            }
            if (ExpressionEvaluator.isAlphabeticOrUnderscore(c)) {
                if (basicRunner.isFunctionName(value)) {
                    return create(value, basicRunner);
                }

                if (basicRunner.isArrayVariableName(value)) {
                    return create(value, basicRunner);
                }

                // it must be a variable
                return new TokenValueNumber(basicRunner.lookupNumberVariable(value), basicRunner);
            }
        }

        if (c == ValueString.LITERAL_DELIMITER) {
            return new TokenValueString(new ValueString(ValueString.convertFromLiteral(value), basicRunner.getKeyboardInput()), basicRunner);
        }

        if (TokenArrayDimensionSeparator.SYMBOL.equals(value)) {
            // since we're converting array variables to functions, "][" turns into ','
            return new TokenFunctionComma(basicRunner);
        }

        if (ExpressionEvaluator.isNumericDigit(c) || c == ValueNumber.DECIMAL_POINT || c == TokenOperatorSubtraction.SYMBOL) {
            return new TokenValueNumber(value, basicRunner);
        }
        if (basicRunner.isFunctionName(value)) {
            return TokenFunction.create(value, basicRunner);
        }
        if (basicRunner.isArrayVariableName(value)) {
            return TokenFunction.create(value, basicRunner);
        }
        if (TokenOperatorIsNotEqualTo.SYMBOL.equals(value) ||
                TokenOperatorIsLessThanOrEqualTo.SYMBOL.equals(value) ||
                TokenOperatorIsGreaterThanOrEqualTo.SYMBOL.equals(value)) {
            return TokenOperator.create(value, basicRunner);
        }

        // it must be a variable
        if (ExpressionEvaluator.isStringVariableName(value)) {
            String variableName = ExpressionEvaluator.removeStringIndicator(value);
            return new TokenValueString(basicRunner.lookupStringVariable(variableName), basicRunner);
        }

        // it must be a number variable
        return new TokenValueNumber(basicRunner.lookupNumberVariable(value), basicRunner);
    }

    public abstract Type getType();

    public abstract String getSymbol();

    public abstract ValueString getValueAsString() throws BasicException;

    @Override
    public String toString() {
        try {
            return getValueAsString().getValue();
        }
        catch (BasicException e) {
            return getSymbol();
        }
    }

    protected static void verifyArgumentIsNumber(Token token) throws BasicException {
        if (token.getType() != Token.Type.NUMBER) {
            throw new BasicException("Expected token type Number");
        }
    }

    protected static void verifyArgumentIsString(Token token) throws BasicException {
        if (token.getType() != Token.Type.STRING_LITERAL) {
            throw new BasicException("Expected token type String");
        }
    }

}
