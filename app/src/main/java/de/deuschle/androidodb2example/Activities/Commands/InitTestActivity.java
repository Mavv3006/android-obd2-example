package de.deuschle.androidodb2example.Activities.Commands;

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

public class InitTestActivity extends CommandActivity {

    private static final String TAG = InitTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // get ecu byte values
        int ecuCount = processedData.length / 17;
        Log.d(TAG, "ECU count: " + ecuCount);

        if (ecuCount == 1) {
            return;
        }

        byte[][] ecuArray = new byte[ecuCount][3];
        for (int i = 0; i < ecuCount; i++) {
            Log.d(TAG, "i = " + i);
            System.arraycopy(processedData, i * 17, ecuArray[i], 0, 3);
        }

        Log.i(TAG, "ecu Array: " + Arrays.deepToString(ecuArray));

        // get ecu string values
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String[] ecuStringArray = new String[ecuCount];  // [7E8, 7EA]
        String[] ecuStringArray2 = new String[ecuCount]; // [7E0, 7E2]
        for (int i = 0; i < ecuCount; i++) {
            stringBuilder = new StringBuilder();
            stringBuilder2 = new StringBuilder();
            for (int j = 0; j < 3; j++) {
                byte value = ecuArray[i][j];
                stringBuilder.append((char) value);
                if (j == 2) {
                    value -= value >= 58 ? 15 : 8;
                }
                stringBuilder2.append((char) value);

            }
            ecuStringArray[i] = stringBuilder.toString();
            ecuStringArray2[i] = stringBuilder2.toString();
        }

        Log.d(TAG, Arrays.toString(ecuStringArray) + " -> " + Arrays.toString(ecuStringArray2));
        Log.i(TAG, "ecu String Array: " + Arrays.toString(ecuStringArray));

        new AlertDialog.Builder(this)
                .setTitle("Select an ECU")
                .setItems(ecuStringArray, (dialog, which) -> {
                    Log.i(TAG, "ECU set to " + ecuStringArray2[which]);
                    addCommand(new SetEcuCommand(ecuStringArray2[which]));
                })
                .show();
    }
}
