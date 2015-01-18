/*
 * 01/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link RDocument}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RDocumentTest {


	@Test
	public void testCharAt_Simple() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);

		for (int i=0; i<text.length(); i++) {
			Assert.assertEquals(text.charAt(i), doc.charAt(i));
		}
	}


	@Test
	public void testCharAt_ModifiedGapChange() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);
		doc.insertString(6, "there ", null);

		String expected = "Hello there world";
		for (int i=0; i<expected.length(); i++) {
			Assert.assertEquals(expected.charAt(i), doc.charAt(i));
		}
	}


	@Test(expected = BadLocationException.class)
	public void testCharAt_Invalid_NegativeOffset() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);

		Assert.assertEquals('a', doc.charAt(-1));

	}


	@Test(expected = BadLocationException.class)
	public void testCharAt_Invalid_OffsetTooLarge() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);

		Assert.assertEquals('a', doc.charAt(text.length()+1));

	}


}