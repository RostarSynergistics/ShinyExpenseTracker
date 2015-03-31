// Code take from: https://github.com/ramish94/AndroidElasticSearch, March 29, 2015

package ca.ualberta.cs.shinyexpensetracker.es;

import java.util.List;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public interface IClaimManager {

	public List<ExpenseClaim> searchClaim(String searchString, String field);
	public ExpenseClaim getClaim(int id);
	public void addClaim(ExpenseClaim expenseClaim);
	public void deleteClaim(int id);
}
