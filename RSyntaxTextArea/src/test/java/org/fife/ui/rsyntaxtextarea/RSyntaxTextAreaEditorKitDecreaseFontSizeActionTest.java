/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DecreaseFontSizeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitDecreaseFontSizeActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_decreaseFontSize() {

		RSyntaxTextArea textArea = createTextArea();

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
		Assert.assertTrue(newFontSize < origFontSize);
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DecreaseFontSizeAction a = new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rtaDecreaseFontSizeAction, a.getMacroID());
	}
}
