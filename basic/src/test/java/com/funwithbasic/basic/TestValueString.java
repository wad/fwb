package com.funwithbasic.basic;

import com.funwithbasic.basic.interfaces.KeyboardInputInterface;
import com.funwithbasic.basic.testhelper.KeyboardInputForTest;
import com.funwithbasic.basic.value.ValueString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestValueString {

    @Test
    public void testValueString() throws BasicException {
        KeyboardInputInterface keyboardInput = new KeyboardInputForTest();
        assertEquals("abc", new ValueString("abc", keyboardInput).getValue());
        assertEquals("abc def ghi", new ValueString("abc def ghi", keyboardInput).getValue());
        assertEquals("abc def ghi", ValueString.convertFromLiteral("\"abc\\sdef\\sghi\""));
        assertEquals("abc\\def\\ghi", ValueString.convertFromLiteral("\"abc\\\\def\\\\ghi\""));
    }
    
}
