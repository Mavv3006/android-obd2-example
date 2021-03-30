package de.deuschle.androidodb2example.Session;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamingSessionTest {
    static String testCommandPID = "FF";
    StreamingSession session;

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
        session = new StreamingSession();
    }

    @Test
    public void testSettingUsedCommands() {
        final List<ObdCommand> commandList = Collections.singletonList(new TestCommand());

        session.setUsedCommands(commandList);

        assertEquals(commandList, session.getMetadata().getUsedCommands());
    }

    @Test
    public void testStartStop() {
        final int delay = 1000;
        final Duration expectedResult = Duration.of(delay, ChronoUnit.MILLIS);

        session.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                session.stop();
                assertTrue(expectedResult.minus(session.getMetadata().getDuration()).isZero());
            }
        }, delay);
    }

    @Test
    public void testCalcNextValueStartingW0() {
        final SessionData sessionData = new SessionData();
        final double newValue = 10.0;

        SessionData newSession = session.calcNextValue(sessionData, newValue);

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

        SessionData newSessionData = session.calcNextValue(sessionData, nextValue);

        assertEquals(expectedResult, newSessionData.value, 0.0);
    }

    @Test
    public void testAddingValuesWhenStopped() {
        session.start();
        session.stop();
        session.addValue(new TestCommand());

        assertEquals(0, session.getValues().size(), 0);
    }

    @Test
    public void testAddingFirstValue() {
        final ObdCommand command = new TestCommand();

        session.addValue(command);

        Map<String, SessionData> sessionValues = session.getValues();
        assertEquals(1, sessionValues.size());
        assertTrue(sessionValues.containsKey(testCommandPID));
        assertEquals(14, sessionValues.get(testCommandPID).value, 0);
    }

    @Test
    public void testDate() {
        session.start();

        assertEquals(LocalDate.now(), session.getMetadata().getDate());
    }

    @Test
    public void testAddingNextValue() {
        final ObdCommand command = new TestCommand();

        session.addValue(command);
        session.addValue(command);


        Map<String, SessionData> values = session.getValues();
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