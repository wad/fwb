package com.funwithbasic.server.dto;

import com.funwithbasic.server.db.DbConstants;
import com.funwithbasic.server.tool.ValidationTool;

public class UserInfo {

    private Integer userId;
    private String userName;
    private String realName;
    private String email;
    private String timezone;
    private String password;
    private Integer userStatusCode;
    private Integer userTypeCode;

    public UserInfo() {
        this(null, null, null, null, null, null, null, null);
    }

    public UserInfo(Integer userId, String userName, String realName, String email, String timezone, String password, Integer userStatusCode, Integer userTypeCode) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
        this.email = email;
        this.timezone = timezone;
        this.password = password;
        this.userStatusCode = userStatusCode;
        this.userTypeCode = userTypeCode;
    }

    public boolean isValidForCreate() {
        if (!ValidationTool.validateAlphaNumericOnlySpacelessField(getUserName(), DbConstants.FIELD_LENGTH_USERNAME))
            return false;
        if (!ValidationTool.validateTextField(getRealName(), DbConstants.FIELD_LENGTH_DEFAULT))
            return false;
        if (!ValidationTool.validateTextField(getEmail(), DbConstants.FIELD_LENGTH_DEFAULT))
            return false;
        if (!ValidationTool.validateTextField(getTimezone(), DbConstants.FIELD_LENGTH_DEFAULT))
            return false;
        if (!ValidationTool.validateTextField(getPassword(), DbConstants.FIELD_LENGTH_DEFAULT))
            return false;
        if (!ValidationTool.validateNaturalIntegerField(getUserStatusCode()))
            return false;
        //noinspection RedundantIfStatement
        if (!ValidationTool.validateNaturalIntegerField(getUserTypeCode()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append("userId=");
        builder.append(getUserId() == null ? "" : getUserId());
        builder.append(" ");
        builder.append("username=");
        builder.append(getUserName() == null ? "" : getUserName());
        builder.append(" ");
        builder.append("realName=");
        builder.append(getRealName() == null ? "" : getRealName());
        builder.append(" ");
        builder.append("email=");
        builder.append(getEmail() == null ? "" : getEmail());
        builder.append(" ");
        builder.append("timezone=");
        builder.append(getTimezone() == null ? "" : getTimezone());
        builder.append(" ");
        builder.append("userStatusCode=");
        builder.append(getUserStatusCode() == null ? "" : String.valueOf(getUserStatusCode()));
        builder.append(" ");
        builder.append("userTypeCode=");
        builder.append(getUserTypeCode() == null ? "" : String.valueOf(getUserTypeCode()));
        builder.append("]");
        return builder.toString();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserStatusCode() {
        return userStatusCode;
    }

    public void setUserStatusCode(Integer userStatusCode) {
        this.userStatusCode = userStatusCode;
    }

    public Integer getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(Integer userTypeCode) {
        this.userTypeCode = userTypeCode;
    }
}
