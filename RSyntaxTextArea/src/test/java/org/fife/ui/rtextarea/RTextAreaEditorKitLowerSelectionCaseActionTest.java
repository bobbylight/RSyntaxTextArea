/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.LowerSelectionCaseAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitLowerSelectionCaseActionTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("HELLO WORLD");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LowerSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("HELLO WORLD", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("HELLO WORLD");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LowerSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("HEllo WORLD", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaLowerSelectionCaseAction,
			new RTextAreaEditorKit.LowerSelectionCaseAction().getMacroID());
	}
}
