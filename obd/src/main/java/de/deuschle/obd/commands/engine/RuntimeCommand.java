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


import java.util.Locale;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;


/**
 * Engine runtime.
 */
public class RuntimeCommand extends ObdCommand {

    private int value = 0;

    /**
     * Default constructor.
     */
    public RuntimeCommand() {
        super(AvailableCommand.ENGINE_RUNTIME);
    }

    /**
     * Copy constructor.
     *
     * @param other a {@link RuntimeCommand} object.
     */
    public RuntimeCommand(RuntimeCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 0C] of the response
        value = buffer.get(2) * 256 + buffer.get(3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        // determine time
        final String hh = String.format(Locale.getDefault(), "%02d", value / 3600);
        final String mm = String.format(Locale.getDefault(), "%02d", (value % 3600) / 60);
        final String ss = String.format(Locale.getDefault(), "%02d", value % 60);
        return String.format("%s:%s:%s", hh, mm, ss);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "s";
    }

}
