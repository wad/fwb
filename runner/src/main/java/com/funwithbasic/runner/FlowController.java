package com.funwithbasic.runner;

import com.funwithbasic.basic.interfaces.FlowControllerInterface;

public class FlowController implements FlowControllerInterface {

    private int delayMilliseconds;
    private boolean shouldBreak;

    public FlowController(int initialDelayInMilliseconds) {
        this.delayMilliseconds = initialDelayInMilliseconds;
        shouldBreak = false;
    }

    @Override
    public boolean shouldBreakNow() {
        return shouldBreak;
    }

    @Override
    public void delayBetweenStatements() {
        try {
            Thread.sleep(delayMilliseconds);
        }
        catch (InterruptedException ignored) {
        }
    }

    public void setShouldBreak(boolean shouldBreak) {
        this.shouldBreak = shouldBreak;
    }

    @Override
    public void reset() {
        setShouldBreak(false);
    }

}
