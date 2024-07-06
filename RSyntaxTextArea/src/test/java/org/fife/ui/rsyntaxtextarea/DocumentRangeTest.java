/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit test for the {@link DocumentRange} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DocumentRangeTest {


	@Test
	void testConstructor_HappyPath() {
		DocumentRange range = new DocumentRange(5, 8);
		Assertions.assertEquals(5, range.getStartOffset());
		Assertions.assertEquals(8, range.getEndOffset());
	}


	@Test
	void testConstructor_NegativeStart() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(-1, 8));
	}


	@Test
	void testConstructor_NegativeEnd() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(5, -2));
	}


	@Test
	void testConstructor_EndLessThanStart() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(9, 8));
	}


	@Test
	void testCompareTo() {

		DocumentRange range = new DocumentRange(5, 8);

		Assertions.assertEquals(1, range.compareTo(null));

		DocumentRange range2 = new DocumentRange(4, 8);
		Assertions.assertTrue(range.compareTo(range2) > 0);

		range2 = new DocumentRange(5, 8);
		Assertions.assertEquals(0, range.compareTo(range2));
		Assertions.assertEquals(0, range.compareTo(range));

		range2 = new DocumentRange(6, 8);
		Assertions.assertTrue(range.compareTo(range2) < 0);

		range2 = new DocumentRange(5, 7);
		Assertions.assertTrue(range.compareTo(range2) > 0);

		range2 = new DocumentRange(5, 9);
		Assertions.assertTrue(range.compareTo(range2) < 0);

	}


	@Test
	void testEquals() {

		DocumentRange range = new DocumentRange(5, 8);

		Assertions.assertFalse(range.equals(null));
		Assertions.assertTrue(range.equals(range));
		Assertions.assertTrue(range.equals(new DocumentRange(5, 8)));
		Assertions.assertFalse(range.equals(new DocumentRange(4, 8)));
		Assertions.assertFalse(range.equals("Hello world"));

	}


	@Test
	void testGetEndOffset() {
		DocumentRange range = new DocumentRange(5, 8);
		Assertions.assertEquals(8, range.getEndOffset());
		range.set(1,  2);
		Assertions.assertEquals(2, range.getEndOffset());
	}


	@Test
	void testGetStartOffset() {
		DocumentRange range = new DocumentRange(5, 8);
		Assertions.assertEquals(5, range.getStartOffset());
		range.set(9,  12);
		Assertions.assertEquals(9, range.getStartOffset());
	}


	@Test
	void testHashCode() {
		// NOTE: Not a good test, assumes formula of hash code!
		DocumentRange range = new DocumentRange(5, 8);
		Assertions.assertEquals(5+8, range.hashCode());
	}


	@Test
	void testIsZeroLength() {
		Assertions.assertFalse(new DocumentRange(1, 3).isZeroLength());
		Assertions.assertFalse(new DocumentRange(2, 3).isZeroLength());
		Assertions.assertTrue(new DocumentRange(3, 3).isZeroLength());
	}


	@Test
	void testSet_HappyPath() {
		DocumentRange range = new DocumentRange(1, 1);
		range.set(5, 8);
		Assertions.assertEquals(5, range.getStartOffset());
		Assertions.assertEquals(8, range.getEndOffset());
	}


	@Test
	void testSet_NegativeStart() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(1, 1).set(-1, 8));

	}


	@Test
	void testSet_NegativeEnd() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(1, 1).set(5, -2));
	}


	@Test
	void testSet_EndLessThanStart() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DocumentRange(1, 1).set(9, 8));
	}


	@Test
	void testToString() {
		DocumentRange range = new DocumentRange(5, 8);
		Assertions.assertEquals("[DocumentRange: 5-8]", range.toString());
	}


	@Test
	void testTranslate() {
		DocumentRange range = new DocumentRange(5, 8);
		range.translate(6);
		Assertions.assertEquals(11, range.getStartOffset());
		Assertions.assertEquals(14, range.getEndOffset());
	}


}
