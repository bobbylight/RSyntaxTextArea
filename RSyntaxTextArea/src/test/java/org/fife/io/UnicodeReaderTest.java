/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Unit tests for the {@code UnicodeReader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class UnicodeReaderTest {

	private static final String CONTENT = "Hello world";
	private static boolean origWriteUtf8Bom;

	private static File createTempFile(Charset charset)	throws IOException {
		return createTempFile(charset.name());
	}

	private static File createTempFile(String charset) throws IOException {

		// Force a BOM to be written for UTF-8 files
		UnicodeWriter.setWriteUtf8BOM(true);

		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();

		UnicodeWriter uw = new UnicodeWriter(file, charset);
		try (PrintWriter w = new PrintWriter(uw)) {
			w.println(CONTENT);
		}
		return file;
	}

	private static File createTempFileWithoutBOM() throws IOException {

		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();

		try (FileOutputStream fs = new FileOutputStream(file)) {
			fs.write(CONTENT.getBytes(StandardCharsets.UTF_8));
		}
		return file;
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
	void testConstructor_file_utf8() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_8);

		try (UnicodeReader r = new UnicodeReader(file)) {
			// Windows returns "UTF-8", Linux returns "UTF8"
			String actualEncoding = r.getEncoding().replace("-", "");
			Assertions.assertEquals("UTF8", actualEncoding);
		}
	}

	@Test
	void testConstructor_file_utf16be() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_16BE);

		try (UnicodeReader r = new UnicodeReader(file)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-16BE", actualEncoding);
		}
	}

	@Test
	void testConstructor_file_utf16le() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_16LE);

		try (UnicodeReader r = new UnicodeReader(file)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-16LE", actualEncoding);
		}
	}

	@Test
	void testConstructor_file_utf32be() throws IOException {

		File file = createTempFile("UTF-32BE");

		try (UnicodeReader r = new UnicodeReader(file)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-32BE", actualEncoding);
		}
	}

	@Test
	void testConstructor_file_utf32le() throws IOException {

		File file = createTempFile("UTF-32LE");

		try (UnicodeReader r = new UnicodeReader(file)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-32LE", actualEncoding);
		}
	}

	@Test
	void testConstructor_fileWithDefaultEncoding_utf8() throws IOException {

		File file = createTempFileWithoutBOM();

		try (UnicodeReader r = new UnicodeReader(file, StandardCharsets.UTF_8.name())) {

			// The encoding specified in the constructor was used since the file
			// had no BOM
			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-8", actualEncoding);
		}
	}

	@Test
	void testConstructor_fileWithDefaultCharset_utf8() throws IOException {

		File file = createTempFileWithoutBOM();

		try (UnicodeReader r = new UnicodeReader(file, StandardCharsets.UTF_8)) {

			// The encoding specified in the constructor was used since the file
			// had no BOM
			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-8", actualEncoding);
		}
	}

	@Test
	void testConstructor_fileWithNoDefaultEncodingSpecified() throws IOException {

		File file = createTempFileWithoutBOM();

		try (UnicodeReader r = new UnicodeReader(file)) {

			// Different OS's have different default encodings.  Just ensure we
			// populated the value.
			Assertions.assertNotNull(r.getEncoding());
		}
	}

	@Test
	void testConstructor_stringFileName_utf8() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_8);
		String fileFullPath = file.getAbsolutePath();

		try (UnicodeReader r = new UnicodeReader(fileFullPath)) {
			// Windows returns "UTF-8", Linux returns "UTF8"
			String actualEncoding = r.getEncoding().replace("-", "");
			Assertions.assertEquals("UTF8", actualEncoding);
		}
	}

	@Test
	void testConstructor_stringFileName_utf16be() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_16BE);
		String fileFullPath = file.getAbsolutePath();

		try (UnicodeReader r = new UnicodeReader(fileFullPath)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-16BE", actualEncoding);
		}
	}

	@Test
	void testConstructor_stringFileName_utf16le() throws IOException {

		File file = createTempFile(StandardCharsets.UTF_16LE);
		String fileFullPath = file.getAbsolutePath();

		try (UnicodeReader r = new UnicodeReader(fileFullPath)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-16LE", actualEncoding);
		}
	}

	@Test
	void testConstructor_stringFileNameWithNoDefaultEncodingSpecified() throws IOException {

		File file = createTempFileWithoutBOM();

		try (UnicodeReader r = new UnicodeReader(file.getAbsolutePath())) {

			// Different OS's have different default encodings.  Just ensure we
			// populated the value.
			Assertions.assertNotNull(r.getEncoding());
		}
	}

	@Test
	void testConstructor_inputStreamWithCharsetSpecified() throws IOException {

		File file = createTempFileWithoutBOM();

		try (InputStream in = new FileInputStream(file);
			 UnicodeReader r = new UnicodeReader(in, StandardCharsets.UTF_8)) {

			String actualEncoding = r.getEncoding();
			Assertions.assertEquals("UTF-8", actualEncoding);
		}
	}
}
