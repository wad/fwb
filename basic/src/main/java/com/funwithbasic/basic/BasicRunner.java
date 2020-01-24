package com.funwithbasic.basic;

import com.funwithbasic.basic.interfaces.*;
import com.funwithbasic.basic.statement.Statement;
import com.funwithbasic.basic.statement.StatementFor;
import com.funwithbasic.basic.statement.StatementLabel;
import com.funwithbasic.basic.statement.StatementLabelShortcut;
import com.funwithbasic.basic.token.function.TokenFunction;
import com.funwithbasic.basic.value.ValueNumber;
import com.funwithbasic.basic.value.ValueString;

import java.util.*;

import static com.funwithbasic.basic.Constants.*;

public class BasicRunner {

    private String version;

    public static final Set<String> FUNCTION_NAMES = new HashSet<String>() {{
        for (TokenFunction.Function function : TokenFunction.Function.values()) {
            add(function.getText().toUpperCase());
        }
    }};

    public static final Set<String> STATEMENT_NAMES = new HashSet<String>() {{
        for (Statement.StatementType statementType : Statement.StatementType.values()) {
            add(statementType.getCmd().toUpperCase());
        }
    }};

    private Map<String, ValueString> stringVariables;
    private Map<String, ValueNumber> numberVariables;

    private Map<String, ValueString> stringConstants;
    private Map<String, ValueNumber> numberConstants;

    private Map<String, Integer> definedArrayNames;
    private Map<String, Map<String, ValueString>> stringArrayVariables;
    private Map<String, Map<String, ValueNumber>> numberArrayVariables;

    Set<String> illegalVariableNames;
    private Map<String, Statement> labels;
    private Stack<Statement> gosubStack;
    private Map<String, StatementFor> forHash;

    private ErrorMessengerInterface errorMessenger;
    private FlowControllerInterface flowController;
    private TextTerminalInterface textTerminal;
    private GraphicsTerminalInterface graphicsTerminal;
    private KeyboardInputInterface keyboardInput;

    public BasicRunner(String version,
                       ErrorMessengerInterface errorMessenger,
                       FlowControllerInterface flowController,
                       TextTerminalInterface textTerminal,
                       GraphicsTerminalInterface graphicsTerminal,
                       KeyboardInputInterface keyboardInput) {
        try {
            if (version == null || version.length() == 0) {
                version = "unspecified";
            }
            if (errorMessenger == null) {
                throw new IllegalArgumentException("Must supply an ErrorMessenger");
            }
            if (flowController == null) {
                throw new IllegalArgumentException("Must supply a FlowController");
            }
            if (textTerminal == null) {
                throw new IllegalArgumentException("Must supply a TextArea");
            }
            if (graphicsTerminal == null) {
                throw new IllegalArgumentException("Must supply a GraphicsArea");
            }
            if (keyboardInput == null) {
                throw new IllegalArgumentException("Must supply a KeyboardInput");
            }

            this.version = "FunWithBasic.com version " + version;
            this.errorMessenger = errorMessenger;
            this.flowController = flowController;
            this.textTerminal = textTerminal;
            this.graphicsTerminal = graphicsTerminal;
            this.keyboardInput = keyboardInput;

            stringVariables = new HashMap<String, ValueString>();
            numberVariables = new HashMap<String, ValueNumber>();

            stringConstants = new HashMap<String, ValueString>();
            numberConstants = new HashMap<String, ValueNumber>();

            definedArrayNames = new HashMap<String, Integer>();
            stringArrayVariables = new HashMap<String, Map<String, ValueString>>();
            numberArrayVariables = new HashMap<String, Map<String, ValueNumber>>();

            illegalVariableNames = new HashSet<String>();
            labels = new HashMap<String, Statement>();
            gosubStack = new Stack<Statement>();
            forHash = new HashMap<String, StatementFor>();

            setSystemVariables();
            setSystemConstants();
            setIllegalVariableNames();
        }
        catch (BasicException be) {
            throw new RuntimeException("Something went wrong in the constructor.", be);
        }
    }

    private void setSystemVariables() {
    }

    private void setSystemConstants() throws BasicException {
        // numbers
        numberConstants.put(Constants.CONST_NAME_PI, CONST_VALUE_PI);
        numberConstants.put(CONST_NAME_TRUE, CONST_VALUE_TRUE);
        numberConstants.put(CONST_NAME_FALSE, CONST_VALUE_FALSE);

        //strings
        stringConstants.put(CONST_NAME_VERSION, new ValueString(version, getKeyboardInput()));
        stringConstants.put(CONST_NAME_AUTHORS, new ValueString(CONST_VALUE_AUTHORS, getKeyboardInput()));
    }

