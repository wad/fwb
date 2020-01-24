package com.funwithbasic.server.tool;

// All dates used in the application, and stored in the database, are UTC.
// Any dates shown to the user are converted at the last possible instant to
// the user's local timezone.

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import org.joda.time.DateTime;

public class DateTool {

    static final String DATE_FORMAT_LONG = "yyyy-MMM-dd HH:mm:ss";
    static final String TIMEZONE_ID_UTC = "UTC";

    public static List<String> getAvailableTimezones() {
        String[] availableIDs = TimeZone.getAvailableIDs();
        Arrays.sort(availableIDs);
        List<String> idWithDisplayName = new ArrayList<String>(availableIDs.length);
        for (String id : availableIDs) {
            idWithDisplayName.add(id + " : " + TimeZone.getTimeZone(id).getDisplayName());
        }
        return idWithDisplayName;
    }

    public static String getDefaultTimezone() {
        return TimeZone.getDefault().getID();
    }

    public static Timestamp getNow() {
        DateTime now = new DateTime();
        return new Timestamp(now.toDate().getTime());
    }

    public static String formatDateAsLocal(Date date) {
        return formatDate(date, getDefaultTimezone());
    }

    public static String formatDateAsUTC(Date date) {
        return formatDate(date, TIMEZONE_ID_UTC);
    }

    public static String formatDate(Date date, String timezoneId) {
        SimpleDateFormat formatUTC = new SimpleDateFormat(DATE_FORMAT_LONG);
        formatUTC.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return formatUTC.format(date);
    }

}
