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
 * Unit tests for the {@link RTextAreaEditorKit.PageAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitPageActionTest extends AbstractRTextAreaTest {


	@Test
	public void testActionPerformedImpl_left_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setCaretPosition(content.indexOf('1'));
		textArea.setBounds(0, 0, 800, 800);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PageAction("foo", true, false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(0, textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_left_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(content.indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PageAction("foo", true, true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(0, textArea.getCaretPosition());
		Assert.assertEquals("line ", textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_right_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PageAction("foo", false, false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(19, textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_right_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PageAction("foo", false, true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(19, textArea.getCaretPosition());
		Assert.assertEquals("line 1\nline 2\nline ", textArea.getSelectedText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.PageAction("foo", true, false).getMacroID());
	}
}
