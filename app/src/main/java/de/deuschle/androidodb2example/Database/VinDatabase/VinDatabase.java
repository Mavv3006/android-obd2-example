package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {VinEntity.class, SupportedCommandsEntity.class}, version = 1, exportSchema = false)
public abstract class VinDatabase extends RoomDatabase {
    public abstract VinDao getVinDao();

    public abstract SupportedCommandsDao getSupportedCommandsDao();
}
