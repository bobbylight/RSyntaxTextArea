/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.InsertContentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitInsertContentActionTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "new-content");
		new RTextAreaEditorKit.InsertContentAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("hello world", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));

		ActionEvent e = new ActionEvent(textArea, 0, "new-content");
		new RTextAreaEditorKit.InsertContentAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("hellonew-content world", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.insertContentAction,
			new RTextAreaEditorKit.InsertContentAction().getMacroID());
	}
}
