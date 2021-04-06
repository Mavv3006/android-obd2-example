package de.deuschle.androidodb2example.Util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class checkAvailableCommandsTest {
    @Test
    public void testSingleValue() {
        String audi_20 = "4120A005B011";

        try {
            assertFalse(CheckAvailableCommands.getAvailability("22", audi_20));
        } catch (Exception e) {
            fail();
        }

    }

    @Test(expected = Exception.class)
    public void testSingleValueException() throws Exception {
        String audi_00 = "41 00 BE 1F A8 13";
        CheckAvailableCommands.getAvailability("22", audi_00);
    }

    @Test
    public void testAvailabilityMap() {
        String audi_00 = "41 00 BE 1F A8 13";
        String audi_20 = "4120A005B011";
        String audi_40 = "41 40 FE D0 04 00";
        ArrayList<String> audi = new ArrayList<>();

        audi.add(audi_00);
        audi.add(audi_20);
        audi.add(audi_40);

        Map<String, Boolean> result = new HashMap<String, Boolean>() {
            {
                put("01", true);
                put("02", false);
                put("03", true);
                put("04", true);
                put("05", true);
                put("06", true);
                put("07", true);
                put("08", false);
                put("09", false);
                put("0A", false);
                put("0B", false);
                put("0C", true);
                put("0D", true);
                put("0E", true);
                put("0F", true);
                put("10", true);
                put("11", true);
                put("12", false);
                put("13", true);
                put("14", false);
                put("15", true);
                put("16", false);
                put("17", false);
                put("18", false);
                put("19", false);
                put("1A", false);
                put("1B", false);
                put("1C", true);
                put("1D", false);
                put("1E", false);
                put("1F", true);
                put("20", true);
                put("21", true);
                put("22", false);
                put("23", true);
                put("24", false);
                put("25", false);
                put("26", false);
                put("27", false);
                put("28", false);
                put("29", false);
                put("2A", false);
                put("2B", false);
                put("2C", false);
                put("2D", false);
                put("2E", true);
                put("2F", false);
                put("30", true);
                put("31", true);
                put("32", false);
                put("33", true);
                put("34", true);
                put("35", false);
                put("36", false);
                put("37", false);
                put("38", false);
                put("39", false);
                put("3A", false);
                put("3B", false);
                put("3C", true);
                put("3D", false);
                put("3E", false);
                put("3F", false);
                put("40", true);
                put("41", true);
                put("42", true);
                put("43", true);
                put("44", true);
                put("45", true);
                put("46", true);
                put("47", true);
                put("48", false);
                put("49", true);
                put("4A", true);
                put("4B", false);
                put("4C", true);
                put("4D", false);
                put("4E", false);
                put("4F", false);
                put("50", false);
                put("51", false);
                put("52", false);
                put("53", false);
                put("54", false);
                put("55", false);
                put("56", true);
                put("57", false);
                put("58", false);
                put("59", false);
                put("5A", false);
                put("5B", false);
                put("5C", false);
                put("5D", false);
                put("5E", false);
                put("5F", false);
                put("60", false);
            }
        };

        HashMap<String, Boolean> availabilityMap = CheckAvailableCommands.getAvailabilityMap(audi);
        assertEquals(result, availabilityMap);
    }

    @Test
    public void testAvailabilityMap00_20() {
        final ArrayList<String> audi01_20 = new ArrayList<>(Collections.singleton("41 00 BE 1F A8 13"));
        final Map<String, Boolean> result = new HashMap<String, Boolean>() {
            {
                put("01", true);
                put("02", false);
                put("03", true);
                put("04", true);
                put("05", true);
                put("06", true);
                put("07", true);
                put("08", false);
                put("09", false);
                put("0A", false);
                put("0B", false);
                put("0C", true);
                put("0D", true);
                put("0E", true);
                put("0F", true);
                put("10", true);
                put("11", true);
                put("12", false);
                put("13", true);
                put("14", false);
                put("15", true);
                put("16", false);
                put("17", false);
                put("18", false);
                put("19", false);
                put("1A", false);
                put("1B", false);
                put("1C", true);
                put("1D", false);
                put("1E", false);
                put("1F", true);
                put("20", true);
            }
        };

        Map<String, Boolean> availabilityMap = CheckAvailableCommands.getAvailabilityMap(audi01_20);

        assertEquals(result, availabilityMap);
    }

    @Test
    public void testAvailabilityMap21_40() {
        final ArrayList<String> audi21_40 = new ArrayList<>(Collections.singleton("4120A005B011"));
        final Map<String, Boolean> result = new HashMap<String, Boolean>() {
            {
                put("21", true);
                put("22", false);
                put("23", true);
                put("24", false);
                put("25", false);
                put("26", false);
                put("27", false);
                put("28", false);
                put("29", false);
                put("2A", false);
                put("2B", false);
                put("2C", false);
                put("2D", false);
                put("2E", true);
                put("2F", false);
                put("30", true);
                put("31", true);
                put("32", false);
                put("33", true);
                put("34", true);
                put("35", false);
                put("36", false);
                put("37", false);
                put("38", false);
                put("39", false);
                put("3A", false);
                put("3B", false);
                put("3C", true);
                put("3D", false);
                put("3E", false);
                put("3F", false);
                put("40", true);
            }
        };

        Map<String, Boolean> availabilityMap = CheckAvailableCommands.getAvailabilityMap(audi21_40);

        assertEquals(result, availabilityMap);
    }

    @Test
    public void testAvailabilityMap41_60() {
        final ArrayList<String> audi41_60 = new ArrayList<>(Collections.singleton("41 40 FE D0 04 00"));
        final Map<String, Boolean> result = new HashMap<String, Boolean>() {
            {
                put("41", true);
                put("42", true);
                put("43", true);
                put("44", true);
                put("45", true);
                put("46", true);
                put("47", true);
                put("48", false);
                put("49", true);
                put("4A", true);
                put("4B", false);
                put("4C", true);
                put("4D", false);
                put("4E", false);
                put("4F", false);
                put("50", false);
                put("51", false);
                put("52", false);
                put("53", false);
                put("54", false);
                put("55", false);
                put("56", true);
                put("57", false);
                put("58", false);
                put("59", false);
                put("5A", false);
                put("5B", false);
                put("5C", false);
                put("5D", false);
                put("5E", false);
                put("5F", false);
                put("60", false);
            }
        };

        Map<String, Boolean> availabilityMap = CheckAvailableCommands.getAvailabilityMap(audi41_60);

        assertEquals(result, availabilityMap);
    }

    @Test
    public void testHexToBin() {
        String toyota_1_40 = "7A1C0000";
        assertEquals("01111010000111000000000000000000", CheckAvailableCommands.hexToBin(toyota_1_40));
    }

}