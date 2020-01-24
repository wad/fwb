package com.funwithbasic.runner;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.funwithbasic.SharedConstants.*;

public class ServerConnectionThread extends Thread {

    private static final int NUM_MILLISECONDS_BETWEEN_POLLS = 1500;

    private String urlString;
    private boolean shouldKeepChecking;
    private List<String> currentProgram;
    private FWBRunner fwbRunner;

    public ServerConnectionThread(String sessionId, String host, String port, FWBRunner fwbRunner) {
        shouldKeepChecking = true;
        this.fwbRunner = fwbRunner;
        currentProgram = null;
        urlString = constructUrl(sessionId, host, port);
    }

    @Override
    public void run() {
        boolean exceptionThrown = false;
        while (!exceptionThrown) {
            if (shouldKeepChecking) {
                currentProgram = loadProgram(urlString);
                if (currentProgram != null) {
                    fwbRunner.actionPerformed(new ActionEvent(this, 0, "program available"));
                }
            }
            try {
                sleep(NUM_MILLISECONDS_BETWEEN_POLLS);
            } catch (InterruptedException e) {
                interrupt();
                exceptionThrown = true;
            }
        }
    }

    public List<String> getProgram() {
        boolean previous = shouldKeepChecking;
        shouldKeepChecking = false;
        List<String> result = currentProgram;
        currentProgram = null;
        shouldKeepChecking = previous;
        return result;
    }

    public void pauseChecking() {
        shouldKeepChecking = false;
    }

    public void resumeChecking() {
        shouldKeepChecking = true;
    }

    // http://funwithbasic.com:8080/fwb/appletConnector;jsessionid=3F2498A3B218ED20562327D24165F857?user_action=get_program
    static String constructUrl(String sessionId, String host, String port) {
        String scheme = "http://";
        String root = APPLICATION_CONTEXT_PATH;
        String servlet = "appletConnector";
        String query = "?" + PARAM_NAME_USER_ACTION + "=" + USER_ACTION_GET_PROGRAM;
        return scheme + host + ":" + port + "/" + root + servlet + ";jsessionid=" + sessionId + query;
    }

    static List<String> loadProgram(String urlString) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<String> programLines = new ArrayList<String>();
        int numLinesWithContent = 0;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; JVM) FunWithBasic java applet");
            connection.setRequestProperty("Pragma", "no-cache");
            connection.connect();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() > 0) {
                    numLinesWithContent++;
                }
                programLines.add(line);
            }
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) { }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException ignored) { }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) { }
            }
        }
        return numLinesWithContent == 0 ? null : programLines;
    }

}
