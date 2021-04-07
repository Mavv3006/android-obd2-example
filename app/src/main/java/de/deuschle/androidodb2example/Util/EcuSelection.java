package de.deuschle.androidodb2example.Util;

import android.util.Log;

import java.util.Arrays;

import de.deuschle.androidodb2example.Exception.OnlyOneEcuException;

public class EcuSelection {
    public static final String TAG = EcuSelection.class.getSimpleName();
    private final String[] displayEcuArray;
    private final String[] internalEcuArray;

    private EcuSelection(String[] displayEcuArray, String[] internalEcuArray) {
        this.displayEcuArray = displayEcuArray;
        this.internalEcuArray = internalEcuArray;
    }

    public static EcuSelection process(byte[] processedData) throws OnlyOneEcuException {
        // get ecu byte values
        int ecuCount = processedData.length / 17;
        Log.d(TAG, "ECU count: " + ecuCount);

        if (ecuCount == 1) {
            throw new OnlyOneEcuException();
        }

        byte[][] ecuArray = new byte[ecuCount][3];
        for (int i = 0; i < ecuCount; i++) {
            Log.d(TAG, "i = " + i);
            System.arraycopy(processedData, i * 17, ecuArray[i], 0, 3);
        }

        Log.i(TAG, "ecu Array: " + Arrays.deepToString(ecuArray));

        // get ecu string values
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String[] ecuStringArray = new String[ecuCount];  // [7E8, 7EA]
        String[] ecuStringArray2 = new String[ecuCount]; // [7E0, 7E2]
        for (int i = 0; i < ecuCount; i++) {
            stringBuilder = new StringBuilder();
            stringBuilder2 = new StringBuilder();
            for (int j = 0; j < 3; j++) {
                byte value = ecuArray[i][j];
                stringBuilder.append((char) value);
                if (j == 2) {
                    value -= value >= 58 ? 15 : 8;
                }
                stringBuilder2.append((char) value);

            }
            ecuStringArray[i] = stringBuilder.toString();
            ecuStringArray2[i] = stringBuilder2.toString();
        }
        EcuSelection ecuSelection = new EcuSelection(ecuStringArray, ecuStringArray2);

        Log.d(TAG, Arrays.toString(ecuSelection.getDisplayEcuArray()) + " -> " + Arrays.toString(ecuSelection.getInternalEcuArray()));
        Log.i(TAG, "ecu String Array: " + Arrays.toString(ecuSelection.getDisplayEcuArray()));

        return ecuSelection;
    }

    public String[] getDisplayEcuArray() {
        return displayEcuArray;
    }

    public String[] getInternalEcuArray() {
        return internalEcuArray;
    }

}
