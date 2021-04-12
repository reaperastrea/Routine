package org.coeg.routine;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutineDao;
import org.coeg.routine.backend.RoutinesHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RoutineDatabaseTest {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private RoutinesHandler handler;

    private int counter = 0;
    private final int total = 4;

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        handler = new RoutinesHandler(context);
    }

    @Test
    public void writeRoutineAndRead() throws Exception {
        handler.deleteAllRoutines();
        Routine routine = new Routine();
        routine.setId(1);
        routine.setName("Talk to senpai");
        routine.setTime(formatter.parse("14:00:00"));
        routine.setActive(true);

        handler.addRoutine(routine);
        List<Routine> query = handler.getAllRoutines();
        counter++;

        assertEquals(query.get(0).getName(), routine.getName());
    }

    @Test
    public void writeMultipleRoutinesAndRead() throws Exception {
        handler.deleteAllRoutines();

        Routine[] routines = {
                new Routine(),
                new Routine(),
                new Routine(),
                new Routine()
        };
        routines[0].setId(1);
        routines[0].setName("Talk to senpai");
        routines[0].setTime(formatter.parse("14:00:00"));
        routines[0].setActive(true);
        routines[1].setId(2);
        routines[1].setName("Get bath");
        routines[1].setTime(formatter.parse("15:00:00"));
        routines[1].setActive(true);
        routines[2].setId(3);
        routines[2].setName("Nap time");
        routines[2].setTime(formatter.parse("16:00:00"));
        routines[2].setActive(true);
        routines[3].setId(4);
        routines[3].setName("Weapons check-up");
        routines[3].setTime(formatter.parse("17:00:00"));
        routines[3].setActive(true);

        handler.addRoutine(routines);
        List<Routine> query = handler.getAllRoutines();
        counter++;
        assertEquals(query.get(0).getName(), routines[0].getName());
        assertEquals(query.get(1).getName(), routines[1].getName());
        assertEquals(query.get(2).getName(), routines[2].getName());
        assertEquals(query.get(3).getName(), routines[3].getName());
    }

    @Test
    public void updateRoutine() throws Exception {
        handler.deleteAllRoutines();
        Routine routine = new Routine();
        routine.setId(1);
        routine.setName("Talk to senpai");
        routine.setTime(formatter.parse("14:00:00"));
        routine.setActive(true);
        handler.addRoutine(routine);

        routine.setId(1);
        routine.setName("Get bath");
        routine.setTime(formatter.parse("15:00:00"));
        routine.setActive(true);
        handler.updateRoutine(routine);

        List<Routine> query = handler.getAllRoutines();
        counter++;
        assertEquals(query.get(0).getName(), routine.getName());
    }

    @Test
    public void deleteRoutine() throws Exception {
        handler.deleteAllRoutines();
        Routine[] routines = {
                new Routine(),
                new Routine()
        };
        routines[0].setId(1);
        routines[0].setName("Talk to senpai");
        routines[0].setTime(formatter.parse("14:00:00"));
        routines[0].setActive(true);
        routines[1].setId(2);
        routines[1].setName("Get bath");
        routines[1].setTime(formatter.parse("15:00:00"));
        routines[1].setActive(true);
        handler.addRoutine(routines);

        String expected = routines[1].getName();
        handler.deleteRoutine(routines[0]);
        List<Routine> query = handler.getAllRoutines();
        // Log.i("deleteRoutine", query.toString());
        counter++;
        assertEquals(query.size(), 1);
    }

    @After
    public void closeDatabase() throws IOException {
        if(counter == total) handler.close();
    }
}