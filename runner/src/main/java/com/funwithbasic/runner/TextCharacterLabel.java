package com.funwithbasic.runner;

import javax.swing.*;

public class TextCharacterLabel extends JLabel {

    private boolean isFrozen;
    String textHeld;

    public TextCharacterLabel() {
        super(" ", JLabel.CENTER);
        textHeld = " ";
        isFrozen = false;
    }

    @Override
    public void setText(String text) {
        textHeld = text;
        if (!isFrozen) {
            super.setText(textHeld);
        }
    }

    public void setFrozen(boolean shouldFreeze) {
        if (!shouldFreeze) {
            super.setText(textHeld);
            invalidate();
        }
        isFrozen = shouldFreeze;
    }

}
