package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.shinyexpensetracker.IView;

public class ExpenseClaim implements IModel<IView<ExpenseClaim>> {
	public enum Status {
		IN_PROGRESS, SUBMITTED, RETURNED, APPROVED
	}

	private String name;
	private Date startDate;
	private Date endDate;
	private Status status;
	private Tag tag;
	
	private transient ArrayList<IView<ExpenseClaim>> views;
	
	/*public ExpenseClaim() {
		this.views = new ArrayList<IView<ExpenseClaim>>();
	}*/
	
	/* RAMISH: Needed the constructor to handle arguments for the AddExpenseClaimActivity class to make an ExpenseClaim
	 * which is why I commented out the old constructor and added a new one to handle arguments.Comment out as needed.
	*/
	
	public ExpenseClaim(String name, Date startDate, Date endDate, Status status, Tag tag) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.tag = tag;
		
		this.views = new ArrayList<IView<ExpenseClaim>>();
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
	@Override
	public void addView(IView<ExpenseClaim> v) {
		views.add(v);
	}
	@Override
	public void removeView(IView<ExpenseClaim> v) {
		views.remove(v);
	}
	@Override
	public void notifyViews() {
		for (IView<ExpenseClaim> v : views) {
			v.update(this);
		}
	}
}
