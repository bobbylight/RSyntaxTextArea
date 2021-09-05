/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.IncreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitIncreaseFontSizeActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.IncreaseFontSizeAction action = new RTextAreaEditorKit.IncreaseFontSizeAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("hello world");
		int origSize = textArea.getFont().getSize();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.IncreaseFontSizeAction().actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getFont().getSize() > origSize);
	}


	@Test
	void testActionPerformedImpl_closeToMaximumSize() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 2).deriveFont(39.5f);
		textArea.setFont(font);

		RTextAreaEditorKit.IncreaseFontSizeAction action = new RTextAreaEditorKit.IncreaseFontSizeAction();
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(40, textArea.getFont().getSize());
	}


	@Test
	void testActionPerformedImpl_alreadyMaxSize() {

		RTextArea textArea = new RTextArea("hello world");
		int origFontSize = (int)RTextAreaEditorKit.IncreaseFontSizeAction.MAXIMUM_SIZE;
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, origFontSize));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.IncreaseFontSizeAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(textArea.getFont().getSize(), origFontSize);
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaIncreaseFontSizeAction,
			new RTextAreaEditorKit.IncreaseFontSizeAction().getMacroID());
	}
}
