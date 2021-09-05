/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.UnselectAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitUnselectActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.append("foo");
		textArea.selectAll();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UnselectAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(3, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaUnselectAction,
			new RTextAreaEditorKit.UnselectAction().getMacroID());
	}
}
