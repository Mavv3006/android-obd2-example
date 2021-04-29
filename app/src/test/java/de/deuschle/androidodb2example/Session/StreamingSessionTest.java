package de.deuschle.androidodb2example.Session;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamingSessionTest {
    static String testCommandPID = "FF";
    StreamingSession streamingSession;

    static class TestCommand extends ObdCommand {
        int[] values = {14, 16};
        int index = 0;

        public TestCommand() {
            super(AvailableCommand.CustomCommand.rawCommand("01 " + testCommandPID));
        }

        @Override
        protected void performCalculations() {
        }

        @Override
        public String getFormattedResult() {
            return getCalculatedResult();
        }

        @Override
        public String getCalculatedResult() {
            return values[index++] + getResultUnit();
        }

        @Override
        public String getResultUnit() {
            return "C";
        }
    }

    @Before
    public void setUp() {
        streamingSession = new StreamingSession();
    }


    @Test
    public void testCalcNextValueStartingW0() {
        final SessionData sessionData = new SessionData();
        final double newValue = 10.0;

        SessionData newSession = streamingSession.calcNextValue(sessionData, newValue);

        assertEquals(newValue, newSession.value, 0.0);
    }

    @Test
    public void testCalcNextValueOther() {
        double nextValue = 14;
        double startingValue = 10;
        int n = 4;
        double expectedResult = startingValue + (nextValue - startingValue) / (n + 1);
        SessionData sessionData = new SessionData();
        sessionData.value = startingValue;
        sessionData.n = n;

        SessionData newSessionData = streamingSession.calcNextValue(sessionData, nextValue);

        assertEquals(expectedResult, newSessionData.value, 0.0);
    }

    @Test
    public void testAddingValuesWhenStopped() {
        streamingSession.start();
        streamingSession.stop();
        streamingSession.addValue(new TestCommand());

        assertEquals(0, streamingSession.getValues().size(), 0);
    }

    @Test
    public void testAddingFirstValue() {
        final ObdCommand command = new TestCommand();

        streamingSession.addValue(command);

        Map<String, SessionData> sessionValues = streamingSession.getValues();
        assertEquals(1, sessionValues.size());
        assertTrue(sessionValues.containsKey(testCommandPID));
        assertEquals(14, sessionValues.get(testCommandPID).value, 0);
    }

    @Test
    public void testAddingNextValue() {
        final ObdCommand command = new TestCommand();

        streamingSession.addValue(command);
        streamingSession.addValue(command);

        Map<String, SessionData> values = streamingSession.getValues();
        assertEquals(1, values.size());
        assertTrue(values.containsKey(testCommandPID));
        assertEquals(15, values.get(testCommandPID).value, 0);
    }

    @Test
    public void testCommandSecoundValue() {
        final ObdCommand command = new TestCommand();

        assertEquals("14C", command.getCalculatedResult());
        assertEquals("16C", command.getCalculatedResult());
    }

    @Test
    public void testTestCommand() {
        final ObdCommand command = new TestCommand();
        assertEquals(testCommandPID, command.getCommandPID());
    }
}