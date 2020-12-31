package de.deuschle.androidodb2example.bluetooth;

public class BluetoothConnectionFactory {
    private static final BluetoothConnection connection = new BluetoothConnectionService();

    public static BluetoothConnection getConnection() {
        return connection;
    }
}
