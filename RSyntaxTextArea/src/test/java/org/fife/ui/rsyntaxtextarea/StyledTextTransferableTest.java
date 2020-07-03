/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.Assert;
import org.junit.Test;

import java.awt.datatransfer.DataFlavor;

/**
 * Unit tests for the {@link StyledTextTransferable} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class StyledTextTransferableTest {


	@Test
	public void testGetDataFlavors() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("foo", rtfBytes);
		Assert.assertEquals(4, t.getTransferDataFlavors().length);
	}


	@Test
	public void testIsDataFlavorSupported_true() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("foo", rtfBytes);
		Assert.assertTrue(t.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor));
		Assert.assertTrue(t.isDataFlavorSupported(new DataFlavor("text/rtf", "RTF")));
		Assert.assertTrue(t.isDataFlavorSupported(DataFlavor.stringFlavor));
	}


	@Test
	public void testIsDataFlavorSupported_false() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("foo", rtfBytes);
		Assert.assertFalse(t.isDataFlavorSupported(DataFlavor.imageFlavor));
	}
}
