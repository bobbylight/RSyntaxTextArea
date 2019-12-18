/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * Unit tests for the {@link HtmlUtil} class.
 */
public class HtmlUtilTest {

	@Test
	public void testEscapeForHtml_inPreBlock_happyPath() {

		String code = "<foo>This &amp; that\n" +
			"<bar attr=\"yes\">\twidget\n</bar>\n" +
			"</foo>";

		String expected = "&lt;foo&gt;This &amp;amp; that<br>" +
			"&lt;bar attr=\"yes\"&gt;    widget<br>&lt;/bar&gt;<br>" +
			"&lt;/foo&gt;";
		Assert.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", true));
	}

	@Test
	public void testEscapeForHtml_noPreBlock_happyPath() {

		String code = "<foo>This &amp; that\n" +
			"<bar attr=\"yes\">\twidget\n</bar>\n" +
			"</foo>";

		String expected = "&lt;foo&gt;This&nbsp;&amp;amp;&nbsp;that<br>" +
			"&lt;bar&nbsp;attr=\"yes\"&gt;&nbsp;&nbsp;&nbsp;&nbsp;widget<br>&lt;/bar&gt;<br>" +
			"&lt;/foo&gt;";
		Assert.assertEquals(expected, HtmlUtil.escapeForHtml(code, "<br>", false));
	}

	@Test
	public void testEscapeForHtml_noPreBlock_happyPath_noNewlineReplacement() {

		String code = "<foo>This &amp; that\n" +
			"<bar attr=\"yes\">\twidget\n</bar>\n" +
			"</foo>";

		String expected = "&lt;foo&gt;This&nbsp;&amp;amp;&nbsp;that" +
			"&lt;bar&nbsp;attr=\"yes\"&gt;&nbsp;&nbsp;&nbsp;&nbsp;widget&lt;/bar&gt;" +
			"&lt;/foo&gt;";
		Assert.assertEquals(expected, HtmlUtil.escapeForHtml(code, null, false));
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
