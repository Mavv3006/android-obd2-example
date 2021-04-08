package de.deuschle.androidodb2example.Activities.Commands;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.deuschle.androidodb2example.Commands.HeadersOnCommand;
import de.deuschle.androidodb2example.Commands.ProtocolAutoCommand;
import de.deuschle.androidodb2example.Commands.SetEcuCommand;
import de.deuschle.androidodb2example.Database.VinDatabase.SupportedCommandsEntity;
import de.deuschle.androidodb2example.Database.VinDatabase.VinDao;
import de.deuschle.androidodb2example.Database.VinDatabase.VinDatabase;
import de.deuschle.androidodb2example.Database.VinDatabase.VinEntity;
import de.deuschle.androidodb2example.Exception.OnlyOneEcuException;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Util.CheckAvailableCommands;
import de.deuschle.androidodb2example.Util.EcuSelection;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.control.VinCommand;
import de.deuschle.obd.commands.protocol.AvailablePidsCommand01to20;
import de.deuschle.obd.commands.protocol.AvailablePidsCommand21to40;
import de.deuschle.obd.commands.protocol.AvailablePidsCommand41to60;
import de.deuschle.obd.commands.protocol.HeadersOffCommand;
import de.deuschle.obd.commands.protocol.LineFeedOffCommand;
import de.deuschle.obd.commands.protocol.ObdWarmStartCommand;
import de.deuschle.obd.commands.protocol.SpacesOffCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class InitAdapterActivity extends CommandActivity {
    public static final String TAG = InitAdapterActivity.class.getSimpleName();
    private final Map<String, Boolean> availabilityMap = new HashMap<>();
    private String vin;
    private TextView statusTextView;
    private boolean isFinished = false;
    private boolean needToSetECU = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + " started");
        setContentView(R.layout.activity_init_adapter);
        setup();
        statusTextView = findViewById(R.id.init_adapter_status);
        Toolbar toolbar = findViewById(R.id.init_adapter_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onStartButtonClicked(View view) {
        statusTextView.setText(R.string.init_adapter_status_running);
        ObdCommand[] commandList = {
                new ObdWarmStartCommand(),
                new ProtocolAutoCommand(),
                new HeadersOnCommand(),
                new AvailablePidsCommand01to20(),
        };
        addCommand(commandList);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        String commandName = activeCommand.getName();
        if (commandName.equals(AvailableCommand.VIN.getValue())) {
            handleVinCommand(activeCommand);
        } else if (commandName.equals(AvailableCommand.PIDS_01_20.getValue())) {
            if (needToSetECU) {
                selectECU(processedData);
                needToSetECU = false;
            } else {
                handlePids01_20(activeCommand);
            }
        } else if (commandName.equals(AvailableCommand.PIDS_21_40.getValue())) {
            handlePids21_40(activeCommand);
        } else if (commandName.equals(AvailableCommand.PIDS_41_60.getValue())) {
            updateAvailabilityMap(activeCommand);
            isFinished = true;
        }

        if (isFinished) {
            statusTextView.setText(R.string.init_adapter_status_finished);
        }
    }

    private void selectECU(byte[] processedData) {
        try {
            EcuSelection selection = EcuSelection.process(processedData);
            showEcuSelection(selection);
        } catch (OnlyOneEcuException ignored) {
            Log.i(TAG, "OnlyOneEcuException fired");
            ObdCommand[] commands = {
                    new HeadersOffCommand(),
                    new LineFeedOffCommand(),
                    new SpacesOffCommand(),
                    new VinCommand()
            };
            addCommand(commands);
        }
    }

    private void showEcuSelection(EcuSelection ecuSelection) {
        new AlertDialog.Builder(this)
                .setTitle("Select an ECU")
                .setItems(ecuSelection.getDisplayEcuArray(), (dialog, which) -> {
                    Log.i(TAG, "ECU set to " + ecuSelection.getInternalEcuArray()[which]);
                    ObdCommand[] commands = {
                            new SetEcuCommand(ecuSelection.getInternalEcuArray()[which]),
                            new HeadersOffCommand(),
                            new LineFeedOffCommand(),
                            new SpacesOffCommand(),
                            new VinCommand()
                    };
                    addCommand(commands);
                })
                .show();
    }

    private void handlePids21_40(ObdCommand activeCommand) {
        updateAvailabilityMap(activeCommand);
        if (availabilityMap.get("40")) {
            addCommand(new AvailablePidsCommand41to60());
        } else {
            isFinished = true;
        }
    }

    private void handlePids01_20(ObdCommand activeCommand) {
        updateAvailabilityMap(activeCommand);
        if (availabilityMap.get("20")) {
            addCommand(new AvailablePidsCommand21to40());
        } else {
            isFinished = true;
        }
    }

    private void updateAvailabilityMap(ObdCommand activeCommand) {
        List<String> pidAvailabilityList = Collections.singletonList(activeCommand.getResult());
        HashMap<String, Boolean> availabilityMap = CheckAvailableCommands.getAvailabilityMap(pidAvailabilityList);
        this.availabilityMap.putAll(availabilityMap);
        SupportedCommandsEntity[] entities = convertMapToArray(availabilityMap);
        Log.d(TAG, "entities to save (" + entities.length + "): " + Arrays.toString(entities));
        new SaveAvailableCommands(application.getVinDatabase()).execute(entities);
    }

    private SupportedCommandsEntity[] convertMapToArray(HashMap<String, Boolean> availabilityMap) {
        SupportedCommandsEntity[] entities = new SupportedCommandsEntity[availabilityMap.size()];
        int i = 0;
        for (Map.Entry<String, Boolean> entry : availabilityMap.entrySet()) {
            entities[i++] = new SupportedCommandsEntity(vin, entry.getKey(), entry.getValue());
        }
        return entities;
    }

    private void handleVinCommand(ObdCommand activeCommand) {
        vin = activeCommand.getResult();
        application.setVin(vin);
        if (checkIfVinIsInDb(activeCommand)) {
            finish();
        }
        new VinSavingTask(application.getVinDatabase()).execute(vin);
        addCommand(new AvailablePidsCommand01to20());
    }

    private boolean checkIfVinIsInDb(ObdCommand activeCommand) {
        statusTextView.setText(R.string.init_adapter_status_finished);
        String vin = activeCommand.getCalculatedResult();

        VinDatabase db = application.getVinDatabase();
        try {
            return new VinCheckingTask(db).execute(vin).get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    static final class VinCheckingTask extends AsyncTask<String, Void, Boolean> {
        private final VinDatabase db;

        VinCheckingTask(VinDatabase db) {
            this.db = db;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            if (strings.length > 1) return null;
            Log.d(TAG, "retrieving vin");
            VinDao dao = db.getVinDao();

            return dao.isVinInDb(strings[0]) == 1;
        }
    }

    static final class VinSavingTask extends AsyncTask<String, Void, Void> {
        private final VinDatabase db;

        VinSavingTask(VinDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (strings.length > 1) return null;
            Log.d(TAG, "saving vin");
            VinDao dao = db.getVinDao();
            dao.insert(new VinEntity(strings[0]));
            return null;
        }
    }

    static final class SaveAvailableCommands extends AsyncTask<SupportedCommandsEntity, Void, Void> {
        private final VinDatabase db;

        SaveAvailableCommands(VinDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(SupportedCommandsEntity... supportedCommandsEntities) {
            Log.d(TAG, "start saving commands");
            int count = 0;
            for (SupportedCommandsEntity entity : supportedCommandsEntities) {
                try {
                    db.getSupportedCommandsDao().insert(entity);
                    count++;
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "saved " + count + " commands");
            return null;
        }
    }
}