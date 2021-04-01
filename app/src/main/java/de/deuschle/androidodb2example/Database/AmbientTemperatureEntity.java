package de.deuschle.androidodb2example.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AmbientTemperatureEntity")
public class AmbientTemperatureEntity {
    @PrimaryKey
    public int sessionId;

    @ColumnInfo(name = "Value")
    public double value;

    @ColumnInfo(name = "n")
    public int n;
}
