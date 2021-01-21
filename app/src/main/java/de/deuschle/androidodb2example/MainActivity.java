package de.deuschle.androidodb2example;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.deuschle.androidodb2example.bluetooth.BluetoothConnection;
import de.deuschle.androidodb2example.bluetooth.BluetoothConnectionFactory;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 12345678;

    BluetoothConnection connection;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fuckMarshMallow();
        setContentView(R.layout.activity_main);
        this.linearLayout = findViewById(R.id.list_layout);
        this.linearLayout.removeAllViewsInLayout();

        this.connection = BluetoothConnectionFactory.getConnection();
        this.connection.scanForDevices();
        List<BluetoothDevice> deviceList = this.connection.getFoundDevices();
        if (deviceList.size() > 0) {
            for (BluetoothDevice device : deviceList) {
                addButton(device);
            }
        } else {
            TextView textView = new TextView(this);
            textView.setText("No devices found");
            linearLayout.addView(textView);
        }
    }

    private void addButton(BluetoothDevice device) {
        Button button = new Button(this);
        button.setText(device.getName());
        button.setOnClickListener(view -> connection.connect(device));
        linearLayout.addView(button);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fuckMarshMallow() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList))
            permissionsNeeded.add("Show Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                StringBuilder message = new StringBuilder("App need access to " + permissionsNeeded.get(0));

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));

                showMessageOKCancel(message.toString(),
                        (dialog, which) -> requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS));
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        Toast.makeText(this, "No new Permission Required- Launching App .You are Awesome!!", Toast.LENGTH_SHORT)
                .show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList) {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            // Check for Rationale Option
            return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        return true;
    }
}