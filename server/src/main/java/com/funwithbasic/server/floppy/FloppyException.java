package com.funwithbasic.server.floppy;

public class FloppyException extends Exception {

    public enum Reason {
        System,
        InvalidFilename,
        CannotCreateExisting,
        FileNotFound
    }

    private Reason reason;

    public Reason getReason() {
        return reason;
    }

    public FloppyException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public FloppyException(Reason reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

}
