/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for the {@link AbstractParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class AbstractParserTest {


	@Test
	public void testGetHyperlinkListener() {

		MockParser parser = new MockParser();
		Assert.assertNull(parser.getHyperlinkListener());

		ExtendedHyperlinkListener ehl = new MockExtendedHyperlinkListener();
		parser.setHyperlinkListener(ehl);
		Assert.assertEquals(ehl, parser.getHyperlinkListener());

	}


	@Test
	public void testGetImageBase_default() {
		MockParser parser = new MockParser();
		Assert.assertNull(parser.getImageBase());
	}


	@Test
	public void testIsEnabled() {
		MockParser parser = new MockParser();
		Assert.assertTrue(parser.isEnabled());
		parser.setEnabled(false);
		Assert.assertFalse(parser.isEnabled());
	}


	@Test
	public void testSetEnabled() {
		MockParser parser = new MockParser();
		Assert.assertTrue(parser.isEnabled());
		parser.setEnabled(false);
		Assert.assertFalse(parser.isEnabled());
	}


	@Test
	public void testSetHyperlinkListener() {

		MockParser parser = new MockParser();
		Assert.assertNull(parser.getHyperlinkListener());

		ExtendedHyperlinkListener ehl = new MockExtendedHyperlinkListener();
		parser.setHyperlinkListener(ehl);
		Assert.assertEquals(ehl, parser.getHyperlinkListener());

	}


}