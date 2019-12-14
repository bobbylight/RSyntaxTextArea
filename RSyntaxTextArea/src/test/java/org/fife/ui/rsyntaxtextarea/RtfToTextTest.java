package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Unit tests for the {@link RtfToText} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RtfToTextTest {

	private static final String SIMPLE_RTF = "{\\rtf1\\ansi\\deff0\n" +
		"{\\colortbl;\\red0\\green0\\blue0;\\red255\\green0\\blue0;}\n" +
		"This line is the default color \\u12459 \\u12459\\line\n" +
		"\\cf2\n" +
		"\\tab This line is red\\line\n" +
		"\\cf1\n" +
		"This line is the default color\n" +
		"}";

	// Note the two Japanese characters aren't separated by a space, space denotes end of control char
	private static final String SIMPLE_TEXT = "This line is the default color \u30ab\u30ab\n" +
		"\tThis line is red\n" +
		"This line is the default color";

	@Test
	public void testGetPlainText_byteArray() throws IOException {
		Assert.assertEquals(SIMPLE_TEXT, RtfToText.getPlainText(SIMPLE_RTF.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testGetPlainText_file() throws IOException {

		File file = File.createTempFile("rstaTest", ".tmp");
		try (PrintWriter w = new PrintWriter(file)) {
			w.print(SIMPLE_RTF);
		}
		file.deleteOnExit();

		Assert.assertEquals(SIMPLE_TEXT, RtfToText.getPlainText(file));
	}

	@Test
	public void testGetPlainText_inputStream() throws IOException {
		try (BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(
			SIMPLE_RTF.getBytes(StandardCharsets.UTF_8)))) {
			Assert.assertEquals(SIMPLE_TEXT, RtfToText.getPlainText(SIMPLE_RTF));
		}
	}

	@Test
	public void testGetPlainText_string() throws IOException {
		Assert.assertEquals(SIMPLE_TEXT, RtfToText.getPlainText(SIMPLE_RTF));
	}
}
