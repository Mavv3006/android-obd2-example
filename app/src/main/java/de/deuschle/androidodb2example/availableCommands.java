import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

public class availableCommands {

    private static int offset;
    private static HashMap<String, Boolean> availabilityMap;


    private static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    private static String hexToBin2(String s) {
        return String.format("%32S", Integer.toBinaryString(Integer.parseInt(s.replaceAll(" ", "").substring(4), 16))).replaceAll(" ", "0");
    }

    private static int hexToDec(String s) {
        return Integer.parseInt(s, 16);
    }

    private static void print(String string) {
        System.out.println(string);
    }

    public static boolean getAvailability(String PID, String availabilityList) throws Exception {
        // Leerzeichen und die ersten zwei Bytes entfernen
        String availableCommandsInput = availabilityList.replaceAll(" ", "").substring(2);

        // Range sind nächsten zwei Bytes
        String range = availableCommandsInput.substring(0,2);
        availableCommandsInput = availableCommandsInput.substring(2);

        if (hexToDec(PID) <= (hexToDec(range)) || hexToDec(PID) > (hexToDec(range)) + 32) {
            throw new Exception("Befehl nicht in der Liste vorhanden");
        }

        // Hexadezimalen String in Binären String übersetzen
        String availableCommandsOutput = hexToBin(availableCommandsInput);

        // Befehl an entsprechender Stelle heraussuchen und ausgeben
        if (hexToDec(PID)%32 != 0) {
            if (availableCommandsOutput.charAt((hexToDec(PID)%32)-1) == '1') {
                return true;
            } else {
                return false;
            }
        } else {
            if (availableCommandsOutput.charAt((hexToDec(PID)-1)%32) == '1') {
                return true;
            } else {
                return false;
            }
        }
    }

    public static HashMap getAvailabilityMap(ArrayList<String> PIDAvailabilityList, int ID) throws Exception {

        // Leerzeichen und die ersten zwei Bytes entfernen
        String availableCommandsInput = PIDAvailabilityList.get(ID).replaceAll(" ", "").substring(2);

        // Range sind nächsten zwei Bytes
        int range = Integer.parseInt(availableCommandsInput.substring(0,2), 16);
        availableCommandsInput = availableCommandsInput.substring(2);

        // Hexadezimaler Input in binären String übersetzen
        String availableCommandsInputCharArray = hexToBin(availableCommandsInput);
        print("availableCommandsInputCharArray: " + availableCommandsInputCharArray);

        for (int i = 0; i < availableCommandsInputCharArray.toString().length() ; i++) {
            if (availableCommandsInputCharArray.charAt(i) == '1') {
                availabilityMap.put(String.format("%02X", i+range+1), true);
            }
            if (availableCommandsInputCharArray.charAt(i) == '0') {
                availabilityMap.put(String.format("%02X", i+range+1), false);
            }
        }
        if (availableCommandsInputCharArray.charAt(availableCommandsInputCharArray.length() - 1) == '1') {
            return getAvailabilityMap(PIDAvailabilityList, ++ID);
        }
        return availabilityMap;
    }
}
