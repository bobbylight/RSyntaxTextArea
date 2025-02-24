/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife;

import org.fife.io.UnicodeWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Utility methods for unit tests.
 */
public final class TestUtil {

	private static final String DEFAULT_TEMP_FILE_EXTENSION = ".tmp";


	/**
	 * Private constructor to prevent instantiation.
	 */
	private TestUtil() {
		// Do nothing
	}


	/**
	 * Creates a temporary file with extension {@code ".tmp"}.
	 *
	 * @param content The content of the file.
	 * @return The file.
	 * @throws IOException If an IO error occurs.
	 * @see #createFile(String, String)
	 * @see #createFile(String, String, File)
	 */
	public static File createFile(String content) throws IOException {
		return createFile(DEFAULT_TEMP_FILE_EXTENSION, content);
	}


	/**
	 * Creates a temporary file.
	 *
	 * @param fileExtension The extension for the file.
	 * @param content The content of the file.
	 * @return The file.
	 * @throws IOException If an IO error occurs.
	 * @see #createFile(String)
	 * @see #createFile(String, String, File)
	 */
	public static File createFile(String fileExtension, String content) throws IOException {
		File file = File.createTempFile("unitTest", fileExtension);
		file.deleteOnExit();
		UnicodeWriter uw = new UnicodeWriter(file, "UTF-8");
		try (PrintWriter w = new PrintWriter(uw)) {
			w.println(content);
		}
		return file;
	}


	/**
	 * Creates a temporary file.
	 *
	 * @param fileExtension The extension for the file.
	 * @param content The content of the file.
	 * @param directory The parent directory for the file.
	 * @return The file.
	 * @throws IOException If an IO error occurs.
	 * @see #createFile(String)
	 * @see #createFile(String, String)
	 */
	public static File createFile(String fileExtension, String content, File directory)
			throws IOException {
		File file = File.createTempFile("unitTest", fileExtension, directory);
		file.deleteOnExit();
		UnicodeWriter uw = new UnicodeWriter(file, "UTF-8");
		try (PrintWriter w = new PrintWriter(uw)) {
			w.println(content);
		}
		return file;
	}
}
