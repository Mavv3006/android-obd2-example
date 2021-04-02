package de.deuschle.androidodb2example.Conversion;

import de.deuschle.obd.enums.AvailableCommand;

public class SupportedCommands {
    public static final String ENGINE_RPM = AvailableCommand.ENGINE_RPM.getCommand().substring(3);
    public static final String SPEED = AvailableCommand.SPEED.getCommand().substring(3);
    public static final String AMBIENT_AIR_TEMP = AvailableCommand.AMBIENT_AIR_TEMP.getCommand().substring(3);
}
