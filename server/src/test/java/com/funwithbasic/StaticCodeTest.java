package com.funwithbasic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("UnusedAssignment")
public class StaticCodeTest {

    @Test
    public void testEventStatus() {
        int i = 0;
        assertEquals(i++, Constants.EventStatus.AboutToAttempt.ordinal());
        assertEquals(i++, Constants.EventStatus.Succeeded.ordinal());
        assertEquals(i++, Constants.EventStatus.Failed.ordinal());
    }

    @Test
    public void testEventType() {
        int i = 0;
        assertEquals(i++, Constants.UserType.Free.ordinal());
        assertEquals(i++, Constants.UserType.Premium.ordinal());
        assertEquals(i++, Constants.UserType.Forever.ordinal());
    }

    @Test
    public void testUserStatus() {
        int i = 0;
        assertEquals(i++, Constants.UserStatus.Normal.ordinal());
        assertEquals(i++, Constants.UserStatus.Deleted.ordinal());
        assertEquals(i++, Constants.UserStatus.Disabled.ordinal());
        assertEquals(i++, Constants.UserStatus.Banned.ordinal());
        assertEquals(i++, Constants.UserStatus.Guest.ordinal());
        assertEquals(i++, Constants.UserStatus.Owner.ordinal());
    }

    @Test
    public void testUserEvent() {
        int i = 0;
        assertEquals(i++, Constants.UserEvent.Create.ordinal());
        assertEquals(i++, Constants.UserEvent.UpdateToPremium.ordinal());
        assertEquals(i++, Constants.UserEvent.Delete.ordinal());
    }

}
