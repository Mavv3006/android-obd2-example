package de.deuschle.androidodb2example.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.DeviceControlActivity;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Streams.BleOutputStream;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final BleOutputStream bleOutputStream = new BleOutputStream();
    private BluetoothLeService bluetoothLeService;
    private boolean isConnected = false;
    Button disconnectButton;
    Button connectButton;
    Toolbar toolbar;
    String deviceName;
    String deviceAddress;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
            bleOutputStream.setBleService(bluetoothLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnected = false;
                toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_disconnected));
                toggleButtons();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                isConnected = true;
                toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_connected));
                ShowDialog();
                toggleButtons();
                Log.e(TAG, "In what we need");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "RECV DATA");
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.d(LogTags.OBD2, "Data: " + data);
            }
        }
    };

    private void ShowDialog() {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();
        deviceAddress = intent.getStringExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS);
        deviceName = intent.getStringExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME);

        // Sets up UI references
        connectButton = findViewById(R.id.main_button_connect);
        disconnectButton = findViewById(R.id.main_button_disconnect);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_disconnected));
        setSupportActionBar(toolbar);

        fuckMarshMallow();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        connectButton.setOnClickListener(view -> bluetoothLeService.connect(deviceAddress));

        disconnectButton.setOnClickListener(view -> bluetoothLeService.disconnect());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    private void toggleButtons() {
        disconnectButton.setEnabled(!disconnectButton.isEnabled());
        connectButton.setEnabled(!connectButton.isEnabled());
    }

    public void goToEngineRPM(View view) {
        Intent intent = new Intent(this, EngineRPM.class);
        startActivity(intent);
    }

    public void readVehicleSpeed(View view) {
        Intent intent = new Intent(this, VehicleSpeedActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mGattUpdateReceiver);
        //unbindService(mServiceConnection);
        if (bluetoothLeService != null) {
            bluetoothLeService.close();
            bluetoothLeService = null;
        }
        Log.d(TAG, "We are in destroy");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == DeviceControlActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initial
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);

            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted

                // Permission Denied
                Toast.makeText(MainActivity.this, "All Permission GRANTED !! Thank You :)", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "One or More Permissions are DENIED Exiting App :(", Toast.LENGTH_SHORT)
                        .show();

                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void fuckMarshMallow() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Show Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                StringBuilder message = new StringBuilder("App need access to " + permissionsNeeded.get(0));

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));

                showMessageOKCancel(message.toString(),
                        (dialog, which) -> requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                DeviceControlActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS));
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    DeviceControlActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        Toast.makeText(MainActivity.this, "No new Permission Required- Launching App .You are Awesome!!", Toast.LENGTH_SHORT)
                .show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return shouldShowRequestPermissionRationale(permission);
        }
        return true;
    }
}