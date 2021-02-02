package de.deuschle.androidodb2example.Streams;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import de.deuschle.androidodb2example.LogTags.LogTags;

public class BleInputStream extends InputStream {
    private String data;
    private Queue<Integer> integerList = new LinkedList<>();

    @Override
    public int read() {
        return integerList.remove();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(String data) {
        this.data = data;
        processData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processData() {
        String[] dataArray = data.split("[ \n\r]");
        List<String> stringList = Collections.singletonList(Arrays.toString(dataArray));
        stringList.stream()
                .filter(s -> s.equals("\n") || s.equals("\r") || s.equals(" ")).collect(Collectors.joining());
        Log.d(LogTags.INPUT_STREAM_DATA, "Splitted Data: " + Arrays.toString(dataArray));

        Queue<Integer> intArray = new LinkedList<>();
        for (String stringData : dataArray) {
            try {
                intArray.add(Integer.parseInt(stringData, 16));
            } catch (NumberFormatException e) {
                Log.e("Error", e.getMessage());
            }
        }

        this.integerList.addAll(intArray);
        Log.d(LogTags.INPUT_STREAM_DATA, "Integer Array: " + this.integerList.toString());
    }

}
