package ca.ualberta.cs.shinyexpensetracker.models;

public class Tag {
	private String value;
	
	public Tag(String s) {
		this.value = s;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean equals(Tag other) {
		return this.getValue().equals(other.getValue());
	}
	
	@Override
	public String toString() {
		return value;
	}
}
