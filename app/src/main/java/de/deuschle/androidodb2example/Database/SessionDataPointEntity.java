package de.deuschle.androidodb2example.Database;

import androidx.room.PrimaryKey;

public abstract class SessionDataPointEntity {
    @PrimaryKey
    public int sessionId;

    public double value;

    public int n;
}
