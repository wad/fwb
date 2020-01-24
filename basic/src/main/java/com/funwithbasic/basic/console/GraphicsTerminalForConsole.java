package com.funwithbasic.basic.console;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.interfaces.GraphicsTerminalInterface;

public class GraphicsTerminalForConsole implements GraphicsTerminalInterface {

    @Override
    public boolean doesExist() {
        return false;
    }

    @Override
    public void create() throws BasicException {
        throw new BasicException("Not implemented here");
    }

    @Override
    public void create(int sizeOfDot, int sizeX, int sizeY) throws BasicException {
        throw new BasicException("Not implemented here");
    }

    @Override
    public void destroy() {
        // not implemented here
    }

    @Override
    public void color(int r, int g, int b) throws BasicException {
        throw new BasicException("Not implemented here");
    }

    @Override
    public void plot(int x, int y) throws BasicException {
        throw new BasicException("Not implemented here");
    }

    @Override
    public void rect(int x1, int y1, int x2, int y2) throws BasicException {
        throw new BasicException("Not implemented here");
    }

    @Override
    public void reset() {
        // do nothing
    }

}
