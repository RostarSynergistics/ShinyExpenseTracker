package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class that represents an expense claim created by a user.
 */
public class ExpenseClaim extends Model<ExpenseClaim> implements
		Comparable<ExpenseClaim> {
	public enum Status {
		IN_PROGRESS("In Progress"),
		SUBMITTED("Submitted"),
		RETURNED("Returned"),
		APPROVED( "Approved");

		private final String text;

		private Status(final String text) {
			this.text = text;
		}

		public String getText() {
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

	public ExpenseClaim(String name, Date startDate, Date endDate, Status status) {
		this(name, startDate, endDate, status, null);
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

	public Destination getDestination(int index) {
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
		destinations.remove(destinations.indexOf(destination));
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
		expenses.remove(expenses.indexOf(expense));
		notifyViews();
	}

	/**
	 * Returns the expense for this claim at the given index
	 * 
	 * @param index
	 *            of the expense item
	 * @return the requested Expense item
	 */
	public ExpenseItem getExpense(int index) {
		return expenses.get(index);
	}
	
	public ArrayList<ExpenseItem> getExpenses() {
		return expenses;
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
	 * 
	 * @return
	 */
	public int getExpenseCount() {
		return expenses.size();
	}

	public ArrayList<ExpenseItem> getExpenseItems() {
		return expenses;
	}

	/**
	 * returns number of destinations
	 * 
	 * @return
	 */
	public int getDestinationCount() {
		return destinations.size();
	}

	public ArrayList<Destination> getDestinations() {
		return destinations;
	}

	// Source:
	// https://commons.apache.org/proper/commons-lang/javadocs/api-3.3.2/org/apache/commons/lang3/builder/EqualsBuilder.html
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ExpenseClaim rhs = (ExpenseClaim) obj;
		return new EqualsBuilder()
				.append(getName(), rhs.getName())
				.append(getStartDate(), rhs.getStartDate())
				.append(getEndDate(), rhs.getEndDate())
				.append(getStatus(), rhs.getStatus())
				.append(getExpenseItems(), rhs.getExpenseItems())
				.append(getDestinations(), rhs.getDestinations())
				// .append(getTagList(), rhs.getTagList())
				.isEquals();
	}

	// Source:
	// https://commons.apache.org/proper/commons-lang/javadocs/api-3.3.2/org/apache/commons/lang3/builder/HashCodeBuilder.html
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
		.append(getName())
		.append(getStartDate())
		.append(getEndDate())
		.append(getStatus())
		.append(getExpenseItems())
		.append(getDestinations())
		//.append(getTagList())
		.toHashCode();
	}
}
