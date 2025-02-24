/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Unit tests for the {@code UnicodeWriter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class UnicodeWriterTest {

	private static final String CONTENT = "Hello world";
	private static boolean origWriteUtf8Bom;

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

	@BeforeEach
	void setUp() {
		origWriteUtf8Bom = UnicodeWriter.getWriteUtf8BOM();
	}

	@AfterEach
	void tearDown() {
		UnicodeWriter.setWriteUtf8BOM(origWriteUtf8Bom);
	}

	@Test
	void testConstructor_stringFileNameAndCharset_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), StandardCharsets.UTF_8)) {
			w.append(CONTENT);
		}
		Assertions.assertEquals(StandardCharsets.UTF_8.name(), getFileEncoding(file));
	}

	@Test
	void testConstructor_stringFileNameAndEncoding_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-8")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-8", getFileEncoding(file));
	}

	@Test
	void testConstructor_stringFileNameAndEncoding_utf16be() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16BE")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-16BE", getFileEncoding(file));
	}

	@Test
	void testConstructor_stringFileNameAndEncoding_utf16le() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16LE")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-16LE", getFileEncoding(file));
	}

	@Test
	void testConstructor_stringFileNameAndEncoding_utf32be() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-32BE")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-32BE", getFileEncoding(file));
	}

	@Test
	void testConstructor_stringFileNameAndEncoding_utf32le() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-32LE")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-32LE", getFileEncoding(file));
	}

	@Test
	void testConstructor_fileAndCharset_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file, StandardCharsets.UTF_8)) {
			w.append(CONTENT);
		}
		Assertions.assertEquals(StandardCharsets.UTF_8.name(), getFileEncoding(file));
	}

	@Test
	void testConstructor_fileAndEncoding_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file, "UTF-8")) {
			w.append(CONTENT);
		}
		Assertions.assertEquals("UTF-8", getFileEncoding(file));
	}

	@Test
	void testConstructor_outputStreamAndCharset_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (UnicodeWriter w = new UnicodeWriter(baos, StandardCharsets.UTF_8)) {
			w.append(CONTENT);
		}
	}

	@Test
	void testConstructor_outputStreamAndEncoding_utf8WithBOM() throws IOException {

		// Force BOM so UnicodeReader can pick up on it
		UnicodeWriter.setWriteUtf8BOM(true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (UnicodeWriter w = new UnicodeWriter(baos, "UTF-8")) {
			w.append(CONTENT);
		}
	}

	@Test
	void testGetEncoding() throws IOException {

		File file = createTempFile();

		try (UnicodeWriter w = new UnicodeWriter(file, "UTF-32LE")) {
			w.append(CONTENT);
			Assertions.assertEquals("UTF-32LE", w.getEncoding());
		}
	}

	@Test
	void testGetSetWriteUtf8BOM() {
		UnicodeWriter.setWriteUtf8BOM(true);
		Assertions.assertTrue(UnicodeWriter.getWriteUtf8BOM());
		UnicodeWriter.setWriteUtf8BOM(false);
		Assertions.assertFalse(UnicodeWriter.getWriteUtf8BOM());
	}

	@Test
	void testWrite_charArray() throws IOException {

		File file = createTempFile();

		String testContent =
			"\u0030\u0030\u0034\u0065\u0032\u0064";
		char[] chars = testContent.toCharArray();

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16BE")) {
			w.write(chars, 0, chars.length);
		}

		// Read file back and verify contents
		try (BufferedReader r = new BufferedReader(new UnicodeReader(file, "UTF-16BE"))) {
			String actual = r.readLine();
			Assertions.assertEquals(actual, testContent);
		}
	}

	@Test
	void testWrite_singleInt() throws IOException {

		File file = createTempFile();

		int testChar = 'x';

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16BE")) {
			w.write(testChar);
		}

		// Read file back and verify contents
		try (BufferedReader r = new BufferedReader(new UnicodeReader(file, "UTF-16BE"))) {
			String actual = r.readLine();
			Assertions.assertEquals(1, actual.length());
			Assertions.assertEquals(testChar, actual.charAt(0));
		}
	}

	@Test
	void testWrite_string() throws IOException {

		File file = createTempFile();

		String testContent =
			"\u0030\u0030\u0034\u0065\u0032\u0064";

		try (UnicodeWriter w = new UnicodeWriter(file.getAbsolutePath(), "UTF-16BE")) {
			w.write(testContent, 0, testContent.length());
		}

		// Read file back and verify contents
		try (BufferedReader r = new BufferedReader(new UnicodeReader(file, "UTF-16BE"))) {
			String actual = r.readLine();
			Assertions.assertEquals(actual, testContent);
		}
	}
}
