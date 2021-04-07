package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vin")
public class VinEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String vin;

    public VinEntity() {
    }

    public VinEntity(String vin) {
        this.vin = vin;
    }
}
