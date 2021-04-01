package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM SessionEntity WHERE sessionId = :sessionId")
    SessionEntity getSessionById(int sessionId);

    @Insert
    void insert(SessionEntity... sessionEntities);

    @Delete
    void delete(SessionEntity sessionId);
}
