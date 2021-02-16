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

import java.util.ArrayList;
import java.util.List;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;


/**
 * Retrieve available PIDs ranging from 21 to 40.
 */
public class AvailablePidsCommand21to40 extends GenericAvailablePidsCommand {

    private final List<Class<? extends ObdCommand>> supportedCommands = new ArrayList<>();

    /**
     * Default constructor.
     */
    public AvailablePidsCommand21to40() {
        super(AvailableCommand.PIDS_21_40, 0x20);
    }

    /**
     * Copy constructor.
     *
     * @param other a {@link AvailablePidsCommand21to40} object.
     */
    public AvailablePidsCommand21to40(AvailablePidsCommand21to40 other) {
        super(other);
    }

}
