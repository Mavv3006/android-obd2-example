package de.deuschle.androidodb2example.Session;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertTrue;

public class StreamingMetadataTest {
    @Test
    public void testCalcDrivingTime() {
        Metadata metadata = new StreamingMetadata();
        metadata.setStartingTime(LocalTime.now());
        final int delay = 1000;
        final Duration expectedResult = Duration.of(delay, ChronoUnit.MILLIS);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                metadata.calcDrivingTime(LocalTime.now());
                assertTrue(expectedResult.minus(metadata.getDuration()).isZero());
            }
        }, delay);
    }
}