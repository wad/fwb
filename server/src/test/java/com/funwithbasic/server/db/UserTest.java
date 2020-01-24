package com.funwithbasic.server.db;

import com.funwithbasic.Constants;
import com.funwithbasic.server.dto.UserInfo;
import com.funwithbasic.server.owner.OwnerService;
import com.funwithbasic.server.tool.ValidationException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserTest {

    private Connection connection;

    @Before
    public void setup() throws SQLException {
        connection = OwnerService.getConnection();
    }

    @Test
    public void testCRUDandPurge() throws SQLException, ValidationException {
        String username = "nobody";

        UserTableManager.purgeUser(connection, username);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setRealName("a");
        userInfo.setEmail("b");
        userInfo.setTimezone("c");
        userInfo.setPassword("d");
        userInfo.setUserStatusCode(Constants.UserStatus.Normal.ordinal());
        userInfo.setUserTypeCode(Constants.UserType.Forever.ordinal());

        int userId = UserTableManager.createUser(connection, userInfo);

        assertTrue(UserTableManager.doesUsernameExist(connection, username));

        UserInfo readFromDatabase = UserTableManager.lookupUser(connection, username);
        assertEquals("[userId=" + userId + " username=nobody realName=a email=b timezone=c userStatusCode=0 userTypeCode=2]", readFromDatabase.toString());

        UserTableManager.deleteUser(connection, userId);

        UserInfo readFromDatabaseAfterDelete = UserTableManager.lookupUser(connection, username);
        assertEquals("[userId=" + userId + " username=nobody realName=a email=b timezone=c userStatusCode=1 userTypeCode=2]", readFromDatabaseAfterDelete.toString());

        UserTableManager.purgeUser(connection, username);
        assertFalse(UserTableManager.doesUsernameExist(connection, username));
    }

    @Test
    public void testDoesUsernameExist() throws SQLException {
        assertTrue(UserTableManager.doesUsernameExist(connection, UserTableManager.USER_NAME_OWNER));
        assertFalse(UserTableManager.doesUsernameExist(connection, "nobodyhere"));
    }

    @Test
    public void testLookupUser() throws SQLException {
        UserInfo owner = UserTableManager.lookupUser(connection, UserTableManager.USER_NAME_OWNER);
        assertEquals("[userId=1 username=owner realName=Owner email=owner@funwithbasic.com timezone=America/Los_Angeles userStatusCode=5 userTypeCode=2]", owner.toString());
    }

}
