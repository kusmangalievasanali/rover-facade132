package rover_facade;

public class Main {

    public static void main(String[] args) {
        runSuccessfulMission();
        runLowBatteryScenario();
        runWeakSignalScenario();
        runNotEnoughEnergyScenario();
    }

    private static void runSuccessfulMission() {
        printTitle("Scenario 1: successful mission");
        RoverControlFacade rover = new RoverControlFacade(80, 90);
        rover.startRover();
        rover.executeMission("Crater Alpha");
        System.out.println(rover.diagnostics().getHealthReport());
        rover.shutdownRover();
    }

    private static void runLowBatteryScenario() {
        printTitle("Scenario 2: startup fails, low battery");
        RoverControlFacade rover = new RoverControlFacade(10, 90);
        try {
            rover.startRover();
        } catch (RoverException e) {
            printError(e);
        }
    }

    private static void runWeakSignalScenario() {
        printTitle("Scenario 3: startup fails at the last step, rollback");
        RoverControlFacade rover = new RoverControlFacade(80, 10);
        try {
            rover.startRover();
        } catch (RoverException e) {
            printError(e);
        }
    }

    private static void runNotEnoughEnergyScenario() {
        printTitle("Scenario 4: mission fails, not enough energy");
        RoverControlFacade rover = new RoverControlFacade(21, 90);
        rover.startRover();
        try {
            rover.executeMission("Ridge Beta");
        } catch (RoverException e) {
            printError(e);
        }
        rover.shutdownRover();
    }

    private static void printTitle(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private static void printError(RoverException e) {
        System.out.println("FAILED: " + e.getMessage());
    }
}