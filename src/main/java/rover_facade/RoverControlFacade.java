package rover_facade;

import java.util.ArrayDeque;
import java.util.Deque;

public class RoverControlFacade {
    private final PowerSystem power;
    private final SensorArray sensors;
    private final MotorController motors;
    private final NavigationModule navigation;
    private final CommunicationModule communication;


    private final Deque<Runnable> undoStack = new ArrayDeque<>();

    public RoverControlFacade(int batteryLevel, int signalStrength) {
        this.power = new PowerSystem(batteryLevel);
        this.sensors = new SensorArray();
        this.motors = new MotorController();
        this.navigation = new NavigationModule();
        this.communication = new CommunicationModule(signalStrength);
    }

    public void startRover() {
        if (isStarted()) {
            throw new RoverException("Rover is already started");
        }
        try {
            power.powerOn();
            undoStack.push(power::powerOff);

            sensors.calibrate();
            undoStack.push(sensors::shutdown);

            motors.initialize();
            undoStack.push(motors::stop);
            motors.selfTest();

            navigation.loadMap();
            undoStack.push(navigation::unloadMap);

            communication.connect();
            undoStack.push(communication::disconnect);
        } catch (RuntimeException e) {
            System.out.println("Facade: startup failed, rolling back...");
            rollback();
            throw new RoverException("Rover startup failed: " + e.getMessage(), e);
        }
        System.out.println("Facade: rover is ready");
    }

    public void executeMission(String destination) {
        requireStarted();
        try {
            Route route = navigation.planRoute(destination);
            power.consume(route.getEnergyCostPercent());
            motors.drive(route);
            communication.sendReport("Arrived at " + destination
                    + ", battery " + power.getBatteryLevel() + "%");
        } catch (RuntimeException e) {
            throw new RoverException("Mission to " + destination + " failed: " + e.getMessage(), e);
        }
    }

    public void shutdownRover() {
        requireStarted();
        try {
            communication.sendReport("Shutting down");
        } catch (RuntimeException e) {
            System.out.println("Facade: final report not sent: " + e.getMessage());
        }
        rollback();
        System.out.println("Facade: rover shut down safely");
    }

    public RoverDiagnosticsFacade diagnostics() {
        return new RoverDiagnosticsFacade(power, sensors, motors, communication);
    }

    public boolean isStarted() {
        return !undoStack.isEmpty();
    }

    private void requireStarted() {
        if (!isStarted()) {
            throw new RoverException("Rover is not started");
        }
    }

    private void rollback() {
        while (!undoStack.isEmpty()) {
            undoStack.pop().run();
        }
    }
}