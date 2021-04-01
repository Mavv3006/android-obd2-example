package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;

import de.deuschle.androidodb2example.Session.SessionData;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM SessionEntity WHERE sessionId = :sessionId")
    SessionEntity getSessionById(int sessionId);

    @Query("SELECT sessionId FROM SessionEntity ORDER BY date DESC LIMIT 1")
    int getLatestSessionId();

    @Insert(entity = SessionData.class)
    void insert(LocalDateTime dateTime);

    @Delete
    void delete(SessionEntity sessionId);
}
