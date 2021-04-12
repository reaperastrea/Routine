package org.coeg.routine.backend;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoutineDao {
    @Query("SELECT * FROM routine")
    List<Routine> getAll();

    @Query("SELECT * FROM routine WHERE id=:id")
    Routine getRoutineById(int id);

    @Update
    public void updateRoutine(Routine routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRoutine(Routine routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRoutine(Routine... routines);

    @Delete
    public void deleteRoutine(Routine routine);

    @Query("DELETE FROM routine")
    public void deleteAll();
}
