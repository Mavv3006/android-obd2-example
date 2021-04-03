package de.deuschle.androidodb2example.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "SessionEntity")
public class SessionEntity {
    @PrimaryKey(autoGenerate = true)
    public int sessionId;

    public LocalDateTime date;

    @Override
    public String toString() {
        String s = "SessionEntity{" +
                "sessionId=" + sessionId;
        if (date == null) {
            return s + '}';
        }
        return s + ", date=" + date.toString() + '}';
    }
}
