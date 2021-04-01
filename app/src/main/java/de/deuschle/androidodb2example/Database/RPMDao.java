package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RPMDao {
    @Query("SELECT * FROM RPMEntity WHERE sessionId = :sessionId")
    RPMEntity getRPMById(int sessionId);

    @Insert
    void insert(RPMEntity... rpmEntities);

    @Delete
    void delete(RPMEntity sessionId);
}
