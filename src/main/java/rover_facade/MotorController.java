package rover_facade;

public class MotorController {
    private static final int WHEEL_COUNT = 6;

    private boolean initialized;
    private boolean selfTestPassed;
    private int totalDistanceMeters;

    public void initialize() {
        initialized = true;
        System.out.println("MotorController: " + WHEEL_COUNT + " wheel motors initialized");
    }

    public void selfTest() {
        if (!initialized) {
            throw new IllegalStateException("Motors are not initialized");
        }
        for (int wheel = 1; wheel <= WHEEL_COUNT; wheel++) {
            System.out.println("MotorController: wheel " + wheel + " torque check OK");
        }
        selfTestPassed = true;
    }

    public void drive(Route route) {
        if (!selfTestPassed) {
            throw new IllegalStateException("Motor self-test has not passed");
        }
        totalDistanceMeters += route.getDistanceMeters();
        System.out.println("MotorController: driving " + route.getDistanceMeters()
                + " m to " + route.getDestination());
    }

    public void stop() {
        selfTestPassed = false;
        initialized = false;
        System.out.println("MotorController: motors stopped");
    }

    public int getTotalDistanceMeters() { return totalDistanceMeters; }
}