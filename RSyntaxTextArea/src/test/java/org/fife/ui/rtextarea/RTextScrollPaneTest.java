/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

/**
 * Unit tests for the {@code RTextScrollPane} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextScrollPaneTest {


	@Test
	void testConstructor_zeroArg() {
		RTextScrollPane sp = new RTextScrollPane();
		Assertions.assertNull(sp.getTextArea());
		// Line numbers will be false if no text area is passed in
		Assertions.assertFalse(sp.getLineNumbersEnabled());
	}


	@Test
	void testConstructor_oneArg_textArea() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertEquals(textArea, sp.getTextArea());
		Assertions.assertTrue(sp.getLineNumbersEnabled());
	}


	@Test
	void testConstructor_twoArg_textArea() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea, false);
		Assertions.assertEquals(textArea, sp.getTextArea());
		Assertions.assertFalse(sp.getLineNumbersEnabled());
	}


	@Test
	void testConstructor_threeArg() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea, false, Color.GREEN);
		Assertions.assertEquals(textArea, sp.getTextArea());
		Assertions.assertFalse(sp.getLineNumbersEnabled());
		Assertions.assertEquals(Color.GREEN, sp.getGutter().getLineNumberColor());
	}


	@Test
	void testGetSetLineNumbersEnabled() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertTrue(sp.getLineNumbersEnabled());
		sp.setLineNumbersEnabled(false);
		Assertions.assertFalse(sp.getLineNumbersEnabled());
	}


	@Test
	void testGetTextAreaa() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertEquals(textArea, sp.getTextArea());
	}


	@Test
	void testIsSetFoldIndicatorEnabled() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertFalse(sp.isFoldIndicatorEnabled());
		sp.setFoldIndicatorEnabled(true);
		Assertions.assertTrue(sp.isFoldIndicatorEnabled());
	}


	@Test
	void testIsSetIconRowHeaaderEnabled() {
		RTextArea textArea = new RTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertFalse(sp.isIconRowHeaderEnabled());
		sp.setIconRowHeaderEnabled(true);
		Assertions.assertTrue(sp.isIconRowHeaderEnabled());
	}


	@Test
	void testSetViewportView_newTextArea() {
		RTextScrollPane sp = new RTextScrollPane(new RTextArea());
		RTextArea textArea = new RTextArea(); // Second, new text area
		sp.setViewportView(textArea);
		Assertions.assertEquals(textArea, sp.getTextArea());
	}


	@Test
	void testSetViewportView_containsTextArea() {
		RTextScrollPane sp = new RTextScrollPane(new RTextArea());
		RTextArea textArea = new RTextArea(); // Second, new text area
		JPanel wrapper = new JPanel();
		wrapper.add(textArea);
		sp.setViewportView(wrapper);
		Assertions.assertEquals(textArea, sp.getTextArea());
	}


	@Test
	void testSetViewportView_error_noTextArea() {
		RTextScrollPane sp = new RTextScrollPane(new RTextArea());
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> sp.setViewportView(new JPanel()));
	}
}
