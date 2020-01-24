package com.funwithbasic.basic.statement;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.ExpressionEvaluator;

public abstract class Statement {

    public static enum StatementType {
        COMMENT("#"),
        REM("REM"),
        LABEL_(":"),
        LABEL("LABEL"),
        TEXT("TEXT"),
        NOTEXT("NOTEXT"),
        END("END"),
        FAIL("FAIL"),
        GOSUB("GOSUB"),
        GOTO("GOTO"),
        CURON("CURON"),
        CUROFF("CUROFF"),
        HOME("HOME"),
        IF("IF"),
        FOR("FOR"),
        NEXT("NEXT"),
        LET("LET"),
        DIM("DIM"),
        MOVE("MOVE"),
        PRINT("PRINT"),
        INPUT("INPUT"),
        GETKEY("GETKEY"),
        RETURN("RETURN"),
        SLEEP("SLEEP"),
        PUTS("PUTS"),
        GRAPHICS("GRAPHICS"),
        NOGRAPHICS("NOGRAPHICS"),
        COLOR("COLOR"),
        PLOT("PLOT"),
        RECT("RECT");

        private String commandText;

        StatementType(String commandText) {
            this.commandText = commandText;
        }

        public String getCmd() {
            return commandText;
        }
    }

    private Statement subsequentStatement;
    private BasicRunner basicRunner;
    private int lineNumberInFile;

    Statement(BasicRunner basicRunner, int lineNumberInFile) {
        this.basicRunner = basicRunner;
        this.lineNumberInFile = lineNumberInFile;
    }

    public static Statement generateFromCode(String rawCodeLine, BasicRunner basicRunner, int lineNumberInFile) throws BasicException {
        Statement statement = null;
        if (rawCodeLine != null) {

            String code = rawCodeLine.trim();
            if (code.length() > 0) {

                boolean hasLineNumberLabel = checkForLineNumberLabel(code);
                String lineNumberLabel = null;
                if (checkForLineNumberLabel(code)) {
                    lineNumberLabel = String.valueOf(getLineNumberLabel(code));
                    code = getCodeAfterLineNumberLabel(code);
                }

                if (StatementLabelShortcut.isItMine(code)) {
                    statement = new StatementLabelShortcut(code, basicRunner, lineNumberInFile);
                } else if (StatementLabel.isItMine(code)) {
                    statement = new StatementLabel(code, basicRunner, lineNumberInFile);
                } else if (StatementGoto.isItMine(code)) {
                    statement = new StatementGoto(code, basicRunner, lineNumberInFile);
                } else if (StatementGosub.isItMine(code)) {
                    statement = new StatementGosub(code, basicRunner, lineNumberInFile);
                } else if (StatementReturn.isItMine(code)) {
                    statement = new StatementReturn(code, basicRunner, lineNumberInFile);
                } else if (StatementEnd.isItMine(code)) {
                    statement = new StatementEnd(code, basicRunner, lineNumberInFile);
                } else if (StatementFail.isItMine(code)) {
                    statement = new StatementFail(code, basicRunner, lineNumberInFile);
                } else if (StatementLet.isItMine(code)) {
                    statement = new StatementLet(code, basicRunner, lineNumberInFile);
                } else if (StatementDim.isItMine(code)) {
                    statement = new StatementDim(code, basicRunner, lineNumberInFile);
                } else if (StatementComment.isItMine(code)) {
                    statement = new StatementComment(basicRunner, lineNumberInFile);
                } else if (StatementRem.isItMine(code)) {
                    statement = new StatementRem(basicRunner, lineNumberInFile);
                } else if (StatementText.isItMine(code)) {
                    statement = new StatementText(code, basicRunner, lineNumberInFile);
                } else if (StatementNoText.isItMine(code)) {
                    statement = new StatementNoText(code, basicRunner, lineNumberInFile);
                } else if (StatementHome.isItMine(code)) {
                    statement = new StatementHome(code, basicRunner, lineNumberInFile);
                } else if (StatementCursorOn.isItMine(code)) {
                    statement = new StatementCursorOn(code, basicRunner, lineNumberInFile);
                } else if (StatementCursorOff.isItMine(code)) {
                    statement = new StatementCursorOff(code, basicRunner, lineNumberInFile);
                } else if (StatementPrint.isItMine(code)) {
                    statement = new StatementPrint(code, basicRunner, lineNumberInFile);
                } else if (StatementInput.isItMine(code)) {
                    statement = new StatementInput(code, basicRunner, lineNumberInFile);
                } else if (StatementGetkey.isItMine(code)) {
                    statement = new StatementGetkey(code, basicRunner, lineNumberInFile);
                } else if (StatementMove.isItMine(code)) {
                    statement = new StatementMove(code, basicRunner, lineNumberInFile);
                } else if (StatementIf.isItMine(code)) {
                    statement = new StatementIf(code, basicRunner, lineNumberInFile);
                } else if (StatementFor.isItMine(code)) {
                    statement = new StatementFor(code, basicRunner, lineNumberInFile);
                } else if (StatementNext.isItMine(code)) {
                    statement = new StatementNext(code, basicRunner, lineNumberInFile);
                } else if (StatementSleep.isItMine(code)) {
                    statement = new StatementSleep(code, basicRunner, lineNumberInFile);
                } else if (StatementPuts.isItMine(code)) {
                    statement = new StatementPuts(code, basicRunner, lineNumberInFile);
                } else if (StatementGraphics.isItMine(code)) {
                    statement = new StatementGraphics(code, basicRunner, lineNumberInFile);
                } else if (StatementNoGraphics.isItMine(code)) {
                    statement = new StatementNoGraphics(code, basicRunner, lineNumberInFile);
                } else if (StatementColor.isItMine(code)) {
                    statement = new StatementColor(code, basicRunner, lineNumberInFile);
                } else if (StatementPlot.isItMine(code)) {
                    statement = new StatementPlot(code, basicRunner, lineNumberInFile);
                } else if (StatementRect.isItMine(code)) {
                    statement = new StatementRect(code, basicRunner, lineNumberInFile);
                } else {
                    throw new BasicException("unknown statement: [" + code + "]");
                }

                if (hasLineNumberLabel) {
                    if (!ExpressionEvaluator.isValidLabelName(lineNumberLabel)) {
                        throw new RuntimeException("A number should be a valid label! " + lineNumberLabel);
                    }
                    if (basicRunner.lookupLabel(lineNumberLabel) != null) {
                        throw new BasicException("Duplicate line number detected: [" + lineNumberLabel + "]");
                    }
                    statement.setLineNumberLabel(lineNumberLabel);
                }

            }
        }

        return statement;
    }

