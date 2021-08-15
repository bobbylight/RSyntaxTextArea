/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.EndLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitEndLineActionTest extends AbstractRTextAreaTest {


	@Test
	@Ignore("Need a displayable text area")
	public void testActionPerformedImpl_noSelect_lineWrap() {
	}


	@Test
	@Ignore("Need a displayable text area")
	public void testActionPerformedImpl_select_lineWrap() {
	}


	@Test
	public void testActionPerformedImpl_noSelect_noLineWrap() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("Hello world");
		textArea.setCaretPosition(2);

		RTextAreaEditorKit.EndLineAction action = new RTextAreaEditorKit.EndLineAction("endLine", false);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals("Hello world".length(), textArea.getSelectionStart());
		Assert.assertEquals("Hello world".length(), textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_select_noLineWrap() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("Hello world");
		textArea.setCaretPosition(2);

		RTextAreaEditorKit.EndLineAction action = new RTextAreaEditorKit.EndLineAction("endLine", true);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(2, textArea.getSelectionStart());
		Assert.assertEquals("Hello world".length(), textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_select_noLineWrap_emptyLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();

		RTextAreaEditorKit.EndLineAction action = new RTextAreaEditorKit.EndLineAction("endLine", true);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("end", new RTextAreaEditorKit.EndLineAction("end", false).getMacroID());
	}
}
