package com.funwithbasic.basic;

import com.funwithbasic.basic.testhelper.BasicTestHelper;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.function.TokenFunction;
import com.funwithbasic.basic.token.value.TokenValueNumber;
import com.funwithbasic.basic.token.value.TokenValueString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestFunctions {
    BasicRunner basicRunner;
    Token A;
    Token abc;
    Token def;
    Token negativeTwo;
    Token negativePointFive;
    Token zero;
    Token pointFive;
    Token piOverFour;
    Token one;
    Token piOverTwo;
    Token two;
    Token three;
    Token pi;
    Token e;
    Token four;
    Token nine;
    Token ten;
    Token sixtyFive;
    Token tenThousand;
    Token millionAsString;
    Token negativeOnePointFiveAsString;
    Token negativePointFiveAsString;
    Token invalidNumberAsString;
    Token invalidNumberAsString2;

    public TestFunctions() throws BasicException {
        basicRunner = new BasicTestHelper().getBasicRunner();
        A = Token.create("\"A\"", basicRunner);
        abc = Token.create("\"abc\"", basicRunner);
        def = Token.create("\"def\"", basicRunner);
        millionAsString = Token.create("\"1000000\"", basicRunner);
        negativeOnePointFiveAsString = Token.create("\"-1.5\"", basicRunner);
        negativePointFiveAsString = Token.create("\"-.5\"", basicRunner);
        invalidNumberAsString = Token.create("\"123abc\"", basicRunner);
        invalidNumberAsString2 = Token.create("\"123.456.789\"", basicRunner);
        negativeTwo = Token.create("-2", basicRunner);
        negativePointFive = Token.create("-0.5", basicRunner);
        zero = Token.create("0", basicRunner);
        pointFive = Token.create("0.5", basicRunner);
        one = Token.create("1", basicRunner);
        two = Token.create("2", basicRunner);
        three = Token.create("3", basicRunner);
        pi = Token.create("3.1415926535", basicRunner);
        e = Token.create("2.71828182845", basicRunner);
        four = Token.create("4", basicRunner);
        ten = Token.create("10", basicRunner);
        nine = Token.create("9", basicRunner);
        sixtyFive = Token.create("65", basicRunner);
        piOverTwo = Token.create("1.570796327", basicRunner);
        piOverFour = Token.create("0.785398163", basicRunner);
        tenThousand = Token.create("10000", basicRunner);
    }

    // String and character-related functions

    @Test
    public void TestFunctionIsNum() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.ISNUM.getText(), basicRunner));
        assertFalse(((TokenValueNumber) (token.evaluate(Arrays.asList(abc)))).getValue().isTrue());
        assertTrue(((TokenValueNumber) (token.evaluate(Arrays.asList(millionAsString)))).getValue().isTrue());
        assertTrue(((TokenValueNumber) (token.evaluate(Arrays.asList(negativeOnePointFiveAsString)))).getValue().isTrue());
        assertTrue(((TokenValueNumber) (token.evaluate(Arrays.asList(negativePointFiveAsString)))).getValue().isTrue());
        try {
            token.evaluate(Arrays.asList(one));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionStrlen() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.STRLEN.getText(), basicRunner));
        assertEquals("3", ((TokenValueNumber) (token.evaluate(Arrays.asList(abc)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(one));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionNum2Str() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.NUM2STR.getText(), basicRunner));
        assertEquals("0", ((TokenValueString) (token.evaluate(Arrays.asList(zero)))).getValue().print());
        assertEquals("-0.5", ((TokenValueString) (token.evaluate(Arrays.asList(negativePointFive)))).getValue().print());
        assertEquals("2", ((TokenValueString) (token.evaluate(Arrays.asList(two)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(invalidNumberAsString));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(invalidNumberAsString2));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionStr2Num() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.STR2NUM.getText(), basicRunner));
        assertEquals("-1.5", ((TokenValueNumber) (token.evaluate(Arrays.asList(negativeOnePointFiveAsString)))).getValue().print());
        assertEquals("-0.5", ((TokenValueNumber) (token.evaluate(Arrays.asList(negativePointFiveAsString)))).getValue().print());
        assertEquals("1000000", ((TokenValueNumber) (token.evaluate(Arrays.asList(millionAsString)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(one));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(invalidNumberAsString));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(invalidNumberAsString2));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionAsc() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.ASC.getText(), basicRunner));
        assertEquals("65", ((TokenValueNumber) (token.evaluate(Arrays.asList(A)))).getValue().print());
        assertEquals("97", ((TokenValueNumber) (token.evaluate(Arrays.asList(abc)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(three));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionCharAt() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.CHARAT.getText(), basicRunner));
        assertEquals("A", ((TokenValueString) (token.evaluate(Arrays.asList(A, zero)))).getValue().print());
        assertEquals("a", ((TokenValueString) (token.evaluate(Arrays.asList(abc, zero)))).getValue().print());
        assertEquals("a", ((TokenValueString) (token.evaluate(Arrays.asList(abc, pointFive)))).getValue().print());
        assertEquals("b", ((TokenValueString) (token.evaluate(Arrays.asList(abc, one)))).getValue().print());
        assertEquals("c", ((TokenValueString) (token.evaluate(Arrays.asList(abc, two)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc, three));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(abc, negativeTwo));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionChr() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.CHR.getText(), basicRunner));
        assertEquals("A", ((TokenValueString) (token.evaluate(Arrays.asList(sixtyFive)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(negativeTwo));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(tenThousand));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionConcat() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.CONCAT.getText(), basicRunner));
        assertEquals("Aabc", ((TokenValueString) (token.evaluate(Arrays.asList(A, abc)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(negativeTwo));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionParams2() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.PARAMS2.getText(), basicRunner));
        assertEquals("1,2", ((TokenValueString) (token.evaluate(Arrays.asList(one, two)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(negativeTwo));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    // Math functions

    @Test
    public void TestFunctionAbs() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.ABS.getText(), basicRunner));
        assertEquals("2", ((TokenValueNumber) (token.evaluate(Arrays.asList(negativeTwo)))).getValue().print());
        assertEquals("2", ((TokenValueNumber) (token.evaluate(Arrays.asList(two)))).getValue().print());
        assertEquals("0.5", ((TokenValueNumber) (token.evaluate(Arrays.asList(pointFive)))).getValue().print());
        assertEquals("0.5", ((TokenValueNumber) (token.evaluate(Arrays.asList(pointFive)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionSin() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.SIN.getText(), basicRunner));
        assertEquals("1", ((TokenValueNumber) (token.evaluate(Arrays.asList(piOverTwo)))).getValue().print());
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(zero)))).getValue().print());
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(pi)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionCos() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.COS.getText(), basicRunner));
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(piOverTwo)))).getValue().print());
        assertEquals("-1", ((TokenValueNumber) (token.evaluate(Arrays.asList(pi)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionTan() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.TAN.getText(), basicRunner));
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(zero)))).getValue().print());
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(pi)))).getValue().print());
        assertEquals("1", ((TokenValueNumber) (token.evaluate(Arrays.asList(piOverFour)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionLn() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.LN.getText(), basicRunner));
        assertEquals("1", ((TokenValueNumber) (token.evaluate(Arrays.asList(e)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(zero));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionLog() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.LOG.getText(), basicRunner));
        assertEquals("1", ((TokenValueNumber) (token.evaluate(Arrays.asList(ten)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(zero));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionSqrt() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.SQRT.getText(), basicRunner));
        assertEquals("3", ((TokenValueNumber) (token.evaluate(Arrays.asList(nine)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
        try {
            token.evaluate(Arrays.asList(negativeTwo));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionInt() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.INT.getText(), basicRunner));
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(piOverFour)))).getValue().print());
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(zero)))).getValue().print());
        assertEquals("3", ((TokenValueNumber) (token.evaluate(Arrays.asList(pi)))).getValue().print());
        assertEquals("-2", ((TokenValueNumber) (token.evaluate(Arrays.asList(negativeTwo)))).getValue().print());
        assertEquals("0", ((TokenValueNumber) (token.evaluate(Arrays.asList(negativePointFive)))).getValue().print());
        try {
            token.evaluate(Arrays.asList(abc));
            fail("Should have thrown");
        }
        catch (BasicException e) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void TestFunctionRnd() throws BasicException {
        TokenFunction token = (TokenFunction)(Token.create(TokenFunction.Function.RND.getText(), basicRunner));
        double random = ((TokenValueNumber) (token.evaluate(new ArrayList<Token>()))).getValue().toDouble();
        assertTrue(random > 0.0D && random < 1.0D);
    }

}
