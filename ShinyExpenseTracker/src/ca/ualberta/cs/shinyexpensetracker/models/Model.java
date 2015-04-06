package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.framework.IView;

/**
 * Base class for domain model classes.
 * 
 * Allows management of associated views (adding/removing/notifying of updates).
 * 
 * NOTE: when extending this class, you MUST provide the same class to <M> as
 * the class itself. For example:
 * 
 * class Chair extends Model<Chair> {}
 * 
 * Source: http://stackoverflow.com/a/20933658/14064
 * 
 * @param <M>
 *            The child class itself (required for IView<M> usage).
 */
public abstract class Model<M extends Model<M>> {
	// Do not use directly as it will be null after retrieval from file using
	// GSON.
	// Access via getViews() instead.
	private transient ArrayList<IView<M>> views;

	/**
	 * Returns a new list of views if the current one is null, or the existing
	 * list otherwise.
	 * 
	 * @return The list of views.
	 */
	private ArrayList<IView<M>> getViews() {
		if (views == null) {
			views = new ArrayList<IView<M>>();
		}

		return views;
	}

	/**
	 * Adds a view for the model to keep track of.
	 * 
	 * @param v
	 *            The view to keep track of.
	 */
	public void addView(IView<M> v) {
		if (!getViews().contains(v)) {
			getViews().add(v);
		}
	}

	/**
	 * Stops the model from keeping track of a view.
	 * 
	 * @param v
	 *            The view to stop keeping track of.
	 */
	public void removeView(IView<M> v) {
		getViews().remove(v);
	}

	/**
	 * Update every view the model is keeping track of.
	 */
	@SuppressWarnings("unchecked")
	public void notifyViews() {
		for (IView<M> view : getViews()) {
			/*
			 * This cast is safe (and thus its warning is suppressed) as this
			 * object must be of type M or the rule set at the top of this class
			 * has not been followed.
			 */
			view.update((M) this);
		}
	}
}
