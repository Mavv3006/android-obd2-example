package de.deuschle.androidodb2example.Conversion;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProcessRawData {
    private static final String TAG = ProcessRawData.class.getSimpleName();
    protected static final List<String> greaterSign = Collections.singletonList(">");
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static byte[] convert(String inputData) {
        List<String> hexArray = preprocess(inputData);

        if (hexArray == null) return new byte[0];

        byte[] resultArray = fillRestultArray(hexArray);

        Log.d(TAG, "Integer Array: " + Arrays.toString(resultArray));
        return resultArray;
    }

    protected static List<String> preprocess(String inputData) {
        String[] splitted = splitData(inputData);
        Log.d(TAG, "splitted: " + Arrays.toString(splitted));

        boolean infoMessage = isInfoMessage(splitted);
        Log.d(TAG, "isInfoMessage: " + infoMessage);

        if (infoMessage) {
            return null;
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
}
