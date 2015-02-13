package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class ClaimStatusTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	private void makeSubmitAndReviewClaim(String name,
			String startDate, String endDate, ExpenseClaimStatus status) {

		Claim claim = new Claim();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		ExpenseClaimList claimList = new ExpenseClaimList();
		
		claim.setName(name);
		claim.setStartDate(df.parse(startDate));
		claim.setEndDate(df.parse(endDate));
		
		claimList.addClaim(claim);
		claimList.submit(claim);
		claimList.setStatus(claim, status);
		
		return claimList.getClaim(claim);
	}

	public void testClaimInProgress() {
		Claim c = makeSubmitAndReviewClaim("my test claim",
				"2015/2/12", "2015/2/16", ExpenseClaimStatus.IN_PROGRESS);
		
		assertEquals("New claim not in in progress state", ExpenseClaimStatus.IN_PROGRESS, c.getStatus());
		assertTrue("Returned claim not editable", c.canEdit());
	}
	
	public void testClaimSubmit() {
		Claim c = makeSubmitAndReviewClaim("my test claim",
				"2015/2/12", "2015/2/16", ExpenseClaimStatus.SUBMIT);
		
		assertEquals("New claim not in submitted state", ExpenseClaimStatus.IN_PROGRESS, c.getStatus());
		assertFalse("Returned claim is editable", c.canEdit());
	}
	
	public void testClaimReturned() {
		Claim c = makeSubmitAndReviewClaim("my test claim",
				"2015/2/12", "2015/2/16", ExpenseClaimStatus.RETURNED);
		
		assertEquals("Returned claim not in returned state", ExpenseClaimStatus.RETURNED, c.getStatus());
		assertTrue("Returned claim not editable", c.canEdit());
	}
	
	public void testClaimApproved() {
		Claim c = makeSubmitAndReviewClaim("my test claim",
				"2015/2/12", "2015/2/16", ExpenseClaimStatus.APPROVED);
		
		assertEquals("Approved claim not in approved state", ExpenseClaimStatus.APPROVED, c.getStatus());
		assertFalse("Returned claim is editable", c.canEdit()); 
	}

}
