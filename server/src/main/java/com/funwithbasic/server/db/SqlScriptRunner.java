package com.funwithbasic.server.db;

import com.funwithbasic.server.tool.LogTool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SqlScriptRunner {

    static final String COMMENT_TOKEN = "--";
    static final String END_OF_STATEMENT_DELIMITER = ";";

    public static void run(Connection connection, String filename) throws SQLException {
        String[] lines = loadFileIntoLines(filename);
        List<String> statements = convertLinesIntoStatements(lines);
        for (String statement : statements) {
            executeStatement(connection, statement);
        }
    }

    static String convertStreamToString(InputStream stream) {
        Scanner scanner = new Scanner(stream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    static String[] loadFileIntoLines(String filename) {
        InputStream resourceAsStream = SqlScriptRunner.class.getClassLoader().getResourceAsStream(filename);
        String wholeFileAsOneString = convertStreamToString(resourceAsStream);
        return wholeFileAsOneString.split("\\r?\\n");
    }

    static List<String> convertLinesIntoStatements(String[] lines) {
        List<String> statements = new ArrayList<String>();
        StringBuilder statement = new StringBuilder();
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith(COMMENT_TOKEN) && trimmedLine.length() > 0) {
                statement.append(" ");
                statement.append(line);
                if (trimmedLine.endsWith(END_OF_STATEMENT_DELIMITER)) {
                    statements.add(statement.toString());
                    statement.setLength(0);
                }
            }
        }
        return statements;
    }

    static void executeStatement(Connection connection, String statement) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LogTool.error("Sql failed with this statement [" + statement + "] : " + e.getMessage(), e);
            throw e;
        }
    }

}
