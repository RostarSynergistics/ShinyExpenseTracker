package ca.ualberta.cs.shinyexpensetracker.persistance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;

/**
 * Handles saving and loading a string value from the contents of a specified
 * file.
 * 
 * Source: 	http://developer.android.com/training/basics/data-storage/files.html
 * (2015-03-15)
 * 			http://developer.android.com/guide/topics/data/data-storage.html#filesInternal (2015-03-15)
 */
public class FilePersistenceStrategy implements IPersistenceStrategy {
	private final Context context;
	private final String fileName;

	/**
	 * Constructor.
	 * 
	 * @param context The application's current context.
	 * @param fileName The file name to load from and save to.
	 */
	public FilePersistenceStrategy(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}

	@Override
	public void save(String value) throws IOException {
		FileOutputStream outputStream;

		try {
			outputStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outputStream.write(value.getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			/*
			 * openFileOutput(..., Context.MODE_PRIVATE) will always create a
			 * new file if one doesn't exist, so this kind of exception will
			 * never be thrown, so this catch only exists to stop Java
			 * from complaining.
			 */
		}
	}

	@Override
	public String load() throws IOException {
		try {
			// Source: http://stackoverflow.com/q/12157125/14064 (2015-03-15)
			FileInputStream stream = context.openFileInput(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuffer buffer = new StringBuffer();
		
			while(reader.ready()) {
				buffer.append(reader.readLine());
				buffer.append('\n');
			}

			return buffer.toString();
		} catch (FileNotFoundException e) {
			// If no file exists, then nothing's been saved before.
			return "";
		}
	}
}
