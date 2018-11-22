/*
 * 12/10/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RSyntaxUtilities} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxUtilitiesTest {


	@Test
	public void testEscapeForHtml_nullInput() {
		Assert.assertNull(RSyntaxUtilities.escapeForHtml(null, "<br>", true));
	}


	@Test
	public void testEscapeForHtml_nullNewlineReplacement() {
		Assert.assertEquals("", RSyntaxUtilities.escapeForHtml("\n", null, true));
	}


	@Test
	public void testEscapeForHtml_happyPath() {
		Assert.assertEquals("hello", RSyntaxUtilities.escapeForHtml("hello", "<br>", true));
		Assert.assertEquals("2 &lt; 4", RSyntaxUtilities.escapeForHtml("2 < 4", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_problemChars() {
		Assert.assertEquals(" <br>&amp;   &lt;&gt;&#39;&#34;&#47;",
				RSyntaxUtilities.escapeForHtml(" \n&\t<>'\"/", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_inPreBlock() {
		Assert.assertEquals("   ",
				RSyntaxUtilities.escapeForHtml("   ", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_notInPreBlock() {
		Assert.assertEquals(" &nbsp;&nbsp;",
				RSyntaxUtilities.escapeForHtml("   ", "<br>", false));
	}


}