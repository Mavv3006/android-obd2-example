package de.deuschle.androidodb2example.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM SessionEntity WHERE sessionId = :sessionId")
    SessionEntity getSessionById(int sessionId);

    @Query("SELECT sessionId FROM SessionEntity ORDER BY date DESC LIMIT 1")
    int getLatestSessionId();

    @Query("SELECT * FROM SessionEntity")
    LiveData<List<SessionEntity>> getAll();

    @Insert
    void insert(SessionEntity sessionEntity);

    @Delete
    int delete(SessionEntity sessionEntity);
}
