/*
 * 12/09/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.nio.charset.UnsupportedCharsetException;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for {@link TextEditorPane}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class TextEditorPaneTest {


	@Test
	public void testConstructor_zeroArg() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertEquals(TextEditorPane.INSERT_MODE, textArea.getTextMode());
		Assert.assertFalse(textArea.getLineWrap());
	}


	@Test
	public void testConstructor_oneArg() {
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.OVERWRITE_MODE);
		Assert.assertEquals(TextEditorPane.OVERWRITE_MODE, textArea.getTextMode());
		Assert.assertFalse(textArea.getLineWrap());
	}


	@Test
	public void testGetSetEncoding() {

		TextEditorPane textArea = new TextEditorPane();
		Assert.assertFalse(textArea.isDirty());

		textArea.setEncoding("UTF-16");
		Assert.assertEquals("UTF-16", textArea.getEncoding());
		Assert.assertTrue(textArea.isDirty());
		textArea.setDirty(false);

		textArea.setEncoding("UTF-8");
		Assert.assertEquals("UTF-8", textArea.getEncoding());
		Assert.assertTrue(textArea.isDirty());

	}


	@Test(expected = NullPointerException.class)
	public void testSetEncoding_invalidArg_null() {
		new TextEditorPane().setEncoding(null);
	}


	@Test(expected = UnsupportedCharsetException.class)
	public void testSetEncoding_invalidArg_unsupportedCharset() {
		new TextEditorPane().setEncoding("xxx");
	}


}