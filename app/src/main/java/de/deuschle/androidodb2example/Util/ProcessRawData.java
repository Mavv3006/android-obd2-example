package de.deuschle.androidodb2example.Util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.deuschle.androidodb2example.Exception.InfoMessageExcpetion;

public class ProcessRawData {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    protected static final List<String> greaterSign = Collections.singletonList(">");
    private static final String TAG = ProcessRawData.class.getSimpleName();

    public static Data convert(String inputData) throws InfoMessageExcpetion {
        List<String> hexArray = preprocess(inputData);

        byte[] resultArray = fillRestultArray(hexArray);

        Log.d(TAG, "Integer Array: " + Arrays.toString(resultArray));
        return new Data(resultArray);
    }

    protected static List<String> preprocess(String inputData) throws InfoMessageExcpetion {
        String[] splitted = splitData(inputData);
        Log.d(TAG, "splitted: " + Arrays.toString(splitted));

        boolean infoMessage = isInfoMessage(splitted);
        Log.d(TAG, "isInfoMessage: " + infoMessage);

        if (infoMessage) {
            throw new InfoMessageExcpetion();
        }

        if (splitted[0].equals("01")) {
            List<String> list = new LinkedList<>(Arrays.asList(splitted));
            list.remove(0);
            list.remove(0);
            splitted = list.toArray(EMPTY_STRING_ARRAY);
            Log.d(TAG, "splitted2: " + Arrays.toString(splitted));
        }

        List<String> hexArray = toHexStringList(Arrays.asList(splitted));

        if (hexArray.equals(greaterSign)) return null;

        Log.d(TAG, "Hex Array: " + hexArray);
        return hexArray;
    }

    protected static String[] splitData(String data) {
        return data.split("[ \n\r]");
    }

    protected static boolean isInfoMessage(String[] splittedData) {
        Log.d(TAG, "checking for info message: '" + Arrays.toString(splittedData) + "'");
        if (splittedData[0].equals("AT")) return true;

        for (String data : splittedData) {
            if (data.equals("NO") || data.equals("DATA")) return true;
        }

        return false;
    }

    protected static List<String> toHexStringList(List<String> clearedData) {
        List<String> hexArray = new ArrayList<>();
        for (String data : clearedData) {
            if (!data.equals("")) {
                for (int i = 0; i < data.length(); i++) {
                    String substring = data.substring(i, i + 1);
                    hexArray.add(substring);
                }
            }
        }
        return hexArray;
    }

    protected static byte[] fillRestultArray(List<String> hexArray) {
        byte[] resultArray = new byte[hexArray.size()];
        for (int i = 0; i < hexArray.size(); i++) {
            resultArray[i] = getByte(hexArray.get(i));
        }
        return resultArray;
    }

    protected static byte getByte(String currentValue) {
        if (currentValue.equals(">")) {
            return 62;
        } else if (currentValue.equals(":")) {
            return 58;
        }

        try {
            byte parsedByte = Byte.parseByte(currentValue, 16);
            if (parsedByte < 10) {
                return (byte) (parsedByte + 48);
            }
            return (byte) (parsedByte + 55);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static class Data {
        private byte[] withHeader;
        private byte[] withoutHeader;

        protected Data(byte[] data) {
            process(data);
        }

        @Override
        public String toString() {
            return "Data{" +
                    "withHeader=" + Arrays.toString(withHeader) +
                    ", withoutHeader=" + Arrays.toString(withoutHeader) +
                    '}';
        }

        public Data() {
            withoutHeader = new byte[]{};
            withHeader = new byte[]{};
        }

        public byte[] getWithHeader() {
            return withHeader;
        }

        public byte[] getWithoutHeader() {
            return withoutHeader;
        }

        protected void process(byte[] data) {
            int ecuCount = data.length / 17;

            withHeader = data;

            if (data.length < 17) {
                withoutHeader = data;
                return;
            }

            withoutHeader = new byte[12 * ecuCount];
            for (int i = 0; i < ecuCount; i++) {
                System.arraycopy(data, 5 + 17 * i, withoutHeader, 12 * i, 12);
            }
        }
    }
}
