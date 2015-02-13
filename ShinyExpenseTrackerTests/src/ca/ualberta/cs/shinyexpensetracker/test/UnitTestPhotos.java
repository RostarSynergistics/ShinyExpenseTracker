package ca.ualberta.cs.shinyexpensetracker.test;


import android.graphics.Bitmap;
import junit.framework.TestCase;

public class UnitTestPhotos extends TestCase {
	
	// Covers US06.01.01 and US06.03.01
	public void testCaseAdd() throws Exception {
		ExpenseItem item = new ExpenseItem();
		
		//Add path in for photo taken
		Bitmap photo = new Bitmap();
		item.add(photo);
		assertTrue(item.hasPhoto());
		assertEquals(photo.item.getPhoto());
		assertTrue(item.getPhoto().getAllocationByteCount <= 65536);	
	}
	
	// Covers US06.03.01
	public void testCaseDelete(){
		ExpenseItem item = new ExpenseItem();
		//Add path for the photo
		Bitmap photo = new Bitmap();
		item.add(photo);
		
		assertTrue(item.hasPhoto());
		item.deletePhoto();
		assertFalse(item.hasPhoto());
	}
}
