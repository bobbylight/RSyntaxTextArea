/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;


/**
 * Unit tests for the {@link HtmlUtil} class.
 */
public class HtmlUtilTest {

	@Test
	public void testEscapeForHtml_nullInput() {
		Assert.assertNull(HtmlUtil.escapeForHtml(null, "<br>", true));
	}


	@Test
	public void testEscapeForHtml_nullNewlineReplacement() {
		Assert.assertEquals("", HtmlUtil.escapeForHtml("\n", null, true));
	}


	@Test
	public void testEscapeForHtml_nonNullNewlineReplacement() {
		Assert.assertEquals("<br>", HtmlUtil.escapeForHtml("\n", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_happyPath() {
		Assert.assertEquals("hello", HtmlUtil.escapeForHtml("hello", "<br>", true));
		Assert.assertEquals("2 &lt; 4", HtmlUtil.escapeForHtml("2 < 4", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_problemChars() {
		Assert.assertEquals(" <br>&amp;    &lt;&gt;&#39;&#34;&#47;",
			HtmlUtil.escapeForHtml(" \n&\t<>'\"/", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_inPreBlock() {
		Assert.assertEquals("   ",
			HtmlUtil.escapeForHtml("   ", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_notInPreBlock() {
		Assert.assertEquals(" &nbsp;&nbsp;",
			HtmlUtil.escapeForHtml("   ", "<br>", false));
	}

	@Test
	public void testEscapeForHtml_tab_inPreBlock() {

		String code = "<foo>\twidget\t</foo>";

		String expected = "&lt;foo&gt;    widget    &lt;&#47;foo&gt;";
		Assert.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", true));
	}

	@Test
	public void testEscapeForHtml_tab_notInPreBlock() {

		String code = "<foo>\twidget\t</foo>";

		String expected = "&lt;foo&gt;&nbsp;&nbsp;&nbsp;&nbsp;widget&nbsp;&nbsp;&nbsp;&nbsp;&lt;&#47;foo&gt;";
		Assert.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", false));
	}

	@Test
	public void testGetHexString_null() {
		Assert.assertNull(HtmlUtil.getHexString(null));
	}

	@Test
	public void testGetHexString_allGreaterThan16() {
		Assert.assertEquals("#ffffff", HtmlUtil.getHexString(Color.white));
	}

	@Test
	public void testGetHexString_allLessThan16() {
		Assert.assertEquals("#000000", HtmlUtil.getHexString(Color.black));
	}

	@Test
	public void testGetTextAsHtml_happyPath() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("package foo;\npublic class Foobar {}");

		// We can't do an exact string comparison due to differing default fonts on different OS's
		String expectedRegex = "<pre style='font-family: \"\\w+\", courier; background: #ffffff'>" +
			"<span style=\"color: #000000;\">age</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">foo</span><span style=\"color: #000000;\">;</span><br>" +
			"<span style=\"color: #000000;\">public</span>" +
			"<span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">class</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">Foobar</span><span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">\\{</span><span style=\"color: #000000;\">}</span></pre>";

		String actual = HtmlUtil.getTextAsHtml(textArea, 4, textArea.getDocument().getLength());
		Assert.assertTrue(actual.matches(expectedRegex));
	}
}
