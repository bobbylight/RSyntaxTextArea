/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Unit tests for the {@link HtmlUtil} class.
 */
class HtmlUtilTest {

	@Test
	void testEscapeForHtml_nullInput() {
		Assertions.assertNull(HtmlUtil.escapeForHtml(null, "<br>", true));
	}


	@Test
	void testEscapeForHtml_nullNewlineReplacement() {
		Assertions.assertEquals("", HtmlUtil.escapeForHtml("\n", null, true));
	}


	@Test
	void testEscapeForHtml_nonNullNewlineReplacement() {
		Assertions.assertEquals("<br>", HtmlUtil.escapeForHtml("\n", "<br>", true));
	}


	@Test
	void testEscapeForHtml_happyPath() {
		Assertions.assertEquals("hello", HtmlUtil.escapeForHtml("hello", "<br>", true));
		Assertions.assertEquals("2 &lt; 4", HtmlUtil.escapeForHtml("2 < 4", "<br>", true));
	}


	@Test
	void testEscapeForHtml_problemChars() {
		Assertions.assertEquals(" <br>&amp;    &lt;&gt;&#39;&#34;&#47;",
			HtmlUtil.escapeForHtml(" \n&\t<>'\"/", "<br>", true));
	}


	@Test
	void testEscapeForHtml_multipleSpaces_inPreBlock() {
		Assertions.assertEquals("   ",
			HtmlUtil.escapeForHtml("   ", "<br>", true));
	}


	@Test
	void testEscapeForHtml_multipleSpaces_notInPreBlock() {
		Assertions.assertEquals(" &nbsp;&nbsp;",
			HtmlUtil.escapeForHtml("   ", "<br>", false));
	}

	@Test
	void testEscapeForHtml_tab_inPreBlock() {

		String code = "<foo>\twidget\t</foo>";

		String expected = "&lt;foo&gt;    widget    &lt;&#47;foo&gt;";
		Assertions.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", true));
	}

	@Test
	void testEscapeForHtml_tab_notInPreBlock() {

		String code = "<foo>\twidget\t</foo>";

		String expected = "&lt;foo&gt;&nbsp;&nbsp;&nbsp;&nbsp;widget&nbsp;&nbsp;&nbsp;&nbsp;&lt;&#47;foo&gt;";
		Assertions.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", false));
	}

	@Test
	void testGetHexString_null() {
		Assertions.assertNull(HtmlUtil.getHexString(null));
	}

	@Test
	void testGetHexString_allGreaterThan16() {
		Assertions.assertEquals("#ffffff", HtmlUtil.getHexString(Color.white));
	}

	@Test
	void testGetHexString_allLessThan16() {
		Assertions.assertEquals("#000000", HtmlUtil.getHexString(Color.black));
	}

	@Test
	void testGetTextAsHtml_happyPath() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("package foo;\npublic class Foobar {}");

		// We can't do an exact string comparison due to differing default fonts on different OS's
		String expectedRegex = "<pre style=\"font-family: '[\\w ]+', courier; background: #ffffff\">" +
			"<span style=\"color: #000000;\">age</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">foo</span><span style=\"color: #000000;\">;</span><br>" +
			"<span style=\"color: #000000;\">public</span>" +
			"<span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">class</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">Foobar</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">\\{</span><span style=\"color: #000000;\">}</span></pre>";

		String actual = HtmlUtil.getTextAsHtml(textArea, 4, textArea.getDocument().getLength());
		Assertions.assertTrue(actual.matches(expectedRegex), "Expected content not found in: " + actual);
	}

	@Test
	void testGetTextAsHtml_backgroundImage() {
		RSyntaxTextArea textArea = new RSyntaxTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("package foo;\npublic class Foobar {}");

		Image image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		textArea.setBackgroundImage(image);
		// We can't do an exact string comparison due to differing default fonts on different OS's
		String expectedRegex = "<pre style=\"font-family: '[\\w ]+', courier;\">.*" +
			"<span style=\"color: #000000;\">ag</span></pre>";

		String actual = HtmlUtil.getTextAsHtml(textArea, 4, 6);
		Assertions.assertTrue(actual.matches(expectedRegex));
	}
}
