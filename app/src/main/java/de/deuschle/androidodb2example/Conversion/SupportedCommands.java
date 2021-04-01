package de.deuschle.androidodb2example.Conversion;

import de.deuschle.obd.enums.AvailableCommand;

public class SupportedCommands {
    public static final String engine_rpm = AvailableCommand.ENGINE_RPM.getCommand().substring(3);
    public static final String speed = AvailableCommand.SPEED.getCommand().substring(3);
    public static final String ambient_air_temp = AvailableCommand.AMBIENT_AIR_TEMP.getCommand().substring(3);
}
