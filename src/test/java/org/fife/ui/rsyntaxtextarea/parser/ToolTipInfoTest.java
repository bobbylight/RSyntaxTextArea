/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for the {@link ToolTipInfo} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ToolTipInfoTest {

	private MockHyperlinkListener mhl;
	private URL imageBase;

	@Before
	public void setUp() throws MalformedURLException {
		mhl = new MockHyperlinkListener();
		imageBase = new URL("file:///localhost/images");
	}


	@Test
	public void testTwoArgConstructor() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assert.assertEquals("text", tti.getToolTipText());
		Assert.assertEquals(mhl, tti.getHyperlinkListener());
		Assert.assertNull(tti.getImageBase());
	}


	@Test
	public void testThreeArgConstructor() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl, imageBase);
		Assert.assertEquals("text", tti.getToolTipText());
		Assert.assertEquals(mhl, tti.getHyperlinkListener());
		Assert.assertEquals(imageBase, tti.getImageBase());
	}


	@Test
	public void testGetHyperlinkListener() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assert.assertEquals(mhl, tti.getHyperlinkListener());
	}


	@Test
	public void testGetImageBase() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl, imageBase);
		Assert.assertEquals(imageBase, tti.getImageBase());
	}


	@Test
	public void testGetToolTipText() {
		ToolTipInfo tti = new ToolTipInfo("text", mhl);
		Assert.assertEquals("text", tti.getToolTipText());
	}


	/**
	 * Mock listener class for unit testing.
	 */
	private static class MockHyperlinkListener implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			// Do nothing
		}

	}


}