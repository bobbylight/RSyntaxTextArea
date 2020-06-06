/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Unit tests for the {@code UnicodeWriter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class UnicodeWriterTest {

	private static final String CONTENT = "Hello world";

	private static File createTempFile() throws IOException {
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
		return file;
	}

	private static String getFileEncoding(File file) throws IOException {
		try (UnicodeReader r = new UnicodeReader(file)) {
			return r.getEncoding();
		}
	}

	@Test
	public void testConstructor_stringFileNameAndEncoding_utf8() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-8")) {
			w.append(CONTENT);
		}
		Assert.assertEquals("UTF-8", getFileEncoding(file));
	}

	@Test
	public void testConstructor_stringFileNameAndEncoding_utf16be() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16BE")) {
			w.append(CONTENT);
		}
		Assert.assertEquals("UTF-16BE", getFileEncoding(file));
	}

	@Test
	public void testConstructor_stringFileNameAndEncoding_utf16le() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16LE")) {
			w.append(CONTENT);
		}
		Assert.assertEquals("UTF-16LE", getFileEncoding(file));
	}

	@Test
	public void testConstructor_stringFileNameAndEncoding_utf32be() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-32BE")) {
			w.append(CONTENT);
		}
		Assert.assertEquals("UTF-32BE", getFileEncoding(file));
	}

	@Test
	public void testConstructor_stringFileNameAndEncoding_utf32le() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-32LE")) {
			w.append(CONTENT);
		}
		Assert.assertEquals("UTF-32LE", getFileEncoding(file));
	}

	@Test
	public void testGetEncoding() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-32LE")) {
			w.append(CONTENT);
			Assert.assertEquals("UTF-32LE", w.getEncoding());
		}
	}

	@Test
	public void testGetSetWriteUtf8BOM() {
		UnicodeWriter.setWriteUtf8BOM(true);
		Assert.assertTrue(UnicodeWriter.getWriteUtf8BOM());
		UnicodeWriter.setWriteUtf8BOM(false);
		Assert.assertFalse(UnicodeWriter.getWriteUtf8BOM());
	}
}
