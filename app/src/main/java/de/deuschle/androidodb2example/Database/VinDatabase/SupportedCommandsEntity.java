package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "supportedCommands")
public class SupportedCommandsEntity {
    @NonNull
    @PrimaryKey
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
