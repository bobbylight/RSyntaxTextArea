/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.IncreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitIncreaseFontSizeActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testConstructor_5Arg() {
		Action a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction(
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
	void testActionPerformedImpl_increaseFontSize_happyPath() {

		RSyntaxTextArea textArea = createTextArea();

		int origFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				origFontSize = style.font.getSize();
				break;
			}
		}

		RSyntaxTextAreaEditorKit.IncreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaIncreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);


		int newFontSize = 0;
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				newFontSize = style.font.getSize();
				break;
			}
		}
		Assertions.assertTrue(newFontSize > origFontSize);
	}

	@Test
	void testActionPerformedImpl_increaseFontSize_capsAtMaxSize() {

		// This action change the font size in increments of 1f, so we start
		// with a font size close enough to the cap to hit it
		float maxSize = 40f;
		RSyntaxTextArea textArea = createTextArea();
		Font font = textArea.getFont();
		textArea.setFont(font.deriveFont(maxSize - 0.5f));

		RSyntaxTextAreaEditorKit.IncreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaIncreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);

		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				float newFontSize = style.font.getSize2D();
				Assertions.assertEquals(maxSize, newFontSize, 0.0001f);
			}
		}
		Assertions.assertEquals(maxSize, textArea.getFont().getSize2D(), 0.0001f);
	}

	@Test
	void testActionPerformedImpl_increaseFontSize_alreadyLargerThanMaxSize() {

		// This action change the font size in increments of 1f, so we start
		// with a font size close enough to the cap to hit it
		float maxSize = 40f;
		RSyntaxTextArea textArea = createTextArea();
		Font font = textArea.getFont();
		textArea.setFont(font.deriveFont(maxSize + 1f));
		float origFontSize = textArea.getFont().getSize2D();

		RSyntaxTextAreaEditorKit.IncreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaIncreaseFontSizeAction);
		a.actionPerformedImpl(e, textArea);

		// Verify fonts remain unchanged in size
		for (Style style : textArea.getSyntaxScheme().getStyles()) {
			if (style.font != null) {
				float newFontSize = style.font.getSize2D();
				Assertions.assertEquals(origFontSize, newFontSize, 0.0001f);
			}
		}
		Assertions.assertEquals(origFontSize, textArea.getFont().getSize2D(), 0.0001f);
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.IncreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rtaIncreaseFontSizeAction, a.getMacroID());
	}
}
