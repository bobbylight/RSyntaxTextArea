/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;


/**
 * Unit tests for the {@link TipWindow} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class TipWindowTest {

	@Test
	void testConstructor_nullTitle() {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		TipWindow tw = new TipWindow(owner, focusableTip, null);
		Assertions.assertTrue(tw.getText().startsWith("<html>"));
		Assertions.assertFalse(tw.getText().contains("null"));
	}

	@Test
	void testConstructor_title_short() {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		TipWindow tw = new TipWindow(owner, focusableTip, "test");
		Assertions.assertTrue(tw.getText().startsWith("<html>"));
		Assertions.assertTrue(tw.getText().contains("test"));
	}

	@Test
	void testConstructor_title_long_plainText() {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		String text = "A very long string of content";
		TipWindow tw = new TipWindow(owner, focusableTip, text);
		Assertions.assertTrue(tw.getText().startsWith("<html>"));
		Assertions.assertTrue(tw.getText().contains(text));
	}

	@Test
	void testConstructor_title_long_plainText_escapedForHtml() {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		String text = "A very <em>long</em> string of content";
		TipWindow tw = new TipWindow(owner, focusableTip, text);
		Assertions.assertTrue(tw.getText().startsWith("<html>"));
		Assertions.assertTrue(tw.getText().contains("A very &lt;em&gt;long&lt;/em&gt; string of content"));
	}

	@Test
	void testConstructor_title_long_html() {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		String text = "<html>This <em>rocks!</em>";
		TipWindow tw = new TipWindow(owner, focusableTip, text);
		Assertions.assertTrue(tw.getText().startsWith("<html>"));
		Assertions.assertTrue(tw.getText().contains("This <em>rocks!</em>"));
	}

	@Test
	void testConstructor_imageBaseInstalledOnTextArea() throws IOException {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);
		focusableTip.setImageBase(new URL("https://google.com"));

		new TipWindow(owner, focusableTip, "test");
	}

	@Test
	void testActionPerformed() throws IOException {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);

		TipWindow tw = new TipWindow(owner, focusableTip, "test");
		tw.actionPerformed();
	}

	@Test
	void testGetSetHyperlinkListener() throws IOException {
		JFrame owner = new JFrame();
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FocusableTip focusableTip = new FocusableTipTest.TestableFocusableTip(textArea, null);
		focusableTip.setImageBase(new URL("https://google.com"));

		TipWindow tw = new TipWindow(owner, focusableTip, "test");
		tw.setHyperlinkListener(e -> {});
		tw.setHyperlinkListener(null);
		tw.setHyperlinkListener(e -> {});
	}
}
