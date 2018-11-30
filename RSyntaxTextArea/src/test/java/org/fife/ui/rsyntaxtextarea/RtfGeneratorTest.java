/*
 * 10/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;


/**
 * Unit tests for the {@link RtfGenerator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RtfGeneratorTest {

	@Test
	public void testNon7BitAscii() {
		RtfGenerator generator = new RtfGenerator(Color.white);
		generator.appendToDoc("\u6c49", null, null, null);
		String rtf = generator.getRtf();
		int firstNewline = rtf.indexOf('\n');
		int secondNewline = rtf.indexOf('\n', firstNewline + 1);
		// "\cb1" is the background definition, "\u27721" is the code point
		Assert.assertTrue(rtf.substring(secondNewline + 1).startsWith("\\cb1 \\u27721"));
	}

}
