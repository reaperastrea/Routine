package org.coeg.routine;

import android.util.Log;

import org.coeg.routine.backend.Routine;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ClassTest {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void dateTest() {
        Date time = null;
        try {
            time = formatter.parse("14:00:00");
        }
        catch(ParseException e) {
            e.printStackTrace();
        }

        Routine routine = new Routine();
        routine.setId(1);
        routine.setName("Talk to senpai");
        routine.setTime(time);
        routine.setActive(true);

        assertEquals(time, routine.getTime()); // fail, since class is Android-based?
    }
}