package de.deuschle.androidodb2example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothConnectionService implements BluetoothConnection {
    public final static UUID UUID_UCSI_NOTIFY_TX = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_UCSI_NOTIFY_RX = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private static final String TAG = BluetoothConnectionService.class.getSimpleName();
    private static final String CharacteristicIndication = " - new Characteristic\n";
    private static final UUID UUID_UCSI_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private final List<BluetoothDevice> foundDevices = new ArrayList<>();
    private final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            deviceFound(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                deviceFound(result.getDevice());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Scanning for devices failed with error code: " + errorCode);
        }
    };
    private BluetoothGatt bluetoothGatt;
    private Context context;
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                switch (newState) {
                    case BluetoothGatt.STATE_CONNECTED:
                        Log.i(TAG, "Connected");
                        gatt.requestMtu(256);
                        gatt.discoverServices();
                        break;
                    case BluetoothGatt.STATE_DISCONNECTED:
                        bluetoothGatt.close();
                        bluetoothGatt = null;
                        Log.i(TAG, "Disconnected");
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                findService(gatt.getServices());
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(characteristic);
        }
    };

    private void broadcastUpdate(BluetoothGattCharacteristic characteristic) {
        final byte[] data = characteristic.getValue();
        String dataString = new String(data, StandardCharsets.US_ASCII);
        writeToFile(CharacteristicIndication + dataString);
    }

    private void findService(List<BluetoothGattService> gattServices) {
        writeToFile("Amount of services found: " + gattServices.size());
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equalsIgnoreCase(UUID_UCSI_SERVICE.toString())) {
                writeToFile("UCSI_SERVICE found. UUID: " + UUID_UCSI_SERVICE.toString());
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                writeToFile("Amount of characteristics found: " + gattServices.size());
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_UCSI_NOTIFY_TX.toString())) {
                        writeToFile("UCSI_NOTIFY_TX found. UUID: " + UUID_UCSI_NOTIFY_TX.toString());
                    } else if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_UCSI_NOTIFY_RX.toString())) {
                        writeToFile("CSI_NOTIFY_RX found. UUID: " + UUID_UCSI_NOTIFY_RX.toString());
                    }
                }
            }
        }
    }

    @Override
    public List<BluetoothDevice> getFoundDevices() {
        return foundDevices;
    }

    private void deviceFound(BluetoothDevice device) {
        foundDevices.add(device);
    }

    @Override
    public void connect(BluetoothDevice device) {
        bluetoothGatt = device.connectGatt(context, true, gattCallback);
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public void scanForDevices() {
        adapter.getBluetoothLeScanner().startScan(scanCallback);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("services.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data + "\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }
}