    private void setIllegalVariableNames() {
        illegalVariableNames.addAll(FUNCTION_NAMES);
        illegalVariableNames.addAll(STATEMENT_NAMES);
    }

    public boolean runProgram(List<String> codeLines) {
        boolean runSucceeded = true;
        Statement statement = null;
        try {
            statement = convertLinesToLinkedStatements(codeLines);
            while (statement != null) {
                if (flowController.shouldBreakNow()) {
                    throw new BasicException("BREAK");
                }
                statement.execute();
                statement = statement.getSubsequentStatement();
                Thread.yield();
                if (statement != null) {
                    flowController.delayBetweenStatements();
                }
            }
        }
        catch (BasicException be) {
            if (statement == null) {
                getErrorMessenger().showErrorMessage(be.getMessage());
            }
            else {
                getErrorMessenger().showErrorMessage(
                        "Halted on line " + statement.getLineNumberInFile() + ":\n" + be.getMessage());
            }
            runSucceeded = false;
        }
        return runSucceeded;
    }

    public ErrorMessengerInterface getErrorMessenger() {
        return errorMessenger;
    }

    public TextTerminalInterface getTextTerminal() {
        return textTerminal;
    }

    public GraphicsTerminalInterface getGraphicsTerminal() {
        return graphicsTerminal;
    }

    public KeyboardInputInterface getKeyboardInput() {
        return keyboardInput;
    }

    Statement convertLinesToLinkedStatements(List<String> codeLines) throws BasicException {
        int lineNumber = 0;
        Statement entryStatement = null;
        Statement previousStatement = null;

        try {
            for (String rawCodeLine : codeLines) {
                lineNumber++;
                Statement statement = Statement.generateFromCode(rawCodeLine, this, lineNumber);

                // if its an empty line or something like that, statement will be null. Skip it.
                if (statement != null) {

                    // entry point into the program
                    if (entryStatement == null) {
                        entryStatement = statement;
                    }

                    // snag the label, if that's what it was
                    if (statement instanceof StatementLabelShortcut) {
                        StatementLabelShortcut statementLabelShortcut = (StatementLabelShortcut)statement;
                        labels.put(statementLabelShortcut.getLabel(), statementLabelShortcut);
                    }
                    else if (statement instanceof StatementLabel) {
                        StatementLabel statementLabel = (StatementLabel)statement;
                        labels.put(statementLabel.getLabel(), statementLabel);
                    }

                    // connect the statements
                    if (previousStatement != null) {
                        previousStatement.setSubsequentStatement(statement);
                    }
                    previousStatement = statement;
                }
            }
        }
        catch (BasicException be) {
            getErrorMessenger().showErrorMessage(
                    "Interpretation problem detected on line " + lineNumber + ": " + be.getMessage());
            throw be;
        }

        return entryStatement;
    }

    public void addLabel(String label, Statement statement) throws BasicException {
        if (labels.get(label) != null) {
            throw new BasicException("Label already exists: [" + label + "]");
        }
        labels.put(label, statement);
    }

    public Statement lookupLabel(String label) {
        return labels.get(label);
    }

    public void stackPush(Statement statementToPush) {
        gosubStack.push(statementToPush);
    }

    public Statement stackPop() throws BasicException {
        return gosubStack.pop();
    }

    public void storeForStatement(String variableName, StatementFor forStatement) throws BasicException {
        if (forHash.get(variableName) != null) {
            throw new BasicException(StatementFor.me().getCmd() +
                    " statement already active for variable " + variableName);
        }
        forHash.put(variableName, forStatement);
    }

    public StatementFor retrieveForStatement(String variableName) {
        return forHash.get(variableName);
    }

    public void removeCompletedForStatement(String variableName) throws BasicException {
        if (forHash.get(variableName) == null) {
            throw new BasicException(StatementFor.me().getCmd() +
                    " statement does not exist for variable " + variableName);
        }
        forHash.remove(variableName);
    }

