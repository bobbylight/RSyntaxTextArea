/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;
import java.util.Arrays;


/**
 * Unit tests for the {@link ClipboardHistoryPopup} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class ClipboardHistoryPopupTest extends AbstractRSyntaxTextAreaTest {

	private JFrame frame;
	private RSyntaxTextArea textArea;


	@Before
	public void setUp() {

		frame = new JFrame();
		textArea = createTextArea();
		frame.add(textArea);
		frame.pack();
	}


	@Test
	@Ignore("These tests run locally, but not in travis-ci environment for some reason")
	public void testGetPreferredSize() {
		Assert.assertNotNull(new ClipboardHistoryPopup(frame, textArea).getPreferredSize());
	}


	@Test
	@Ignore("These tests run locally, but not in travis-ci environment for some reason")
	public void testSetContents() {
		new ClipboardHistoryPopup(frame, textArea).setContents(Arrays.asList(
			"one", "two", "three"
		));
	}
}
