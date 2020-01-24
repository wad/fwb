package com.funwithbasic.runner;

import com.funwithbasic.SharedConstants;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.interfaces.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class BasicThread extends Thread {

    private BasicRunner basicRunner;
    private List<String> code;
    private FWBRunner fwbRunner;

    public BasicThread(
            FWBRunner fwbRunner,
            ErrorMessengerInterface errorMessenger,
            FlowControllerInterface flowController,
            TextTerminalInterface textTerminal,
            GraphicsTerminalInterface graphicsTerminal,
            KeyboardInputInterface keyboardInput) {
        this.fwbRunner = fwbRunner;
        basicRunner = new BasicRunner(
                SharedConstants.VERSION,
                errorMessenger,
                flowController,
                textTerminal,
                graphicsTerminal,
                keyboardInput);
    }

    @Override
    public void run() {
        basicRunner.runProgram(code);
        fwbRunner.actionPerformed(new ActionEvent(this, 0, "program execution complete"));
    }

    public void stopAndDiscard() {
        interrupt();
        code = null;
        basicRunner = null;
    }

    public void runProgram(List<String> code) {
        this.code = code;
        start();
    }

    public void reset() {
        stopAndDiscard();
    }

}
