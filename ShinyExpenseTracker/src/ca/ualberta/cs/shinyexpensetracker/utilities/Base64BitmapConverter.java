package ca.ualberta.cs.shinyexpensetracker.utilities;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Base64BitmapConverter {
	public static final int TARGET_IMAGE_SIZE = 42000;

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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int srcWidth, srcHeight;
		int dstWidth, dstHeight;
		double reduction;
		int outputSize = photo.getByteCount();
		
		srcWidth = photo.getWidth();
		srcHeight = photo.getHeight();
		
		Log.d("compression", "rawFileSize = " + outputSize);
		
		// First pass
		photo.compress(CompressFormat.JPEG, 100, stream);
		Log.d("compression", "firstPassSize = " + stream.size());

		// Second pass
		outputSize = stream.size();
		if (outputSize > TARGET_IMAGE_SIZE) {
			stream.reset();
			// Try a simple JPEG compression
			photo.compress(CompressFormat.JPEG, 90, stream);
			Log.d("compression", "secondPassSize = " + stream.size());
		}
		
		// Third pass
		outputSize = stream.size();
		if (outputSize > TARGET_IMAGE_SIZE) {
			stream.reset();
			// Try to exploit inverse square law for file compression
			reduction = Math.sqrt((float) TARGET_IMAGE_SIZE / outputSize);
			dstWidth = (int) (srcWidth*reduction);
			dstHeight = (int) (srcHeight*reduction);
			photo = Bitmap.createScaledBitmap(photo, dstWidth, dstHeight, false);
			photo.compress(CompressFormat.JPEG, 90, stream);
			Log.d("compression", "thirdPassSize = " + stream.size());
		}

		// Final pass: continuously reduce quality and size
		outputSize = stream.size();
		
		if (outputSize > TARGET_IMAGE_SIZE) {
			// Try to exploit inverse square law for file compression
			reduction = Math.sqrt((float) TARGET_IMAGE_SIZE / outputSize);
			dstWidth = (int) (srcWidth*reduction);
			dstHeight = (int) (srcHeight*reduction);
			photo = Bitmap.createScaledBitmap(photo, dstWidth, dstHeight, false);
			
			int quality = 90;
			
			do {
				// Continue reducing quality until the requirement is met
				quality -= 5;
				
				stream.reset();
				photo.compress(CompressFormat.JPEG, quality, stream);
				outputSize = stream.size();

				// Get ready for next iteration
				dstWidth = (int) (dstWidth*0.95);
				dstHeight = (int) (dstHeight*.95);
				
				photo = Bitmap.createScaledBitmap(photo, dstWidth, dstHeight, false);
				Log.d("compression", "finalPassSize = " + stream.size());
				
			} while (outputSize > TARGET_IMAGE_SIZE);
		}
		
		Log.d("compression", "Final Result = " + stream.size());
		
		byte[] b = stream.toByteArray();
		
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
