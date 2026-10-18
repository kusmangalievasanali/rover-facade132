package rover_facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SensorArray {
    private static final List<String> SENSORS = List.of("camera", "thermometer", "gyroscope");

    private final Set<String> calibrated = new HashSet<>();

    public void calibrate() {
        for (String sensor : SENSORS) {
            calibrated.add(sensor);
            System.out.println("SensorArray: calibrated " + sensor);
        }
    }

    public boolean isCalibrated() {
        return calibrated.size() == SENSORS.size();
    }

    public int readTemperatureCelsius() {
        if (!isCalibrated()) {
            throw new IllegalStateException("Sensors are not calibrated");
        }
        return -63;
    }

    public void shutdown() {
        calibrated.clear();
        System.out.println("SensorArray: shut down");
    }
}