package de.deuschle.androidodb2example.Conversion;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessRawDataTest {
    private static final String TAG = "test";

    @Test
    public void testSplittingData() {
        final String rawData = "41 46 2B";
        final String[] expectedResult = new String[]{"41", "46", "2B"};

        final String[] result = ProcessRawData.splitData(rawData);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testHexStringList() {
        final List<String> clearedData = Collections.singletonList("2B");
        final List<String> expectedResult = Arrays.asList("2", "B");

        final List<String> result = ProcessRawData.toHexStringList(clearedData);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testPreprocess() {
        final String rawData = "41 46 2B";
        final List<String> expectedResult = Arrays.asList("2", "B");

        final List<String> result = ProcessRawData.preprocess(rawData);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFillingResultArray() {
        final List<String> hexArray = Arrays.asList("2", "B");
        final byte[] expectedResult = new byte[]{2 + 48, 11 + 55};

        final byte[] result = ProcessRawData.fillRestultArray(hexArray);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testConvertRawData() {
        final String rawData = "41 46 2B";
        final byte[] expectedResult = new byte[]{2 + 48, 11 + 55};

        final byte[] result = ProcessRawData.convert(rawData);

        Assert.assertArrayEquals(expectedResult, result);
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
    public void testProcessingAvailablePids() {
        final String rawData = "7e8 06 41 00 be 3f a8 13\n7ea 06 41 00 98 3a 80 13\r\n\r\n>";
        final byte[] processedData = ProcessRawData.convert(rawData);
        Log.i(TAG, Arrays.toString(processedData));
    }

    @Test
    public void testHaveColon() {
        final String rawData = "41 00 be 3f a8 13\n>";
        final byte[] processedData = ProcessRawData.convert(rawData);
        final byte last = processedData[processedData.length - 1];
        Assert.assertEquals(62, last);
    }

    @Test
    public void testFillHexArrayWithColon() {
        final List<String> hexArray = Collections.singletonList(">");
        final byte[] result = ProcessRawData.fillRestultArray(hexArray);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(62, result[0]);
    }
}