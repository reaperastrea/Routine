package org.coeg.routine.backend;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Routine.class }, version = 1)
public abstract class RoutineDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Routine";
    private static volatile RoutineDatabase instance;

    static synchronized RoutineDatabase getInstance(Context context) {
        if(instance == null) instance = create(context);
        return instance;
    }

    public RoutineDatabase() {};

    private static RoutineDatabase create(final Context context) {
        return Room.databaseBuilder(context, RoutineDatabase.class, DATABASE_NAME).build();
    }

    protected abstract RoutineDao routineDao();
}
