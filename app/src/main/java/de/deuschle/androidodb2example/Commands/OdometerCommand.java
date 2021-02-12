package de.deuschle.androidodb2example.Commands;

import java.util.Locale;

import br.ufrn.imd.obd.commands.ObdCommand;
import br.ufrn.imd.obd.enums.AvailableCommand;

public class OdometerCommand extends ObdCommand {

    double value = 0;

    private static final double aFactor = Math.pow(2, 24);
    private static final double bFactor = Math.pow(2, 16);
    private static final double cFactor = Math.pow(2, 8);


    public OdometerCommand(OdometerCommand other) {
        super(other);
    }

    public OdometerCommand() {
        super(AvailableCommand.CustomCommand.rawCommand("01 A6"));
    }

    @Override
    protected void performCalculations() {
        int a = buffer.get(2);
        int b = buffer.get(3);
        int c = buffer.get(4);
        int d = buffer.get(5);
        value = (a * aFactor + b * bFactor + c * cFactor + d) / 10;
    }

    @Override
    public String getFormattedResult() {
        return String.format(Locale.getDefault(), "%f%s", value, getResultUnit());
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(value);
    }

    @Override
    public String getResultUnit() {
        return "hm";
    }
}
