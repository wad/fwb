package com.funwithbasic.basic.interfaces;

import com.funwithbasic.basic.BasicException;

public interface KeyboardInputInterface {

    String readLine() throws BasicException;
    
    int readKeypress();

    int getNumCharactersWaitingInBuffer();

    boolean isValidCharacter(int c);

    boolean isValidPrintableCharacter(int c);
    
    boolean isValidControlCharacter(int c);

    void clearBuffer();

    void reset();

}
