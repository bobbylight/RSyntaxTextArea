/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.IncreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitIncreaseFontSizeActionTest {


	@Test
	public void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("hello world");
		int origSize = textArea.getFont().getSize();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.IncreaseFontSizeAction().actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getFont().getSize() > origSize);
	}


	@Test
	public void testActionPerformedImpl_alreadyMaxSize() {

		RTextArea textArea = new RTextArea("hello world");
		int origFontSize = (int)RTextAreaEditorKit.IncreaseFontSizeAction.MAXIMUM_SIZE;
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, origFontSize));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.IncreaseFontSizeAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(textArea.getFont().getSize(), origFontSize);
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaIncreaseFontSizeAction,
			new RTextAreaEditorKit.IncreaseFontSizeAction().getMacroID());
	}
}
