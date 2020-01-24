package com.funwithbasic.runner;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.GraphicsTerminalInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicsTerminal extends JFrame implements GraphicsTerminalInterface, KeyListener {

    public static final int DEFAULT_DOT_SIZE = 10;
    public static final int DEFAULT_SIZE_X = 30;
    public static final int DEFAULT_SIZE_Y = 30;

    public static final int MAX_DOT_SIZE = 100;
    public static final int MAX_SIZE_X_PIXELS = 10000;
    public static final int MAX_SIZE_Y_PIXELS = 10000;

    private int sizeX;
    private int sizeY;

    private int colorR;
    private int colorG;
    private int colorB;

    private boolean doesExist;

    private FWBRunner fwbRunner;
    private GraphicsPanel graphicsPanel;

    public GraphicsTerminal(FWBRunner fwbRunner) {
        this.fwbRunner = fwbRunner;
        doesExist = false;
    }

    @Override
    public boolean doesExist() {
        return doesExist;
    }

    @Override
    public void create() {
        try {
            create(DEFAULT_DOT_SIZE, DEFAULT_SIZE_X, DEFAULT_SIZE_Y);
        } catch (BasicException be) {
            throw new RuntimeException("Default graphics area sizes are invalid", be);
        }
    }

    @Override
    public void create(int sizeOfDot, int sizeX, int sizeY) throws BasicException {
        if (doesExist) {
            throw new BasicException("Graphics terminal already exists, destroy it first");
        }

        if (sizeOfDot > MAX_DOT_SIZE || sizeOfDot < 1 ||
                (sizeX * sizeOfDot) >= MAX_SIZE_X_PIXELS || sizeX < 1 ||
                (sizeY * sizeOfDot) >= MAX_SIZE_Y_PIXELS || sizeY < 1) {
            throw new BasicException("Illegal text terminal size");
        }
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        doesExist = true;

        setLayout(new BorderLayout());

        graphicsPanel = new GraphicsPanel(sizeOfDot, sizeX, sizeY);
        add(graphicsPanel);

        setSize(1, 1);
        pack();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                fwbRunner.actionPerformed(new ActionEvent(this, 0, "graphics terminal closed"));
            }
        });

        addKeyListener(this);

        // set the default color to white
        colorR = 255;
        colorG = 255;
        colorB = 255;
    }

    @Override
    public void destroy() {
        doesExist = false;
        setVisible(false);
        if (graphicsPanel != null) {
            remove(graphicsPanel);
            graphicsPanel = null;
        }
    }

    @Override
    public void color(int r, int g, int b) throws BasicException {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new BasicException("Illegal color value");
        }

        colorR = r;
        colorG = g;
        colorB = b;
    }

    @Override
    public void plot(int x, int y) throws BasicException {
        ensureExists();
        checkCoordinates(x, y);
        graphicsPanel.plot(x, y, colorR, colorG, colorB);
    }

    @Override
    public void rect(int x1, int y1, int x2, int y2) throws BasicException {
        ensureExists();
        checkCoordinates(x1, y1);
        checkCoordinates(x2, y2);
        graphicsPanel.fillbox(x1, y1, x2, y2, colorR, colorG, colorB);
    }

    private void checkCoordinates(int x, int y) throws BasicException {
        if (x >= sizeX || x < 0 || y >= sizeY || y < 0) {
            throw new BasicException("Invalid coordinates");
        }
    }

    private void ensureExists() throws BasicException {
        if (!doesExist) {
            create();
        }
        setVisible(true);
    }

    @Override
    public void reset() {
        setVisible(false);
        if (doesExist()) {
            destroy();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        fwbRunner.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        fwbRunner.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        fwbRunner.keyReleased(e);
    }

}
