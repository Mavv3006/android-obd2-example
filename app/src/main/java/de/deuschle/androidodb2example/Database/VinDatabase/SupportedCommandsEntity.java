package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "supportedCommands", primaryKeys = {"vin", "pid"})
public class SupportedCommandsEntity {
    @NonNull
    public String vin;
    @NonNull
    public String pid;
    public int isSupported;

    public SupportedCommandsEntity(@NonNull String vin, @NonNull String pid, int isSupported) {
        this.vin = vin;
        this.pid = pid;
        this.isSupported = isSupported;
    }

    public SupportedCommandsEntity(@NonNull String vin, @NonNull String pid, boolean isSupported) {
        this.vin = vin;
        this.pid = pid;
        this.isSupported = isSupported ? 1 : 0;
    }
}
