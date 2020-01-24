package com.funwithbasic;

public class Constants {

    public static final String APPLICATION_CONTEXT_PATH = SharedConstants.APPLICATION_CONTEXT_PATH;

    public static final String DOC_TYPE_TEXT = "text/plain; charset=utf-8";
    public static final String DOC_TYPE_HTML = "text/html; charset=utf-8";

    public static final String PARAM_NAME_FILE_CONTENT = "file_content";

    public static final String PARAM_NAME_USER_ACTION = SharedConstants.PARAM_NAME_USER_ACTION;
    public static final String USER_ACTION_RUN_PROGRAM = SharedConstants.USER_ACTION_RUN_PROGRAM;
    public static final String USER_ACTION_GET_PROGRAM = SharedConstants.USER_ACTION_GET_PROGRAM;

    public static final String APPLET_WIDTH = SharedConstants.APPLET_WIDTH;
    public static final String APPLET_HEIGHT = SharedConstants.APPLET_HEIGHT;

    public static final String SESSION_PARAM_NAME_MESSAGE_FROM_SERVER = "message_from_server";
    public static final String SESSION_PARAM_NAME_PROGRAM = "program";
    public static final String SESSION_PARAM_NAME_SESSION_STATUS = "session_status";

    public enum SessionStatus {
        unknown("unknown"),
        appletWaiting("applet_waiting"),
        userRequestedRunProgram("user_requested_run_program"),
        appletRunningProgram("applet_running_program");

        private String paramValue;

        SessionStatus(String paramValue) {
            this.paramValue = paramValue;
        }

        public String getParamValue() {
            return paramValue;
        }

        public static SessionStatus determineFromString(String sessionStatusString) {
            for (SessionStatus sessionStatus : SessionStatus.values()) {
                if (sessionStatus.getParamValue().equals(sessionStatusString)) {
                    return sessionStatus;
                }
            }
            return unknown;
        }
    }

    public enum EventStatus {
        AboutToAttempt,
        Succeeded,
        Failed
    }

    public enum UserType {
        Free,
        Premium,
        Forever
    }

    public enum UserStatus {
        Normal,
        Deleted,
        Disabled,
        Banned,
        Guest,
        Owner
    }

    public enum UserEvent {
        Create,
        UpdateToPremium,
        Delete
    }

}
