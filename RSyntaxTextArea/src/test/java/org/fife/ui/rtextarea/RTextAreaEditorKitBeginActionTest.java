/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitBeginActionTest extends AbstractRTextAreaTest {


	@Test
	public void testActionPerformedImpl_select() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setCaretPosition(3);

		RTextAreaEditorKit.BeginAction action = new RTextAreaEditorKit.BeginAction("begin", true);
		action.actionPerformedImpl(null, textArea);

		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_noSelect() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setCaretPosition(3);

		RTextAreaEditorKit.BeginAction action = new RTextAreaEditorKit.BeginAction("begin", false);
		action.actionPerformedImpl(null, textArea);

		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("begin", new RTextAreaEditorKit.BeginAction("begin", false).getMacroID());
	}
}
