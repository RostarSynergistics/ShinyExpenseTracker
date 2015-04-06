package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.UUID;

import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;

/**
 * A Destination represents a destination of travel on an expense claim.
 * 
 */
public class Destination extends Model<Destination> {
	private UUID id;
	private String name;
	private String reasonForTravel;

	public Destination(String name, String reasonForTravel) throws ValidationException {
		validateName(name);

		this.id = UUID.randomUUID();
		this.name = name;
		this.reasonForTravel = reasonForTravel;
	}

	private void validateName(String name) throws ValidationException {
		if (name == null || name.length() == 0) {
			throw new ValidationException("Destination requires a name.");
		}
	}

	public UUID getID() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) throws ValidationException {
		validateName(name);
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
