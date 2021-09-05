/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit test for the {@link AbstractParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class AbstractParserTest {


	@Test
	void testGetHyperlinkListener() {

		MockParser parser = new MockParser();
		Assertions.assertNull(parser.getHyperlinkListener());

		ExtendedHyperlinkListener ehl = new MockExtendedHyperlinkListener();
		parser.setHyperlinkListener(ehl);
		Assertions.assertEquals(ehl, parser.getHyperlinkListener());

	}


	@Test
	void testGetImageBase_default() {
		MockParser parser = new MockParser();
		Assertions.assertNull(parser.getImageBase());
	}


	@Test
	void testIsEnabled() {
		MockParser parser = new MockParser();
		Assertions.assertTrue(parser.isEnabled());
		parser.setEnabled(false);
		Assertions.assertFalse(parser.isEnabled());
	}


	@Test
	void testSetEnabled() {
		MockParser parser = new MockParser();
		Assertions.assertTrue(parser.isEnabled());
		parser.setEnabled(false);
		Assertions.assertFalse(parser.isEnabled());
	}


	@Test
	void testSetHyperlinkListener() {

		MockParser parser = new MockParser();
		Assertions.assertNull(parser.getHyperlinkListener());

		ExtendedHyperlinkListener ehl = new MockExtendedHyperlinkListener();
		parser.setHyperlinkListener(ehl);
		Assertions.assertEquals(ehl, parser.getHyperlinkListener());

	}


}
