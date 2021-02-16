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
package de.deuschle.obd.commands.control;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

/**
 * Timing Advance
 */
public class TimingAdvanceCommand extends ObdCommand {

    private double timingAdvance = -65f;

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     */
    public TimingAdvanceCommand() {
        super(AvailableCommand.TIMING_ADVANCE);
    }

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     *
     * @param other a {@link TimingAdvanceCommand} object.
     */
    public TimingAdvanceCommand(TimingAdvanceCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        timingAdvance = buffer.get(2) / 2f - 64;
    }

    @Override
    public String getFormattedResult() {
        return getCalculatedResult() + getResultUnit();
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(timingAdvance);
    }

    @Override
    public String getResultUnit() {
        return "°";
    }

}
