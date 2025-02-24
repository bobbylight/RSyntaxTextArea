/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertBreakAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitInsertBreakActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void test_notEditable() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertBreakAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello world", textArea.getText());
	}

	@Test
	void test_notEnabled() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertBreakAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello world", textArea.getText());
	}

	@Test
	void test_noSelection_defaults_caretAtEnd_withCurly_closingCurlyBraceAdded_tabs() {

		String origContent = "  public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Both the new line and the closing curly's line are indented the same amount as
		// the opening curly's line. Also the caret's line has an extra indentation (defaults
		// to tab)
		String expectedResultContent = origContent + "\n  \t\n  }\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n  \t".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_caretAtEnd_withCurly_closingCurlyBraceAdded_spaces() {

		String origContent = "  public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The new line with the caret is indented to the next "real" indentation offset, in this case, 4.
		// The closing curly is indented to match the indentation of the opening curly's line
		String expectedResultContent = origContent + "\n    \n  }\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n    ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_caretAtEnd_withCurlyPair_indentPreserved() {

		String origContent = "  public void foo() { doSomething(); }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Since the opening curly was paired on the same line, we just indent to the same level
		String expectedResultContent = origContent + "\n  ";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n  ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_caretAtEnd_withCurlyPairsButOneUnclosed_nextLineIndented() {

		String origContent = "  public void foo() { doSomething(); } {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Since there was an unpaired opening curly, the next line is indented and a closing curly
		// is added on another new line
		String expectedResultContent = origContent + "\n  \t\n  }\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n  \t".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_default_caretInMiddleOfLineOtherThanFirst() {

		String origContent = "  zero\n  one two three\nfour five six";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.indexOf("two"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The current line is preserved, and the new line is indented
		// at the same level as that line
		String expectedResultContent = "  zero\n  one \n  two three\nfour five six";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.indexOf("two"), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_allWSLine_caretAtEnd_allWSLineCleared() {

		String origContent = "public void foo() {\n\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The line the caret was on loses the '\t' character to become blank, and the new line
		// is indented
		String expectedResultContent = "public void foo() {\n\n\t";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_allWSLine_caretInMiddle_wsLineClearedAndWSAfterCaretRemoved() {

		String origContent = "public void foo() {\n\t   ";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		// Caret is after the '\t' character in the "\t   " line - whitespace before and after it
		textArea.setCaretPosition(origContent.length() - 3);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// All the whitespace on that line is cleared and the "   " after the caret is effectively removed
		// (or replaced with '\t' because of auto-indent, depending on how you look at it).
		String expectedResultContent = "public void foo() {\n\n\t";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_wsAndTextAfterCaret_emulatedTabs() {

		String origContent = "  we are";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(true);

		// Caret is before " are", but the leading " " char is removed when the newline is inserted
		textArea.setCaretPosition(origContent.length() - " are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// "are" is moved to the next line and indented to match the indentation of "we".
		// The preceding " " character does not affect the new line's indentation
		String expectedResultContent = "  we\n  are";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_textAfterCaret_emulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(true);

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line is indented to match the previous line
		String expectedResultContent = "    we\n    are";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_defaults_textAfterCaret_noEmulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(false); // default

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line is indented to match the previous line.
		// Note the prior indentation is preserved - spaces - even though tabs are enabled
		String expectedResultContent = "    we\n    are";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_caretAtEnd_withCurly_closingCurlyBraceAdded() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Caret is on a new, empty line, and the line after that is an unindented closing curly
		String expectedResultContent = "public void foo() {\n\n}\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + 1, textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_caretAtEnd_withCurlyPair() {

		String origContent = "  public void foo() { doSomething(); }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Since the opening curly was paired on the same line, no matching closing curly is added
		String expectedResultContent = origContent + "\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_caretInMiddleOfLineOtherThanFirst() {

		String origContent = "  zero\n  one two three\nfour five six";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.indexOf("two"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The current line is preserved, and the new line is not indented
		// at the same level as that line
		String expectedResultContent = "  zero\n  one \ntwo three\nfour five six";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.indexOf("two"), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_allWSLine_caretAtEnd_allWSLineCleared() {

		String origContent = "public void foo() {\n\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The line the caret was on is cleared and the caret is on a new line with no whitespace
		String expectedResultContent = "public void foo() {\n\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_allWSLine_caretInMiddle_wsLineClearedAndWSAfterCaretRemoved() {

		String origContent = "public void foo() {\n\t\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length() - 1); // tab before and after

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The line the caret was on is cleared and the caret is on a new line with no whitespace
		String expectedResultContent = "public void foo() {\n\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_wsAndTextAfterCaret_emulatedTabs() {

		String origContent = "  we are";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(true);

		// Caret is before " are", but the leading " " char is removed when the newline is inserted
		textArea.setCaretPosition(origContent.length() - " are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// "are" is moved to the next line not indented.
		// The preceding " " character does not affect the new line's indentation
		String expectedResultContent = "  we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_textAfterCaret_emulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(true);

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line not indented.
		String expectedResultContent = "    we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_textAfterCaret_noEmulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(true); // default
		textArea.setTabsEmulated(false); // default

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line not indented.
		String expectedResultContent = "    we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_caretAtEnd_withCurly_closingCurlyBraceAdded() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Caret is on a new, empty line, and the line after that is an unindented closing curly
		String expectedResultContent = "public void foo() {\n\n}\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + 1, textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_caretAtEnd_withCurlyPair() {

		String origContent = "  public void foo() { doSomething(); }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Since the opening curly was paired on the same line, no matching closing curly is added
		String expectedResultContent = origContent + "\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_allWSLine_caretAtEnd_allWSLineCleared() {

		String origContent = "public void foo() {\n\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The line the caret was on is not cleared and the caret is on a new line with no whitespace
		String expectedResultContent = "public void foo() {\n\t\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_allWSLine_caretInMiddle_wsAfterCaretRemoved() {

		String origContent = "public void foo() {\n\t\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);

		textArea.setCaretPosition(origContent.length() - 1); // tab before and after

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The line the caret was on keeps the '\t' that was before the caret,
		// and the caret is on a new line with no whitespace
		String expectedResultContent = "public void foo() {\n\t\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_wsAndTextAfterCaret_emulatedTabs() {

		String origContent = "  we are";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);
		textArea.setTabsEmulated(true);

		// Caret is before " are", but the leading " " char is removed when the newline is inserted
		textArea.setCaretPosition(origContent.length() - " are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// "are" is moved to the next line not indented.
		// The preceding " " character does not affect the new line's indentation
		String expectedResultContent = "  we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_textAfterCaret_emulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);
		textArea.setTabsEmulated(true);

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line not indented.
		String expectedResultContent = "    we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_textAfterCaret_noEmulatedTabs() {

		String origContent = "    weare";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);
		textArea.setTabsEmulated(false); // default

		textArea.setCaretPosition(origContent.length() - "are".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// Text that was moved to a new line not indented.
		String expectedResultContent = "    we\nare";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.length() - "are".length(), textArea.getCaretPosition());
	}

	@Test
	void test_noSelection_autoIndentDisabled_clearWSLinesDisabled_caretInMiddleOfLineOtherThanFirst() {

		String origContent = "  zero\n  one two three\nfour five six";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);
		textArea.setClearWhitespaceLinesEnabled(false);

		textArea.setCaretPosition(origContent.indexOf("two"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		// The current line is preserved, and the new line is indented
		// at the same level as that line
		String expectedResultContent = "  zero\n  one \ntwo three\nfour five six";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.indexOf("two"), textArea.getCaretPosition());
	}

	@Test
	void test_selection_multipleLines() {
		String origContent = "something\nspecial";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.indexOf("thing"));
		textArea.moveCaretPosition(origContent.indexOf('c'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "some\ncial";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.indexOf('\n') + 1, textArea.getCaretPosition());
	}

	@Test
	void test_selection_singleLine() {
		String origContent = "somethingspecial";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition("something".length());
		textArea.moveCaretPosition(origContent.indexOf('c'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "something\ncial";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(expectedResultContent.indexOf('\n') + 1, textArea.getCaretPosition());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.InsertBreakAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.insertBreakAction, a.getMacroID());
	}
}
