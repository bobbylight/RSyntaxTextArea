/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.datatransfer.DataFlavor;

/**
 * Unit tests for the {@link StyledTextTransferable} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class StyledTextTransferableTest {


	@Test
	void testGetDataFlavors() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("", "foo", rtfBytes);
		Assertions.assertEquals(4, t.getTransferDataFlavors().length);
	}


	@Test
	void testIsDataFlavorSupported_true() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("", "foo", rtfBytes);
		Assertions.assertTrue(t.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor));
		Assertions.assertTrue(t.isDataFlavorSupported(new DataFlavor("text/rtf", "RTF")));
		Assertions.assertTrue(t.isDataFlavorSupported(DataFlavor.stringFlavor));
	}


	@Test
	void testIsDataFlavorSupported_false() {
		byte[] rtfBytes = {};
		StyledTextTransferable t = new StyledTextTransferable("", "foo", rtfBytes);
		Assertions.assertFalse(t.isDataFlavorSupported(DataFlavor.imageFlavor));
	}
}
