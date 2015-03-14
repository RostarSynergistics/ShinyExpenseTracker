package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;

public class ExpenseClaim extends Model<ExpenseClaim> implements Comparable<ExpenseClaim> {
	public enum Status {
		IN_PROGRESS("In Progress"),
		SUBMITTED("Submitted"), 
		RETURNED("Returned"),
		APPROVED("Approved");
		
		private final String text;
		
		private Status(final String text){
			this.text = text;
		}
		
		public String getText(){
			return this.text;
		}
		
		public static Status fromString(String text) {
			if (text != null) {
				for (Status s : Status.values()) {
					if (text.equalsIgnoreCase(s.text)) {
						return s;
					}
				}
			}
			return null;
		}
	}

	private String name;

	private Date startDate;
	private Date endDate;
	private Status status;
	private TagList tagList;
	private ArrayList<ExpenseItem> expenses = new ArrayList<ExpenseItem>();
	
	public ExpenseClaim(String name) {
		this(name, new Date(), null, Status.IN_PROGRESS, null);
	}

	public ExpenseClaim(String name, Date startDate) {
		this(name, startDate, null, Status.IN_PROGRESS, null);
	}
	
	public ExpenseClaim(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = Status.IN_PROGRESS;
		this.tagList = new TagList();
	}
	
	public ExpenseClaim(String name, Date startDate, Date endDate,
			Status status, TagList tagList) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.tagList = tagList;
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
	public TagList getTagList() {
		return tagList;
	}
	public void setTagList(TagList tagList) {
		this.tagList = tagList;
	}
	public ArrayList<ExpenseItem> getExpenses(){
		return expenses;
	}
	public void addExpense(ExpenseItem expense) {
		expenses.add(expense);
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
