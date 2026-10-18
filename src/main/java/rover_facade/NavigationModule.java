package rover_facade;

import java.util.HashMap;
import java.util.Map;

public class NavigationModule {
    private static final int METERS_PER_ENERGY_PERCENT = 50;

    private final Map<String, Integer> knownLocations = new HashMap<>();
    private boolean mapLoaded;

    public void loadMap() {
        knownLocations.put("Crater Alpha", 500);
        knownLocations.put("Ridge Beta", 1200);
        knownLocations.put("Dune Gamma", 300);
        mapLoaded = true;
        System.out.println("NavigationModule: map loaded, " + knownLocations.size() + " locations known");
    }

    public Route planRoute(String destination) {
        if (!mapLoaded) {
            throw new IllegalStateException("Map is not loaded");
        }
        Integer distance = knownLocations.get(destination);
        if (distance == null) {
            throw new IllegalArgumentException("Unknown destination: " + destination);
        }
        int energyCost = Math.max(1, distance / METERS_PER_ENERGY_PERCENT);
        System.out.println("NavigationModule: route to " + destination + " = "
                + distance + " m, cost " + energyCost + "%");
        return new Route(destination, distance, energyCost);
    }

    public void unloadMap() {
        knownLocations.clear();
        mapLoaded = false;
    }
}