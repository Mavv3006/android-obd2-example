package de.deuschle.androidodb2example.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VehicleSpeedEntity")
public class VehicleSpeedEntity {
    @PrimaryKey
    public int sessionId;

    public double value;

    public int n;
}