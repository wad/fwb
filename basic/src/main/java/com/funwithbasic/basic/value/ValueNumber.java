package com.funwithbasic.basic.value;

import com.funwithbasic.basic.BasicException;

public class ValueNumber extends Value {

    public static final char DECIMAL_POINT = '.';

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    private static final int DIGITS_OF_PRECISION = 6;

    private double value;

    public ValueNumber() {
        value = 0D;
    }

    public ValueNumber(String stringValue) throws BasicException {
        try {
            value = Double.parseDouble(stringValue);
        }
        catch (NumberFormatException e) {
            throw new BasicException("Could not parse number: [" + stringValue + "]");
        }
    }

    public ValueNumber(int intValue) {
        value = (double)intValue;
    }

    public ValueNumber(double doubleValue) {
        value = doubleValue;
    }

    public ValueNumber(boolean booleanValue) {
        value = booleanValue ? TRUE : FALSE;
    }

    public boolean isTrue() {
        return value > (double)FALSE;
    }

    public static boolean isEqualTo(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() == b.getValueRoundedToPrecision();
    }

    public static boolean isNotEqualTo(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() != b.getValueRoundedToPrecision();
    }

    public static boolean isGreaterThan(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() > b.getValueRoundedToPrecision();
    }

    public static boolean isGreaterThanOrEqualsTo(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() >= b.getValueRoundedToPrecision();
    }

    public static boolean isLessThan(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() < b.getValueRoundedToPrecision();
    }

    public static boolean isLessThanOrEqualTo(ValueNumber a, ValueNumber b) {
        return a.getValueRoundedToPrecision() <= b.getValueRoundedToPrecision();
    }

    public static ValueNumber add(ValueNumber a, ValueNumber b) {
        return new ValueNumber(a.value + b.value);
    }

    public static ValueNumber subtract(ValueNumber a, ValueNumber b) {
        return new ValueNumber(a.value - b.value);
    }

    public static ValueNumber multiply(ValueNumber a, ValueNumber b) {
        return new ValueNumber(a.value * b.value);
    }

    public static ValueNumber divide(ValueNumber a, ValueNumber b) throws BasicException {
        if (b.value == 0D) {
            throw new BasicException("Division by zero error");
        }
        return new ValueNumber(a.value / b.value);
    }

    public static ValueNumber modulus(ValueNumber a, ValueNumber b) {
        return new ValueNumber(a.value % b.value);
    }

    public static ValueNumber exponent(ValueNumber a, ValueNumber b) {
        return new ValueNumber(Math.pow(a.value, b.value));
    }

    @Override
    public String print() throws BasicException {
        String result = toString();
        int indexOfE = result.indexOf('E');
        if (indexOfE >= 0) {
            throw new BasicException("Number too large to print: " + result);
        }
        return result;
    }

    @Override
    public String toString() {
        double rounded = getValueRoundedToPrecision();
        // anything after the decimal?
        if (Math.round(rounded) == rounded) {
            return String.valueOf(Long.valueOf((long)rounded));
        }
        else {
            return String.valueOf(rounded);
        }
    }

    /**
     * This chops off any fractional component. Negative numbers thus move towards zero.
     *
     * @return the whole number part
     */
    public long toLong() {
        long result = (long)(Math.floor(Math.abs(value)));
        return value < 0 ? -1 * result : result;
    }

    public double toDouble() {
        return value;
    }

    private double getValueRoundedToPrecision() {
        return round(value, DIGITS_OF_PRECISION);
    }

    private static double round(double a, int digits) {
        double shiftFactor = Math.pow(10, digits);
        a = a * shiftFactor;
        return ((double) Math.round(a)) / shiftFactor;
    }

}
