/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.deuschle.obd.commands.fuel;


import java.util.Locale;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

/**
 * Wideband AFR
 */
public class WidebandAirFuelRatioCommand extends ObdCommand {

    private float wafr = 0;

    /**
     * <p>Constructor for WidebandAirFuelRatioCommand.</p>
     */
    public WidebandAirFuelRatioCommand() {
        super(AvailableCommand.WIDEBAND_AIR_FUEL_RATIO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float a = buffer.get(2);
        float b = buffer.get(3);
        wafr = (((a * 256) + b) / 32768) * 14.7f;//((A*256)+B)/32768
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return String.format(Locale.getDefault(), "%.2f", getWidebandAirFuelRatio()) + ":1 AFR";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(getWidebandAirFuelRatio());
    }

    /**
     * <p>getWidebandAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getWidebandAirFuelRatio() {
        return wafr;
    }

}
