package com.funwithbasic.server.owner;

import com.funwithbasic.server.db.DbConstants;
import com.funwithbasic.server.db.SqlScriptRunner;
import com.funwithbasic.server.db.UserTableManager;
import com.funwithbasic.server.floppy.FloppyException;
import com.funwithbasic.server.floppy.FloppyService;
import com.funwithbasic.server.tool.LogTool;
import com.funwithbasic.server.tool.PropertyTool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerService {

    static final String COMMAND_INSTALL = "install";
    static final String COMMAND_UNINSTALL = "uninstall";

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static void main(String... args) throws SQLException {
        if (args.length != 2) {
            LogTool.warn("You need to specify what you want the installer to do: '" + COMMAND_INSTALL + "' or '" + COMMAND_UNINSTALL + "'," +
                    " and then the owner password");
            LogTool.info("Note that if you are installing fresh, you need to first create the database. See the contents of create_database.sql");
        } else {
            String ownerPassword = args[1];
            Connection connection = null;
            try {
                connection = getConnection();
                String command = args[0];
                if (COMMAND_INSTALL.equals(command)) {
                    install(connection, ownerPassword);
                } else if (COMMAND_UNINSTALL.equals(command)) {
                    uninstall(connection, ownerPassword);
                }
            } catch (SQLException e) {
                LogTool.error("Could not get database connection: " + e.getMessage(), e);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        LogTool.error("Failed to close connection: " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    static String install(Connection connection, String ownerPassword) {
        StringBuilder reply = new StringBuilder();
        try {
            verifyOwnerPassword(ownerPassword);
            reply.append(LogTool.info("install start"));
            if (determineIsAlreadyInstalled(connection)) {
                reply.append(LogTool.warn("Did not install, it appears to already be installed."));
                reply.append(LogTool.info("install FAILED"));
            } else {
                reply.append(LogTool.info("create tables"));
                SqlScriptRunner.run(connection, DbConstants.SCRIPT_FILENAME_CREATE_ALL_TABLES);

                reply.append(LogTool.info("install default users"));
                UserTableManager.installDefaultUsers(connection);

                int numFloppies = FloppyService.listFloppies().size();
                reply.append(LogTool.info("There are " + numFloppies + " user directories (floppies) already present."));

                reply.append(LogTool.info("install complete"));
            }
        } catch (Exception e) {
            reply.append(LogTool.error("Failed to install: " + e.getMessage(), e));
        }
        return reply.toString();
    }

    static String uninstall(Connection connection, String ownerPassword) {
        StringBuilder reply = new StringBuilder();
        try {
            verifyOwnerPassword(ownerPassword);
            reply.append(LogTool.info("uninstall start"));
            reply.append(LogTool.info("drop tables"));
            SqlScriptRunner.run(connection, DbConstants.SCRIPT_FILENAME_DROP_ALL_TABLES);
            reply.append(LogTool.info("drop floppy directories (user data)"));
            try {
                List<String> floppies = FloppyService.listFloppies();
                for (String floppy : floppies) {
                    FloppyService.deleteFloppy(Integer.parseInt(floppy));
                }
            } catch (FloppyException e) {
                reply.append(LogTool.warn("Failed to delete floppies: " + e.getMessage()));
            }
            reply.append(LogTool.info("uninstall complete"));
        } catch (Exception e) {
            reply.append(LogTool.error("Failed to uninstall: " + e.getMessage(), e));
        }
        return reply.toString();
    }

    static void verifyOwnerPassword(String ownerPassword) throws Exception {
        if (!PropertyTool.get(PropertyTool.prop.ownerPassword).equals(ownerPassword)) {
            throw new Exception("Owner password did not match what is in " + PropertyTool.PROPERTY_FILE);
        }
    }

    public static boolean determineIsAlreadyInstalled(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
        List<String> tableNames = new ArrayList<String>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }
        return !tableNames.isEmpty();
    }

    public static Connection getConnection() throws SQLException {
        String host = PropertyTool.get(PropertyTool.prop.databaseHostName);
        String dbName = PropertyTool.get(PropertyTool.prop.databaseName);
        String user = PropertyTool.get(PropertyTool.prop.databaseUserName);
        String password = PropertyTool.get(PropertyTool.prop.databaseUserPassword);
        String connectionString = "jdbc:mysql://" + host + "/" + dbName + "?user=" + user + "&password=" + password;
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            String message = "Could not get database driver (" + JDBC_DRIVER + "):" + e.getMessage();
            LogTool.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

}
