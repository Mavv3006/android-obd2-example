package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vin")
public class VinEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String vin;
}
