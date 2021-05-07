package org.coeg.routine.backend;

import android.content.Context;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class DBFetching {
    //private Context context;
    private RoutinesHandler handler;
    private LinkedList<Routine> routines = new LinkedList<>();
    private final Executor executor;

    public DBFetching(Executor executor) {
        //this.context = context;
        this.executor = executor;
    }

    public LinkedList<Routine> fetchingDB(Context context) {
        try {
            handler = new RoutinesHandler(context);
            routines = (LinkedList<Routine>) handler.getAllRoutines();
            return routines;
        } catch (Exception e) {
            return new LinkedList<Routine>();
        }
    }

    public void asyncDB(Context context){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                LinkedList<Routine> ignoredRoutines = fetchingDB(context);
            }
        });

    }

}
