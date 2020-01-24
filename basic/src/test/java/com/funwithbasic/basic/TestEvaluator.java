package com.funwithbasic.basic;

import com.funwithbasic.basic.testhelper.BasicTestHelper;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.operator.*;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.value.ValueNumber;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestEvaluator {

    @Test
    public void testExpressionEvaluation() throws BasicException {
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        assertEquals("Evaluation should work", 6L, ExpressionEvaluator.evaluateExpression("1+5", basicRunner).asNumber().toLong());

        assertEquals("Evaluation should work", 2L, ExpressionEvaluator.evaluateExpression("1+5-4", basicRunner).asNumber().toLong());

        assertEquals("Evaluation should work", "3.000122", ExpressionEvaluator.evaluateExpression("3+4*2/(1-5)^2^3", basicRunner).toString());

        // comparators
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("5=5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.FALSE, ExpressionEvaluator.evaluateExpression("5<>5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("3.1415<>5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("3.1415<5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.FALSE, ExpressionEvaluator.evaluateExpression("9.1415<5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.FALSE, ExpressionEvaluator.evaluateExpression("3.1415>5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("9.1415>5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("5>=5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("6>=5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.TRUE, ExpressionEvaluator.evaluateExpression("5<=5", basicRunner).asNumber().toLong());
        assertEquals("Evaluation should work", ValueNumber.FALSE, ExpressionEvaluator.evaluateExpression("6<5", basicRunner).asNumber().toLong());

        // modulus
        assertEquals("Modulus should work", "1", ExpressionEvaluator.evaluateExpression("5%2", basicRunner).toString());
        assertEquals("Modulus should work", "0", ExpressionEvaluator.evaluateExpression("6%2", basicRunner).toString());
    }

    @Test
    public void testInsertingSpacesBetweenTokens() throws BasicException {

        // empty string
        assertEquals("", ExpressionEvaluator.insertSpacesBetweenTokens(""));
        assertEquals("", ExpressionEvaluator.insertSpacesBetweenTokens(" "));
        assertEquals("", ExpressionEvaluator.insertSpacesBetweenTokens("      "));

        // single number
        assertEquals("6", ExpressionEvaluator.insertSpacesBetweenTokens("6"));

        // single number with spaces around it
        assertEquals("6", ExpressionEvaluator.insertSpacesBetweenTokens(" 6"));
        assertEquals("6", ExpressionEvaluator.insertSpacesBetweenTokens("6 "));
        assertEquals("6", ExpressionEvaluator.insertSpacesBetweenTokens(" 6 "));
        assertEquals("6", ExpressionEvaluator.insertSpacesBetweenTokens("    6       "));

        // single multi-digit number
        assertEquals("72", ExpressionEvaluator.insertSpacesBetweenTokens("72"));
        assertEquals("72", ExpressionEvaluator.insertSpacesBetweenTokens(" 72 "));
        assertEquals("7212345", ExpressionEvaluator.insertSpacesBetweenTokens("7212345"));
        assertEquals("7212345", ExpressionEvaluator.insertSpacesBetweenTokens(" 7212345 "));

        // single digit float
        assertEquals(".1", ExpressionEvaluator.insertSpacesBetweenTokens(".1"));
        assertEquals(".1", ExpressionEvaluator.insertSpacesBetweenTokens(" .1   "));

        // floats
        assertEquals(".1234", ExpressionEvaluator.insertSpacesBetweenTokens(".1234"));
        assertEquals("56.1234", ExpressionEvaluator.insertSpacesBetweenTokens("56.1234"));
        assertEquals("56. + 9", ExpressionEvaluator.insertSpacesBetweenTokens("56.+9"));

        // negatives and positives
        assertEquals("-5", ExpressionEvaluator.insertSpacesBetweenTokens("-5"));
        assertEquals("-5", ExpressionEvaluator.insertSpacesBetweenTokens("  -5 "));
        assertEquals("-578", ExpressionEvaluator.insertSpacesBetweenTokens("-578"));
        assertEquals("+5", ExpressionEvaluator.insertSpacesBetweenTokens("+5"));
        assertEquals("+5", ExpressionEvaluator.insertSpacesBetweenTokens("  +5 "));
        assertEquals("+578", ExpressionEvaluator.insertSpacesBetweenTokens("+578"));

        // negative and positive floats
        assertEquals("-578.2", ExpressionEvaluator.insertSpacesBetweenTokens("-578.2"));
        assertEquals("-578.2468", ExpressionEvaluator.insertSpacesBetweenTokens("-578.2468"));
        assertEquals("-.2468", ExpressionEvaluator.insertSpacesBetweenTokens("-.2468"));
        assertEquals("+578.2", ExpressionEvaluator.insertSpacesBetweenTokens("+578.2"));
        assertEquals("+578.2468", ExpressionEvaluator.insertSpacesBetweenTokens("+578.2468"));
        assertEquals("+.2468", ExpressionEvaluator.insertSpacesBetweenTokens("+.2468"));

        // some number-only math
        assertEquals("1 + 2", ExpressionEvaluator.insertSpacesBetweenTokens("1+2"));
        assertEquals("1.5 + 2", ExpressionEvaluator.insertSpacesBetweenTokens("1.5+2"));
        assertEquals("1 + ( 3 - 1 )", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1+(3-1)")));
        assertEquals("( 1 ) + 5", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("(1)+5")));
        assertEquals("1 + 2 + 3 * 5 - 1.5 + 2 / 3 / 5 + 1 + ( 5 - 2 * 99 * 2.3 ) / 5.6 + ( 2.3 * 6 ) + 5 + .2",
                tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1+2+3*5-1.5+2/3/5+1+(5-2*99*2.3)/5.6+(2.3*6)+5+.2")));

        // try some variables
        assertEquals("1 + 2 * abc / 5.7 + qqq * vvv * ( 12.4 * a )",
                tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1+2*abc/5.7 + qqq*vvv*(12.4*a)")));

        // some comparators
        assertEquals("1 = 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1=4")));
        assertEquals("1 = 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 = 4")));
        assertEquals("1 > 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1>4")));
        assertEquals("1 > 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 > 4")));
        assertEquals("1 >= 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1>=4")));
        assertEquals("1 >= 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 >= 4")));
        assertEquals("1 < 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1<4")));
        assertEquals("1 < 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 < 4")));
        assertEquals("1 <= 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1<=4")));
        assertEquals("1 <= 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 <= 4")));
        assertEquals("1 <> 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1<>4")));
        assertEquals("1 <> 4", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("1 <> 4")));

        // string literals
        assertEquals("\"abc\"", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("\"abc\"")));
        assertEquals("\"abc\"  \"def\"", ExpressionEvaluator.insertSpacesBetweenTokens("\"abc\" \"def\""));
        assertEquals("\"abc\"  \"def\"  \"j\\sk\\sl\"", ExpressionEvaluator.insertSpacesBetweenTokens("\"abc\" \"def\" \"j k l\""));

        // square brackets
        assertEquals("X [ 4 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4]")));
        assertEquals("X [ 4 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4]    ")));
        assertEquals("X [ 4 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens(" X [ 4 ]")));
        assertEquals("X [ 4 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens(" X [ 4 ] ")));
        assertEquals("X [ 4 ][ 22 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4][22]")));
        assertEquals("X [ 4 ][ 22 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4] [22]")));
        assertEquals("X [ 4 ][ 22 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4]  [22]")));
        assertEquals("X [ 4 ][ 22 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4][22] ")));
        assertEquals("X [ 4 ][ 22 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens(" X [ 4 ] [ 22 ] ")));
        assertEquals("X [ 4 ][ yyy ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens(" X [ 4 ] [ yyy ] ")));
        assertEquals("X [ 4 ][ yyy ][ 1 ]", tidy(ExpressionEvaluator.insertSpacesBetweenTokens("X[4][yyy][1]")));
    }

    @Test
    public void testShuntingYard() throws BasicException {
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        String expression = "1+5";
        ValueNumber result = ExpressionEvaluator.evaluateExpression(expression, basicRunner).asNumber();
        assertEquals("Evaluation should work", 6L, result.asNumber().toLong());
    }

    /*
     * It's valid for the insertSpacesBetweenTokens to insert a few extra spaces in there. Those are taken out
     * when the string is tokenized. This method takes them out to make the tests easier to write.
     */
    private String tidy(String in) {
        String[] pieces = in.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String piece : pieces) {
            if (piece.length() > 0) {
                builder.append(piece);
                builder.append(' ');
            }
        }
        return builder.toString().trim();
    }

    @Test
    public void testEvaluatePostfixSimpleAddition() throws BasicException {
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        List<Token> postfix = new ArrayList<Token>();
        postfix.add(new TokenValueNumber(new ValueNumber(4), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(3), basicRunner));
        postfix.add(new TokenOperatorAddition(basicRunner));

        ValueNumber answer = ExpressionEvaluator.solvePostfix(postfix).asNumber();
        assertEquals("Wrong answer", 7, answer.asNumber().toLong());
    }

    // This is the example on http://en.wikipedia.org/wiki/Reverse_Polish_notation
    @Test
    public void testEvaluatePostfixMath() throws BasicException {
        // Infix: 5 + ((1 + 2) * 4) - 3
        // Postfix: 5 1 2 + 4 * 3 - +
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        List<Token> postfix = new ArrayList<Token>();
        postfix.add(new TokenValueNumber(new ValueNumber(5), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(1), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(2), basicRunner));
        postfix.add(new TokenOperatorAddition(basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(4), basicRunner));
        postfix.add(new TokenOperatorMultiplication(basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(3), basicRunner));
        postfix.add(new TokenOperatorSubtraction(basicRunner));
        postfix.add(new TokenOperatorAddition(basicRunner));

        ValueNumber answer = ExpressionEvaluator.solvePostfix(postfix).asNumber();
        assertEquals("Wrong answer", 14, answer.asNumber().toLong());
    }

    // This is the example on http://en.wikipedia.org/wiki/Shunting-yard_algorithm
    @Test
    public void testEvaluatePostfixMathExample() throws BasicException {
        // Infix: 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3
        // Postfix: 3 4 2 * 1 5 - 2 3 ^ ^ / +
        // output:  3 4 + 2 * 1 5 - / 2 ^ 3 ^
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        List<Token> postfix = new ArrayList<Token>();
        postfix.add(new TokenValueNumber(new ValueNumber(3), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(4), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(2), basicRunner));
        postfix.add(new TokenOperatorMultiplication(basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(1), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(5), basicRunner));
        postfix.add(new TokenOperatorSubtraction(basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(2), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(3), basicRunner));
        postfix.add(new TokenOperatorExponent(basicRunner));
        postfix.add(new TokenOperatorExponent(basicRunner));
        postfix.add(new TokenOperatorDivision(basicRunner));
        postfix.add(new TokenOperatorAddition(basicRunner));

        ValueNumber answer = ExpressionEvaluator.solvePostfix(postfix).asNumber();
        assertEquals("Wrong answer", "3.000122", answer.toString());
    }

    // This is the example on http://en.wikipedia.org/wiki/Shunting-yard_algorithm
    @Test
    public void testEvaluatePostfixMathAnother() throws BasicException {
        // Infix: 1 + 5 - 4
        // postfix: 1 5 4 - +
        BasicRunner basicRunner = new BasicTestHelper().getBasicRunner();
        List<Token> postfix = new ArrayList<Token>();
        postfix.add(new TokenValueNumber(new ValueNumber(1), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(5), basicRunner));
        postfix.add(new TokenValueNumber(new ValueNumber(4), basicRunner));
        postfix.add(new TokenOperatorSubtraction(basicRunner));
        postfix.add(new TokenOperatorAddition(basicRunner));

        ValueNumber answer = ExpressionEvaluator.solvePostfix(postfix).asNumber();
        assertEquals("Wrong answer", "2", answer.toString());
    }

}
