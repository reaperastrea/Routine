package org.coeg.routine.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RoutinesHandler {
    private static RoutinesHandler instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Routine";
    private static final String TABLE = "routines";

    private RoutineDatabase db;
    private RoutineDao routineDao;

    public RoutinesHandler(Context context) {
        db = RoutineDatabase.getInstance(context);
        routineDao = db.routineDao();
    }

    public void close() {
        db.close();
    }

    public Routine getRoutine(int id) {
        return routineDao.getRoutineById(id);
    }

    public List<Routine> getAllRoutines() {
        return routineDao.getAll();
    }

    public void addRoutine(Routine routine) {
        routineDao.insertRoutine(routine);
    }

    public void addRoutine(Routine... routine) {
        routineDao.insertRoutine(routine);
    }

    public void updateRoutine(Routine routine) {
        routineDao.updateRoutine(routine);
    }

    public void deleteRoutine(Routine routine) {
        routineDao.deleteRoutine(routine);
    }

    public void deleteAllRoutines() {
        routineDao.deleteAll();
    }

    public int getContactsCount() {
        return routineDao.getAll().size();
    }
}
