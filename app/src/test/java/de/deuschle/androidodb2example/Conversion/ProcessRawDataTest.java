package de.deuschle.androidodb2example.Conversion;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessRawDataTest {
    @Test
    public void testSplittingData() {
        final String rawData = "41 46 2B";
        final String[] expectedResult = new String[]{"41", "46", "2B"};

        final String[] result = ProcessRawData.splitData(rawData);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testInfoMessage() {
        Assert.assertTrue(ProcessRawData.isInfoMessage(ProcessRawData.restartHexArray));
        Assert.assertTrue(ProcessRawData.isInfoMessage(ProcessRawData.okHexArray));
    }

    @Test
    public void testClearingData() {
        final String[] splitData = new String[]{"41", "46", "2B"};
        final List<String> expectedResult = Collections.singletonList("2B");

        final List<String> result = ProcessRawData.clearData(splitData);

        Assert.assertEquals(expectedResult, result);
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
}