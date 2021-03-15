package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.protocol.ObdResetCommand;
import de.deuschle.obd.commands.protocol.SpacesOffCommand;

public class InitActivity extends CommandActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Toolbar toolbar = findViewById(R.id.init_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.init_toolbar_title);
        }

        this.valueTextView = findViewById(R.id.init_connection_status);

        setup();
    }

    public void onRestartAdapterButtonClick(View view) {
        addCommand(new ObdResetCommand());
    }

    public void onDeactivateSpaceSeparationButtonClick(View view) {
        addCommand(new SpacesOffCommand());
    }
}
