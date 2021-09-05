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
	void testActionPerformedImpl_increaseFontSize() {

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
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.IncreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rtaIncreaseFontSizeAction, a.getMacroID());
	}
}
