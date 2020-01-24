package com.funwithbasic.basic.interfaces;

import com.funwithbasic.basic.BasicException;

public interface GraphicsTerminalInterface {

    boolean doesExist();

    void create() throws BasicException;

    void create(int sizeOfDot, int sizeX, int sizeY) throws BasicException;

    void color(int r, int g, int b) throws BasicException;

    void plot(int x, int y) throws BasicException;

    void rect(int x1, int y1, int x2, int y2) throws BasicException;

    void destroy();

    void reset();

}
