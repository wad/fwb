package com.funwithbasic.basic.console;

import com.funwithbasic.basic.interfaces.ErrorMessengerInterface;

public class ErrorMessengerForConsole implements ErrorMessengerInterface {

    @Override
    public void showErrorMessage(String message) {
        System.err.println(message);
    }

    @Override
    public void reset() {
        // do nothing
    }

}
