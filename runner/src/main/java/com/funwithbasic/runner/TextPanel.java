package com.funwithbasic.runner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TextPanel extends JPanel {

    private int numRows;
    private int numColumns;
    private TextCharacterLabel[][] screenBuffer;

    private boolean isCursorVisible;
    private int cursorPositionRow = 0;
    private int cursorPositionColumn = 0;

    private Border borderCursor;

    public TextPanel(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;

        setLayout(new GridLayout(numRows, numColumns));

        screenBuffer = new TextCharacterLabel[numRows][numColumns];
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                TextCharacterLabel label = new TextCharacterLabel();
                label.setOpaque(true);
                label.setForeground(Color.GREEN);
                label.setBackground(Color.BLACK);
                label.setFont(new Font("Courier", Font.BOLD, 16));
                label.setPreferredSize(new Dimension(12, 19));
                screenBuffer[row][column] = label;
                add(label);
            }
        }

        isCursorVisible = false;
        borderCursor = LineBorder.createGrayLineBorder();
        setCursorPosition(0, 0);

        setVisible(true);
    }

    public void putCharacterAtPosition(int row, int column, char c) {
        screenBuffer[row][column].setText(String.valueOf(c));
    }

    public int getCharacterAtPosition(int row, int column) {
        return screenBuffer[row][column].getText().charAt(0);
    }

    public void scrollUp() {
        freezeLabels(true);
        for (int row = 0; row < (numRows - 1); row++) {
            for (int column = 0; column < numColumns; column++) {
                screenBuffer[row][column].setText(screenBuffer[row + 1][column].getText());
            }
        }
        for (int column = 0; column < numColumns; column++) {
            screenBuffer[numRows - 1][column].setText(" ");
        }
        freezeLabels(false);

        // try to clear up the odd appearance when scrolling up, as the labels refresh
        // in a random order. Doesn't work so well.
        try {
            Thread.sleep(200);
        }
        catch (InterruptedException e) {
            // do nothing
        }
    }

    public void freezeLabels(boolean shouldFreeze) {
        for (int row = 0; row < (numRows - 1); row++) {
            for (int column = 0; column < numColumns; column++) {
                screenBuffer[row][column].setFrozen(shouldFreeze);
            }
        }
    }

    public void setCursorVisibility(boolean isVisible) {
        if (isCursorVisible != isVisible) {
            isCursorVisible = isVisible;
            if (isVisible) {
                turnOnCursor(screenBuffer[cursorPositionRow][cursorPositionColumn]);
            } else {
                turnOffCursor(screenBuffer[cursorPositionRow][cursorPositionColumn]);
            }
        }
    }

    public void setCursorPosition(int row, int column) {
        turnOffCursor(screenBuffer[cursorPositionRow][cursorPositionColumn]);
        cursorPositionColumn = column;
        cursorPositionRow = row;
        if (isCursorVisible) {
            turnOnCursor(screenBuffer[cursorPositionRow][cursorPositionColumn]);
        }
    }

    private void turnOffCursor(JLabel label) {
        label.setBorder(null);
    }

    private void turnOnCursor(JLabel label) {
        label.setBorder(borderCursor);
    }

    @Override
    public boolean isFocusable() {
        // needed for keyboard binding
        return true;
    }
    
}
