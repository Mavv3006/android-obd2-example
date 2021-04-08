package de.deuschle.androidodb2example.ViewModel;

import androidx.appcompat.widget.SwitchCompat;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.SpeedCommand;

import static org.junit.Assert.assertArrayEquals;

public class StreamingActivityViewModelTest {

    StreamingActivityViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new StreamingActivityViewModel();
    }

    @Test
    public void addStreamingCommand() {
        SwitchCompat switchCompat = new SwitchCompat(ApplicationProvider.getApplicationContext());
        switchCompat.setChecked(true);
        String key = "test";
        ObdCommand command = new SpeedCommand();
        viewModel.putCommand(key, command);

        viewModel.addStreamingCommand(switchCompat, key);

        assertArrayEquals(new ObdCommand[]{command}, viewModel.getCommands());
    }
}