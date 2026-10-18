# Facade Pattern — Mars Rover Control

Implementation of the **Facade** structural design pattern in Java.

## Topic

A Mars rover consists of five complex subsystems (power, sensors, motors, navigation,
communication) that must be started in a strict order and can fail at any step.
`RoverControlFacade` gives the client three simple methods instead of 12+ low-level calls.

## Pattern Structure

| Role | Class | Description |
|---|---|---|
| Complex Subsystem | `PowerSystem` | Battery level, power on/off, energy consumption |
| Complex Subsystem | `SensorArray` | Calibration of camera, thermometer, gyroscope |
| Complex Subsystem | `MotorController` | Initialization, self-test, driving (6 wheels) |
| Complex Subsystem | `NavigationModule` | Map loading, route planning, energy cost calculation |
| Complex Subsystem | `CommunicationModule` | Connection to mission control, sending reports |
| Facade | `RoverControlFacade` | `startRover()`, `executeMission(destination)`, `shutdownRover()` |
| Additional Facade | `RoverDiagnosticsFacade` | `isHealthy()`, `getHealthReport()` — read-only diagnostics, separate from the main facade |
| Client | `Main` | Uses only the facades, never touches subsystem classes |

Supporting classes: `Route` (value object), `RoverException` (single exception type of the facade).

## Key Properties of the Pattern

- Subsystem classes **do not know about the facades** (no back-references).
- Subsystem classes remain **usable directly** by advanced clients who bypass the facade.
- The facade only **coordinates**; all checks and calculations live in the subsystem.

## How to Run

1. Open the project in IntelliJ IDEA and wait for Gradle sync.
2. Open `src/main/java/rover_facade/Main.java`.
3. Click the green Run button next to `main()` (or press `Shift+F10`).

Or from the terminal:

```
.\gradlew.bat build
java -cp build/classes/java/main rover_facade.Main
```

## Clean Code Principles Applied

1. **Meaningful names** — `startRover()`, `executeMission()`, `shutdownRover()` and
   `getHealthReport()` explain themselves; the client needs no knowledge of the
   subsystem to read the code.

2. **Single Responsibility** — the facade coordinates the order of calls and rollback;
   it does not reimplement subsystem logic. Battery checks, calibration state and route
   cost calculation stay inside `PowerSystem`, `SensorArray` and `NavigationModule`.

3. **No leaking of subsystem details** — the public API of the facade uses only simple
   types (`int`, `String`, `boolean`). `Route` is never exposed, subsystem exceptions are
   wrapped into `RoverException`, and the constructor of `RoverDiagnosticsFacade` is
   package-private.

4. **Small, focused subsystem classes** — each class has one responsibility and a handful
   of short methods with clear preconditions (e.g. motors cannot drive before the
   self-test passes).

5. **Proper error handling** — a custom `RoverException` hides internal exceptions from
   the client. If startup fails, an undo stack rolls back only the steps that already
   succeeded, in reverse order. `shutdownRover()` always completes, even if the final
   report cannot be sent.

## Example Output (failed startup with rollback)

```
PowerSystem: powered on, battery 80%
SensorArray: calibrated camera
...
NavigationModule: map loaded, 3 locations known
Facade: startup failed, rolling back...
MotorController: motors stopped
SensorArray: shut down
PowerSystem: powered off
FAILED: Rover startup failed: Signal too weak: 10% (minimum 30%)
```
