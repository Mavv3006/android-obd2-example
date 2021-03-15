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
package de.deuschle.obd.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.obd.enums.AvailableCommand;


/**
 * Base persistent OBD command.
 */
public abstract class PersistentCommand extends ObdCommand {
    private static Map<String, String> knownValues = new HashMap<>();
    private static Map<String, List<Integer>> knownBuffers = new HashMap<>();

    /**
     * <p>Constructor for PersistentCommand.</p>
     *
     * @param command a {@link java.lang.String} object.
     */
    public PersistentCommand(AvailableCommand command) {
        super(command);
    }

    /**
     * <p>Constructor for PersistentCommand.</p>
     *
     * @param other a {@link ObdCommand} object.
     */
    public PersistentCommand(ObdCommand other) {
        super(other.cmd);
    }

    /**
     * <p>reset.</p>
     */
    public static void reset() {
        knownValues = new HashMap<>();
        knownBuffers = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readResult(InputStream inputStream) throws IOException {
        super.readResult(inputStream);
        String key = getKey();
        knownValues.put(key, rawData);
        knownBuffers.put(key, new ArrayList<>(buffer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(InputStream in, OutputStream out) throws IOException, InterruptedException {
        String key = getKey();
        if (knownValues.containsKey(key)) {
            rawData = knownValues.get(key);
            buffer = knownBuffers.get(key);
            performCalculations();
        } else {
            super.run(in, out);
        }
    }

    private String getKey() {
        return getClass().getSimpleName() + (cmd != null ? cmd.getCommand() : "");
    }
}
