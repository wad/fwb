package com.funwithbasic.basic.testhelper;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.KeyboardInputInterface;
import com.funwithbasic.basic.interfaces.TextTerminalInterface;

public class TextTerminalForTest extends TestLogger implements TextTerminalInterface {

    public static final int DEFAULT_SIZE_COLUMNS = 80;
    public static final int DEFAULT_SIZE_ROWS = 23;

    private static final int MAX_SIZE_COLUMNS = 1000;
    private static final int MAX_SIZE_ROWS = 1000;

    private int sizeColumns;
    private int sizeRows;

    private int posColumns;
    private int posRows;

    private boolean doesExist;

    public TextTerminalForTest() {
        super();
        doesExist = false;
    }

    @Override
    public boolean doesExist() {
        return doesExist;
    }

    @Override
    public void create(KeyboardInputInterface keyboardInput, int numRows, int numColumns) throws BasicException {
        if (doesExist) {
            throw new BasicException("TextArea already exists, destroy it first");
        }
        if (numColumns < 1 || numColumns >= MAX_SIZE_COLUMNS || numRows < 1 || numRows >= MAX_SIZE_ROWS) {
            throw new BasicException("Illegal size");
        }
        this.sizeRows = numRows;
        this.sizeColumns = numColumns;
        posColumns = 0;
        posRows = 0;
        doesExist = true;
    }

    @Override
    public void create(KeyboardInputInterface keyboardInput) {
        try {
            create(keyboardInput, DEFAULT_SIZE_ROWS, DEFAULT_SIZE_COLUMNS);
        }
        catch (BasicException be) {
            throw new RuntimeException("Default text area sizes are invalid", be);
        }
    }

    @Override
    public void destroy() {
        doesExist = false;
    }

    @Override
    public void home(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
    }

    @Override
    public void setCursorPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        this.posRows = row;
        this.posColumns = column;
    }

    @Override
    public int getCursorPositionRow(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return posRows;
    }

    @Override
    public int getCursorPositionColumn(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return posColumns;
    }

    @Override
    public int getCharacterAtPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        return 0; // todo
    }

    @Override
    public void putStringAtPosition(KeyboardInputInterface keyboardInput, int row, int column, String string) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        // todo
    }

    @Override
    public void print(KeyboardInputInterface keyboardInput, String stringToPrint, boolean includeNewline) throws BasicException {
        ensureExists(keyboardInput);
        appendToLog(stringToPrint);
        if (includeNewline) {
            appendToLog("\n");
        }
    }

    @Override
    public void setCursorVisibility(KeyboardInputInterface keyboardInput, boolean isVisible) throws BasicException {
        ensureExists(keyboardInput);
    }

    @Override
    public boolean isCursorVisible(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return true; // todo
    }

    private void verifyCoordinates(int posRow, int posColumn) throws BasicException {
        if (posRow >= sizeRows || posColumn >= sizeColumns || posRow < 0 || posColumn < 0) {
            throw new BasicException("Attempted to set invalid cursor position");
        }
    }

    private void ensureExists(KeyboardInputInterface keyboardInput) throws BasicException {
        if (!doesExist) {
            create(keyboardInput);
        }
    }

    @Override
    public void reset() {
        // do nothing
    }

}
