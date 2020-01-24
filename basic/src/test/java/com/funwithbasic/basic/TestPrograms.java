package com.funwithbasic.basic;

import com.funwithbasic.basic.testhelper.BasicTestHelper;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPrograms {

    private static final String PATH_FOR_RUNNING_TESTS_FROM_MVN = "src/test/resources/test_programs/";
    private static final String PATH_FOR_RUNNING_TESTS_FROM_IDE = "basic/" + PATH_FOR_RUNNING_TESTS_FROM_MVN;
    private static final String BASIC_EXTENSION = ".basic";
    private static final String OUTPUT_EXTENSION = ".output";
    private static final String ERROR_EXTENSION = ".error";

    private static final String FILENAME_OF_MARKER_FILE = "000_do_not_delete_me";
    private String pathToUse = null;

    // This is a silly hack to determine how these tests are being run, from maven or from the IDE.
    private String determinePath() {
        if (pathToUse == null) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(PATH_FOR_RUNNING_TESTS_FROM_MVN + FILENAME_OF_MARKER_FILE);
                pathToUse = PATH_FOR_RUNNING_TESTS_FROM_MVN;
            } catch(FileNotFoundException e) {
                pathToUse = PATH_FOR_RUNNING_TESTS_FROM_IDE;
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (IOException x) {
                        // do nothing
                    }
                }
            }
        }
        return pathToUse;
    }

    @Test
    public void testProgramsHappyDay() throws IOException {
        List<String> filenameBases = new ArrayList<String>() {{
            add("simple_addition");
            add("misc_stuff");
            add("if");
            add("loop");
            add("conditional_operators");
            add("string_equality");
            add("printing");
            add("move_cursor");
            add("input");
            add("print_newlines");
            add("asc");
            add("whitespace");
            add("with_line_numbers");
        }};
        for (String filenameBase : filenameBases) {
            BasicTestHelper basicTestHelper = new BasicTestHelper();
            BasicRunner runner = basicTestHelper.getBasicRunner();
            assertTrue(runner.runProgram(BasicLoader.loadIntoStringList(determinePath() + filenameBase + BASIC_EXTENSION)));
            assertEquals(BasicLoader.loadIntoString(determinePath() + filenameBase + OUTPUT_EXTENSION), basicTestHelper.getLog());
        }
    }

    @Test
    public void testProgramsThatErrorOut() throws IOException {
        List<String> filenameBases = new ArrayList<String>() {{
            add("mixing_strings_and_numbers");
            add("printing_invalid_characters");
        }};
        for (String filenameBase : filenameBases) {
            BasicTestHelper basicTestHelper = new BasicTestHelper();
            BasicRunner runner = basicTestHelper.getBasicRunner();
            assertFalse(runner.runProgram(BasicLoader.loadIntoStringList(determinePath() + filenameBase + BASIC_EXTENSION)));
            assertEquals(BasicLoader.loadIntoString(determinePath() + filenameBase + ERROR_EXTENSION), basicTestHelper.getErrorLog());
        }
    }

}
