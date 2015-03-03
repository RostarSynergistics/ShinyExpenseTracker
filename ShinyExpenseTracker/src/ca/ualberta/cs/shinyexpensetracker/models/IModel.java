package ca.ualberta.cs.shinyexpensetracker.models;

import ca.ualberta.cs.shinyexpensetracker.IView;

@SuppressWarnings("rawtypes")
public interface IModel<V extends IView> {
	// FIXME generic types are evil and cause infinity warnings.
	public void addView(V v);
	public void removeView(V v);
	public void notifyViews();
}
