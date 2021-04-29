package de.deuschle.androidodb2example.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.Activities.Commands.AmbientTemperatureActivity;
import de.deuschle.androidodb2example.Activities.Commands.InitAdapterActivity;
import de.deuschle.androidodb2example.Activities.Commands.InitTestActivity;
import de.deuschle.androidodb2example.Activities.Commands.RPMActivity;
import de.deuschle.androidodb2example.Activities.Commands.Streaming.StreamingActivity;
import de.deuschle.androidodb2example.Activities.Commands.VehicleSpeedActivity;
import de.deuschle.androidodb2example.Activities.Session.SessionListActivity;
import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObdApplication application = (ObdApplication) (getApplication());
        String deviceAddress = application.getDeviceAdress();
        String deviceName = application.getDeviceName();

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(deviceName);
        toolbar.setSubtitle(deviceAddress);
        setSupportActionBar(toolbar);
    }

    public void goToEngineRPM(View view) {
        startActivity(new Intent(this, RPMActivity.class));
    }

    public void readVehicleSpeed(View view) {
        startActivity(new Intent(this, VehicleSpeedActivity.class));
    }

    public void readAmbientTemperature(View view) {
        startActivity(new Intent(this, AmbientTemperatureActivity.class));
    }

    public void goToInit(View view) {
        startActivity(new Intent(this, InitTestActivity.class));
    }

    public void goToCombinedStreaming(View view) {
        startActivity(new Intent(this, StreamingActivity.class));
    }

    public void goToPreviousSessions(View view) {
        startActivity(new Intent(this, SessionListActivity.class));
    }

    public void goToInitAdapter(View view) {
        startActivity(new Intent(this, InitAdapterActivity.class));
    }
}