package de.deuschle.androidodb2example.Streams;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import de.deuschle.androidodb2example.LogTags.LogTags;

public class BleInputStream extends MyInputStream {
    private static final String TAG = BleInputStream.class.getSimpleName();
    private String inputData;
    private final Deque<Integer> integerList = new LinkedList<>();

    // Arrays that from init commands (ObdResetCommand and SpacesOffCommand)
    private static final String restartHexArray = "[ELM327, v2.1, >]";
    private static final String okHexArray = "[OK, >]";

    /**
     * @return {@code true} if the stream is empty or the "end-of data"-signal (>)
     * is the last element in the stream. {@code false} otherwise
     */
    public boolean isFinished() {
        Log.d(TAG, "size of list: " + integerList.size());
        if (integerList.isEmpty()) return true;
        try {
            int last = integerList.peekLast();
            boolean is62 = last == 62;
            Log.d(TAG, "is finished: " + is62);
            if (is62) {
                Log.d(LogTags.OBD2, "Integer List: " + Arrays.toString(integerList.toArray()));
                return true;
            }
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

    @Override
    public int read() {
        if (integerList.size() > 0) {
            return integerList.removeFirst();
        }
        return -1;
    }

    @Override
    public void setData(String data) {
        this.inputData = data;
        Log.i(TAG, "The data to process: " + data);
        processData();
    }

    private void processData() {
        String[] splittedDataArray = inputData.split("[ \n\r]");

        Log.d(LogTags.INPUT_STREAM_DATA, "Splitted Data: " + Arrays.toString(splittedDataArray));

        String splittedDataArrayString = Arrays.toString(splittedDataArray);

        if (splittedDataArrayString.equals(restartHexArray)) {
            Log.i(TAG, "Restart hex array recognised");
            return;
        }
        if (splittedDataArrayString.equals(okHexArray)) {
            Log.i(TAG, "Ok hex array recognised");
            return;
        }

        List<String> clearedData = new ArrayList<>();

        for (int i = 0; i < splittedDataArray.length; i++) {
            if (i >= 2) {
                clearedData.add(splittedDataArray[i]);
            }
        }

        Log.d(LogTags.INPUT_STREAM_DATA, "Cleared Splitted Data: " + clearedData);
        if (clearedData.isEmpty()) return;

        List<String> hexArray = new ArrayList<>();
        for (String data : clearedData) {
            if (!data.equals("")) {
                for (int i = 0; i < data.length(); i++) {
                    String substring = data.substring(i, i + 1);
                    hexArray.add(substring);
                }
            }
        }

        Log.d(LogTags.INPUT_STREAM_DATA, "Hex Array: " + hexArray);

        for (String s : hexArray) {
            if (s.equals(">")) {
                integerList.add(62);
                break;
            }

            int parsedInteger = Integer.parseInt(s, 16);
            if (parsedInteger >= 0 && parsedInteger <= 9) {
                integerList.add(parsedInteger + 48);
            } else if (parsedInteger >= 10 && parsedInteger <= 15) {
                integerList.add(parsedInteger + 55);
            }
        }

        Log.d(LogTags.INPUT_STREAM_DATA, "Integer Array: " + this.integerList.toString());
    }

}
