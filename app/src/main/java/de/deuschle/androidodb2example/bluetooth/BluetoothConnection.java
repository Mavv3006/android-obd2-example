package de.deuschle.androidodb2example.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.List;

public interface BluetoothConnection {
    void connect(BluetoothDevice device);

    boolean disconnect();

    void scanForDevices();

    void setContext(Context context);

    List<BluetoothDevice> getFoundDevices();
}
