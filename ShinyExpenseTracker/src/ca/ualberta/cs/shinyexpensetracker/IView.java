package ca.ualberta.cs.shinyexpensetracker;

public interface IView<M extends IModel> {
	public void update(M m);
}
