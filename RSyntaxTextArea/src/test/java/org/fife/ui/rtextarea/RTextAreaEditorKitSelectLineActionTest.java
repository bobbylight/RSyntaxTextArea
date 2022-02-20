/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.SelectLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitSelectLineActionTest extends AbstractRTextAreaTest {


	@Test
	void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setBounds(0, 0, 80, 80);
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SelectLineAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(7, textArea.getSelectionStart());
		Assertions.assertEquals(13, textArea.getSelectionEnd());
		Assertions.assertEquals(13, textArea.getCaretPosition());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.selectLineAction,
			new RTextAreaEditorKit.SelectLineAction().getMacroID());
	}
}
