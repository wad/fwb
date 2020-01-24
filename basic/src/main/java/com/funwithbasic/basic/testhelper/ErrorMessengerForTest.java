package com.funwithbasic.basic.testhelper;

import com.funwithbasic.basic.interfaces.ErrorMessengerInterface;

public class ErrorMessengerForTest extends TestLogger implements ErrorMessengerInterface {

    public ErrorMessengerForTest() {
        super();
    }

    @Override
    public void showErrorMessage(String message) {
        appendToLog(message);
        appendToLog("\n");
    }

    @Override
    public void reset() {
        // do nothing
    }

}
