package com.funwithbasic.runner;

import com.funwithbasic.basic.interfaces.ErrorMessengerInterface;

import javax.swing.*;

public class ErrorMessenger implements ErrorMessengerInterface {

    private FWBRunner fwbRunner;

    public ErrorMessenger(FWBRunner fwbRunner) {
        this.fwbRunner = fwbRunner;
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(fwbRunner,
                message,
                "Message",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void reset() {
        // todo: get rid of an error message if one is showing
    }

}
