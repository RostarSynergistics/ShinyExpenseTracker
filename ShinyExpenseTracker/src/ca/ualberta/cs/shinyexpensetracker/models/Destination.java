package ca.ualberta.cs.shinyexpensetracker.models;

import org.apache.commons.lang3.builder.EqualsBuilder;

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
	private String name;
	private String reasonForTravel;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reasonForTravel == null) ? 0 : reasonForTravel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Destination other = (Destination) obj;
		
		return new EqualsBuilder()
		.append(getName(), other.getName())
		.append(getReasonForTravel(), other.getReasonForTravel())
		.append(getGeolocation(), other.getGeolocation())
		.isEquals();
	}
}
