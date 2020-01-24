package com.funwithbasic.basic.value;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.KeyboardInputInterface;

public class ValueString extends Value {

    public static final char LITERAL_DELIMITER = '"';
    public static final char ESCAPE_INDICATOR = '\\';
    public static final char ESCAPE_INDICATOR_BACKSLASH = '\\';
    public static final char ESCAPE_INDICATOR_SPACE = 's';

    private String value;
    private KeyboardInputInterface keyboardInput;

    public ValueString(String value, KeyboardInputInterface keyboardInput) throws BasicException {
        this.keyboardInput = keyboardInput;
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) throws BasicException {
        for (int i = 0; i < value.length(); i++) {
            if (!keyboardInput.isValidPrintableCharacter(value.charAt(i))) {
                int charValue = value.charAt(i);
                throw new BasicException("Invalid character detected in String [" + value +
                        "]. Position=" + i + ". Value=" + charValue);
            }
        }
    }

    /**
     * Get the string, with no surrounding quotes.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    public String getValueWithQuotes() {
        return LITERAL_DELIMITER + value + LITERAL_DELIMITER;
    }

    @Override
    public String print() {
        return getValue();
    }

    public static int strlen(ValueString a) {
        return a.value.length();
    }

    public static ValueString concat(ValueString a, ValueString b) throws BasicException {
        return new ValueString(a.value + b.value, a.keyboardInput);
    }

    public static boolean isEqualTo(ValueString a, ValueString b) {
        return a.value.equals(b.value);
    }

    public static ValueString charAt(ValueString a, int b) throws BasicException {
        if (b < 0 || b >= a.value.length()) {
            throw new BasicException("Invalid index specified: " + b);
        }
        return new ValueString(String.valueOf(a.value.charAt(b)), a.keyboardInput);
    }

    public static String convertFromLiteral(String in) {
        if (in == null || in.length() < 2) {
            throw new RuntimeException("Invalid string literal: [" + in + "]");
        }

        if (in.charAt(0) != LITERAL_DELIMITER && in.charAt(in.length()) != LITERAL_DELIMITER) {
            throw new RuntimeException("Missing quotes on string literal: [" + in + "]");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < in.length() - 1; i++) {
            char c = in.charAt(i);
            if (c == ESCAPE_INDICATOR) {
                if (i == in.length() - 2) {
                    throw new RuntimeException("Invalid escape character in: [" + in + "]");
                }
                char nextCharacter = in.charAt(i + 1);
                if (nextCharacter == ESCAPE_INDICATOR_BACKSLASH) {
                    builder.append('\\');
                    i++;
                } else if (nextCharacter == ESCAPE_INDICATOR_SPACE) {
                    builder.append(' ');
                    i++;
                } else {
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
