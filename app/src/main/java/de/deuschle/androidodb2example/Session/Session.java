package de.deuschle.androidodb2example.Session;

import java.util.Map;

public interface Session {
    Metadata getMetadata();

    Map<String, SessionData> getValues();

    void willBeSaved();

    boolean isSaved();

    /**
     * @return {@code true} if the session should be saved. {@code false} otherwise.
     */
    boolean needsToBeSaved();

    /**
     * @return {@code true}, if the session has been started.
     * {@code false} if the session has not yet been started.
     */
    boolean isStarted();
}
