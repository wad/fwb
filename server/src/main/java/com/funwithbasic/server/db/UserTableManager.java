package com.funwithbasic.server.db;

import com.funwithbasic.Constants;
import com.funwithbasic.server.tool.DateTool;
import com.funwithbasic.server.tool.LogTool;
import com.funwithbasic.server.dto.UserInfo;
import com.funwithbasic.server.tool.ValidationException;

import java.sql.*;

public class UserTableManager {

    public static final String TABLE_NAME_USERS = "users";

    static final String USER_NAME_OWNER = "owner";

    public static void installDefaultUsers(Connection connection) throws SQLException {
        UserInfo userOwner = new UserInfo();
        userOwner.setUserName(USER_NAME_OWNER);
        userOwner.setRealName("Owner");
        userOwner.setEmail("owner@funwithbasic.com");
        userOwner.setTimezone(DateTool.getDefaultTimezone());
        userOwner.setPassword("password");
        userOwner.setUserStatusCode(Constants.UserStatus.Owner.ordinal());
        userOwner.setUserTypeCode(Constants.UserType.Forever.ordinal());
        try {
            createUser(connection, userOwner);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public static int createUser(Connection connection, UserInfo userInfo) throws SQLException, ValidationException {
        if (!userInfo.isValidForCreate()) {
            throw new ValidationException(userInfo.toString());
        }
        int eventLogId = UserEventTableManager.createUserEventLogEntry(
                connection,
                "creating " + userInfo.toString(),
                Constants.UserEvent.Create,
                Constants.EventStatus.AboutToAttempt);
        boolean success = false;
        SQLException exceptionFromUserAdd = null;
        Integer userId = null;
        try {
            String sql = "insert into " + TABLE_NAME_USERS + " values (default, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userInfo.getUserName());
            preparedStatement.setString(2, userInfo.getRealName());
            preparedStatement.setString(3, userInfo.getEmail());
            preparedStatement.setString(4, userInfo.getTimezone());
            preparedStatement.setString(5, userInfo.getPassword());
            preparedStatement.setInt(6, userInfo.getUserStatusCode());
            preparedStatement.setInt(7, userInfo.getUserTypeCode());
            int numRowsAdded = preparedStatement.executeUpdate();
            if (numRowsAdded != 1) {
                throw new SQLException("Expected one row to be added, but got " + numRowsAdded);
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            userId = resultSet.getInt(1);

            success = true;
        } catch (SQLException e) {
            exceptionFromUserAdd = new SQLException("SQL issue adding user " + userInfo.toString() + ": " + e.getMessage(), e);
        }
        if (success) {
            UserEventTableManager.updateUserEventLogEntry(connection, eventLogId, userId, Constants.EventStatus.Succeeded);
        } else {
            try {
                UserEventTableManager.updateUserEventLogEntry(connection, eventLogId, userId, Constants.EventStatus.Failed);
            } catch (SQLException e) {
                // just throw this one away
            }
            throw exceptionFromUserAdd;
        }
        return userId;
    }

    public static boolean doesUsernameExist(Connection connection, String username) throws SQLException {
        return getUserIdFromUserName(connection, username) != null;
    }

    public static Integer getUserIdFromUserName(Connection connection, String userName) throws SQLException {
        Integer userId = null;
        try {
            String sql = "SELECT user_id FROM " + TABLE_NAME_USERS + " where user_name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                userId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to look up username: " + userName, e);
        }
        return userId;
    }

    public static UserInfo lookupUser(Connection connection, int userId) throws SQLException {
        try {
            UserInfo result = null;
            String sql = "SELECT * FROM " + TABLE_NAME_USERS + " where user_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                result = new UserInfo();
                result.setUserId(resultSet.getInt(1));
                result.setUserName(resultSet.getString(2));
                result.setRealName(resultSet.getString(3));
                result.setEmail(resultSet.getString(4));
                result.setTimezone(resultSet.getString(5));
                result.setPassword(resultSet.getString(6));
                result.setUserStatusCode(resultSet.getInt(7));
                result.setUserTypeCode(resultSet.getInt(8));
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException("Failed to look up userId: " + userId, e);
        }
    }

    public static UserInfo lookupUser(Connection connection, String userName) throws SQLException {
        try {
            UserInfo result = null;
            String sql = "SELECT * FROM " + TABLE_NAME_USERS + " where user_name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                result = new UserInfo();
                result.setUserId(resultSet.getInt(1));
                result.setUserName(resultSet.getString(2));
                result.setRealName(resultSet.getString(3));
                result.setEmail(resultSet.getString(4));
                result.setTimezone(resultSet.getString(5));
                result.setPassword(resultSet.getString(6));
                result.setUserStatusCode(resultSet.getInt(7));
                result.setUserTypeCode(resultSet.getInt(8));
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException("Failed to look up userName: " + userName, e);
        }
    }

    public static void deleteUser(Connection connection, int userId) throws SQLException {
        UserInfo userInfo = lookupUser(connection, userId);
        if (userInfo == null) {
            LogTool.warn("Attempted to delete a user that does not exist, with id " + userId);
        } else {
            userInfo.setUserStatusCode(Constants.UserStatus.Deleted.ordinal());
            try {
                updateUser(connection, userInfo, Constants.UserEvent.Delete);
            } catch (ValidationException e) {
                String message = "Got validation exception trying to delete a user id " + userId + ": " + e.getMessage();
                LogTool.error(message, e);
                throw new RuntimeException(message, e);
            }
        }
    }

    public static void updateUser(Connection connection, UserInfo userInfo, Constants.UserEvent updateCode) throws SQLException, ValidationException {
        if (!userInfo.isValidForCreate()) {
            throw new ValidationException(userInfo.toString());
        }

        UserInfo oldVersion = lookupUser(connection, userInfo.getUserName());

        String eventComment = "update from " + oldVersion.toString() + " to " + userInfo.toString();
        int eventLogId = UserEventTableManager.createUserEventLogEntry(connection, eventComment, updateCode, Constants.EventStatus.AboutToAttempt);
        boolean success = false;
        SQLException exceptionFromUserUpdate = null;
        int userId = userInfo.getUserId();
        try {
            String sql = "update " + TABLE_NAME_USERS + " set user_name=?, real_name=?, email=?, timezone=?, password=?, user_status_code_id=?, user_type_code_id=? where user_id=" + userId;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userInfo.getUserName());
            preparedStatement.setString(2, userInfo.getRealName());
            preparedStatement.setString(3, userInfo.getEmail());
            preparedStatement.setString(4, userInfo.getTimezone());
            preparedStatement.setString(5, userInfo.getPassword());
            preparedStatement.setInt(6, userInfo.getUserStatusCode());
            preparedStatement.setInt(7, userInfo.getUserTypeCode());
            int numRowsUpdated = preparedStatement.executeUpdate();
            if (numRowsUpdated != 1) {
                throw new SQLException("Expected one row to be updated, but got " + numRowsUpdated);
            }
            success = true;
        } catch (SQLException e) {
            exceptionFromUserUpdate = new SQLException("SQL issue updating user " + userInfo.toString() + ": " + e.getMessage(), e);
        }
        if (success) {
            UserEventTableManager.updateUserEventLogEntry(connection, eventLogId, userId, Constants.EventStatus.Succeeded);
        } else {
            try {
                UserEventTableManager.updateUserEventLogEntry(connection, eventLogId, userId, Constants.EventStatus.Failed);
            } catch (SQLException e) {
                // just throw this one away
            }
            throw exceptionFromUserUpdate;
        }
    }

    public static void purgeUser(Connection connection, String username) throws SQLException {
        UserInfo userToBePurged = lookupUser(connection, username);
        if (userToBePurged != null) {
            int userId = userToBePurged.getUserId();

            UserEventTableManager.purgeUserEvents(connection, userId);

            String sql = "delete from " + TABLE_NAME_USERS + " where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

}
