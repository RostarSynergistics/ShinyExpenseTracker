package ca.ualberta.cs.shinyexpensetracker.test;

import android.test.ActivityInstrumentationTestCase2;

public class OfflineTests extends ActivityInstrumentationTestCase2<T> {

	private String DataSource;
	private boolean internetConnection;
	
	public testDataCached() {
		internetConnection = false;
		assertTrue("Internet Connection", !internetConnection);
		Claim claim = new Claim();
		assertTrue("claim not saved locally", DataSource.equals('local'));
	}
	
	
	
}
