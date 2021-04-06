package de.deuschle.androidodb2example.Activities.Commands;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.engine.SpeedCommand;

public class VehicleSpeedActivity extends CommandActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_speed);
        Toolbar toolbar = findViewById(R.id.vehicle_speed_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.vehicle_speed_toolbar_title));
        }

        valueTextView = findViewById(R.id.vehicle_speed_text_view_value);
        setup();
    }

    public void readData(View view) {
        addCommand(new SpeedCommand());
    }
}