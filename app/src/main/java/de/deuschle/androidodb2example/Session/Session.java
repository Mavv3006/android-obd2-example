package de.deuschle.androidodb2example.Session;

import java.util.Map;

public interface Session {
    Metadata getMetadata();

    Map<String, SessionData> getValues();
}
