package ca.ualberta.cs.shinyexpensetracker.models;

import ca.ualberta.cs.shinyexpensetracker.IView;

public interface IModel {
	// FIXME generic types are evil and cause infinity warnings.
	public void addView(IView<IModel> v);
	public void removeView(IView<IModel> v);
	public void notifyViews();
}
