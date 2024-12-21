/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ToggleCommentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitToggleCommentActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_notEditable() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_leadingWhitespace_endsAtStartOfLine() {
		String text = "    line 1\n    line 2\n    line 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("    line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Final line is not commented out
		String expected = "//    line 1\n//    line 2\n    line 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_leadingWhitespace_endsInMiddleOfLine() {
		String text = "    line 1\n    line 2\n    line 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("ne 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		String expected = "//    line 1\n//    line 2\n//    line 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_leadingWhitespace_endsInMiddleOfLine_someLinesAlreadyCommented() {
		// One line commented after the leading whitespace, another before it
		String text = "    line 1\n    //line 2\n//    line 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("ne 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Comments get doubled up, no matter where they are in the line
		String expected = "//    line 1\n//    //line 2\n////    line 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_noLeadingWhitespace_endsAtStartOfLine() {
		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Final line is not commented out
		String expected = "//line 1\n//line 2\nline 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_noLeadingWhitespace_endsInMiddleOfLine() {
		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("ne 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		String expected = "//line 1\n//line 2\n//line 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startTokenOnly_leadingWhitespace() {
		String text = "    this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("//" + text, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startTokenOnly_noLeadingWhitespace() {
		String text = "this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("//" + text, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startAndEnd_missingEnd_leadingWhitespace() {
		String text = "    <!-- this is code";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// If the "end" token isn't there, we assume the whole thing needs to be commented out
		Assertions.assertEquals("<!--" + text + "-->", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startAndEnd_missingEnd_noLeadingWhitespace() {
		String text = "<!-- this is code";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// If the "end" token isn't there, we assume the whole thing needs to be commented out
		Assertions.assertEquals("<!--" + text + "-->", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_languageWithoutComments() {
		String text = "this is code";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_BBCODE, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(text, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_multiLine_leadingWhitespace_endsAtStartOfLine() {
		String text = "  //line 1\n  //line 2\n  //line 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(text.indexOf("//"));
		textArea.moveCaretPosition(text.indexOf("  //line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line not uncommented
		String expectedText = "  line 1\n  line 2\n  //line 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_multiLine_leadingWhitespace_endsInMiddleOfLine() {
		String text = "  //line 1\n  //line 2\n  //line 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(text.indexOf("//"));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line is uncommented
		String expectedText = "  line 1\n  line 2\n  line 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_multiLine_noLeadingWhitespace_endsAtStartOfLine() {
		String text = "//line 1\n//line 2\n//line 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(text.indexOf("//"));
		textArea.moveCaretPosition(text.indexOf("//line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line not uncommented
		String expectedText = "line 1\nline 2\n//line 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_multiLine_noLeadingWhitespace_endsInMiddleOfLine() {
		String text = "//line 1\n//line 2\n//line 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(text.indexOf("//"));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line is uncommented
		String expectedText = "line 1\nline 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_remove_multiLine_startAndEnd_leadingWhitespace_endsAtStartOfLine() {
		String text = "  <!-- line 1 -->\n" +
			"  <!-- line 2 -->\n" +
			"  <!-- line 3 -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(text.indexOf("line 1"));
		textArea.moveCaretPosition(text.indexOf("  <!-- line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line not uncommented
		String expectedText = "   line 1 \n" +
			"   line 2 \n" +
			"  <!-- line 3 -->";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_remove_multiLine_startAndEnd_leadingWhitespace_endsInMiddleOfLine() {
		String text = "  <!-- line 1 -->\n" +
			"  <!-- line 2 -->\n" +
			"  <!-- line 3 -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(text.indexOf("line 1"));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line is uncommented
		String expectedText = "   line 1 \n" +
			"   line 2 \n" +
			"   line 3 ";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_remove_multiLine_startAndEnd_noLeadingWhitespace_endsAtStartOfLine() {
		String text = "<!-- line 1 -->\n" +
			"<!-- line 2 -->\n" +
			"<!-- line 3 -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(text.indexOf("line 1"));
		textArea.moveCaretPosition(text.indexOf("<!-- line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line not uncommented
		String expectedText = " line 1 \n" +
			" line 2 \n" +
			"<!-- line 3 -->";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_remove_multiLine_startAndEnd_noLeadingWhitespace_endsInMiddleOfLine() {
		String text = "<!-- line 1 -->\n" +
			"<!-- line 2 -->\n" +
			"<!-- line 3 -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(text.indexOf("line 1"));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Last line is uncommented
		String expectedText = " line 1 \n" +
			" line 2 \n" +
			" line 3 ";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_remove_singleLine_leadingWhitespace_caretAtStartOfLine() {
		String text = "    // this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("     this is code", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_leadingWhitespace_caretAtStartOfLine_startAndEnd() {
		String text = "    <!-- this is code -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("     this is code ", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_leadingWhitespace_caretInMiddleOfLine() {
		String text = "    // this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf("is"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("     this is code", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_noLeadingWhitespace_caretAtStartOfLine() {
		String text = "// this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_noLeadingWhitespace_caretAtStartOfLine_startAndEnd() {
		String text = "<!-- this is code -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code ", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_noLeadingWhitespace_caretInMiddleOfLine() {
		String text = "// this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf("is"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaToggleCommentAction,
			new RSyntaxTextAreaEditorKit.ToggleCommentAction().getMacroID());
	}
}
