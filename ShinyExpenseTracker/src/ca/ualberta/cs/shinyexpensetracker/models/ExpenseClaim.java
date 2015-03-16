package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * ExpenseClaim objects extend Model and implement Comparable
 * 
 * Has a name: String,
 * 		startDate: Date,
 * 		endDate: Date,
 * 		Status: enum Status
 * 		Destinations: ArrayList<Destination>
 * 		TagList: TagList
 * 		Expenses: ArrayList<ExpenseItem>
 *
 */
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
	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	private TagList tagList;
	private ArrayList<ExpenseItem> expenses = new ArrayList<ExpenseItem>();

	
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
		notifyViews();
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		notifyViews();
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		notifyViews();
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
		notifyViews();
	}
	
	public TagList getTagList() {
		return tagList;
	}
	public void setTagList(TagList tagList) {
		this.tagList = tagList;
		notifyViews();
	}
	
	public Destination getDestination(int index){
		return destinations.get(index);
	}
	
	public void addDestination(Destination destination) {
		destinations.add(destination);
		notifyViews();
	}
	
	public void removeDestination(int index) {
		destinations.remove(index);
		notifyViews();
	}
	
	public void removeDestination(Destination destination) {
		destinations.remove(destinations.indexOf(destination) );
		notifyViews();
	}
	
	public void addExpense(ExpenseItem expense) {
		expenses.add(expense);
		notifyViews();
	}
	
	public void removeExpense(int index) {
		// TODO Issue #21.
		// This function is partially complete, but does not
		// delete from the local stores or ElasticSearch.
		expenses.remove(index);
		notifyViews();
	}
	
	public void removeExpense(ExpenseItem expense) {
		expenses.remove(expenses.indexOf(expense) );
		notifyViews();
	}

	/**
	 * Returns the expense for this claim at the given index
	 * @param index of the expense item
	 * @return the requested Expense item
	 */
	public ExpenseItem getExpense(int index){
		return expenses.get(index);
	}
	
	/**
	 * Comparison of two claims is the comparison of their start date.
	 */
	public int compareTo(ExpenseClaim other) {
		return this.getStartDate().compareTo(other.getStartDate());
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * returns number of expenses
	 * @return
	 */
	public int getExpenseCount() {
		return expenses.size();
	}
	
	/**
	 * returns number of destinations
	 * @return
	 */
	public int getDestinationCount() {
		return destinations.size();
	}
}
