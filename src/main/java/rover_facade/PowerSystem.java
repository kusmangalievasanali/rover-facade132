package rover_facade;

public class PowerSystem {
    private static final int MIN_BATTERY_TO_START = 20;

    private int batteryLevel;
    private boolean poweredOn;

    public PowerSystem(int batteryLevel) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalArgumentException("Battery level must be 0-100, got: " + batteryLevel);
        }
        this.batteryLevel = batteryLevel;
    }

    public void powerOn() {
        if (batteryLevel < MIN_BATTERY_TO_START) {
            throw new IllegalStateException("Battery too low to start: " + batteryLevel
                    + "% (minimum " + MIN_BATTERY_TO_START + "%)");
        }
        poweredOn = true;
        System.out.println("PowerSystem: powered on, battery " + batteryLevel + "%");
    }

    public void consume(int percent) {
        requirePoweredOn();
        if (percent > batteryLevel) {
            throw new IllegalStateException("Not enough energy: need " + percent
                    + "%, have " + batteryLevel + "%");
        }
        batteryLevel -= percent;
        System.out.println("PowerSystem: consumed " + percent + "%, left " + batteryLevel + "%");
    }

    public void powerOff() {
        poweredOn = false;
        System.out.println("PowerSystem: powered off");
    }

    public boolean isPoweredOn() { return poweredOn; }

    public int getBatteryLevel() { return batteryLevel; }

    private void requirePoweredOn() {
        if (!poweredOn) {
            throw new IllegalStateException("Power system is off");
        }
    }
}