package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;

import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;

/**
 * Class that represents an expense claim created by a user.
 */
public class ExpenseClaim extends Model<ExpenseClaim> implements Comparable<ExpenseClaim> {
	public enum Status {
		IN_PROGRESS("In Progress"), SUBMITTED("Submitted"), RETURNED("Returned"), APPROVED("Approved");

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

	private final UUID id;
	private UUID userID;
	private String name;
	private Date startDate;
	private Date endDate;
	private Status status;
	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	private TagList tagList = new TagList();

	private int color;
	private ArrayList<ExpenseItem> expenseItems = new ArrayList<ExpenseItem>();
	private ArrayList<String> comments = new ArrayList<String>();

	public ExpenseClaim(UUID userID, String name) throws ValidationException {
		this(userID, name, new Date(), new Date(), Status.IN_PROGRESS, new TagList());
	}

	public ExpenseClaim(UUID userID, String name, Date startDate, Date endDate) throws ValidationException {
		this(userID, name, startDate, endDate, Status.IN_PROGRESS, new TagList());
	}

	public ExpenseClaim(UUID userID, String name, Date startDate, Date endDate, Status status) throws ValidationException {
		this(userID, name, startDate, endDate, status, new TagList());
	}

	public ExpenseClaim(UUID userID, String name, Date startDate, Date endDate, Status status, TagList tagList) throws ValidationException {
		this(UUID.randomUUID(), userID, name, startDate, endDate, status, tagList);
	}

	public ExpenseClaim(UUID id, UUID userID, String name, Date startDate, Date endDate, Status status) throws ValidationException {
		this(id, name, startDate, endDate, status, null);
	}

	public ExpenseClaim(UUID id, UUID userID, String name, Date startDate, Date endDate, Status status, TagList tagList) throws ValidationException {

		super();

		validateName(name);
		validateDates(startDate, endDate);
		validateStatus(status);

		this.id = id;
		this.userID = userID;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.tagList = tagList;

	}

	private void validateDates(Date newStartDate, Date newEndDate) throws ValidationException {
		if (newStartDate == null) {
			throw new ValidationException("Expense Claim requires a start date.");
		}

		if (newEndDate == null) {
			throw new ValidationException("Expense Claim requires a end date.");
		}

		if (newStartDate.after(newEndDate)) {
			throw new ValidationException("Expense Claim's start date must be before or equal to its end date.");
		}
	}

	public void validateName(String newName) throws ValidationException {
		if (newName == null || newName.length() == 0) {
			throw new ValidationException("Expense Claim requires a name.");
		}
	}

	public UUID getID() {
		return id;
	}

	public void setUserId(UUID id) {
		this.userID = id;
	}

	public UUID getUserID() {
		return userID;
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

	public void setStatus(Status status) throws ValidationException {
		validateStatus(status);
		this.status = status;
		notifyViews();
	}

	private void validateStatus(Status newStatus) throws ValidationException {
		if (newStatus == Status.SUBMITTED && hasIncompleteExpenseItems()) {
			throw new ValidationException("Cannot submit an incomplete claim.");
		}

		if (newStatus == Status.APPROVED || newStatus == Status.RETURNED) {
			if (getComments().size() == 0) {
				String verb = newStatus == Status.APPROVED ? "approve" : "return";
				throw new ValidationException("You must comment on a claim before you " + verb + "it.");
			}
		}
	}

	public boolean hasIncompleteExpenseItems() {
		boolean value = false;

		for (ExpenseItem item : expenseItems) {
			if (item.isMarkedIncomplete()) {
				value = true;
				break;
			}
		}

		return value;
	}

	public TagList getTagList() {
		return tagList;
	}

	public void setTagList(TagList tagList) {
		this.tagList = tagList;
		notifyViews();
	}

	public Destination getDestinationByID(UUID id) {
		Destination foundDestination = null;

		for (Destination destination : destinations) {
			if (destination.getID().equals(id)) {
				foundDestination = destination;
				break;
			}
		}

		return foundDestination;
	}

	public Destination getDestinationAtPosition(int position) {
		return destinations.get(position);
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

	public void addExpenseItem(ExpenseItem expense) {
		expenseItems.add(expense);
		notifyViews();
	}

	public void removeExpense(int index) {
		// TODO Issue #21.
		// This function is partially complete, but does not
		// delete from the local stores or ElasticSearch.
		expenseItems.remove(index);
		notifyViews();
	}

	public void removeExpense(ExpenseItem expense) {
		expenseItems.remove(expenseItems.indexOf(expense));
		notifyViews();
	}

	public ExpenseItem getExpenseItemByID(UUID id) {
		ExpenseItem foundItem = null;

		for (ExpenseItem item : expenseItems) {
			if (item.getID().equals(id)) {
				foundItem = item;
				break;
			}
		}

		return foundItem;
	}

	public ExpenseItem getExpenseItemAtPosition(int position) {
		return expenseItems.get(position);
	}

	public ArrayList<ExpenseItem> getExpenses() {
		return expenseItems;
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
		return expenseItems.size();
	}

	public ArrayList<ExpenseItem> getExpenseItems() {
		return expenseItems;
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

	/**
	 * Adds a tag to the claims tag list
	 * 
	 * @param tag
	 *            to put in the
	 * @return boolean if the tag was added
	 */
	public boolean addTag(Tag tag) {
		return tagList.addTag(tag);
	}

	/**
	 * Removes a tag to the claims tag list
	 * 
	 * @param tag
	 *            to be removed
	 * @return boolean if the tag was removed
	 */
	public boolean removeTag(Tag tag) {
		return tagList.removeTag(tag);
	}

	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public void addComment(String comment) {
		comments.add(comment);
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

		return new EqualsBuilder().append(getID(), rhs.getID()).append(getUserID(), rhs.getUserID())
				.append(getName(), rhs.getName()).append(getStartDate(), rhs.getStartDate())
				.append(getEndDate(), rhs.getEndDate()).append(getStatus(), rhs.getStatus())
				.append(getExpenseItems(), rhs.getExpenseItems()).append(getDestinations(), rhs.getDestinations())
				.append(getTagList(), rhs.getTagList()).isEquals();

	}

	public String getComment(int index) {
		return comments.get(index);

	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
}
