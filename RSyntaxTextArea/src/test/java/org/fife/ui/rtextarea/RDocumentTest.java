/*
 * 01/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for {@link RDocument}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RDocumentTest {


	@Test
	void testCharAt_Simple() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);

		for (int i=0; i<text.length(); i++) {
			Assertions.assertEquals(text.charAt(i), doc.charAt(i));
		}
	}


	@Test
	void testCharAt_ModifiedGapChange() throws Exception {

		String text = "Hello world";

		RDocument doc = new RDocument();
		doc.insertString(0, text, null);
		doc.insertString(6, "there ", null);

		String expected = "Hello there world";
		for (int i=0; i<expected.length(); i++) {
			Assertions.assertEquals(expected.charAt(i), doc.charAt(i));
		}
	}


	@Test
	void testCharAt_Invalid_NegativeOffset() {

		Assertions.assertThrows(BadLocationException.class, () -> {

			String text = "Hello world";

			RDocument doc = new RDocument();
			doc.insertString(0, text, null);

			Assertions.assertEquals('a', doc.charAt(-1));
		});
	}


	@Test
	void testCharAt_Invalid_OffsetTooLarge() {

		Assertions.assertThrows(BadLocationException.class, () -> {

			String text = "Hello world";

			RDocument doc = new RDocument();
			doc.insertString(0, text, null);

			Assertions.assertEquals('a', doc.charAt(text.length() + 1));
		});
	}


}
