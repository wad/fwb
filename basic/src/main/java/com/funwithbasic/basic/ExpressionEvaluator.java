package com.funwithbasic.basic;

import com.funwithbasic.basic.token.*;
import com.funwithbasic.basic.token.function.TokenFunctionComma;
import com.funwithbasic.basic.token.operator.*;
import com.funwithbasic.basic.token.value.TokenValue;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import com.funwithbasic.basic.value.Value;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEvaluator {

    // every variable name that indicates a string ends with this character
    public static final char STRING_VARIABLE_INDICATOR = '$';

    public static Value evaluateExpression(String expression, BasicRunner basicRunner) throws BasicException {
        List<Token> tokensInfix = tokenize(expression.trim(), basicRunner);
        List<Token> tokensPostfix = convertInfixToPostfix(tokensInfix);
        return solvePostfix(tokensPostfix);
    }

    static Value solvePostfix(List<Token> tokensPostfix) throws BasicException {
        Stack<Token> stack = new Stack<Token>(tokensPostfix.size());
        while (!tokensPostfix.isEmpty()) {
            Token token = tokensPostfix.get(0);
            tokensPostfix.remove(0);
            if (token instanceof TokenValue) {
                stack.push(token);
            } else {
                if (!(token instanceof TokenWithArguments)) {
                    throw new BasicException("Expected something with arguments");
                }
                TokenWithArguments tokenWithArguments = (TokenWithArguments) token;
                int numArguments = tokenWithArguments.getNumArguments();
                if (stack.size() < numArguments) {
                    throw new BasicException("Expected " + numArguments + " on the stackOfTokens, but only have " + stack.size());
                }
                List<Token> arguments = new ArrayList<Token>(numArguments);
                for (int i = 0; i < numArguments; i++) {
                    arguments.add(0, stack.pop());
                }
                stack.push(tokenWithArguments.evaluate(arguments));
            }
        }
        if (stack.size() != 1) {
            throw new BasicException("Expected one token on the stack, but found " + stack.size());
        }
        Token resultToken = stack.pop();
        Value result;

        if (resultToken instanceof TokenValueNumber) {
            result = ((TokenValueNumber) resultToken).getValue();
        } else if (resultToken instanceof TokenValueString) {
            result = ((TokenValueString) resultToken).getValue();
        } else {
            throw new RuntimeException("Unexpected type of token");
        }
        return result;
    }

    static List<Token> convertInfixToPostfix(List<Token> tokensInInfixOrder) throws BasicException {
        // Implementation of the shunting yard algorithm
        // http://en.wikipedia.org/wiki/Shunting-yard_algorithm
        List<Token> tokensInPostfixOrder = new ArrayList<Token>(tokensInInfixOrder.size());
        Stack<Token> operatorStack = new Stack<Token>(tokensInInfixOrder.size());
        for (Token token : tokensInInfixOrder) {
            switch (token.getType()) {

                case NUMBER:
                    tokensInPostfixOrder.add(token);
                    break;

                case STRING_LITERAL:
                    tokensInPostfixOrder.add(token);
                    break;
                
                case FUNCTION:
                    operatorStack.push(token);
                    break;

                case COMMA:
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != Token.Type.PAREN_LEFT) {
                        tokensInPostfixOrder.add(operatorStack.pop());
                        if (operatorStack.isEmpty()) {
                            throw new BasicException("Mismatched parenthesis or square brackets");
                        }
                    }
                    break;

                case OPERATOR:
                    boolean keepGoing = true;
                    while (keepGoing) {
                        TokenOperator operator = (TokenOperator) token;
                        if (operatorStack.isEmpty()) {
                            keepGoing = false;
                        } else {
                            Token topOfStack = operatorStack.peek();

                            if (topOfStack.getType() != Token.Type.OPERATOR) {
                                keepGoing = false;
                            } else {
                                TokenOperator operatorOnTopOfStack = (TokenOperator) topOfStack;
                                boolean a = operator.isLeftAssociative() && operator.getPrecedence() <= operatorOnTopOfStack.getPrecedence();
                                boolean b = operator.isRightAssociative() && operator.getPrecedence() < operatorOnTopOfStack.getPrecedence();
                                if (a || b) {
                                    keepGoing = true;
                                    tokensInPostfixOrder.add(operatorStack.pop());
                                } else {
                                    keepGoing = false;
                                }
                            }
                        }
                    }
                    operatorStack.push(token);
                    break;

                case PAREN_LEFT:
                    operatorStack.push(token);
                    break;

                case PAREN_RIGHT:
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != Token.Type.PAREN_LEFT) {
                        Token topOfStack = operatorStack.pop();
                        if (operatorStack.isEmpty()) {
                            throw new BasicException("Mismatched parenthesis or square brackets detected");
                        }
                        tokensInPostfixOrder.add(topOfStack);
                    }
                    Token leftParen = operatorStack.pop();
                    if (leftParen.getType() != Token.Type.PAREN_LEFT) {
                        throw new BasicException("Expected left parenthesis");
                    }
                    if (!operatorStack.isEmpty()) {
                        Token topOfStack = operatorStack.peek();
                        if (topOfStack.getType() == Token.Type.FUNCTION) {
                            tokensInPostfixOrder.add(operatorStack.pop());
                        }
                    }
                    break;

                case SQUARE_BRACKET_LEFT:
                case SQUARE_BRACKET_RIGHT:
                case ARRAY_DIMENSION_SEPARATOR:
                    throw new RuntimeException("Should never see square bracket tokens, they are converted to parens and commas");

                default:
                    throw new BasicException("Unexpected token type. Token: " + token.toString());
            }
        }

        while (!operatorStack.isEmpty()) {
            Token topOfStack = operatorStack.peek();
            if (topOfStack.getType() == Token.Type.PAREN_LEFT || topOfStack.getType() == Token.Type.PAREN_RIGHT) {
                throw new BasicException("Mismatched parenthesis or square brackets detected");
            }
            tokensInPostfixOrder.add(operatorStack.pop());
        }

        return tokensInPostfixOrder;
    }

    public static List<Token> tokenize(String rightSide, BasicRunner basicRunner) throws BasicException {
        String[] tokenStrings = insertSpacesBetweenTokens(rightSide).split("\\s+");
        List<Token> tokens = new ArrayList<Token>();
        for (String value : tokenStrings) {
            if (value.length() > 0) {
                Token token = Token.create(value, basicRunner);
                tokens.add(token);
            }
        }
        return tokens;
    }

    private enum TokenSeparationState {
        LOOKING_FOR_TOKEN,
        IN_ALPHANUMERIC_TOKEN,
        IN_NUMERIC_TOKEN,
        IN_STRING_LITERAL_TOKEN
    }

    static String insertSpacesBetweenTokens(String rightSide) throws BasicException {

        TokenSeparationState state = TokenSeparationState.LOOKING_FOR_TOKEN;

        StringBuilder builder = new StringBuilder();
        int i = 0;
        int length = rightSide.length();
        boolean previousTokenAllowsSign = true;
        while (i < length) {
            char c = rightSide.charAt(i);
            switch (state) {

                case LOOKING_FOR_TOKEN: {
                    if (c == ' ') {
                        i++;
                    } else if (c == ValueString.LITERAL_DELIMITER) {
                        builder.append(' ');
                        builder.append(c);
                        state = TokenSeparationState.IN_STRING_LITERAL_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == TokenOperatorAddition.SYMBOL) {
                        // is this plus sign the last character? It's invalid then.
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is '" + c + "'. That's invalid: [" + rightSide + "]");
                        }

                        if (!previousTokenAllowsSign) {
                            // it's an addition operator.
                            builder.append(' ');
                            builder.append(c);
                            builder.append(' ');
                            state = TokenSeparationState.LOOKING_FOR_TOKEN;
                            previousTokenAllowsSign = true;
                            i++;
                        } else {
                            // we need to consider the next character to figure out if this is an addition operator or a plus sign.
                            char nextCharacter = rightSide.charAt(i + 1);

                            // does it look like part of a number?
                            if (isNumericDigit(nextCharacter) || nextCharacter == ValueNumber.DECIMAL_POINT) {
                                // it's a plus sign, part of a number.
                                builder.append(c);
                                state = TokenSeparationState.IN_NUMERIC_TOKEN;
                                previousTokenAllowsSign = false;
                                i++;
                            } else {
                                // it's an addition operator.
                                builder.append(' ');
                                builder.append(c);
                                builder.append(' ');
                                state = TokenSeparationState.LOOKING_FOR_TOKEN;
                                previousTokenAllowsSign = true;
                                i++;
                            }
                        }
                    } else if (c == TokenOperatorSubtraction.SYMBOL) {
                        // is this minus sign the last character? It's invalid then.
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        if (!previousTokenAllowsSign) {
                            // it's a subtraction operator.
                            builder.append(' ');
                            builder.append(c);
                            builder.append(' ');
                            state = TokenSeparationState.LOOKING_FOR_TOKEN;
                            previousTokenAllowsSign = true;
                            i++;
                        } else {
                            // we need to consider the next character to figure out if this is a subtraction operator or a negative sign.
                            char nextCharacter = rightSide.charAt(i + 1);

                            // does it look like part of a number?
                            if (isNumericDigit(nextCharacter) || nextCharacter == ValueNumber.DECIMAL_POINT) {
                                // it's a negative sign, part of a number.
                                builder.append(c);
                                state = TokenSeparationState.IN_NUMERIC_TOKEN;
                                previousTokenAllowsSign = false;
                                i++;
                            } else {
                                // it's a subtraction operator.
                                builder.append(' ');
                                builder.append(c);
                                builder.append(' ');
                                state = TokenSeparationState.LOOKING_FOR_TOKEN;
                                previousTokenAllowsSign = true;
                                i++;
                            }
                        }
                    } else if (c == TokenOperatorMultiplication.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorDivision.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorModulus.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorExponent.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorAnd.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorOr.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorNot.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenParenLeft.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenParenRight.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == TokenSquareBracketLeft.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenSquareBracketRight.SYMBOL) {
                        boolean isJustBracketToken = true;
                        boolean isLastCharacter = false;
                        if (i == length - 1) {
                            isLastCharacter = true;
                        }
                        else {
                            // need to see if this is actually an array dimension separator token, which
                            // can have spaces between ']' and '['. Remove those interior spaces, if any.
                            int j = 1;
                            char nextCharacter = rightSide.charAt(i + j);
                            while (nextCharacter == ' ') {
                                j++;
                                if (i + j == length) {
                                    isLastCharacter = true;
                                    break;
                                }
                                nextCharacter = rightSide.charAt(i + j);
                            }
                            if (nextCharacter == TokenSquareBracketLeft.SYMBOL) {
                                isJustBracketToken = false;
                                builder.append(' ');
                                builder.append(TokenArrayDimensionSeparator.SYMBOL);
                                builder.append(' ');
                                previousTokenAllowsSign = true;
                                i += j + 1;
                            }
                        }
                        if (isJustBracketToken || isLastCharacter) {
                            // last character, so no need to check for array dimension separator situation
                            builder.append(' ');
                            builder.append(c);
                            builder.append(' ');
                            previousTokenAllowsSign = false;
                            i++;
                        }
                    } else if (isAlphabeticOrUnderscore(c)) {
                        builder.append(c);
                        state = TokenSeparationState.IN_ALPHANUMERIC_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (isNumericDigit(c)) {
                        builder.append(c);
                        state = TokenSeparationState.IN_NUMERIC_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == ValueNumber.DECIMAL_POINT) {
                        builder.append(c);
                        state = TokenSeparationState.IN_NUMERIC_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == TokenFunctionComma.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsEqualTo.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsLessThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is <= or <>
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsGreaterThan.SYMBOL || nextCharacter == TokenOperatorIsEqualTo.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsGreaterThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is >=
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsEqualTo.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else {
                        throw new BasicException("Failed to parse expression on character " + i + ": [" + rightSide + "]");
                    }
                }
                break;

                case IN_ALPHANUMERIC_TOKEN: {
                    if (c == ' ') {
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (isAlphanumericOrUnderscore(c)) {
                        builder.append(c);
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == STRING_VARIABLE_INDICATOR) {
                        builder.append(STRING_VARIABLE_INDICATOR);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorAddition.SYMBOL) {
                        // in this case, since we're at the end of a string token, a '+' character should be
                        // an addition operator, not a plus sign.
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorSubtraction.SYMBOL) {
                        // in this case, since we're at the end of a string token, a '-' character should be
                        // a subtraction operator, not a negative sign.
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorMultiplication.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorDivision.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorModulus.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorExponent.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorAnd.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorOr.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorNot.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenFunctionComma.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenParenLeft.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenParenRight.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == TokenSquareBracketLeft.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenSquareBracketRight.SYMBOL) {
                        boolean isJustBracketToken = true;
                        boolean isLastCharacter = false;
                        if (i == length - 1) {
                            isLastCharacter = true;
                        }
                        else {
                            // need to see if this is actually an array dimension separator token, which
                            // can have spaces between ']' and '['. Remove those interior spaces, if any.
                            int j = 1;
                            char nextCharacter = rightSide.charAt(i + j);
                            while (nextCharacter == ' ') {
                                j++;
                                if (i + j == length) {
                                    isLastCharacter = true;
                                    break;
                                }
                                nextCharacter = rightSide.charAt(i + j);
                            }
                            if (nextCharacter == TokenSquareBracketLeft.SYMBOL) {
                                isJustBracketToken = false;
                                builder.append(' ');
                                builder.append(TokenArrayDimensionSeparator.SYMBOL);
                                builder.append(' ');
                                state = TokenSeparationState.LOOKING_FOR_TOKEN;
                                previousTokenAllowsSign = true;
                                i += j + 1;
                            }
                        }
                        if (isJustBracketToken || isLastCharacter) {
                            // last character, so no need to check for array dimension separator situation
                            builder.append(' ');
                            builder.append(c);
                            builder.append(' ');
                            state = TokenSeparationState.LOOKING_FOR_TOKEN;
                            previousTokenAllowsSign = false;
                            i++;
                        }
                    } else if (c == TokenOperatorIsEqualTo.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsLessThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is <= or <>
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsGreaterThan.SYMBOL || nextCharacter == TokenOperatorIsEqualTo.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsGreaterThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is >=
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsGreaterThan.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else {
                        throw new BasicException("Failed to parse expression on character " + i + ": [" + rightSide + "]");
                    }
                }
                break;

                case IN_NUMERIC_TOKEN: {
                    if (isNumericDigit(c)) {
                        builder.append(c);
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == ValueNumber.DECIMAL_POINT) {
                        builder.append(c);
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == ' ') {
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorAddition.SYMBOL) {
                        // in this case, since we're at the end of a numeric token, a '+' character should be
                        // an addition operator, not a plus sign.
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorSubtraction.SYMBOL) {
                        // in this case, since we're at the end of a numeric token, a '-' character should be
                        // a subtraction operator, not a negative sign.
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorMultiplication.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorDivision.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorModulus.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorExponent.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorAnd.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorOr.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorNot.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenParenRight.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == TokenSquareBracketRight.SYMBOL) {
                        boolean isJustBracketToken = true;
                        boolean isLastCharacter = false;
                        if (i == length - 1) {
                            isLastCharacter = true;
                        }
                        else {
                            // need to see if this is actually an array dimension separator token, which
                            // can have spaces between ']' and '['. Remove those interior spaces, if any.
                            int j = 1;
                            char nextCharacter = rightSide.charAt(i + j);
                            while (nextCharacter == ' ') {
                                j++;
                                if (i + j == length) {
                                    isLastCharacter = true;
                                    break;
                                }
                                nextCharacter = rightSide.charAt(i + j);
                            }
                            if (nextCharacter == TokenSquareBracketLeft.SYMBOL) {
                                isJustBracketToken = false;
                                builder.append(' ');
                                builder.append(TokenArrayDimensionSeparator.SYMBOL);
                                builder.append(' ');
                                state = TokenSeparationState.LOOKING_FOR_TOKEN;
                                previousTokenAllowsSign = true;
                                i += j + 1;
                            }
                        }
                        if (isJustBracketToken || isLastCharacter) {
                            // last character, so no need to check for array dimension separator situation
                            builder.append(' ');
                            builder.append(c);
                            builder.append(' ');
                            state = TokenSeparationState.LOOKING_FOR_TOKEN;
                            previousTokenAllowsSign = false;
                            i++;
                        }
                    } else if (c == TokenFunctionComma.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsEqualTo.SYMBOL) {
                        builder.append(' ');
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsLessThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is <= or <>
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsGreaterThan.SYMBOL || nextCharacter == TokenOperatorIsEqualTo.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else if (c == TokenOperatorIsGreaterThan.SYMBOL) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        // we need to consider the next character to figure out if this is >=
                        char nextCharacter = rightSide.charAt(i + 1);

                        builder.append(' ');
                        if (nextCharacter == TokenOperatorIsEqualTo.SYMBOL) {
                            // skip ahead to the next character
                            builder.append(c);
                            builder.append(nextCharacter);
                            i++;
                        } else {
                            builder.append(c);
                        }
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = true;
                        i++;
                    } else {
                        throw new BasicException("Failed to parse expression on character " + i + ": [" + rightSide + "]");
                    }
                }
                break;

                case IN_STRING_LITERAL_TOKEN: {
                    if (c == ValueString.ESCAPE_INDICATOR) {
                        if (i == length - 1) {
                            throw new BasicException("Last character of expression is " + c + ". That's invalid: [" + rightSide + "]");
                        }

                        char nextCharacter = rightSide.charAt(i + 1);
                        if (nextCharacter == ValueString.LITERAL_DELIMITER) {
                            builder.append(ValueString.LITERAL_DELIMITER);
                            i += 2;
                        } else {
                            builder.append(ValueString.ESCAPE_INDICATOR);
                            builder.append(ValueString.ESCAPE_INDICATOR_BACKSLASH);
                            i++;
                        }

                        previousTokenAllowsSign = false;
                    } else if (c == ' ') {
                        builder.append(ValueString.ESCAPE_INDICATOR);
                        builder.append(ValueString.ESCAPE_INDICATOR_SPACE);
                        previousTokenAllowsSign = false;
                        i++;
                    } else if (c == ValueString.LITERAL_DELIMITER) {
                        builder.append(c);
                        builder.append(' ');
                        state = TokenSeparationState.LOOKING_FOR_TOKEN;
                        previousTokenAllowsSign = false;
                        i++;
                    }
                    else {
                        builder.append(c);
                        previousTokenAllowsSign = false;
                        i++;
                    }
                }
                break;
            }
        }
        return builder.toString().trim();
    }

    static boolean isAlphanumericOrUnderscore(char c) {
        return isAlphabeticOrUnderscore(c) || isNumericDigit(c);
    }

    public static boolean isAlphabeticOrUnderscore(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    public static boolean isNumericDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isValidLabelName(String label) {
        if (label == null || label.length() < 1) {
            return false;
        }
        for (int i = 0; i < label.length(); i++) {
            char c = label.charAt(i);
            if (!isAlphanumericOrUnderscore(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidVariableName(String variableName) {
        if (variableName == null || variableName.length() < 1) {
            return false;
        }
        char firstCharacter = variableName.charAt(0);
        if (!isAlphabeticOrUnderscore(firstCharacter)) {
            return false;
        }
        for (int i = 1; i < variableName.length(); i++) {
            char character = variableName.charAt(i);
            if (!isAlphanumericOrUnderscore(character) && character != '_') {
                return false;
            }
        }
        return true;
    }

    public static boolean isStringVariableName(String variableName) {
        if (variableName == null || variableName.length() == 0) {
            return false;
        }
        char lastCharacterOfName = variableName.charAt(variableName.length() - 1);
        return lastCharacterOfName == STRING_VARIABLE_INDICATOR;
    }

    public static String removeStringIndicator(String variableName) {
        return variableName.substring(0, variableName.length() - 1);
    }

}
