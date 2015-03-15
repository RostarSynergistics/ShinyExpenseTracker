package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;

public class ExpenseClaim extends Model<ExpenseClaim> implements Comparable<ExpenseClaim> {
	public enum Status {
		IN_PROGRESS, SUBMITTED, RETURNED, APPROVED
	}

	private String name;

	private Date startDate;
	private Date endDate;
	private Status status;
	private Tag tag;
	private ArrayList<ExpenseItem> items = new ArrayList<ExpenseItem>();
	
	public ExpenseClaim(String name) {
		this(name, new Date(), null, Status.IN_PROGRESS, null);
	}

	public ExpenseClaim(String name, Date startDate) {
		this(name, startDate, null, Status.IN_PROGRESS, null);
	}
	
	public ExpenseClaim(String name, Date startDate, Date endDate) {
		this(name, startDate, endDate, Status.IN_PROGRESS, null);
	}
	
	public ExpenseClaim(String name, Date startDate, Date endDate,
			Status status, Tag tag) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.tag = tag;
	}
	
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
	public void addItem(ExpenseItem item){
		this.items.add(item);
	}
	public void removeItem(ExpenseItem item){
		this.items.remove(item);
	}
	public ExpenseItem getItemById(int id){
		return this.items.get(id);
	}
	/**
	 * Comparison of two claims is the comparison of their start date.
	 */
	public int compareTo(ExpenseClaim other) {
		return this.getStartDate().compareTo(other.getStartDate());
	}
	
	// #22 This needs to be replaced when we make a better list view
	public String toString() {
		return getName();
	}
}
