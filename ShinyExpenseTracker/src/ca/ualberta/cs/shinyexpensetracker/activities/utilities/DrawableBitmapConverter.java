package ca.ualberta.cs.shinyexpensetracker.activities.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Handles the conversion of an image saved in a Drawable into a Bitmap.
 */
public class DrawableBitmapConverter {
	/**
	 * Convert a Drawable object to Bitmap, scale down if needed
	 * 
	 * @param drawable
	 *            The Drawable with an image set.
	 * @param widthPixels
	 *            The width of the image.
	 * @param heightPixels
	 *            The height of the image.
	 * @return The corresponding Bitmap.
	 */
	public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
		Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutableBitmap);
		drawable.setBounds(0, 0, widthPixels, heightPixels);
		drawable.draw(canvas);
		int sizeInBytes = mutableBitmap.getByteCount();
		if (sizeInBytes > 65536) {
			mutableBitmap = scaleImage(mutableBitmap, widthPixels, heightPixels);
		}
		return mutableBitmap;
	}

	/**
	 * Scales image down to 64K, if needed.
	 * 
	 * @param mutableBitmap
	 *            The Bitmap to scale down.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return The Bitmap, scaled down if needed.
	 */
	private static Bitmap scaleImage(Bitmap mutableBitmap, int width, int height) {
		int sizeInBytes = mutableBitmap.getByteCount();
		if (sizeInBytes <= 65536) {
			return mutableBitmap;
		}
		double ratio = width / height;
		final int MAX_PIXELS = 16384;
		int newWidth = (int) Math.floor(Math.sqrt(MAX_PIXELS * ratio));
		Log.e("newWidth", String.valueOf(newWidth));
		int newHeight = (int) Math.floor(Math.sqrt(MAX_PIXELS / ratio));
		Log.e("newHeight", String.valueOf(newHeight));
		mutableBitmap = Bitmap.createScaledBitmap(mutableBitmap, newWidth, newHeight, false);
		return mutableBitmap;
	}
}