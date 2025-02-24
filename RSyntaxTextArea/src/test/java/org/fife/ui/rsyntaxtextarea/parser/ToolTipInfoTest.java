/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link ToolTipInfo} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ToolTipInfoTest {

	private MockHyperlinkListener mhl;
	private URL imageBase;

	@BeforeEach
	void setUp() throws MalformedURLException {
		mhl = new MockHyperlinkListener();
		imageBase = new URL("file:///localhost/images");
	}


	@Test
	void testTwoArgConstructor() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assertions.assertEquals("text", tti.getToolTipText());
		Assertions.assertEquals(mhl, tti.getHyperlinkListener());
		Assertions.assertNull(tti.getImageBase());
	}


	@Test
	void testThreeArgConstructor() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl, imageBase);
		Assertions.assertEquals("text", tti.getToolTipText());
		Assertions.assertEquals(mhl, tti.getHyperlinkListener());
		Assertions.assertEquals(imageBase, tti.getImageBase());
	}


	@Test
	void testGetHyperlinkListener() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assertions.assertEquals(mhl, tti.getHyperlinkListener());
	}


	@Test
	void testGetImageBase() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl, imageBase);
		Assertions.assertEquals(imageBase, tti.getImageBase());
	}


	@Test
	void testGetToolTipText() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assertions.assertEquals("text", tti.getToolTipText());
	}


	/**
	 * Mock listener class for unit testing.
	 */
	private static final class MockHyperlinkListener implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			// Do nothing
		}

	}


}
