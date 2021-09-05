/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.SelectWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitSelectWordActionTest {


	@Test
	void testActionPerformedImpl() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.SelectWordAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(7, textArea.getSelectionStart());
		Assertions.assertEquals(11, textArea.getSelectionEnd());
		Assertions.assertEquals(11, textArea.getCaretPosition());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.selectWordAction,
			new RSyntaxTextAreaEditorKit.SelectWordAction().getMacroID());
	}
}
