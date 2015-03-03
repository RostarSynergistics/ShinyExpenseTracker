package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

// FIXME Changed from interface to abstract
abstract public class ClaimDataExporter {
	abstract public void export(ExpenseClaim claim);
}
