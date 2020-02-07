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
 * Unit tests for the {@link RTextAreaEditorKit.EndAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitEndActionTest {


	@Test
	public void testActionPerformedImpl_selectFalse() {

		String text = "line 1\nline 2\nline 3";
		RTextArea textArea = new RTextArea(text);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.EndAction("foo", false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(text.length(), textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_selectTrue() {

		String text = "line 1\nline 2\nline 3";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.EndAction("foo", true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(text.length(), textArea.getCaretPosition());
		Assert.assertNotNull(textArea.getSelectedText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.EndAction("foo", false).getMacroID());
	}
}
