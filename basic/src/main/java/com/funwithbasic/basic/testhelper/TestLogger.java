package com.funwithbasic.basic.testhelper;

public class TestLogger {

    private StringBuilder log;

    public TestLogger() {
        log = new StringBuilder();
    }

    public void resetLog() {
        log.setLength(0);
    }

    public String getLog() {
        if (log.length() == 0) {
            return "";
        }
        return log.toString();
    }

    protected void appendToLog(String s) {
        log.append(s);
    }

}
