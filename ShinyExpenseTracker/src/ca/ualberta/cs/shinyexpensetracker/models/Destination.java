package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.UUID;

/**
 * A Destination represents a destination of travel on an expense claim.
 * 
 */
public class Destination extends Model<Destination> {
	private UUID id;
	private String name;
	private String reasonForTravel;

	public Destination(String name, String reasonForTravel) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.reasonForTravel = reasonForTravel;
	}

	public UUID getID() {
		return id;
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
}
