package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ExpenseItemListFragmentTest.class);
		//$JUnit-END$
		return suite;
	}

}
