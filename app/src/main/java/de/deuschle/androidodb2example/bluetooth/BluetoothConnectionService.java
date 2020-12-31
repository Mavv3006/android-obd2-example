package de.deuschle.androidodb2example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class BluetoothConnectionService implements BluetoothConnection {
    @Override
    public Set<BluetoothDevice> getBoundedDevices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.getBondedDevices();
    }

    @Override
    public boolean connect(BluetoothDevice device) {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }
}
