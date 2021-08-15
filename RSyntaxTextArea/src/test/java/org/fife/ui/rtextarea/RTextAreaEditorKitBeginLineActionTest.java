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
 * Unit tests for the {@link RTextAreaEditorKit.BeginLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitBeginLineActionTest extends AbstractRTextAreaTest {


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

		RSyntaxTextArea textArea = new RSyntaxTextArea("   Hello world");
		textArea.setCaretPosition(14);
		int firstNonWhiteSpaceOffs = 3;

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", false);

		// First, go to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(3, textArea.getSelectionEnd());

		// Then to offset 0
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(0, textArea.getSelectionEnd());

		// Then back to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_select_noLineWrap() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("   Hello world");
		textArea.setCaretPosition(14);
		int firstNonWhiteSpaceOffs = 3;

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", true);

		// First, go to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(14, textArea.getSelectionEnd());

		// Then to offset 0
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(14, textArea.getSelectionEnd());

		// Then back to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(14, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_select_noLineWrap_emptyLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", true);

		action.actionPerformedImpl(null, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("begin", new RTextAreaEditorKit.BeginLineAction("begin", false).getMacroID());
	}
}
