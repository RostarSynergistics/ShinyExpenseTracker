package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.IModel;

public interface IView<M extends IModel> {
	public void update(M m);
}
