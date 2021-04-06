package de.deuschle.androidodb2example;

import android.app.Application;

import androidx.room.Room;

import java.util.LinkedList;
import java.util.Queue;

import de.deuschle.androidodb2example.Database.StreamingDataDatabase;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.obd.commands.ObdCommand;

public class ObdApplication extends Application {
    private final Queue<ObdCommand> commandQueue = new LinkedList<>();
    private String deviceAdress;
    private String deviceName;
    private Session session;

    public StreamingDataDatabase getDatabase() {
        return Room.databaseBuilder(this, StreamingDataDatabase.class, getString(R.string.database_name)).build();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getDeviceAdress() {
        return deviceAdress;
    }

    public void setDeviceAdress(String deviceAdress) {
        this.deviceAdress = deviceAdress;
    }

    public Queue<ObdCommand> getCommandQueue() {
        return commandQueue;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
