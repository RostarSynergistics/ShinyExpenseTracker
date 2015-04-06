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
	
	public Destination(String name, String reasonForTravel) {
		this.name = name;
		this.reasonForTravel = reasonForTravel;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reasonForTravel == null) {
			if (other.reasonForTravel != null)
				return false;
		} else if (!reasonForTravel.equals(other.reasonForTravel))
			return false;
		return true;
	}
	
	
}
