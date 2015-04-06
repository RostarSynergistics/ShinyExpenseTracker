package ca.ualberta.cs.shinyexpensetracker.models;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Coordinate extends Model<Coordinate> {
	private double latitude;
	private double longitude;
	public static final Coordinate DEFAULT_COORDINATE = new Coordinate(53.526821, -113.526591);
	public static final double LONGEST_DISTANCE_BETWEEN_POINTS = 20037.5;
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

	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    /*
	     * Copied from the answer by Espen Herseth Halvorsen on April 6, 2015
	     * at http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
	     */
		
		double earthRadius = 6371; //kilometers
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	    }
	
	public double distanceTo(Coordinate other) {
		double distance = 0.0;
		double lat1 = this.getLatitude();
		double lng1 = this.getLongitude();
		double lat2 = other.getLatitude();
		double lng2 = other.getLongitude();
		
		distance = distFrom(lat1, lng1, lat2, lng2);
		
		return distance;
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
