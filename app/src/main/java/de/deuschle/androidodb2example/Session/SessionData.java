package de.deuschle.androidodb2example.Session;

public class SessionData {
    public double value = 0;
    public int n = 0;
    public int sessionId;

    @Override
    public String toString() {
        return "SessionData{" +
                "value=" + value +
                ", n=" + n +
                ", sessionId=" + sessionId +
                '}';
    }
}
