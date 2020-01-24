package com.funwithbasic.runner;

import com.funwithbasic.SharedConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class FWBRunner extends JApplet implements ActionListener, KeyListener {

    static final int FLOW_CONTROLLER_DELAY = 5;

    static final String RUN = "Run";
    static final String RUN_NEW = "Run New";

    String sessionId;
    java.util.List<String> currentProgram;

    // checks periodically for new clicks of the RUN button from the web application
    ServerConnectionThread serverConnectionThread;

    // temporary components for getting a missing session ID
    PanelSessionIdGetter panelSessionIdGetter;
    JButton buttonSubmitSessionId;

    // holds the panelPrincipal
    Container container;

    // holds all the components of the application
    PanelPrincipal panelPrincipal;

    // the visible components
    TextTerminal textTerminal;
    GraphicsTerminal graphicsTerminal;

    // other components
    BasicThread basicThread;
    FlowController flowController;
    KeyboardInput keyboardInput;
    ErrorMessenger errorMessenger;

    // control buttons
    JButton buttonRun;
    JButton buttonBreak;
    JButton buttonReset;

    enum RunnerState {
        INITIAL,
        RECENTLY_RESET,
        PROGRAM_RUNNING,
        PROGRAM_STOPPED,
        BREAK_SIGNAL_SENT,
        NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK
    }
    RunnerState runnerState;

    enum ActionSource {
        UserClickedRunButton,
        UserClickedBreakButton,
        UserClickedResetButton,
        NewProgramArrived,
        ProgramEnded,
        UserSubmittedSessionId,
        GraphicsTerminalClosed
    }

    @Override
    public void init() {
        currentProgram = convertStringToList(SharedConstants.INITIAL_PROGRAM);
        runnerState = RunnerState.INITIAL;
        container = getContentPane();
        sessionId = getParameter(SharedConstants.SESSION_ID_PARAMETER_NAME);
        if (sessionId == null) {
            panelSessionIdGetter = new PanelSessionIdGetter(this);
            container.add(panelSessionIdGetter);
        } else {
            setupPanelPrincipal();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (determineActionSource(e.getSource())) {
            case UserClickedBreakButton:
                switch (runnerState) {
                    case PROGRAM_RUNNING:
                        setButtons(false, false, false);
                        keyboardInput.breakRequested();
                        flowController.setShouldBreak(true);
                        runnerState = RunnerState.BREAK_SIGNAL_SENT;
                        serverConnectionThread.resumeChecking();
                        break;
                    case BREAK_SIGNAL_SENT:
                    case RECENTLY_RESET:
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                    case PROGRAM_STOPPED:
                    case INITIAL:
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case UserClickedResetButton:
                switch (runnerState) {
                    case PROGRAM_STOPPED:
                        reset();
                        setButtons(true, false, false);
                        serverConnectionThread.resumeChecking();
                        break;
                    case PROGRAM_RUNNING:
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                    case BREAK_SIGNAL_SENT:
                    case RECENTLY_RESET:
                    case INITIAL:
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case UserClickedRunButton:
                switch (runnerState) {
                    case PROGRAM_STOPPED:
                        reset();
                    case RECENTLY_RESET:
                        buttonRun.setText(RUN);
                        setButtons(false, true, false);
                        serverConnectionThread.pauseChecking();
                        runnerState = RunnerState.PROGRAM_RUNNING;
                        basicThread.runProgram(currentProgram);
                        break;
                    case PROGRAM_RUNNING:
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                    case BREAK_SIGNAL_SENT:
                    case INITIAL:
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case NewProgramArrived:
                switch (runnerState) {
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                        currentProgram = serverConnectionThread.getProgram();
                        break;
                    case BREAK_SIGNAL_SENT:
                        buttonRun.setText(RUN_NEW);
                        currentProgram = serverConnectionThread.getProgram();
                        runnerState = RunnerState.NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK;
                        break;
                    case PROGRAM_RUNNING:
                        keyboardInput.breakRequested();
                        flowController.setShouldBreak(true);
                        buttonRun.setText(RUN_NEW);
                        setButtons(false, false, false);
                        currentProgram = serverConnectionThread.getProgram();
                        runnerState = RunnerState.NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK;
                        break;
                    case PROGRAM_STOPPED:
                    case RECENTLY_RESET:
                        buttonRun.setText(RUN_NEW);
                        setButtons(true, false, false);
                        currentProgram = serverConnectionThread.getProgram();
                        break;
                    case INITIAL:
                        buttonRun.setText(RUN_NEW);
                        setButtons(true, false, false);
                        serverConnectionThread.pauseChecking();
                        runnerState = RunnerState.PROGRAM_STOPPED;
                        currentProgram = serverConnectionThread.getProgram();
                        break;
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case ProgramEnded:
                switch (runnerState) {
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                        buttonRun.setText(RUN_NEW);
                    case BREAK_SIGNAL_SENT:
                        setButtons(true, false, true);
                        basicThread.stopAndDiscard();
                        basicThread = null;
                        runnerState = RunnerState.PROGRAM_STOPPED;
                        serverConnectionThread.resumeChecking();
                        break;
                    case PROGRAM_RUNNING:
                        flowController.setShouldBreak(false);
                        basicThread.stopAndDiscard();
                        basicThread = null;
                        setButtons(true, false, true);
                        runnerState = RunnerState.PROGRAM_STOPPED;
                        serverConnectionThread.resumeChecking();
                        break;
                    case PROGRAM_STOPPED:
                    case RECENTLY_RESET:
                    case INITIAL:
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case GraphicsTerminalClosed:
                switch (runnerState) {
                    case RECENTLY_RESET:
                    case BREAK_SIGNAL_SENT:
                    case NEW_PROGRAM_ARRIVED_BUT_WAITING_FOR_BREAK:
                        break;
                    case PROGRAM_RUNNING:
                        setButtons(false, false, false);
                        keyboardInput.breakRequested();
                        flowController.setShouldBreak(true);
                        runnerState = RunnerState.BREAK_SIGNAL_SENT;
                        serverConnectionThread.resumeChecking();
                        break;
                    case PROGRAM_STOPPED:
                        reset();
                        setButtons(true, false, false);
                        serverConnectionThread.resumeChecking();
                        break;
                    case INITIAL:
                    default:
                        throw new IllegalStateException("State: " + runnerState.name());
                }
                break;
            case UserSubmittedSessionId:
                sessionId = panelSessionIdGetter.getSessionId();
                panelSessionIdGetter.setVisible(false);
                container.remove(panelSessionIdGetter);
                panelSessionIdGetter = null;
                setupPanelPrincipal();
                break;
            default:
                throw new IllegalStateException("Unknown action source: " + e.getSource().getClass().getName());
        }
    }

    private void setupPanelPrincipal() {
        System.out.println("FunWithBasic version: " + SharedConstants.VERSION);
        System.out.println("Session ID: " + sessionId);
        panelPrincipal = new PanelPrincipal(this);
        container.add(panelPrincipal);
        serverConnectionThread = new ServerConnectionThread(sessionId, SharedConstants.HOST, SharedConstants.PORT, this);
        serverConnectionThread.start();
        setButtons(false, false, false);
    }

    private void reset() {
        runnerState = RunnerState.RECENTLY_RESET;
        if (basicThread != null) {
            basicThread.reset();
        }
        basicThread = null;
        keyboardInput.reset();
        textTerminal.reset();
        graphicsTerminal.reset();
        errorMessenger.reset();
        flowController.reset();

        remove(graphicsTerminal);
        graphicsTerminal.dispose();

        basicThread = new BasicThread(this, errorMessenger, flowController, textTerminal, graphicsTerminal, keyboardInput);
        runnerState = RunnerState.RECENTLY_RESET;
    }

    class PanelSessionIdGetter extends JPanel {
        JTextField textFieldSessionId;

        PanelSessionIdGetter(FWBRunner fwbRunner) {
            setLayout(new FlowLayout());
            add(new JLabel("Enter the SessionId"));
            textFieldSessionId = new JTextField(30);
            add(textFieldSessionId);
            buttonSubmitSessionId = new JButton("Submit");
            add(buttonSubmitSessionId);
            buttonSubmitSessionId.addActionListener(fwbRunner);
        }

        String getSessionId() {
            return textFieldSessionId.getText();
        }
    }

    class PanelPrincipal extends JPanel {

        public PanelPrincipal(FWBRunner fwbRunner) {
            setLayout(new BorderLayout());

            // CENTER: Text output
            textTerminal = new TextTerminal();
            add(textTerminal, BorderLayout.CENTER);

            // SOUTH: ControlBar
            JPanel panelNorth = new JPanel();
            buttonRun = new JButton(RUN);
            buttonBreak = new JButton("Break");
            buttonReset = new JButton("Reset");
            buttonRun.addActionListener(fwbRunner);
            buttonBreak.addActionListener(fwbRunner);
            buttonReset.addActionListener(fwbRunner);
            setButtons(false, false, false);
            panelNorth.setLayout(new FlowLayout());
            panelNorth.add(buttonRun);
            panelNorth.add(buttonBreak);
            panelNorth.add(buttonReset);
            add(panelNorth, BorderLayout.NORTH);

            graphicsTerminal = new GraphicsTerminal(fwbRunner);

            errorMessenger = new ErrorMessenger(fwbRunner);
            flowController = new FlowController(FLOW_CONTROLLER_DELAY);
            keyboardInput = new KeyboardInput(textTerminal);
            basicThread = new BasicThread(fwbRunner, errorMessenger, flowController, textTerminal, graphicsTerminal, keyboardInput);

            textTerminal.create(keyboardInput);
        }
    }

    private ActionSource determineActionSource(Object sourceObject) {
        ActionSource actionSource = null;

        if (sourceObject == serverConnectionThread) {
            actionSource = ActionSource.NewProgramArrived;
        } else if (sourceObject == buttonSubmitSessionId) {
            actionSource = ActionSource.UserSubmittedSessionId;
        } else if (sourceObject == buttonRun) {
            actionSource = ActionSource.UserClickedRunButton;
        } else if (sourceObject == buttonBreak) {
            actionSource = ActionSource.UserClickedBreakButton;
        } else if (sourceObject == buttonReset) {
            actionSource = ActionSource.UserClickedResetButton;
        } else if (sourceObject == basicThread) {
            actionSource = ActionSource.ProgramEnded;
        } else if (sourceObject.getClass().getName().contains(GraphicsTerminal.class.getName())) {
            actionSource = ActionSource.GraphicsTerminalClosed;
        }

        if (actionSource == null) {
            throw new RuntimeException("Could not determine action source. sourceObject=" + sourceObject.getClass().getName());
        }

        return actionSource;
    }

    private void setButtons(boolean runEnabled, boolean breakEnabled, boolean resetEnabled) {
        buttonRun.setEnabled(runEnabled);
        buttonReset.setEnabled(resetEnabled);
        buttonBreak.setEnabled(breakEnabled);
        removeKeyListener(this);
        addKeyListener(this);
        requestFocusInWindow();
    }

    static List<String> convertStringToList(String program) {
        List<String> result = new ArrayList<String>();
        Collections.addAll(result, program.split("\\n"));
        return result;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (keyboardInput != null) {
            keyboardInput.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyboardInput != null) {
            keyboardInput.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keyboardInput != null) {
            keyboardInput.keyReleased(e);
        }
    }

}
