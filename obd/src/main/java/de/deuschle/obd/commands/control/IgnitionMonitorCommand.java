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
package de.deuschle.obd.commands.control;


import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class IgnitionMonitorCommand extends ObdCommand {

    private boolean ignitionOn = false;

    /**
     * Default constructor.
     */
    public IgnitionMonitorCommand() {
        super(AvailableCommand.IGNITION_MONITOR);
    }

    /**
     * Copy constructor.
     *
     * @param other a {@link IgnitionMonitorCommand} object.
     */
    public IgnitionMonitorCommand(IgnitionMonitorCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        final String result = getResult();
        ignitionOn = result.equalsIgnoreCase("ON");
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }

    @Override
    public String getCalculatedResult() {
        return getResult();
    }

    @Override
    protected void fillBuffer() {
        // Empty method
    }

    public boolean isIgnitionOn() {
        return ignitionOn;
    }
}


