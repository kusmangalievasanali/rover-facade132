package rover_facade;

import java.util.ArrayList;
import java.util.List;

public class CommunicationModule {
    private static final int MIN_SIGNAL_STRENGTH = 30;

    private final int signalStrength;
    private final List<String> sentReports = new ArrayList<>();
    private boolean connected;

    public CommunicationModule(int signalStrength) {
        if (signalStrength < 0 || signalStrength > 100) {
            throw new IllegalArgumentException("Signal strength must be 0-100, got: " + signalStrength);
        }
        this.signalStrength = signalStrength;
    }

    public void connect() {
        if (signalStrength < MIN_SIGNAL_STRENGTH) {
            throw new IllegalStateException("Signal too weak: " + signalStrength
                    + "% (minimum " + MIN_SIGNAL_STRENGTH + "%)");
        }
        connected = true;
        System.out.println("CommunicationModule: connected to mission control, signal " + signalStrength + "%");
    }

    public void sendReport(String message) {
        if (!connected) {
            throw new IllegalStateException("Not connected to mission control");
        }
        sentReports.add(message);
        System.out.println("CommunicationModule: report sent -> " + message);
    }

    public void disconnect() {
        connected = false;
        System.out.println("CommunicationModule: disconnected");
    }

    public boolean isConnected() { return connected; }

    public int getSignalStrength() { return signalStrength; }

    public int getSentReportsCount() { return sentReports.size(); }
}