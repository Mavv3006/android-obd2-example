package de.deuschle.androidodb2example;

import android.app.Application;

import java.util.LinkedList;
import java.util.Queue;

import de.deuschle.obd.commands.ObdCommand;

public class ObdApplication extends Application {
    private final Queue<ObdCommand> commandQueue = new LinkedList<>();
    private String deviceAdress;
    private String deviceName;

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
