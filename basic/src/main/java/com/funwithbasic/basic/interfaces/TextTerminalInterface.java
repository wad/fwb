package com.funwithbasic.basic.interfaces;

import com.funwithbasic.basic.BasicException;

public interface TextTerminalInterface {

    boolean doesExist();

    void create(KeyboardInputInterface keyboardInput, int numRows, int numColumns) throws BasicException;

    void create(KeyboardInputInterface keyboardInput) throws BasicException;

    void home(KeyboardInputInterface keyboardInput) throws BasicException;

    void setCursorPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException;

    int getCursorPositionRow(KeyboardInputInterface keyboardInput) throws BasicException;

    int getCursorPositionColumn(KeyboardInputInterface keyboardInput) throws BasicException;

    int getCharacterAtPosition(KeyboardInputInterface keyboardInput, int row, int column) throws BasicException;

    void putStringAtPosition(KeyboardInputInterface keyboardInput, int row, int column, String string) throws BasicException;

    void print(KeyboardInputInterface keyboardInput, String stringToPrint, boolean includeNewline) throws BasicException;

    void setCursorVisibility(KeyboardInputInterface keyboardInput, boolean isVisible) throws BasicException;

    boolean isCursorVisible(KeyboardInputInterface keyboardInput) throws BasicException;

    void destroy();

    void reset();

}
