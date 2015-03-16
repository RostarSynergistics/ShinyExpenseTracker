package ca.ualberta.cs.shinyexpensetracker.models;

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
}
