/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

/**
 * Unit tests for the {@link Style} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class StyleTest {


	@Test
	void testConstructor_zeroArg() {
		Style style = new Style();
		Assertions.assertEquals(Style.DEFAULT_FOREGROUND, style.foreground);
		Assertions.assertEquals(Style.DEFAULT_BACKGROUND, style.background);
		Assertions.assertEquals(Style.DEFAULT_FONT, style.font);
		Assertions.assertFalse(style.underline);
	}


	@Test
	void testConstructor_oneArg() {
		Style style = new Style(Color.RED);
		Assertions.assertEquals(Color.RED, style.foreground);
		Assertions.assertEquals(Style.DEFAULT_BACKGROUND, style.background);
		Assertions.assertEquals(Style.DEFAULT_FONT, style.font);
		Assertions.assertFalse(style.underline);
	}


	@Test
	void testConstructor_twoArg() {
		Style style = new Style(Color.RED, Color.BLUE);
		Assertions.assertEquals(Color.RED, style.foreground);
		Assertions.assertEquals(Color.BLUE, style.background);
		Assertions.assertEquals(Style.DEFAULT_FONT, style.font);
		Assertions.assertFalse(style.underline);
	}


	@Test
	void testConstructor_threeArg() {
		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font);
		Assertions.assertEquals(Color.RED, style.foreground);
		Assertions.assertEquals(Color.BLUE, style.background);
		Assertions.assertEquals(font, style.font);
		Assertions.assertFalse(style.underline);
	}


	@Test
	void testConstructor_fourArg() {
		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Assertions.assertEquals(Color.RED, style.foreground);
		Assertions.assertEquals(Color.BLUE, style.background);
		Assertions.assertEquals(font, style.font);
		Assertions.assertTrue(style.underline);
	}


	@Test
	void testClone() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);

		Assertions.assertEquals(style, style.clone());
	}


	@Test
	void testEquals_areEqual() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Style style2 = new Style(Color.RED, Color.BLUE, font, true);

		//noinspection SimplifiableJUnitAssertion
		Assertions.assertTrue(style.equals(style2));
	}


	@Test
	void testEquals_notEqual() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Style style2 = new Style(Color.RED, Color.BLUE, font, false);

		//noinspection SimplifiableJUnitAssertion
		Assertions.assertFalse(style.equals(style2));
	}


	@Test
	void testHashCode() {
		Assertions.assertNotEquals(0, new Style(Color.RED, Color.BLUE).hashCode());
	}


	@Test
	void testToString() {
		String expected = "[Style: foreground: java.awt.Color[r=0,g=0,b=0], " +
			"background: null, underline: false, font: null]";
		Assertions.assertEquals(expected, new Style().toString());
	}
}
