package com.funwithbasic.basic.token;

import com.funwithbasic.basic.BasicException;

import java.util.List;

public interface TokenWithArguments {

    int getNumArguments() throws BasicException;

    Token evaluate(List<Token> arguments) throws BasicException;

}
