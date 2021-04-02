package de.deuschle.androidodb2example.Util;

import android.util.Log;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.deuschle.androidodb2example.Exception.InfoMessageExcpetion;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ProcessRawDataTest {
    private static final String TAG = "test";

    @Test
    public void testSplittingData() {
        final String rawData = "41 46 2B";
        final String[] expectedResult = new String[]{"41", "46", "2B"};

        final String[] result = ProcessRawData.splitData(rawData);

        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testHexStringList() {
        final List<String> clearedData = Collections.singletonList("2B");
        final List<String> expectedResult = Arrays.asList("2", "B");

        final List<String> result = ProcessRawData.toHexStringList(clearedData);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testPreprocess() throws InfoMessageExcpetion {
        final String rawData = "41 46 2B";
        final List<String> expectedResult = Arrays.asList("4", "1", "4", "6", "2", "B");

        final List<String> result = ProcessRawData.preprocess(rawData);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFillingResultArray() {
        final List<String> hexArray = Arrays.asList("2", "B");
        final byte[] expectedResult = new byte[]{2 + 48, 11 + 55};

        final byte[] result = ProcessRawData.fillRestultArray(hexArray);

        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testConvertRawData() throws InfoMessageExcpetion {
        final String rawData = "41 46 2B";
        final byte[] expectedResult = new byte[]{4 + 48, 1 + 48, 4 + 48, 6 + 48, 2 + 48, 11 + 55};

        final byte[] result = ProcessRawData.convert(rawData);

        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testConvertRawData2() throws InfoMessageExcpetion {
        final String rawData = "41462B";
        final byte[] expectedResult = new byte[]{4 + 48, 1 + 48, 4 + 48, 6 + 48, 2 + 48, 11 + 55};

        final byte[] result = ProcessRawData.convert(rawData);

        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testInputStreamWithEmptyList() {
        final byte[] array = new byte[0];
        Log.i(TAG, Arrays.toString(array));
        InputStream inputStream = new ByteArrayInputStream(array);
        try {
            Log.i(TAG, String.valueOf(inputStream.read()));
        } catch (IOException e) {
            Log.e(TAG, "reading failed because: " + e.getMessage());
        }
    }

    @Test
    public void testProcessingAvailablePids() throws InfoMessageExcpetion {
        final String rawData = "7e8 06 41 00 be 3f a8 13\n7ea 06 41 00 98 3a 80 13\r\n\r\n>";
        final byte[] processedData = ProcessRawData.convert(rawData);
        Log.i(TAG, Arrays.toString(processedData));
    }

    @Test
    public void testHaveColon() throws InfoMessageExcpetion {
        final String rawData = "41 00 be 3f a8 13\n>";
        final byte[] processedData = ProcessRawData.convert(rawData);
        final byte last = processedData[processedData.length - 1];
        assertEquals(62, last);
    }

    @Test
    public void testFillHexArrayWithColon() {
        final List<String> hexArray = Collections.singletonList(">");
        final byte[] result = ProcessRawData.fillRestultArray(hexArray);
        assertEquals(1, result.length);
        assertEquals(62, result[0]);
    }

    @Test
    public void testGetByte() {
        // values in 0, 1, ..., 9
        for (int i = 0; i < 10; i++) {
            final byte result = ProcessRawData.getByte(String.valueOf(i));
            assertEquals(i + 48, result);
        }

        // values in A, B, ..., F
        int value;
        for (int i = 10; i < 16; i++) {
            value = i + 55;
            assertEquals(value, ProcessRawData.getByte(Character.toString((char) value)));
        }

        // stop sign
        assertEquals(62, ProcessRawData.getByte(">"));

        // everything else
        assertEquals(0, ProcessRawData.getByte("z"));
    }
}