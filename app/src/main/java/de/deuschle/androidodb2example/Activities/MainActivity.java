package de.deuschle.androidodb2example.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.R;

public class MainActivity extends AppCompatActivity {
    Button disconnectButton;
    Button connectButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_disconnected));
        setSupportActionBar(toolbar);

        connectButton = findViewById(R.id.main_button_connect);
        disconnectButton = findViewById(R.id.main_button_disconnect);

        connectButton.setOnClickListener(view -> {
            // TODO: connect to BLE device
            toggleButtons();
            toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_connected));
        });

        disconnectButton.setOnClickListener(view -> {
            // TODO: disconnect with BLE device
            toggleButtons();
            toolbar.setSubtitle(getString(R.string.bluetooth_connection_status) + " " + getString(R.string.bluetooth_connection_disconnected));
        });
    }

    private void toggleButtons() {
        disconnectButton.setEnabled(!disconnectButton.isEnabled());
        connectButton.setEnabled(!connectButton.isEnabled());
    }

    public void goToEngineRPM(View view) {
        Intent intent = new Intent(this, EngineRPM.class);
        startActivity(intent);
    }
}