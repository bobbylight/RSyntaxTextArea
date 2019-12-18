/*
 * 10/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the {@link RtfGenerator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RtfGeneratorTest {

	/**
	 * RTF generated from the simple text below, without the header that may have
	 * stuff that differs across OS's such as monospaced font names.
	 */
	private static final String SIMPLE_RTF_MINUS_HEADER =
		"{\\colortbl ;\\red255\\green0\\blue0;\\red255\\green255\\blue255;}\n" +
		"\\cb2 This line is the default color \\u12459 \\u12459 \n" +
		"\\cf1 \\tab This line is red\n" +
		"\\cf0 This line is the default color\\line}";

	// Note the two Japanese characters aren't separated by a space, space denotes end of control char
	private static final List<String> SIMPLE_TEXT = Arrays.asList(
		"This line is the default color \u30ab\u30ab\n",
		"\tThis line is red\n",
		"This line is the default color"
	);

	@Test
	public void testHappyPath() {

		RtfGenerator generator = new RtfGenerator(Color.white);
		generator.appendToDoc(SIMPLE_TEXT.get(0), null, null, null);
		generator.appendToDoc(SIMPLE_TEXT.get(1), null, Color.red, null);
		generator.appendToDoc(SIMPLE_TEXT.get(2), null, null, null);

		// We can't do an exact string comparison due to differing default fonts on different OS's
		Assert.assertTrue(generator.getRtf().contains(SIMPLE_RTF_MINUS_HEADER));
	}

	@Test
	public void testNon7BitAscii() {
		RtfGenerator generator = new RtfGenerator(Color.white);
		generator.appendToDoc("\u6c49", null, null, null);
		String rtf = generator.getRtf();
		int firstNewline = rtf.indexOf('\n');
		int secondNewline = rtf.indexOf('\n', firstNewline + 1);
		// "\cb1" is the background definition, "\u27721" is the code point
		Assert.assertTrue(rtf.substring(secondNewline + 1).startsWith("\\cb1 \\u27721 "));
	}

}
