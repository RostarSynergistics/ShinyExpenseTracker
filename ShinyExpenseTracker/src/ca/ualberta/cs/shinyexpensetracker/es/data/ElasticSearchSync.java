package ca.ualberta.cs.shinyexpensetracker.es.data;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 *This class will sync any changes the user made with any changes the in the server 
 *
 */
public class ElasticSearchSync {
	ExpenseClaimList localList;
	ExpenseClaimList cloudList;
	
	public ElasticSearchSync(ExpenseClaimList localList, ExpenseClaimList cloudList) {
		this.cloudList = cloudList;
		this.localList = localList;
	}
	
	/**
	 * sync two lists. The local list will only change data that the approval has changed
	 * @return The synced expense claim list 
	 */
	public ExpenseClaimList sync(){
		for(ExpenseClaim claim :cloudList.getClaims()){
			ExpenseClaim localClaim =localList.getClaim(claim.getUuid());
			if (localClaim != null){
				localClaim.setComments(claim.getComments());
				
				if(localClaim.getStatus() == Status.SUBMITTED ){
					localClaim.setStatus(claim.getStatus());
				}
				
			}
		}
		return localList;
	}
}