    static boolean checkForLineNumberLabel(String code) {
        int firstSpace = code.indexOf(' ');
        if (firstSpace == -1) {
            return false;
        }
        String firstBit = code.substring(0, firstSpace).trim();
        if (firstBit.length() == 0) {
            return false;
        }
        for (int i = 0; i < firstBit.length(); i++) {
            char c = firstBit.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    static int getLineNumberLabel(String code) throws BasicException {
        int firstSpace = code.indexOf(' ');
        String firstBit = code.substring(0, firstSpace).trim();
        try {
            return Integer.parseInt(firstBit);
        }
        catch (NumberFormatException fne) {
            throw new BasicException("Could not determine line number from \"" + firstBit + "\"");
        }
    }

    static String getCodeAfterLineNumberLabel(String code) {
        int firstSpace = code.indexOf(' ');
        return code.substring(firstSpace).trim();
    }

    BasicRunner getBasicRunner() {
        return basicRunner;
    }

    public int getLineNumberInFile() {
        return lineNumberInFile;
    }

    public void setSubsequentStatement(Statement subsequentStatement) {
        this.subsequentStatement = subsequentStatement;
    }

    public Statement getSubsequentStatement() {
        return subsequentStatement;
    }

    public abstract void execute() throws BasicException;

    void invalid(StatementType statementType, String code, String message) throws BasicException {
        throw new BasicException("Invalid " + statementType.getCmd() + " statement on line " +
                getLineNumberInFile() + (message == null ? "" : (" (" + message + ")")) + ": " + code);
    }

    void invalid(StatementType statementType, String code) throws BasicException {
        invalid(statementType, code, null);
    }

    void setLineNumberLabel(String lineNumberLabel) throws BasicException {
        getBasicRunner().addLabel(lineNumberLabel, this);
    }

}
