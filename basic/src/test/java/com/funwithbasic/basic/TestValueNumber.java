package com.funwithbasic.basic;

import com.funwithbasic.basic.value.ValueNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestValueNumber {

    @Test
    public void testMathOperations() {

        // output should look like integers, if there is no fractional component.
        ValueNumber a = new ValueNumber(1);
        assertEquals("2", ValueNumber.add(a, a).toString());

        // if there are fractional components, we should see them.
        ValueNumber b = new ValueNumber(1.5D);
        assertEquals("2.5", ValueNumber.add(a, b).toString());

        // digits out of the precision range don't matter. Verify this.
        ValueNumber c = new ValueNumber(7.9999999999999911D);
        assertEquals("9", ValueNumber.add(a, c).toString());
        assertEquals("9.5", ValueNumber.add(b, c).toString());
    }

    @Test
    public void testToLong() {
        assertEquals(1L, new ValueNumber(1).toLong());
        assertEquals(0L, new ValueNumber(0.5).toLong());
        assertEquals(0L, new ValueNumber(-0.5).toLong());
        assertEquals(1L, new ValueNumber(1.3).toLong());
        assertEquals(1L, new ValueNumber(1.7).toLong());
        assertEquals(-1L, new ValueNumber(-1.7).toLong());
        assertEquals(-5L, new ValueNumber(-5).toLong());
    }
}
