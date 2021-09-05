/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;


/**
 * Unit tests for the {@link RTextAreaEditorKit.DecreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitDecreaseFontSizeActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.DecreaseFontSizeAction action = new RTextAreaEditorKit.DecreaseFontSizeAction(
			"decreaseFontSize", null, "Description", 0, null);
		Assertions.assertEquals("decreaseFontSize", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);
		int origFontSize = textArea.getFont().getSize();

		RTextAreaEditorKit.DecreaseFontSizeAction action = new RTextAreaEditorKit.DecreaseFontSizeAction();
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(origFontSize - 1, textArea.getFont().getSize());
	}


	@Test
	void testActionPerformedImpl_closeToMinimumSize() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 2).deriveFont(2.5f);
		textArea.setFont(font);

		RTextAreaEditorKit.DecreaseFontSizeAction action = new RTextAreaEditorKit.DecreaseFontSizeAction();
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(2, textArea.getFont().getSize());
	}


	@Test
	void testActionPerformedImpl_alreadyAtMinimumSize() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 2));

		RTextAreaEditorKit.DecreaseFontSizeAction action = new RTextAreaEditorKit.DecreaseFontSizeAction();
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(2, textArea.getFont().getSize());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaDecreaseFontSizeAction,
			new RTextAreaEditorKit.DecreaseFontSizeAction().getMacroID());
	}
}
