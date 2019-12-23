/**
 * Coordinate: A class encapsulating a latitude-longitude coordinate.
 *
 * @author Alvin Lin axl1439@rit.edu
 */
public class Coordinate {

  private double latitude;
  private double longitude;

  public Coordinate(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getDistance(Coordinate other) {
    double deltaLat = this.latitude - other.latitude;
    double deltaLon = this.longitude - other.longitude;
    return Math.sqrt((deltaLat * deltaLat) + (deltaLon * deltaLon)) * 100;
  }
}
