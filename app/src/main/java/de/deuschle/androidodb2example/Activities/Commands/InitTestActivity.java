package de.deuschle.androidodb2example.Activities.Commands;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.Commands.HeadersOnCommand;
import de.deuschle.androidodb2example.Commands.ProtocolAutoCommand;
import de.deuschle.androidodb2example.Commands.SetEcuCommand;
import de.deuschle.androidodb2example.Exception.OnlyOneEcuException;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Util.EcuSelection;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.protocol.AvailablePidsCommand01to20;
import de.deuschle.obd.commands.protocol.HeadersOffCommand;
import de.deuschle.obd.commands.protocol.LineFeedOffCommand;
import de.deuschle.obd.commands.protocol.ObdResetCommand;
import de.deuschle.obd.commands.protocol.SpacesOffCommand;

public class InitTestActivity extends CommandActivity {

    private static final String TAG = InitTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + " started");
        setContentView(R.layout.activity_init_test);

        Toolbar toolbar = findViewById(R.id.init_test_toolbar);
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
        ObdCommand[] commandList = {
                new ObdResetCommand(),
                new ProtocolAutoCommand()
        };
        addCommand(commandList);
    }

    public void onDeactivateSpaceSeparationButtonClick(View view) {
        addCommand(new SpacesOffCommand());
    }

    public void onLineFeedOffButtonClick(View view) {
        addCommand(new LineFeedOffCommand());
    }

    public void onSelectEcuButtonClick(View view) {
        ObdCommand[] commandList = {
                new HeadersOnCommand(),
                new AvailablePidsCommand01to20(),
                new HeadersOffCommand()
        };
        addCommand(commandList);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        if (!activeCommand.getCommandPID().equals("00")) {
            return;
        }

        try {
            EcuSelection ecuSelection = EcuSelection.process(processedData);
            showEcuSelection(ecuSelection);
        } catch (OnlyOneEcuException ignored) {
        }
    }

    private void showEcuSelection(EcuSelection ecuSelection) {
        new AlertDialog.Builder(this)
                .setTitle("Select an ECU")
                .setItems(ecuSelection.getDisplayEcuArray(), (dialog, which) -> {
                    Log.i(TAG, "ECU set to " + ecuSelection.getInternalEcuArray()[which]);
                    addCommand(new SetEcuCommand(ecuSelection.getInternalEcuArray()[which]));
                })
                .show();
    }
}
