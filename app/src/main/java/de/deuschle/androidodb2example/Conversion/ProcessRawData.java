package de.deuschle.androidodb2example.Conversion;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessRawData {
    private static final String TAG = ProcessRawData.class.getSimpleName();
    protected static final String restartHexArray = "[ELM327, v2.1, >]";
    protected static final String okHexArray = "[OK, >]";

    public static byte[] convert(String inputData) {
        List<String> hexArray = preprocess(inputData);

        if (hexArray == null) return null;

        byte[] resultArray;

        if (hexArray.get(0).equals(">")) {
            resultArray = new byte[]{62};
        } else {
            resultArray = fillRestultArray(hexArray);
        }

        Log.d(TAG, "Integer Array: " + Arrays.toString(resultArray));
        return resultArray;
    }

    protected static List<String> preprocess(String inputData) {
        String[] splitted = splitData(inputData);

        if (isInfoMessage(Arrays.toString(splitted))) {
            return null;
        }

        List<String> clearedData = clearData(splitted);

        Log.d(TAG, "Cleared Splitted Data: " + clearedData);

        if (clearedData.isEmpty()) {
            return null;
        }

        List<String> hexArray = toHexStringList(clearedData);
        Log.d(TAG, "Hex Array: " + hexArray);
        return hexArray;
    }

    protected static String[] splitData(String data) {
        return data.split("[ \n\r]");
    }

    protected static boolean isInfoMessage(String splittedData) {
        return splittedData.equals(restartHexArray) | splittedData.equals(okHexArray);
    }

    protected static List<String> clearData(String[] splitted) {
        List<String> clearedData = new ArrayList<>();
        for (int i = 0; i < splitted.length; i++) {
            if (i >= 2) {
                clearedData.add(splitted[i]);
            }
        }
        return clearedData;
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
            byte parsedByte = Byte.parseByte(hexArray.get(i), 16);
            if (parsedByte >= 0 && parsedByte <= 9) {
                resultArray[i] = (byte) (parsedByte + 48);
            } else if (parsedByte >= 10 && parsedByte <= 15) {
                resultArray[i] = (byte) (parsedByte + 55);
            }
        }
        return resultArray;
    }
}
