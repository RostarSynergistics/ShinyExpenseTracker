package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.Date;

public class ExpenseClaim extends Model<ExpenseClaim> {
	public enum Status {
		IN_PROGRESS, SUBMITTED, RETURNED, APPROVED
	}

	private String name;
	private Date startDate;
	private Date endDate;
	private Status status;
	private Tag tag;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
