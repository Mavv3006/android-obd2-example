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
package de.deuschle.obd.commands.protocol;

import de.deuschle.obd.enums.AvailableCommand;

/**
 * Warm-start the OBD connection.
 */
public class ObdWarmStartCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for ObdWarmStartCommand.</p>
     */
    public ObdWarmStartCommand() {
        super(AvailableCommand.WARM_START);
    }

    /**
     * <p>Constructor for ObdWarmStartCommand.</p>
     *
     * @param other a {@link ObdWarmStartCommand} object.
     */
    public ObdWarmStartCommand(ObdWarmStartCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

}
