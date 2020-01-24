package com.funwithbasic.basic;

import com.funwithbasic.basic.value.ValueNumber;

public class Constants {

    // names of constants start with this value
    public static final String CONST_PREFIX = "_";

    // this constant depends on the value sent in by whatever calls this code
    public static final String CONST_NAME_VERSION = CONST_PREFIX + "VERSION";

    public static final String CONST_NAME_AUTHORS = CONST_PREFIX + "AUTHORS";
    public static final String CONST_VALUE_AUTHORS = "Eric Wadsworth";

    public static final String CONST_NAME_PI = CONST_PREFIX + "PI";
    public static final ValueNumber CONST_VALUE_PI = new ValueNumber(Math.PI);

    // these are the results of conditionals
    public static final String CONST_NAME_TRUE = CONST_PREFIX + "TRUE";
    public static final ValueNumber CONST_VALUE_TRUE = new ValueNumber(ValueNumber.TRUE);
    public static final String CONST_NAME_FALSE = CONST_PREFIX + "FALSE";
    public static final ValueNumber CONST_VALUE_FALSE = new ValueNumber(ValueNumber.FALSE);

}
