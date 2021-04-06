package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SupportedCommandsDao {
    @Query("SELECT * FROM supportedCommands")
    List<SupportedCommandsEntity> getAll();

    @Insert
    void insert(List<SupportedCommandsEntity> entities);

    @Insert
    void insert(SupportedCommandsEntity entity);
}
