package com.funwithbasic.basic.console;

import com.funwithbasic.basic.BasicLoader;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.Constants;

import java.io.IOException;

public class BasicConsole {

    public static void main(String... args) {
        if (args.length != 1) {
            System.err.println("Specify the filename of a basic file to run.");
        } else {
            String filenameToRun = args[0];
            BasicRunner basicRunner = new BasicRunner(
                    Constants.CONST_NAME_VERSION,
                    new ErrorMessengerForConsole(),
                    new FlowControllerForConsole(),
                    new TextTerminalForConsole(),
                    new GraphicsTerminalForConsole(),
                    new KeyboardInputForConsole());
            try {
                basicRunner.runProgram(BasicLoader.loadIntoStringList(filenameToRun));
            }
            catch (IOException ioe) {
                System.err.println("Problem loading the file: " + ioe.getMessage());
            }
        }
    }

}
