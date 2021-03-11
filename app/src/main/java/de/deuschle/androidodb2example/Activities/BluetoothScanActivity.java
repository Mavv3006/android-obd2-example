package de.deuschle.androidodb2example.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;

public class BluetoothScanActivity extends AppCompatActivity {
    static final int REQUEST_ENABLE_BT = 1;
    static final long SCAN_PERIOD = 10_000; // Stops scanning after 10 seconds
    static final String TAG = BluetoothScanActivity.class.getSimpleName();
    static final String[] NEEDED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    static final String PERMISSION_DIALOG_TITLE = "This app needs to access the location";
    static final String PERMISSION_DIALOG_MESSAGE = "This is because BLE only works with " +
            "the location permission granted.";

    final Map<String, BluetoothDevice> bluetoothDevices = new HashMap<>();

    BluetoothLeScanner bluetoothLeScanner;
    boolean isScanning;
    ObdApplication application;
    LinearLayout linearLayout;
    BluetoothAdapter bluetoothAdapter;
    Handler handler;

    final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            handleResult(result);
        }

        private void handleResult(ScanResult result) {
            runOnUiThread(() -> createView(result.getDevice()));
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (final ScanResult result : results) {
                handleResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Error during scanning. Error code: " + errorCode);
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "deviceName=" + deviceName.getText() +
                    ", deviceAddress=" + deviceAddress.getText() +
                    '}';
        }
    }

    private void createView(BluetoothDevice device) {
        View view = getLayoutInflater().inflate(R.layout.listitem_device, linearLayout, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.deviceAddress = view.findViewById(R.id.device_address);
        viewHolder.deviceName = view.findViewById(R.id.device_name);

        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(device.getName());
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        String viewHolderString = viewHolder.toString();
        if (bluetoothDevices.containsKey(viewHolderString)) {
            return;
        } else {
            bluetoothDevices.put(viewHolderString, device);
        }

        view.setTag(viewHolderString);
        view.setOnClickListener(this::onListItemClicked);

        linearLayout.addView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        Toolbar toolbar = findViewById(R.id.bluetooth_toolbar);
        setSupportActionBar(toolbar);

        linearLayout = findViewById(R.id.devices_list);
        application = (ObdApplication) (getApplication());
        handler = new Handler();

        checkForBluetooth();

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(BluetoothScanActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(BluetoothScanActivity.this, "Permission already granted",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle(PERMISSION_DIALOG_TITLE)
                    .setMessage(PERMISSION_DIALOG_MESSAGE)
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(BluetoothScanActivity.this, NEEDED_PERMISSIONS, REQUEST_ENABLE_BT))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkForBluetooth() {
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            finish();
            return;
        }

        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Log.d(TAG, "isScanning - " + isScanning);
        if (isScanning) {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setVisible(true)
                    .setActionView(R.layout.actionbar_indeterminate_progress);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setVisible(false).setActionView(null);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_scan) {
            askForPermission();
            startScanning();
        } else if (itemId == R.id.menu_stop) {
            stopScanning();
        }
        return true;
    }

    private void stopScanning() {
        isScanning = false;
        bluetoothLeScanner.stopScan(scanCallback);
        invalidateOptionsMenu();
    }

    private void startScanning() {
        handler.postDelayed(() -> {
            if (isScanning) {
                Log.i(TAG, "stop scanning");
                stopScanning();
            }
        }, SCAN_PERIOD);

        isScanning = true;
        this.linearLayout.removeAllViews();
        bluetoothLeScanner.startScan(scanCallback);
        invalidateOptionsMenu();
    }

    protected void onListItemClicked(View view) {
        String tag = (String) view.getTag();
        Log.d(TAG, tag);
        BluetoothDevice device = bluetoothDevices.get(tag);
        if (device == null) return;

        if (isScanning) {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning = false;
        }

        application.setDeviceAdress(device.getAddress());
        application.setDeviceName(device.getName());

        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScanning();
    }
}