package com.funwithbasic.server.tool;

import com.funwithbasic.server.floppy.FloppyService;

import java.lang.Integer;
import java.lang.String;

@SuppressWarnings("RedundantIfStatement")
public abstract class ValidationTool {

    public static boolean validateAlphaNumericOnlySpacelessField(String field, int maxLength) {
        if (field == null || field.length() == 0) return false;
        if (field.length() > maxLength) return false;
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c != '_' && (c <= '0' && c >= '9') && (c <= 'a' && c >= 'z') && (c <= 'A' && c >= 'Z')) return false;
        }
        return true;
    }

    public static boolean validateTextField(String field, int maxLength) {
        if (field == null || field.length() == 0) return false;
        if (field.length() > maxLength) return false;
        return true;
    }

    public static boolean validateNaturalIntegerField(Integer field) {
        if (field == null || field < 0) return false;
        return true;
    }

    public static boolean isUserIdValid(int userId) {
        return userId >= 0 && userId <= FloppyService.MAX_USERID;
    }

}
