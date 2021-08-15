/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.EndWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitEndWordActionTest {


	@Test
	public void testActionPerformedImpl_noSelect() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(6);

		RTextAreaEditorKit.EndWordAction action = new RTextAreaEditorKit.EndWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(9, textArea.getSelectionStart());
		Assert.assertEquals(9, textArea.getSelectionEnd());

	}


	@Test
	public void testActionPerformedImpl_select() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(6);

		RTextAreaEditorKit.EndWordAction action = new RTextAreaEditorKit.EndWordAction("name", true);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(6, textArea.getSelectionStart());
		Assert.assertEquals(9, textArea.getSelectionEnd());

	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("end", new RTextAreaEditorKit.EndWordAction("end", false).getMacroID());
	}
}
