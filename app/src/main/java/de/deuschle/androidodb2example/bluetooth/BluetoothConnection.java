package de.deuschle.androidodb2example.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

public interface BluetoothConnection {
    Set<BluetoothDevice> getBoundedDevices();

    boolean connect(BluetoothDevice device);

    boolean disconnect();
}
