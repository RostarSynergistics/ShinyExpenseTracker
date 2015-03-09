package ca.ualberta.cs.shinyexpensetracker.models;

public class Tag {
	private String value;
	
	public Tag(String s) {
		this.value = s;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Tag))
			return false;
		else{
			String otherValue = ((Tag) other).getValue();
			return this.getValue().equals(otherValue);
		}
	}
}
