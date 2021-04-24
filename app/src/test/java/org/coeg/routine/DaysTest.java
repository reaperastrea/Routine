package org.coeg.routine;

import org.coeg.routine.backend.Days;
import org.coeg.routine.backend.Routine;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class DaysTest {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void daysTest() throws Exception {
        Days[] days = new Days[] { Days.Monday, Days.Wednesday, Days.Friday };
        Routine routine = new Routine();
        int len;

        routine.setId(1);
        routine.setName("Test Routine");
        routine.setActive(true);
        routine.setDays(days);
        routine.setTime(formatter.parse("14:00:00"));

        Days[] routineDays = routine.getDays();
        len = routineDays.length;

        assertEquals(21, routine.getDaysAsInteger());
        assertEquals(days.length, routineDays.length);
        for(int i = 0; i < len; i++) assertEquals(days[i], routineDays[i]);
    }
}
