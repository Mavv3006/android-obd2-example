package de.deuschle.androidodb2example.Session;

public class SessionData {
    public double value = 0;
    public int n = 0;
    public int sessionId;

    public SessionData() {
    }

    public SessionData(int sessionId) {
        this.sessionId = sessionId;
    }

    public SessionData(double value, int n, int sessionId) {
        this.value = value;
        this.n = n;
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "value=" + value +
                ", n=" + n +
                ", sessionId=" + sessionId +
                '}';
    }
}
