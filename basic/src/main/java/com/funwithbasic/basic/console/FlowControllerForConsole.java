package com.funwithbasic.basic.console;

import com.funwithbasic.basic.interfaces.FlowControllerInterface;

public class FlowControllerForConsole implements FlowControllerInterface {

    @Override
    public boolean shouldBreakNow() {
        return false;
    }

    @Override
    public void delayBetweenStatements() {
    }

    @Override
    public void reset() {
        // do nothing
    }

}
