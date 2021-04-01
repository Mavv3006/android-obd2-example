package de.deuschle.androidodb2example.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "SessionEntity")
public class SessionEntity {
    @PrimaryKey(autoGenerate = true)
    public int sessionId;

    public LocalDateTime date;
}
