package de.deuschle.androidodb2example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;


public class availableCommands {

    private final HashMap<String, Boolean> availabilityMap = new HashMap<>();

    // Umwandlung von Hexadezimal zu Binär: String darf keine Leerzeichen enthalten
    protected static String hexToBin(String s) {
        return String.format("%32S", new BigInteger(s, 16).toString(2)).replaceAll(" ", "0");
    }

    private static int hexToDec(String s) {
        return Integer.parseInt(s, 16);
    }

    public static boolean getAvailability(String PID, String availabilityList) throws Exception {
        // Leerzeichen und die ersten zwei Bytes entfernen
        String availableCommandsInput = availabilityList.replaceAll(" ", "");

        // Range sind nächsten zwei Bytes
        String range = availableCommandsInput.substring(2,4);

        if (hexToDec(PID) <= (hexToDec(range)) || hexToDec(PID) > (hexToDec(range)) + 32) {
            throw new Exception("Befehl nicht in der Liste vorhanden");
        }

        // Hexadezimalen String in Binären String übersetzen
        String availableCommandsOutput = hexToBin(availableCommandsInput.substring(4));

        // Befehl an entsprechender Stelle heraussuchen und ausgeben
        if (hexToDec(PID)%32 != 0) {
            return availableCommandsOutput.charAt((hexToDec(PID) % 32) - 1) == '1';
        }
        return availableCommandsOutput.charAt((hexToDec(PID) - 1) % 32) == '1';

    }

    public static HashMap<String, Boolean> getAvailabilityMap(ArrayList<String> PIDAvailabilityList, int ID)  {
        return new availableCommands().getInternalAvailabilityMap(PIDAvailabilityList, ID);
    }

    private HashMap<String, Boolean> getInternalAvailabilityMap(ArrayList<String> PIDAvailabilityList, int ID) {
        // Leerzeichen und die ersten zwei Bytes entfernen
        String availableCommandsInput = PIDAvailabilityList.get(ID).replaceAll(" ", "");

        // Range sind nächsten zwei Bytes
        int range = Integer.parseInt(availableCommandsInput.substring(2,4), 16);

        // Hexadezimaler Input in binären String übersetzen
        String availableCommandsInputCharArray = hexToBin(availableCommandsInput.substring(4));

        for (int i = 0; i < availableCommandsInputCharArray.length() ; i++) {
            availabilityMap.put(String.format("%02X", i+range+1), availableCommandsInputCharArray.charAt(i) == '1');
        }
        if (availableCommandsInputCharArray.charAt(availableCommandsInputCharArray.length() - 1) == '1') {
            return getInternalAvailabilityMap(PIDAvailabilityList, ++ID);
        }
        return availabilityMap;
    }
}
