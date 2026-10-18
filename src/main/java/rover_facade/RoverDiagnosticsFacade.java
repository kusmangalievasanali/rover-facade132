package rover_facade;

public class RoverDiagnosticsFacade {
    private static final int LOW_BATTERY_PERCENT = 20;

    private final PowerSystem power;
    private final SensorArray sensors;
    private final MotorController motors;
    private final CommunicationModule communication;

    RoverDiagnosticsFacade(PowerSystem power, SensorArray sensors,
                           MotorController motors, CommunicationModule communication) {
        this.power = power;
        this.sensors = sensors;
        this.motors = motors;
        this.communication = communication;
    }

    public boolean isHealthy() {
        return power.isPoweredOn()
                && power.getBatteryLevel() >= LOW_BATTERY_PERCENT
                && sensors.isCalibrated()
                && communication.isConnected();
    }

    public String getHealthReport() {
        return "=== Rover health report ===\n"
                + describePower() + "\n"
                + describeSensors() + "\n"
                + describeMotors() + "\n"
                + describeCommunication() + "\n"
                + "Status: " + (isHealthy() ? "HEALTHY" : "ATTENTION NEEDED");
    }

    private String describePower() {
        return "Power: " + (power.isPoweredOn() ? "ON" : "OFF")
                + ", battery " + power.getBatteryLevel() + "%";
    }

    private String describeSensors() {
        if (!sensors.isCalibrated()) {
            return "Sensors: not calibrated";
        }
        return "Sensors: calibrated, temperature " + sensors.readTemperatureCelsius() + " C";
    }

    private String describeMotors() {
        return "Motors: total distance " + motors.getTotalDistanceMeters() + " m";
    }

    private String describeCommunication() {
        return "Communication: " + (communication.isConnected() ? "connected" : "offline")
                + ", signal " + communication.getSignalStrength() + "%";
    }
}