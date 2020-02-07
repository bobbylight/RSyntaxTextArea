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
 * Unit tests for the {@link RTextAreaEditorKit.SelectLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitSelectLineActionTest {


	@Test
	public void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SelectLineAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(7, textArea.getSelectionStart());
		Assert.assertEquals(13, textArea.getSelectionEnd());
		Assert.assertEquals(13, textArea.getCaretPosition());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.selectLineAction,
			new RTextAreaEditorKit.SelectLineAction().getMacroID());
	}
}
