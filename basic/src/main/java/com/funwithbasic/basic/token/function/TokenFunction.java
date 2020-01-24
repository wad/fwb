package com.funwithbasic.basic.token.function;

import com.funwithbasic.basic.BasicException;
import com.funwithbasic.basic.BasicRunner;
import com.funwithbasic.basic.token.Token;
import com.funwithbasic.basic.token.TokenNonValue;
import com.funwithbasic.basic.token.TokenWithArguments;
import com.funwithbasic.basic.value.ValueString;

import java.util.List;

public abstract class TokenFunction extends TokenNonValue implements TokenWithArguments {

    // Any function name that starts with this prefix is not intended to be called by the BASIC code directly.
    public static final String FUNCTION_NAME_PREFIX_INDICATING_UNEXPOSED = "__";

    public static enum Function {
        ABS("ABS"),
        ASC("ASC"),
        CHR("CHR"),
        COS("COS"),
        INT("INT"),
        LN("LN"),
        LOG("LOG"),
        RND("RND"),
        SIN("SIN"),
        SQRT("SQRT"),
        TAN("TAN"),
        CONCAT("CONCAT"),
        STRLEN("STRLEN"),
        ISNUM("ISNUM"),
        STR2NUM("STR2NUM"),
        NUM2STR("NUM2STR"),
        CHARAT("CHARAT"),
        EVAL("EVAL"),
        PARAMS2(FUNCTION_NAME_PREFIX_INDICATING_UNEXPOSED + "PARAMS2"),
        PARAMS3(FUNCTION_NAME_PREFIX_INDICATING_UNEXPOSED + "PARAMS3"),
        PARAMS4(FUNCTION_NAME_PREFIX_INDICATING_UNEXPOSED + "PARAMS4"),
        ARRAY_VARIABLE(FUNCTION_NAME_PREFIX_INDICATING_UNEXPOSED + "ARRAY");

        private String functionText;

        Function(String functionText) {
            this.functionText = functionText;
        }

        public String getText() {
            return functionText;
        }
    }

    public static TokenFunction create(String value, BasicRunner basicRunner) throws BasicException {
        if (Function.SIN.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionSin(basicRunner);
        }
        if (Function.COS.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionCos(basicRunner);
        }
        if (Function.TAN.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionTan(basicRunner);
        }
        if (Function.SQRT.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionSqrt(basicRunner);
        }
        if (Function.LOG.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionLog(basicRunner);
        }
        if (Function.LN.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionLn(basicRunner);
        }
        if (Function.INT.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionInt(basicRunner);
        }
        if (Function.RND.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionRnd(basicRunner);
        }
        if (Function.ABS.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionAbs(basicRunner);
        }
        if (Function.CHR.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionChr(basicRunner);
        }
        if (Function.ASC.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionAsc(basicRunner);
        }
        if (Function.CONCAT.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionConcat(basicRunner);
        }
        if (Function.STRLEN.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionStrlen(basicRunner);
        }
        if (Function.ISNUM.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionIsNum(basicRunner);
        }
        if (Function.STR2NUM.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionStr2Num(basicRunner);
        }
        if (Function.NUM2STR.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionNum2Str(basicRunner);
        }
        if (Function.CHARAT.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionCharAt(basicRunner);
        }
        if (Function.EVAL.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionEval(basicRunner);
        }
        if (Function.PARAMS2.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionParams2(basicRunner);
        }
        if (Function.PARAMS3.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionParams3(basicRunner);
        }
        if (Function.PARAMS4.getText().equalsIgnoreCase(value)) {
            return new TokenFunctionParams4(basicRunner);
        }
        if (basicRunner.isArrayVariableName(value)) {
            return new TokenFunctionArrayVariable(basicRunner, value);
        }
        throw new BasicException("Cannot parse function name: [" + value + "]");
    }

    @Override
    public Type getType() {
        return Type.FUNCTION;
    }

    void verifyNumArguments(List<Token> arguments) throws BasicException {
        if (arguments.size() != getNumArguments()) {
            throw new BasicException("Expected " + getNumArguments() + " arguments but received " + arguments.size());
        }
    }

    @Override
    public ValueString getValueAsString() throws BasicException {
        return new ValueString(getSymbol(), basicRunner.getKeyboardInput());
    }

}
