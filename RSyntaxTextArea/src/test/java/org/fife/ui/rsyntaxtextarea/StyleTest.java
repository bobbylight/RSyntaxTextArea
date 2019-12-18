/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * Unit tests for the {@link Style} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class StyleTest {


	@Test
	public void testConstructor_zeroArg() {
		Style style = new Style();
		Assert.assertEquals(Style.DEFAULT_FOREGROUND, style.foreground);
		Assert.assertEquals(Style.DEFAULT_BACKGROUND, style.background);
		Assert.assertEquals(Style.DEFAULT_FONT, style.font);
		Assert.assertFalse(style.underline);
	}


	@Test
	public void testConstructor_oneArg() {
		Style style = new Style(Color.RED);
		Assert.assertEquals(Color.RED, style.foreground);
		Assert.assertEquals(Style.DEFAULT_BACKGROUND, style.background);
		Assert.assertEquals(Style.DEFAULT_FONT, style.font);
		Assert.assertFalse(style.underline);
	}


	@Test
	public void testConstructor_twoArg() {
		Style style = new Style(Color.RED, Color.BLUE);
		Assert.assertEquals(Color.RED, style.foreground);
		Assert.assertEquals(Color.BLUE, style.background);
		Assert.assertEquals(Style.DEFAULT_FONT, style.font);
		Assert.assertFalse(style.underline);
	}


	@Test
	public void testConstructor_threeArg() {
		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font);
		Assert.assertEquals(Color.RED, style.foreground);
		Assert.assertEquals(Color.BLUE, style.background);
		Assert.assertEquals(font, style.font);
		Assert.assertFalse(style.underline);
	}


	@Test
	public void testConstructor_fourArg() {
		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Assert.assertEquals(Color.RED, style.foreground);
		Assert.assertEquals(Color.BLUE, style.background);
		Assert.assertEquals(font, style.font);
		Assert.assertTrue(style.underline);
	}


	@Test
	public void testClone() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);

		Assert.assertEquals(style, style.clone());
	}


	@Test
	public void testEquals_areEqual() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Style style2 = new Style(Color.RED, Color.BLUE, font, true);

		//noinspection SimplifiableJUnitAssertion
		Assert.assertTrue(style.equals(style2));
	}


	@Test
	public void testEquals_notEqual() {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 10);
		Style style = new Style(Color.RED, Color.BLUE, font, true);
		Style style2 = new Style(Color.RED, Color.BLUE, font, false);

		//noinspection SimplifiableJUnitAssertion
		Assert.assertFalse(style.equals(style2));
	}


	@Test
	public void testHashCode() {
		Assert.assertNotEquals(0, new Style(Color.RED, Color.BLUE).hashCode());
	}


	@Test
	public void testToString() {
		String expected = "[Style: foreground: java.awt.Color[r=0,g=0,b=0], " +
			"background: null, underline: false, font: null]";
		Assert.assertEquals(expected, new Style().toString());
	}
}
