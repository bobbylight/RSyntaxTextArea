/*
 * 10/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RtfGenerator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RtfGeneratorTest {

	@Test
	public void testNon7BitAscii() {
		RtfGenerator generator = new RtfGenerator();
		generator.appendToDoc("\u6c49", null, null, null);
		String rtf = generator.getRtf();
		int firstNewline = rtf.indexOf('\n');
		int secondNewline = rtf.indexOf('\n', firstNewline + 1);
		Assert.assertTrue(rtf.substring(secondNewline + 1).startsWith("\\u27721"));
	}

}
