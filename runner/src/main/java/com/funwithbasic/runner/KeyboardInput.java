package com.funwithbasic.runner;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.KeyboardInputInterface;
import com.funwithbasic.basic.interfaces.TextTerminalInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class KeyboardInput implements KeyboardInputInterface, KeyListener {

    // This value is added to the keycode returned from the keyReleased() method, to distinguish
    // them from the codes from the isTyped() method.
    private static final int IS_RELEASED = 10000;

    // This is what comes back if an invalid key is pressed, or if no key was pressed
    private static final int KEY_INVALID = 0;

    // these ones are actually captured by the keyTyped() method.
    private static final int KEY_BACKSPACE = 8;
    private static final int KEY_LINEFEED = 10; // this is the ENTER key
    private static final int KEY_ESCAPE = 27;

    // these ones are only captured on the keyReleased() method.
    private static final int KEY_DELETE = IS_RELEASED + 127;
    private static final int KEY_PGUP = IS_RELEASED + 33;
    private static final int KEY_PGDN = IS_RELEASED + 34;
    private static final int KEY_END = IS_RELEASED + 35;
    private static final int KEY_HOME = IS_RELEASED + 36;
    private static final int KEY_ARROW_LEFT = IS_RELEASED + 37;
    private static final int KEY_ARROW_UP = IS_RELEASED + 38;
    private static final int KEY_ARROW_RIGHT = IS_RELEASED + 39;
    private static final int KEY_ARROW_DOWN = IS_RELEASED + 40;
    private static final int KEY_INSERT = IS_RELEASED + 155;

    private static final int FIRST_VALID_ASCII_PRINTABLE_CHARACTER = 32;
    private static final int LAST_VALID_ASCII_PRINTABLE_CHARACTER = 126;

     // Any keys typed after the buffer is full are ignored until some are consumed.
    public static int KEY_BUFFER_SIZE = 20;
    private Vector<Integer> keypressBuffer;

    // This is needed so that if the user clicks "BREAK" while in an INPUT command, it works
    private boolean breakRequested;

    // This is needed so that the INPUT command can echo the user's keypresses to the text terminal
    private TextTerminal textTerminal;

    public KeyboardInput(TextTerminalInterface textTerminal) {
        this.textTerminal = (TextTerminal)textTerminal;
        reset();
    }

    @Override
    public String readLine() throws BasicException {
        int startPosRow = textTerminal.getCursorPositionRow(this);
        int startPosColumn = textTerminal.getCursorPositionColumn(this);
        StringBuilder line = new StringBuilder();
        boolean keepGettingCharacters = true;
        while (keepGettingCharacters && !breakRequested) {

            if (!keypressBuffer.isEmpty()) {
                int key = keypressBuffer.get(0);
                keypressBuffer.remove(0);
                if (key == KEY_LINEFEED) {
                    textTerminal.print(this, "", true);
                    keepGettingCharacters = false;
                }
                else if (key == KEY_BACKSPACE) {
                    if (line.length() > 0) {
                        line.deleteCharAt(line.length() - 1);
                        textTerminal.backspace(startPosRow, startPosColumn);
                    }
                }
                else if (isValidPrintableCharacter(key)) {
                    line.append(String.valueOf((char)key));
                    textTerminal.print(this, String.valueOf((char)key), false);
                }
            }
            Thread.yield();
        }
        breakRequested = false;
        return line.toString();
    }

    @Override
    public int readKeypress() {
        while (!keypressBuffer.isEmpty()) {
            int key = keypressBuffer.get(0);
            keypressBuffer.remove(0);
            if (isValidCharacter(key)) {
                return key;
            }
        }
        return KEY_INVALID;
    }

    @Override
    public int getNumCharactersWaitingInBuffer() {
        return keypressBuffer.size();
    }

    @Override
    public void clearBuffer() {
        keypressBuffer.clear();
    }

    // Used to handle the case where the user BREAKs the execution in the middle of an INPUT command.
    public void breakRequested() {
        breakRequested = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (keypressBuffer.size() < KEY_BUFFER_SIZE) {
            int key = (int) e.getKeyChar();
            keypressBuffer.add(key);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // not checking this
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // don't accept any more characters until there is room in the buffer
        if (keypressBuffer.size() < KEY_BUFFER_SIZE) {
            int key = IS_RELEASED + e.getKeyCode();
            if (isValidControlCharacter(key) && !codeAppearsOnBothKeyTypedAndKeyReleasedEvents(key)) {
                keypressBuffer.add(key);
            }
        }
    }

    /*
     * Some keys appear both in the keyTyped() and keyReleased() method. This identifies those.
     *
     * @param key the code of the key that we're checking
     * @return true if it's one of the ones that appears on both methods.
     */
    private boolean codeAppearsOnBothKeyTypedAndKeyReleasedEvents(int key) {
        switch (key) {
            case KEY_BACKSPACE:
            case KEY_LINEFEED:
                return true;
        }
        return false;
    }

    @Override
    public boolean isValidCharacter(int c) {
        return isValidPrintableCharacter(c) || isValidControlCharacter(c);
    }

    @Override
    public boolean isValidPrintableCharacter(int c) {
        return c >= FIRST_VALID_ASCII_PRINTABLE_CHARACTER && c <= LAST_VALID_ASCII_PRINTABLE_CHARACTER;
    }

    @Override
    public boolean isValidControlCharacter(int c) {
        switch (c) {
            case KEY_LINEFEED:
            case KEY_BACKSPACE:
            case KEY_ESCAPE:
            case KEY_DELETE:
            case KEY_PGUP:
            case KEY_PGDN:
            case KEY_END:
            case KEY_HOME:
            case KEY_ARROW_LEFT:
            case KEY_ARROW_UP:
            case KEY_ARROW_RIGHT:
            case KEY_ARROW_DOWN:
            case KEY_INSERT:
                return true;
        }
        return false;
    }

    @Override
    public void reset() {
        keypressBuffer = new Vector<Integer>(KEY_BUFFER_SIZE);
        breakRequested = false;
    }

}
