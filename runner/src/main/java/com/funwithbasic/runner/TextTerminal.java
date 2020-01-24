package com.funwithbasic.runner;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.KeyboardInputInterface;
import com.funwithbasic.basic.interfaces.TextTerminalInterface;

import javax.swing.*;
import java.awt.*;

public class TextTerminal extends JPanel implements TextTerminalInterface {

    public static final int DEFAULT_ROWS = 24;
    public static final int DEFAULT_COLUMNS = 40;

    public static final int MAX_ROWS = 256;
    public static final int MAX_COLUMNS = 256;

    private int numRows;
    private int numColumns;

    private boolean isCursorVisible;
    private int currentCursorPositionRow;
    private int currentCursorPositionColumn;

    private boolean doesExist;

    private TextPanel textPanel;
    private KeyboardInput keyboardInput;

    public TextTerminal() {
        doesExist = false;
    }

    @Override
    public boolean doesExist() {
        return doesExist;
    }

    @Override
    public void create(KeyboardInputInterface keyboardInput) {
        try {
            create(keyboardInput, DEFAULT_ROWS, DEFAULT_COLUMNS);
        }
        catch (BasicException be) {
            throw new RuntimeException("Default text area sizes are invalid", be);
        }
    }

    @Override
    public void create(KeyboardInputInterface keyboardInput, int numRows, int numColumns) throws BasicException {
        if (doesExist) {
            throw new BasicException("Text terminal already exists, destroy it first");
        }
        if (numRows >= MAX_ROWS || numRows < 1 || numColumns >= MAX_COLUMNS || numColumns < 1) {
            throw new BasicException("Illegal text terminal size");
        }
        this.numRows = numRows;
        this.numColumns = numColumns;

        doesExist = true;

        setLayout(new BorderLayout());

        textPanel = new TextPanel(numRows, numColumns);
        if (keyboardInput != null) {
            this.keyboardInput = (KeyboardInput) keyboardInput;
        }
        add(textPanel);

        setSize(1, 1);

        setVisible(true);
        setCursorVisibility(keyboardInput, true);

        home(keyboardInput);
    }

    @Override
    public void destroy() {
        doesExist = false;
        setVisible(false);
        if (textPanel != null) {
            textPanel.removeKeyListener(keyboardInput);
            remove(textPanel);
            textPanel = null;
        }
    }

    @Override
    public void home(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                textPanel.putCharacterAtPosition(row, column, ' ');
            }
        }
        setCursorPosition(keyboardInput, 0, 0);
    }

    @Override
    public void setCursorPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        currentCursorPositionRow = row;
        currentCursorPositionColumn = column;
        textPanel.setCursorPosition(currentCursorPositionRow, currentCursorPositionColumn);
    }

    @Override
    public int getCursorPositionRow(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return currentCursorPositionRow;
    }

    @Override
    public int getCursorPositionColumn(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return currentCursorPositionColumn;
    }

    @Override
    public int getCharacterAtPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        return textPanel.getCharacterAtPosition(row, column);
    }

    @Override
    public void putStringAtPosition(KeyboardInputInterface keyboardInput, int row, int column, String string) throws BasicException {
        ensureExists(keyboardInput);
        verifyCoordinates(row, column);
        writeString(row, column, string, false, false);
    }

    @Override
    public void print(KeyboardInputInterface keyboardInput, String text, boolean includeNewLine) throws BasicException {
        ensureExists(keyboardInput);
        writeString(currentCursorPositionRow, currentCursorPositionColumn, text, includeNewLine, true);
    }

    @Override
    public void setCursorVisibility(KeyboardInputInterface keyboardInput, boolean isVisible) throws BasicException {
        ensureExists(keyboardInput);
        isCursorVisible = isVisible;
        textPanel.setCursorVisibility(isCursorVisible);
    }

    @Override
    public boolean isCursorVisible(KeyboardInputInterface keyboardInput) throws BasicException {
        ensureExists(keyboardInput);
        return isCursorVisible;
    }

    public void backspace(int limitRow, int limitColumn) {
        if (currentCursorPositionRow == limitRow && currentCursorPositionColumn == limitColumn) {
            return;
        }

        if (currentCursorPositionRow == limitRow) {
            currentCursorPositionColumn--;
        } else {
            if (currentCursorPositionColumn > 0) {
                currentCursorPositionColumn--;
            } else {
                currentCursorPositionRow--;
                currentCursorPositionColumn = numColumns - 1;
            }
        }
        textPanel.putCharacterAtPosition(currentCursorPositionRow, currentCursorPositionColumn, ' ');
        textPanel.setCursorPosition(currentCursorPositionRow, currentCursorPositionColumn);
    }

    private void verifyCoordinates(int row, int column) throws BasicException {
        if (row >= numRows || column >= numColumns || row < 0 || column < 0) {
            throw new BasicException("Attempted to set invalid cursor position");
        }
    }

    private void ensureExists(KeyboardInputInterface keyboardInput) throws BasicException {
        if (!doesExist) {
            create(keyboardInput);
        }
        setVisible(true);
    }

    private void writeString(int startRow, int startColumn, String string, boolean includeNewline, boolean updateCursorPosition) {
        textPanel.setCursorVisibility(false);
        int row = startRow;
        int col = startColumn;
        for (int i = 0; i < string.length(); i++) {
            textPanel.putCharacterAtPosition(row, col++, string.charAt(i));
            if (col == numColumns) {
                col = 0;
                row++;
            }
            if (row == numRows) {
                textPanel.scrollUp();
                row--;
            }
        }

        if (includeNewline) {
            col = 0;
            row++;
            if (row == numRows) {
                textPanel.scrollUp();
                row--;
            }
        }

        if (updateCursorPosition) {
            currentCursorPositionRow = row;
            currentCursorPositionColumn = col;
            textPanel.setCursorPosition(currentCursorPositionRow, currentCursorPositionColumn);
        }

        textPanel.setCursorVisibility(isCursorVisible);
    }

    public void printTerminationMessage() {
        if (textPanel != null) {
            textPanel.freezeLabels(false);
            try {
                putStringAtPosition(keyboardInput, numRows - 1, 0, "---> done <---");
            }
            catch (BasicException be) {
                // do nothing
            }
        }
    }

    @Override
    public void reset() {
        try {
            home(keyboardInput);
        } catch (BasicException ignored) {
        }
    }

}
