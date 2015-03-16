package ca.ualberta.cs.shinyexpensetracker.framework;

/**
 * 
 * Updates based on model changes
 *
 * @param <M>
 */
public interface IView<M> {
	public void update(M m);
}
