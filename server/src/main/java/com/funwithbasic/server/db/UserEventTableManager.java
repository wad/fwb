package com.funwithbasic.server.db;

import com.funwithbasic.Constants;
import com.funwithbasic.server.tool.DateTool;

import java.sql.*;

public class UserEventTableManager {

    public static final String TABLE_NAME_USER_EVENT_LOGS = "user_event_logs";

    public static int createUserEventLogEntry(
            Connection connection,
            String comment,
            Constants.UserEvent userEvent,
            Constants.EventStatus eventStatus) throws SQLException {
        try {
            String sql = "insert into " + TABLE_NAME_USER_EVENT_LOGS + " values (default, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, null);
            preparedStatement.setTimestamp(2, DateTool.getNow());
            preparedStatement.setInt(3, userEvent.ordinal());
            preparedStatement.setInt(4, eventStatus.ordinal());
            preparedStatement.setString(5, comment);

            int numRowsAdded = preparedStatement.executeUpdate();
            if (numRowsAdded != 1) {
                throw new SQLException("Expected one row to be added, but got " + numRowsAdded);
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int newEventId = resultSet.getInt(1);
            return newEventId;
        } catch (SQLException e) {
            throw new SQLException("SQL issue adding a user event for user comment={" + comment + "}: " + e.getMessage(), e);
        }
    }

    public static void updateUserEventLogEntry(
            Connection connection,
            int eventLogId,
            Integer userId,
            Constants.EventStatus eventStatus) throws SQLException {
        try {
            String userIdPortion = userId == null ? " " : (", user_id=" + userId);
            String sql = "update " + TABLE_NAME_USER_EVENT_LOGS + " set event_status_code_id=" + eventStatus.ordinal() + userIdPortion + " where log_id=" + eventLogId;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int numRowsChanged = preparedStatement.executeUpdate();
            if (numRowsChanged != 1) {
                throw new SQLException("Expected one row to be changed, but got " + numRowsChanged);
            }
        } catch (SQLException e) {
            throw new SQLException("SQL issue updating a user event (" + eventLogId + ") to " + eventStatus.ordinal() + ": " + e.getMessage(), e);
        }
    }

    public static void purgeUserEvents(Connection connection, int userId) throws SQLException {
        String sql = "delete from " + TABLE_NAME_USER_EVENT_LOGS + " where user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();
    }

}
