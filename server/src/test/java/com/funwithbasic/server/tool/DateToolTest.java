package com.funwithbasic.server.tool;

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateToolTest {

    @Test
    public void testDateFormatTimezones() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(DateTool.TIMEZONE_ID_UTC));
        cal.set(GregorianCalendar.YEAR, 2010);
        cal.set(GregorianCalendar.MONTH, 10);
        cal.set(GregorianCalendar.DATE, 12);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 8);
        cal.set(GregorianCalendar.MINUTE, 28);
        cal.set(GregorianCalendar.SECOND, 44);
        cal.set(GregorianCalendar.MILLISECOND, 45);
        Date date = cal.getTime();

        String asLocal = DateTool.formatDateAsLocal(date);
        String asUTC = DateTool.formatDateAsUTC(date);
        String asMST = DateTool.formatDate(date, "MST");

        String expectedLocal = "2010-Nov-12 00:28:44";
        String expectedUTC = "2010-Nov-12 08:28:44";
        String expectedMST = "2010-Nov-12 01:28:44";

        assertEquals(expectedLocal, asLocal);
        assertEquals(expectedUTC, asUTC);
        assertEquals(expectedMST, asMST);
    }

}
