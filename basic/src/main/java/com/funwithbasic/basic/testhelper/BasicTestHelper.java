package com.funwithbasic.basic.testhelper;

import com.funwithbasic.basic.BasicRunner;

public class BasicTestHelper {

    private ErrorMessengerForTest errorMessenger;
    private FlowControllerForTest flowController;
    private TextTerminalForTest textTerminal;
    private GraphicsTerminalForTest graphicsArea;
    private KeyboardInputForTest keyboardInput;

    public BasicTestHelper() {
        errorMessenger = new ErrorMessengerForTest();
        flowController = new FlowControllerForTest();
        textTerminal = new TextTerminalForTest();
        graphicsArea = new GraphicsTerminalForTest();
        keyboardInput = new KeyboardInputForTest();
    }

    public String getLog() {
        return textTerminal.getLog();
    }

    public void resetLog() {
        textTerminal.resetLog();
    }

    public String getErrorLog() {
        return errorMessenger.getLog();
    }

    public void resetErrorLog() {
        errorMessenger.resetLog();
    }

    public BasicRunner getBasicRunner() {
        errorMessenger.resetLog();
        textTerminal.resetLog();
        return new BasicRunner("test", errorMessenger, flowController, textTerminal, graphicsArea, keyboardInput);
    }

}
