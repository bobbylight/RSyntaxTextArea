/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for the {@link DocumentRange} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DocumentRangeTest {


	@Test
	public void testConstructor_HappyPath() {
		DocumentRange range = new DocumentRange(5, 8);
		Assert.assertEquals(5, range.getStartOffset());
		Assert.assertEquals(8, range.getEndOffset());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeStart() throws Exception {
		new DocumentRange(-1, 8);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeEnd() throws Exception {
		new DocumentRange(5, -2);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EndLessThanStart() throws Exception {
		new DocumentRange(9, 8);
	}


	@Test
	public void testCompareTo() {

		DocumentRange range = new DocumentRange(5, 8);

		Assert.assertTrue(range.compareTo(null) == 1);

		DocumentRange range2 = new DocumentRange(4, 8);
		Assert.assertTrue(range.compareTo(range2) > 0);

		range2 = new DocumentRange(5, 8);
		Assert.assertTrue(range.compareTo(range2) == 0);
		Assert.assertTrue(range.compareTo(range) == 0);

		range2 = new DocumentRange(6, 8);
		Assert.assertTrue(range.compareTo(range2) < 0);

		range2 = new DocumentRange(5, 7);
		Assert.assertTrue(range.compareTo(range2) > 0);

		range2 = new DocumentRange(5, 9);
		Assert.assertTrue(range.compareTo(range2) < 0);

	}


	@Test
	public void testEquals() {

		DocumentRange range = new DocumentRange(5, 8);

		Assert.assertFalse(range.equals(null));
		Assert.assertTrue(range.equals(range));
		Assert.assertTrue(range.equals(new DocumentRange(5, 8)));
		Assert.assertFalse(range.equals(new DocumentRange(4, 8)));
		Assert.assertFalse(range.equals("Hello world"));

	}


	@Test
	public void testGetEndOffset() {
		DocumentRange range = new DocumentRange(5, 8);
		Assert.assertEquals(8, range.getEndOffset());
		range.set(1,  2);
		Assert.assertEquals(2, range.getEndOffset());
	}


	@Test
	public void testGetStartOffset() {
		DocumentRange range = new DocumentRange(5, 8);
		Assert.assertEquals(5, range.getStartOffset());
		range.set(9,  12);
		Assert.assertEquals(9, range.getStartOffset());
	}


	@Test
	public void testHashCode() {
		// NOTE: Not a good test, assumes formula of hash code!
		DocumentRange range = new DocumentRange(5, 8);
		Assert.assertEquals(5+8, range.hashCode());
	}


	@Test
	public void testIsZeroLength() {
		Assert.assertFalse(new DocumentRange(1, 3).isZeroLength());
		Assert.assertFalse(new DocumentRange(2, 3).isZeroLength());
		Assert.assertTrue(new DocumentRange(3, 3).isZeroLength());
	}


	@Test
	public void testSet_HappyPath() {
		DocumentRange range = new DocumentRange(1, 1);
		range.set(5, 8);
		Assert.assertEquals(5, range.getStartOffset());
		Assert.assertEquals(8, range.getEndOffset());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSet_NegativeStart() throws Exception {
		new DocumentRange(1, 1).set(-1, 8);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSet_NegativeEnd() throws Exception {
		new DocumentRange(1, 1).set(5, -2);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSet_EndLessThanStart() throws Exception {
		new DocumentRange(1, 1).set(9, 8);
	}


	@Test
	public void testToString() {
		DocumentRange range = new DocumentRange(5, 8);
		Assert.assertEquals("[DocumentRange: 5-8]", range.toString());
	}


	@Test
	public void testTranslate() {
		DocumentRange range = new DocumentRange(5, 8);
		range.translate(6);
		Assert.assertEquals(11, range.getStartOffset());
		Assert.assertEquals(14, range.getEndOffset());
	}


}