package ca.ualberta.cs.shinyexpensetracker.models;

/**
 * 
 * Destination object extends Model.  
 * Has a name: String,
 * 		reason for travel: String
 * 
 * Destinations are added to expense claims
 *
 */
public class Destination extends Model<Destination> {
	public String name;
	public String reasonForTravel;
	private Coordinate geolocation;
	
	public Destination(String name, String reasonForTravel, Coordinate geolcoation) {
		this.name = name;
		this.reasonForTravel = reasonForTravel;
		this.geolocation = geolcoation;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getReasonForTravel() {
		return this.reasonForTravel;
	}
	public void setReasonForTravel(String reasonForTravel) {
		this.reasonForTravel = reasonForTravel;
	}

	public Coordinate getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(Coordinate geolocation) {
		this.geolocation = geolocation;
	}
	
}
