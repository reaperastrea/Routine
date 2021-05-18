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
    private static final String TABLE_ROUTINES = "routines";
    private static final String TABLE_HISTORY = "history";

    private RoutineDatabase db;
    private RoutineDao routineDao;
    private HistoryDao historyDao;

    public RoutinesHandler(Context context) {
        db = RoutineDatabase.getInstance(context);
        routineDao = db.routineDao();
        historyDao = db.historyDao();
    }

    public void close() {
        db.close();
    }

    public Routine getRoutine(int id) {
        return routineDao.getRoutineById(id);
    }

    public History getHistory(int id) { return historyDao.getHistoryById(id); }

    public List<Routine> getAllRoutines() {
        return routineDao.getAll();
    }

    public List<History> getAllHistory() { return historyDao.getAll(); }

    public long addRoutine(Routine routine) {
        return routineDao.insertRoutine(routine);
    }

    public void addRoutine(Routine... routine) {
        routineDao.insertRoutine(routine);
    }

    public void addHistory(History history) { historyDao.insertHistory(history); }

    public void addHistory(History... history) { historyDao.insertHistory(history); }

    public void updateRoutine(Routine routine) {
        routineDao.updateRoutine(routine);
    }

    public void updateHistory(History history) { historyDao.updateHistory(history); }

    public void deleteRoutine(Routine routine) {
        routineDao.deleteRoutine(routine);
    }

    public void deleteHistory(History history) { historyDao.deleteHistory(history); }

    public void deleteAllRoutines() {
        routineDao.deleteAll();
    }

    public void deleteAllHistory() { historyDao.deleteAll(); }

    public int getRoutineCount() { return routineDao.getAll().size(); }

    public int getHistoryCount() { return historyDao.getAll().size(); }
}
