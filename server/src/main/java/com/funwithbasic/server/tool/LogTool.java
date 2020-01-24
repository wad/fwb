package com.funwithbasic.server.tool;

public class LogTool {

    public static String info(String message) {
        stdout(message);
        return message + "\n";
    }

    public static String warn(String message) {
        stderr(message);
        return message + "\n";
    }

    public static String error(String message, Throwable t) {
        error(message);
        t.printStackTrace();
        return message + " [" + t.getMessage() + "]\n";
    }

    public static String error(String message) {
        stderr(message);
        return message + "\n";
    }

    private static void stdout(String message) {
        System.out.println(message);
    }

    private static void stderr(String message) {
        System.err.println(message);
    }

}
