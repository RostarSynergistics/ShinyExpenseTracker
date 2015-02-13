package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Calendar;
import java.util.Date;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;

public class ViewSubmittedExpenseClaimDetailActivityTests extends
		ActivityInstrumentationTestCase2<ViewSubmittedExpenseClaimDetailActivity> {

	private Instrumentation instrumentation;

	protected void setUp() throws Exception {
		super.setUp();
		
		instrumentation = getInstrumentation();
	}

	// Covers use case #8
	public void testThatViewDetailsWork() {
		int id = 1;
		String name = "Jane Smith";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		
		ExpenseClaim testClaim = new ExpenseClaim();
		testClaim.setId(id);
		testClaim.setName(name);
		testClaim.setStatus("submitted");
		testClaim.setStartDate(startDate);
		testClaim.setEndDate(endDate);
		
        ViewSubmittedExpenseClaimDetailActivity activity = startWithIntent(testClaim);
        
        assertEquals("Names not the same", name, activity.getName());
        assertEquals("Start Dates not the same", startDate, activity.getStartDate());
        assertEquals("End Dates not the same", endDate, activity.getEndDate());
	}
	
	// Covers use case #10
	public void testThatAddingACommentWorks() {
		int id = 1;
		String name = "Jane Smith";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		
		ExpenseClaim testClaim = new ExpenseClaim();
		testClaim.setId(id);
		testClaim.setName(name);
		testClaim.setStatus("submitted");
		testClaim.setStartDate(startDate);
		testClaim.setEndDate(endDate);
		
        final ViewSubmittedExpenseClaimDetailActivity activity = startWithIntent(testClaim);
        final String commentText = "New comment";
        
        instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				activity.getCommentsButton().performClick();
				activity.setCommentText(commentText);
				activity.getAddCommentButton().performClick();
			}
		});
        
        instrumentation.waitForIdleSync();

        ArrayAdapter<Comment> adapter = activity.getCommentsAdapter();
        assertEquals("Comments adapter has less or more items than expected", 1, adapter.getCount());
        assertEquals("Comment doesn't have correct text", commentText, adapter[0].getText());
	}
	
	// Covers use case #12
	public void testThatApprovingAClaimWorks() {
		int id = 1;
		String name = "Jane Smith";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		
		ExpenseClaim testClaim = new ExpenseClaim();
		testClaim.setId(id);
		testClaim.setName(name);
		testClaim.setStatus("submitted");
		testClaim.setStartDate(startDate);
		testClaim.setEndDate(endDate);
		
        final ViewSubmittedExpenseClaimDetailActivity activity = startWithIntent(testClaim);
        
        instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				activity.getApproveButton().performClick();
			}
		});

        instrumentation.waitForIdleSync();

        assertEquals("View doesn't reflect updated state", "approved", activity.getStatus());
	}
	
	// Covers use case #11
	public void testThatReturningAClaimWorks() {
		int id = 1;
		String name = "Jane Smith";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date endDate = cal.getTime();
		
		ExpenseClaim testClaim = new ExpenseClaim();
		testClaim.setId(id);
		testClaim.setName(name);
		testClaim.setStatus("submitted");
		testClaim.setStartDate(startDate);
		testClaim.setEndDate(endDate);
		
        final ViewSubmittedExpenseClaimDetailActivity activity = startWithIntent(testClaim);
        
        instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				activity.getReturnButton().performClick();
			}
		});

        instrumentation.waitForIdleSync();

        assertEquals("View doesn't reflect updated state", "returned", activity.getStatus());
	}
	
	private ViewSubmittedExpenseClaimDetailActivity startWithIntent(ExpenseClaim claim) {
		Intent intent = new Intent();
		intent.putExtra(ViewSubmittedExpenseClaimDetailActivity.CLAIM_ID_KEY, claim.id);
		setActivityIntent(intent);
		return (ViewSubmittedExpenseClaimDetailActivity) getActivity();
	}
}
