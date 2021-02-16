/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.deuschle.obd.commands.engine;


import de.deuschle.obd.commands.temperature.TemperatureCommand;
import de.deuschle.obd.enums.AvailableCommand;

/**
 * Displays the current engine Oil temperature.
 */
public class OilTempCommand extends TemperatureCommand {

    /**
     * Default constructor.
     */
    public OilTempCommand() {
        super(AvailableCommand.ENGINE_OIL_TEMP);
    }

    /**
     * Copy constructor.
     *
     * @param other a {@link OilTempCommand} object.
     */
    public OilTempCommand(OilTempCommand other) {
        super(other);
    }

}
