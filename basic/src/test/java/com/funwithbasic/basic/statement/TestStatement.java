package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStatement {

    @Test
    public void testHasLineNumber() {
        assertFalse(Statement.checkForLineNumberLabel("abc def"));
        assertTrue(Statement.checkForLineNumberLabel("10 abc"));
        assertTrue(Statement.checkForLineNumberLabel("0 abc"));
    }

    @Test
    public void testGetLineNumber() throws BasicException {
        assertEquals(4, Statement.getLineNumberLabel("4 abc def"));
        assertEquals(6, Statement.getLineNumberLabel("6  abc def"));
        assertEquals(1000, Statement.getLineNumberLabel("1000 abc"));
        assertEquals(0, Statement.getLineNumberLabel("0 3 abc"));

        try {
            Statement.getLineNumberLabel("99999999999999999 xyz");
            fail("Should have thrown");
        }
        catch (BasicException be) {
            assertTrue("expected", true);
        }
    }

    @Test
    public void testGetCodeAfterLineNumber() {
        assertEquals("abc def ghi", Statement.getCodeAfterLineNumberLabel("15   abc def ghi "));
    }

}
