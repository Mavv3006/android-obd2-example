package de.deuschle.androidodb2example.Streams;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BleInputStreamTest {
    MyInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        this.inputStream = new BleInputStream();
    }

    @Test
    public void testIsFinishedWithoutData() {
        Assert.assertTrue(inputStream.isFinished());
    }

    @Test
    public void testIsFinishedWithEndOfDataSignal() {
        inputStream.setData(">");
        Assert.assertTrue(inputStream.isFinished());
    }

    @Test
    public void test_is_finished_when_ok_is_set() {
        final String data = "OK >";
        inputStream.setData(data);
        Assert.assertTrue(inputStream.isFinished());
    }

    @Test
    public void test_is_finished_when_restart_is_set() {
        final String data = "ELM327 v2.1 >";
        inputStream.setData(data);
        Assert.assertTrue(inputStream.isFinished());
    }
}