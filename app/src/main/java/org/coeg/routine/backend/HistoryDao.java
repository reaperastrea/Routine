package org.coeg.routine.backend;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history")
    List<History> getAll();

    @Query("SELECT * FROM history WHERE id=:id")
    History getHistoryById(int id);

    @Update
    public void updateHistory(History history);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertHistory(History history);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertHistory(History... histories);

    @Delete
    public void deleteHistory(History history);

    @Query("DELETE FROM history")
    public void deleteAll();
}
