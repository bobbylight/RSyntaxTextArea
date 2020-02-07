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
 * Unit tests for the {@link RTextAreaEditorKit.UpperSelectionCaseAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitUpperSelectionCaseActionTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("hello World");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(8);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UpperSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("hello World", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("hello World");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UpperSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("heLLO WOrld", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaUpperSelectionCaseAction,
			new RTextAreaEditorKit.UpperSelectionCaseAction().getMacroID());
	}
}
