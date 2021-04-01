package de.deuschle.androidodb2example.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "SessionEntity")
public class SessionEntity {
    @PrimaryKey
    public int sessionId;

    @ColumnInfo(name = "date")
    public LocalDate date;
}
