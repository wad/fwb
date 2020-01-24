package com.funwithbasic.basic.testhelper;

import com.funwithbasic.basic.interfaces.KeyboardInputInterface;

public class KeyboardInputForTest implements KeyboardInputInterface {

    public static final int KEY_TYPED_BACKSPACE = 8;
    public static final int KEY_CODE_ENTER = 10;

    public KeyboardInputForTest() {
    }

    @Override
    public String readLine() {
        return "LINE READ";
    }

    @Override
    public int readKeypress() {
        return 'j'; // todo
    }

    @Override
    public int getNumCharactersWaitingInBuffer() {
        return 0; // todo
    }

    @Override
    public void clearBuffer() {
    }

    @Override
    public boolean isValidCharacter(int c) {
        return isValidPrintableCharacter(c) || isValidControlCharacter(c);
    }

    @Override
    public boolean isValidPrintableCharacter(int c) {
        return c >= 32 && c <= 126;
    }

    @Override
    public boolean isValidControlCharacter(int c) {
        switch (c) {
            case KEY_CODE_ENTER:
            case KEY_TYPED_BACKSPACE:
                return true;
        }
        return false;
    }

    @Override
    public void reset() {
        // do nothing
    }

}
