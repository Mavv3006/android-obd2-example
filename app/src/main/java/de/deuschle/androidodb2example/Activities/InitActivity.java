package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;

import de.deuschle.androidodb2example.Commands.HeadersOnCommand;
import de.deuschle.androidodb2example.Commands.ProtocolAutoCommand;
import de.deuschle.androidodb2example.Commands.SetEcuCommand;
import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.protocol.AvailablePidsCommand01to20;
import de.deuschle.obd.commands.protocol.HeadersOffCommand;
import de.deuschle.obd.commands.protocol.LineFeedOffCommand;
import de.deuschle.obd.commands.protocol.ObdResetCommand;
import de.deuschle.obd.commands.protocol.SpacesOffCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class InitActivity extends CommandActivity {

    private static final String TAG = InitActivity.class.getSimpleName();

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

    public void onReadControlUnitsButtonClick(View view) {
        ObdCommand[] commandList = {
                new HeadersOnCommand(),
                new AvailablePidsCommand01to20(),
                new HeadersOffCommand()
        };
        addCommand(commandList);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        if (!activeCommand.getCommandPID().equals(AvailableCommand.PIDS_01_20.getValue().substring(3))) {
            return;
        }

        // get ecu byte values
        int ecuCount = (processedData.length - 1) % 16;
        byte[][] ecuArray = new byte[ecuCount][3];
        for (int i = 0; i < ecuCount; i++) {
            System.arraycopy(processedData, i, ecuArray[i], 0, 3);
        }

        Log.i(TAG, "ecu Array: " + Arrays.deepToString(ecuArray));

        // get ecu string values
        StringBuilder stringBuilder;
        String[] ecuStringArray = new String[ecuCount];
        for (int i = 0; i < ecuCount; i++) {
            stringBuilder = new StringBuilder();
            for (int j = 0; j < 3; j++) {
                stringBuilder.append((char) ecuArray[i][j]);
            }
            ecuStringArray[i] = stringBuilder.toString();
        }

        Log.i(TAG, "ecu String Array: " + Arrays.toString(ecuStringArray));
        final String[] selectedEcu = new String[1];

        new AlertDialog.Builder(this)
                .setTitle("Select an ECU")
                .setItems(ecuStringArray, (dialog, which) -> selectedEcu[0] = ecuStringArray[which])
                .show();

        if (selectedEcu[0] != null && selectedEcu[0].length() > 0) {
            addCommand(new SetEcuCommand(selectedEcu[0]));
        } else {
            Log.e(TAG, "No ECU has been selected");
        }
    }
}
