package rover_facade;

public final class Route {
    private final String destination;
    private final int distanceMeters;
    private final int energyCostPercent;

    public Route(String destination, int distanceMeters, int energyCostPercent) {
        this.destination = destination;
        this.distanceMeters = distanceMeters;
        this.energyCostPercent = energyCostPercent;
    }

    public String getDestination() { return destination; }
    public int getDistanceMeters() { return distanceMeters; }
    public int getEnergyCostPercent() { return energyCostPercent; }
}