/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.BadLocationException;

/**
 * Unit tests for the {@code RDocumentCharSequence} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RDocumentCharSequenceTest {

	private RDocument doc;

	private static final String CONTENT = "This is some test content";


	@BeforeEach
	void setUp() throws BadLocationException {
		doc = new RDocument();
		doc.insertString(0, CONTENT, null);
	}


	@Test
	void testConstructor_twoArg() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertEquals(CONTENT.substring(2), cs.toString());
	}


	@Test
	void testConstructor_threeArg() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2, 6);
		Assertions.assertEquals(CONTENT.substring(2, 6), cs.toString());
	}


	@Test
	void testCharAt_endAtEndOfDocument() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		for (int i = 2; i < CONTENT.length(); i++) {
			Assertions.assertEquals(CONTENT.charAt(i), cs.charAt(i - 2));
		}
	}


	@Test
	void testCharAt_endBeforeEndOfDocument() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2, 11);
		for (int i = 2; i < 11; i++) {
			Assertions.assertEquals(CONTENT.charAt(i), cs.charAt(i - 2));
		}
	}


	@Test
	void testCharAt_error_negativeIndex() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.charAt(-1));
	}


	@Test
	void testCharAt_error_indexTooLarge() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.charAt(CONTENT.length()));
	}


	@Test
	void testLength() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2, 11);
		Assertions.assertEquals(9, cs.length());
	}


	@Test
	void testSubSequence_happyPath() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		CharSequence subSequence = cs.subSequence(3, 6);
		Assertions.assertEquals(CONTENT.substring(5, 8), subSequence.toString());
	}


	@Test
	void testSubSequence_error_startLessThanZero() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.subSequence(-1, 4));
	}


	@Test
	void testSubSequence_error_endLessThanZero() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.subSequence(1, -3));
	}


	@Test
	void testSubSequence_error_endGreaterThaanLength() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.subSequence(1, 9999));
	}


	@Test
	void testSubSequence_error_startGreaterThanEnd() {
		RDocumentCharSequence cs = new RDocumentCharSequence(doc, 2);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> cs.subSequence(5, 4));
	}
}
