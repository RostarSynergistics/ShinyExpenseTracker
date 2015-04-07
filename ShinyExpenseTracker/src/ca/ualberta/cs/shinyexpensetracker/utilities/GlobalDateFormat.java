package ca.ualberta.cs.shinyexpensetracker.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

/**
 * Global date format singleton. This should be used instead
 * of creating new SimpleDateFormatters to keep the format
 * consistent across the app.
 * 
 * Example:
 *   GlobalDateFormat.toString(new Date());
 *   GlobalDateFormat.toDate("Mar 4, 2015");
 */
public final class GlobalDateFormat {
	private static final String YMD_FORMAT = "yyyy MM dd";
	public static final String DEFAULT_FORMAT = "MMM dd, yyyy";
	private static SimpleDateFormat dateFormatter;
	
	/**
	 * Sets the global format to the given template
	 * Warning: May cause problems with parsing.
	 * Locale defaults to Canada.
	 * 
	 * @param template The format to use across the app
	 */
	public static void setFormat(String template) {
		setFormat(template, Locale.CANADA);
	}
	
	/**
	 * Sets the format to a given template with a given locale
	 * @param template The format to use across the app
	 * @param locale The locale, usually the same as the app's
	 */
	public static void setFormat(String template, Locale locale) {
		dateFormatter = new SimpleDateFormat(template, locale);
	}
	
	/**
	 * Converts a string to a date using the global format.
	 * May throw ParseExceptions if the format changes.
	 *  
	 * @param dateString String to parse, matching the global format
	 * @return The parsed date
	 * @throws ParseException
	 */
	public static Date parse(String dateString) throws ParseException {
		return getFormatter().parse(dateString);
	}

	/**
	 * Convert a date object into a String.
	 * @param date The date to convert
	 * @return The formatted string
	 */
	public static String format(Date date) {
		return getFormatter().format(date);
	}
	
	/**
	 * Gets the current date formatter. If the global
	 * format changes, local instances are preserved.
	 * 
	 * @return The current date format in the system.
	 */
	public static SimpleDateFormat getFormatter() {
		if (dateFormatter == null) {
			setFormat(DEFAULT_FORMAT);
		}
		return dateFormatter;
	}

	/**
	 * Creates a date that doesn't have to be parsed
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date makeDate(int year, int month, int day) {
		try {
			return new SimpleDateFormat(YMD_FORMAT, Locale.CANADA)
						.parse("" + year + " " + month + " " + day);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.wtf("make date", "Received an unparsable date");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a date string that will be valid in the date formatter.
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String makeString(int year, int month, int day) {
		try {
			Date date = new SimpleDateFormat(YMD_FORMAT, Locale.CANADA)
						.parse("" + year + " " + month + " " + day);
			return format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.wtf("make date", "Received an unparsable date");
			throw new RuntimeException(e);
		}
	}
}
