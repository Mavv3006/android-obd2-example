package de.deuschle.androidodb2example.Activities.Commands.Streaming;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.deuschle.androidodb2example.Activities.Commands.CommandActivity;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.SaveSession;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.StreamingDataDatabase;
import de.deuschle.androidodb2example.Database.VinDatabase.SupportedCommandsDao;
import de.deuschle.androidodb2example.Database.VinDatabase.SupportedCommandsEntity;
import de.deuschle.androidodb2example.Database.VinDatabase.VinDatabase;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Session.StreamingSession;
import de.deuschle.androidodb2example.Util.SupportedCommands;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.RPMCommand;
import de.deuschle.obd.commands.engine.SpeedCommand;
import de.deuschle.obd.commands.temperature.AmbientAirTemperatureCommand;

public class StreamingActivity extends CommandActivity {
    private static final String TAG = StreamingActivity.class.getSimpleName();
    private static final String STREAMING = "streaming";
    private static final String NOT_STREAMING = "not streaming";
    private static final String TOOLBAR_TITLE = "Combined Streaming";
    private StreamingSession session;
    private SwitchMaterial vehicleSpeedSwitch;
    private SwitchMaterial engineSpeedSwitch;
    private SwitchMaterial ambientTemperatureSwitch;
    private Button startStreamingButton;
    private Button stopStreamingButton;
    private Toolbar toolbar;
    private boolean isStreaming = false;
    private final Map<String, ObdCommand> supportedCommands = new HashMap<>();

    protected void addStreamingCommand() {
        List<ObdCommand> streamingCommands = new ArrayList<>();
        if (vehicleSpeedSwitch.isChecked()) {
            streamingCommands.add(supportedCommands.get(SupportedCommands.SPEED));
        }
        if (engineSpeedSwitch.isChecked()) {
            streamingCommands.add(supportedCommands.get(SupportedCommands.ENGINE_RPM));
        }
        if (ambientTemperatureSwitch.isChecked()) {
            streamingCommands.add(supportedCommands.get(SupportedCommands.AMBIENT_AIR_TEMP));
        }
        addCommand((ObdCommand[]) streamingCommands.toArray());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        toolbar.setTitle(TOOLBAR_TITLE);
    }

    protected void initLayout() {
        setContentView(R.layout.activity_streaming);
        toolbar = findViewById(R.id.streaming_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        startStreamingButton = findViewById(R.id.streaming_button_start);
        stopStreamingButton = findViewById(R.id.streaming_button_stop);
        vehicleSpeedSwitch = findViewById(R.id.streaming_switch_vehicle_speed);
        engineSpeedSwitch = findViewById(R.id.streaming_switch_engine_speed);
        ambientTemperatureSwitch = findViewById(R.id.streaming_switch_ambient_temperature);
    }

    @Override
    protected void setup() {
        initLayout();
        session = new StreamingSession();
        toolbar.setSubtitle(NOT_STREAMING);
        startStreamingButton.setOnClickListener(this::onStartStreamingButtonClick);
        stopStreamingButton.setOnClickListener(this::onStopStreamingButtonClick);
        super.setup();
        final String vin = application.getVin();
        final VinDatabase db = application.getVinDatabase();
        try {
            List<SupportedCommandsEntity> supportedPids = new SupportedCommandsTask(db, vin)
                    .execute(SupportedCommands.getAllAsArray())
                    .get();
            for (SupportedCommandsEntity pid : supportedPids) {
                handleSupportedCommandEntity(pid);
            }
        } catch (ExecutionException | InterruptedException ignored) {
            engineSpeedSwitch.setEnabled(false);
            vehicleSpeedSwitch.setEnabled(false);
            ambientTemperatureSwitch.setEnabled(false);
        }
    }

    private void handleSupportedCommandEntity(SupportedCommandsEntity pid) {
        if (pid.pid.equals(SupportedCommands.ENGINE_RPM)) {
            if (pid.isSupported == 1) {
                supportedCommands.put(SupportedCommands.ENGINE_RPM, new RPMCommand());
            } else {
                engineSpeedSwitch.setEnabled(false);
            }
        }
        if (pid.pid.equals(SupportedCommands.SPEED)) {
            if (pid.isSupported == 1) {
                supportedCommands.put(SupportedCommands.SPEED, new SpeedCommand());
            } else {
                vehicleSpeedSwitch.setEnabled(false);
            }
        }
        if (pid.pid.equals(SupportedCommands.AMBIENT_AIR_TEMP)) {
            if (pid.isSupported == 1) {
                supportedCommands.put(SupportedCommands.AMBIENT_AIR_TEMP, new AmbientAirTemperatureCommand());
            } else {
                ambientTemperatureSwitch.setEnabled(false);
            }
        }
    }

    public void onStopStreamingButtonClick(View view) {
        stopStreaming();
    }

    public void onStartStreamingButtonClick(View view) {
        session.start();
        toggleStreamingButton();
        addStreamingCommand();
    }

    protected void toggleStreamingButton() {
        startStreamingButton.setEnabled(!startStreamingButton.isEnabled());
        stopStreamingButton.setEnabled(!stopStreamingButton.isEnabled());
        isStreaming = !isStreaming;
        toolbar.setSubtitle(isStreaming ? STREAMING : NOT_STREAMING);
    }

    @Override
    protected void addCommand(ObdCommand command) {
        if (!isStreaming) return;
        super.addCommand(command);
    }

    @Override
    protected void addCommand(ObdCommand[] commands) {
        if (!isStreaming) return;
        super.addCommand(commands);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        // TODO: remove try-catch when possible
        try {
            session.addValue(activeCommand);
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException");
            e.printStackTrace();
        }
        addStreamingCommand(activeCommand);
    }

    protected void stopStreaming() {
        session.stop();
        if (isStreaming) toggleStreamingButton();
        Log.i(LogTags.STREAMING, "Stopped Streaming");
        application.getCommandQueue().clear();
        if (!session.needsToBeSaved()) return;

        StreamingDataDatabase db = Room.databaseBuilder(getApplicationContext(), StreamingDataDatabase.class, getString(R.string.streaming_data_database_name)).build();
        SaveSession saveSession = new SaveSession(session, db, this);
        saveSession.save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopStreaming();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopStreaming();
    }

    protected void addStreamingCommand(ObdCommand obdCommand) {
        addCommand(obdCommand);
    }

    private static class SupportedCommandsTask extends AsyncTask<String, Void, List<SupportedCommandsEntity>> {
        private final VinDatabase db;
        private final String vin;

        private SupportedCommandsTask(VinDatabase db, String vin) {
            this.db = db;
            this.vin = vin;
        }

        @Override
        protected List<SupportedCommandsEntity> doInBackground(String... lists) {
            SupportedCommandsDao dao = db.getSupportedCommandsDao();
            return dao.getSupportedPids(vin, Arrays.asList(lists));
        }
    }
}