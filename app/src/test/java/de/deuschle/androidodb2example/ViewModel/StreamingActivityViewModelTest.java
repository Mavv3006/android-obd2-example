package de.deuschle.androidodb2example.ViewModel;

import org.junit.Before;
import org.junit.Test;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.SpeedCommand;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StreamingActivityViewModelTest {
    StreamingActivityViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new StreamingActivityViewModel();
    }

    @Test
    public void putCommand() {
        String key = "test";
        ObdCommand command = new SpeedCommand();

        viewModel.putCommand(key, command);

        assertTrue(viewModel.supportedCommands.containsKey(key));
        assertNotNull(viewModel.supportedCommands.get(key));
        assertEquals(viewModel.supportedCommands.get(key).getName(), command.getName());
    }

    @Test
    public void getCommands() {
        ObdCommand[] expectedArray = {new SpeedCommand()};
        viewModel.commandList.add(expectedArray[0]);

        ObdCommand[] resultArray = viewModel.getCommands();

        assertArrayEquals(expectedArray, resultArray);
    }
}