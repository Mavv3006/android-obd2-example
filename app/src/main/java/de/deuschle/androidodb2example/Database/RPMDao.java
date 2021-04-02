package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.deuschle.androidodb2example.Session.SessionData;

@Dao
public interface RPMDao {
    @Query("SELECT * FROM RPMEntity WHERE sessionId = :sessionId")
    RPMEntity getRPMById(int sessionId);

    @Query("SELECT * FROM RPMEntity")
    List<RPMEntity> getAll();

    @Insert(entity = RPMEntity.class)
    void insert(SessionData rpmEntities);

    @Delete
    void delete(RPMEntity entity);
}
