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
 * Unit tests for the {@link RTextAreaEditorKit.VerticalPageAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitVerticalPageActionTest {


	@Test
	public void testActionPerformedImpl_up_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setCaretPosition(content.indexOf('1'));
		textArea.setBounds(0, 0, 800, 800);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", -1, false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(5, textArea.getCaretPosition()); // End of first line
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_up_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(content.indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", -1, true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(5, textArea.getCaretPosition()); // End of first line
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_down_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(20, textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_down_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", 1, true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(20, textArea.getCaretPosition());
		Assert.assertEquals("line 1\nline 2\nline 3", textArea.getSelectedText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).getMacroID());
	}
}
