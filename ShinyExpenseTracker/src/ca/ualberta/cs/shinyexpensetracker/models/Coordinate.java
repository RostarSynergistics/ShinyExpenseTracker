package ca.ualberta.cs.shinyexpensetracker.models;

public class Coordinate extends Model<Coordinate> {
	private double latitude;
	private double longitude;
	public static final Coordinate NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES = new Coordinate(39.03808, 125.7296);
	public Coordinate() {
		super();
	}

	public Coordinate(double latitude, double longitude) {
		super();
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		while (latitude < -90.0 || latitude > 90.0) {
			latitude = latitude - latitude/Math.abs(latitude)*180.0;
		}
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		while (longitude < -180.0 || longitude > 180.0) {
			longitude = longitude - longitude/Math.abs(longitude)*360.0;
		}
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Latitude: " + latitude + "\nLongitude: " + longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}
	
}
