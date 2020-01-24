package com.funwithbasic.basic.testhelper;

import com.funwithbasic.basic.interfaces.FlowControllerInterface;

public class FlowControllerForTest implements FlowControllerInterface {

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
