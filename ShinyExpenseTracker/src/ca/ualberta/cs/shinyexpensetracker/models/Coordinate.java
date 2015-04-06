package ca.ualberta.cs.shinyexpensetracker.models;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Coordinate extends Model<Coordinate> {
	private double latitude;
	private double longitude;
	public static final Coordinate DEFAULT_COORDINATE = new Coordinate(53.526821, -113.526591);
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
		
		return new EqualsBuilder()
		.append(getLatitude(), other.getLatitude())
		.append(getLongitude(), other.getLongitude())
		.isEquals();
	}
	
}
