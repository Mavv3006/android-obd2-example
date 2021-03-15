package de.deuschle.androidodb2example.Conversion;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessRawData {
    private static final String TAG = ProcessRawData.class.getSimpleName();
    protected static final List<String> infoStringList = Arrays.asList(
            "[AT, Z, , ELM327, v2.1]",
            "[AT, S0, , OK, , , , >]",
            "[, >]"
    );
    protected static final List<String> greaterSign = Collections.singletonList(">");

    public static byte[] convert(String inputData) {
        List<String> hexArray = preprocess(inputData);

        if (hexArray == null) return new byte[0];

        byte[] resultArray = fillRestultArray(hexArray);

        Log.d(TAG, "Integer Array: " + Arrays.toString(resultArray));
        return resultArray;
    }

    protected static List<String> preprocess(String inputData) {
        String[] splitted = splitData(inputData);

        if (isInfoMessage(Arrays.toString(splitted))) {
            return null;
        }

        List<String> hexArray = toHexStringList(Arrays.asList(splitted));

        if (hexArray.equals(greaterSign)) return null;

        Log.d(TAG, "Hex Array: " + hexArray);
        return hexArray;
    }

    protected static String[] splitData(String data) {
        return data.split("[ \n\r]");
    }

    protected static boolean isInfoMessage(String splittedData) {
        Log.d(TAG, "checking for info message: '" + splittedData + "'");
        return infoStringList.contains(splittedData);
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

    private static byte getByte(String currentValue) {
        if (currentValue.equals(">")) {
            return 62;
        }

        byte parsedByte = Byte.parseByte(currentValue, 16);
        if (parsedByte >= 0 && parsedByte <= 9) {
            return (byte) (parsedByte + 48);
        } else if (parsedByte >= 10 && parsedByte <= 15) {
            return (byte) (parsedByte + 55);
        }

        return 0;
    }
}
