package ca.ualberta.cs.shinyexpensetracker.utilities;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64BitmapConverter {
	/**
	 * Converts a Bitmap photo to its Base64 string representation.
	 * 
	 * Source: http://stackoverflow.com/a/9768973/14064 (2015-04-03)
	 * 
	 * @param photo
	 *            The Bitmap to convert.
	 * @return The photo's Base64 string representation.
	 */
	public static String convertToBase64(Bitmap photo) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * Converts a Base64 string representation of an image back to a BitMap.
	 * 
	 * Source: http://stackoverflow.com/a/9768973/14064 (2015-04-03)
	 * 
	 * @param encodedString
	 *            The Base64 string representation of a Bitmap.
	 * @return The Bitmap.
	 */
	public static Bitmap convertFromBase64(String encodedString) {
		byte[] decodedByte = Base64.decode(encodedString, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
}
