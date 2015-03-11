package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import ca.ualberta.cs.shinyexpensetracker.IView;

/**
 * Base class for domain model classes.
 * 
 * Allows management of associated views (adding/removing/notifying of updates).
 * 
 * NOTE: when extending this class, you MUST provide the same class to <M> as the class itself.
 * For example:
 *    class Chair extends Model<Chair> {}
 * 
 * Source: http://stackoverflow.com/a/20933658/14064
 *
 * @param <M> The child class itself (required for IView<M> usage).
 */
public abstract class Model<M extends Model<M>> {
	private transient ArrayList<IView<M>> views;

	public Model() {
		views = new ArrayList<IView<M>>();
	}

	/**
	 * Adds a view for the model to keep track of.
	 * 
	 * @param v The view to keep track of.
	 */
	public void addView(IView<M> v) {
		views.add(v);
	}

	/**
	 * Stops the model from keeping track of a view.
	 * 
	 * @param v The view to stop keeping track of.
	 */
	public void removeView(IView<M> v) {
		views.remove(v);
	}

	/**
	 * Update every view the model is keeping track of.
	 */
	@SuppressWarnings("unchecked")
	public void notifyViews() {
		for (IView<M> view : views) {
			/*
			 * This cast is safe (and thus its warning is suppressed)
			 * as this object must be of type M or the rule set at the top
			 * of this class has not been followed.
			 */
			view.update((M)this);
		}
	}
}