    public void setStringVariable(String variableName, ValueString value) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid variable name: [" + variableName + "]");
        }
        if (stringConstants.containsKey(variableName.toUpperCase())) {
            throw new BasicException("Cannot set a value to a constant: [" + variableName + "]");
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal variable name: [" + variableName + "]");
        }
        if (stringArrayVariables.containsKey(variableName)) {
            throw new BasicException("Variable name already used as a string array variable name");
        }
        stringVariables.put(variableName, value);
    }

    public void setStringArrayVariable(String variableName, String index, ValueString value) {
        Map<String, ValueString> array = stringArrayVariables.get(variableName);
        if (array == null) {
            array = new HashMap<String, ValueString>();
            stringArrayVariables.put(variableName, array);
        }
        array.put(index, value);
    }

    public ValueString lookupStringVariable(String variableName) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid variable name: [" + variableName + "]");
        }
        ValueString result = stringConstants.get(variableName);
        if (result == null) {
            result = stringVariables.get(variableName);
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal variable name: [" + variableName + "]");
        }
        return result == null ? new ValueString("", getKeyboardInput()) : result;
    }

    public ValueString lookupStringArrayVariable(String variableName, String index) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid array variable name: [" + variableName + "]");
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal array variable name: [" + variableName + "]");
        }
        Map<String, ValueString> array = stringArrayVariables.get(variableName);
        if (array == null) {
            // this array has not been set, just return nothing back
            return new ValueString("", getKeyboardInput());
        }
        else {
            ValueString result = array.get(index);
            return result == null ? new ValueString("", getKeyboardInput()) : result;
        }
    }

    public void setNumberVariable(String variableName, ValueNumber value) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid variable name: [" + variableName + "]");
        }
        if (numberConstants.containsKey(variableName.toUpperCase())) {
            throw new BasicException("Cannot set a value to a constant: [" + variableName + "]");
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal variable name: [" + variableName + "]");
        }
        if (numberArrayVariables.containsKey(variableName)) {
            throw new BasicException("Variable name already used as a number array variable name");
        }
        numberVariables.put(variableName, value);
    }

    public void setNumberArrayVariable(String variableName, String index, ValueNumber value) {
        Map<String, ValueNumber> array = numberArrayVariables.get(variableName);
        if (array == null) {
            array = new HashMap<String, ValueNumber>();
            numberArrayVariables.put(variableName, array);
        }
        array.put(index, value);
    }

    public ValueNumber lookupNumberVariable(String variableName) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid variable name: [" + variableName + "]");
        }
        ValueNumber result = numberConstants.get(variableName);
        if (result == null) {
            result = numberVariables.get(variableName);
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal variable name: [" + variableName + "]");
        }
        return result == null ? new ValueNumber() : result;
    }

    public ValueNumber lookupNumberArrayVariable(String variableName, String index) throws BasicException {
        if (!ExpressionEvaluator.isValidVariableName(variableName)) {
            throw new BasicException("Invalid array variable name: [" + variableName + "]");
        }
        if (illegalVariableNames.contains(variableName.toUpperCase())) {
            throw new BasicException("Illegal array variable name: [" + variableName + "]");
        }
        Map<String, ValueNumber> array = numberArrayVariables.get(variableName);
        if (array == null) {
            // this array has not been set, just return nothing back
            return new ValueNumber();
        }
        else {
            ValueNumber result = array.get(index);
            return result == null ? new ValueNumber() : result;
        }
    }

    public boolean isFunctionName(String name) {
        return FUNCTION_NAMES.contains(name.toUpperCase());
    }

    public boolean isArrayVariableName(String value) {
        return definedArrayNames.containsKey(value);
    }

    public void defineArrayVariable(String variableName, int numDimensions) throws BasicException {
        String varNameToCheck = variableName;
        if (ExpressionEvaluator.isStringVariableName(variableName)) {
            varNameToCheck = ExpressionEvaluator.removeStringIndicator(variableName);
        }
        if (!ExpressionEvaluator.isValidVariableName(varNameToCheck)) {
            throw new BasicException("Invalid variable name: [" + variableName + "]");
        }
        if (numberConstants.containsKey(varNameToCheck.toUpperCase())) {
            throw new BasicException("Cannot set a value to a constant: [" + variableName + "]");
        }
        if (illegalVariableNames.contains(varNameToCheck.toUpperCase())) {
            throw new BasicException("Illegal variable name: [" + variableName + "]");
        }
        if (definedArrayNames.keySet().contains(variableName)) {
            throw new BasicException("Array variable already defined: [" + variableName + "]");
        }
        definedArrayNames.put(variableName, numDimensions);
    }

    public int getNumDimensionsForArray(String variableName) throws BasicException {
        Integer numDimensions = definedArrayNames.get(variableName);
        if (numDimensions == null) {
            throw new BasicException("Array not defined: [" + variableName + "]");
        }
        return numDimensions;
    }

}
