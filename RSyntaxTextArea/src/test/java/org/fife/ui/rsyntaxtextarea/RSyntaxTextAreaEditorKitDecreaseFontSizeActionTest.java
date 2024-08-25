/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DecreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitDecreaseFontSizeActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testConstructor_5Arg() {
		Action a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction(
			"name", null, "desc", 1, null
		);
		Assertions.assertEquals("name", a.getValue(Action.NAME));
		Assertions.assertNull(a.getValue(Action.LARGE_ICON_KEY));
		Assertions.assertNull(a.getValue(Action.SMALL_ICON));
		Assertions.assertEquals("desc", a.getValue(Action.SHORT_DESCRIPTION));
		Assertions.assertEquals(1, a.getValue(Action.MNEMONIC_KEY));
		Assertions.assertNull(a.getValue(Action.ACCELERATOR_KEY));
	}

	@Test
	void testActionPerformedImpl_decreaseFontSize() {

		// Wrap the text area in a scroll pane to test parent events/revalidation
		RSyntaxTextArea textArea = createTextArea();
		new RTextScrollPane(textArea);

		int origFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				origFontSize = style.font.getSize();
				break;
			}
		}

		RSyntaxTextAreaEditorKit.DecreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaDecreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);

		int newFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				newFontSize = style.font.getSize();
				break;
			}
		}
		Assertions.assertTrue(newFontSize < origFontSize);
	}

	@Test
	void testActionPerformedImpl_closeToMinSizeSoPartialDecrease() {

		float currentSize = 2.5f; // min size allowed is 2f

		// Wrap the text area in a scroll pane to test parent events/revalidation
		RSyntaxTextArea textArea = createTextArea();
		textArea.setFont(textArea.getFont().deriveFont(currentSize));
		new RTextScrollPane(textArea);

		RSyntaxTextAreaEditorKit.DecreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaDecreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);

		int newFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				newFontSize = style.font.getSize();
				break;
			}
		}
		Assertions.assertEquals(newFontSize, 2f);
	}

	@Test
	void testActionPerformedImpl_alreadyMinimumSize() {

		float minSize = 2f;

		// Wrap the text area in a scroll pane to test parent events/revalidation
		RSyntaxTextArea textArea = createTextArea();
		textArea.setFont(textArea.getFont().deriveFont(minSize));
		new RTextScrollPane(textArea);

		RSyntaxTextAreaEditorKit.DecreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaDecreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);

		int newFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				newFontSize = style.font.getSize();
				break;
			}
		}
		Assertions.assertEquals(newFontSize, minSize);
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DecreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rtaDecreaseFontSizeAction, a.getMacroID());
	}
}
