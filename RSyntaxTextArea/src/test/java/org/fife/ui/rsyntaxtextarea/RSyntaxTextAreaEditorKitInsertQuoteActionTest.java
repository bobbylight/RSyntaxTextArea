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

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertQuoteAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitInsertQuoteActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String expected = textArea.getText();

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(expected, textArea.getText()); // Unchanged
	}

	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String expected = textArea.getText();

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(expected, textArea.getText()); // Unchanged
	}

	@Test
	void testActionPerformedImpl_disabled_noSelection() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setInsertPairedCharacters(false);
		textArea.setCaretPosition(origContent.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("one \"word here", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_disabled_selection() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setInsertPairedCharacters(false);
		textArea.setCaretPosition(origContent.indexOf("word"));
		textArea.moveCaretPosition(textArea.getCaretPosition() + 4);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("one \" here", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_enabled_selection() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf("word"));
		textArea.moveCaretPosition(textArea.getCaretPosition() + 4);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// Selection is simply wrapped in quotes if there is a selection
		Assertions.assertEquals("one \"word\" here", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_enabled_overwriteMode() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setTextMode(RSyntaxTextArea.OVERWRITE_MODE);
		textArea.setCaretPosition(origContent.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// Overwrite mode inserts a single quote
		Assertions.assertEquals("one \"ord here", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_notInString_languageWithInvalidStrings() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// Both the opening and closing quotes are inserted
		Assertions.assertEquals("one \"\"word here", textArea.getText());
		Assertions.assertEquals("one \"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_notInString_languageWithOnlyValidStrings() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_PERL, origContent);
		textArea.setCaretPosition(origContent.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// Both the opening and closing quotes are inserted
		Assertions.assertEquals("one \"\"word here", textArea.getText());
		Assertions.assertEquals("one \"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_notInString_languageWithoutStrings() {

		String origContent = "one word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, origContent);
		textArea.setCaretPosition(origContent.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// Both the opening and closing quotes are inserted
		Assertions.assertEquals("one \"word here", textArea.getText());
		Assertions.assertEquals("one \"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inString_middle() {

		String origContent = "one \"word\" here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf("rd"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A new closing quote is inserted
		Assertions.assertEquals("one \"wo\"rd\" here", textArea.getText());
		Assertions.assertEquals("one \"wo\"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inString_middle_languageWithOnlyValidStrings() {

		String origContent = "one \"word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_PERL, origContent);
		textArea.setCaretPosition(origContent.indexOf("rd"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A closing quote is inserted
		Assertions.assertEquals("one \"wo\"rd here", textArea.getText());
		Assertions.assertEquals("one \"wo\"".length(), textArea.getCaretPosition());
	}

	/**
	 * See <a href="https://github.com/bobbylight/RSyntaxTextArea/issues/539">issue 539</a>.
	 */
	@Test
	void testActionPerformedImpl_enabled_insertMode_inString_beforeLastChar_languageWithOnlyValidStrings() {

		String origContent = "one \"word here";

		// Caret just before the last char, "e"
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_PERL, origContent);
		textArea.setCaretPosition(origContent.length() - 1);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A closing quote is inserted, trailing "e" pushed forward
		Assertions.assertEquals("one \"word her\"e", textArea.getText());
		Assertions.assertEquals(textArea.getText().length() - 1, textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inString_afterLastChar_languageWithOnlyValidStrings() {

		String origContent = "one \"word here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_PERL, origContent);
		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A closing quote is inserted after all text
		Assertions.assertEquals("one \"word here\"", textArea.getText());
		Assertions.assertEquals(textArea.getText().length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inString_atEndQuote() {

		String origContent = "one \"word\" here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.lastIndexOf('"'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// The existing closing quote is overwritten
		Assertions.assertEquals("one \"word\" here", textArea.getText());
		Assertions.assertEquals("one \"word\"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inInvalidString() {

		String origContent = "one \"w\\lord\" here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.lastIndexOf('"'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A single double quote is inserted - we don't do anything for invalid strings currently
		Assertions.assertEquals("one \"w\\lord\"\" here", textArea.getText());
		Assertions.assertEquals("one \"w\\lord\"".length(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_enabled_insertMode_inComment_atEndQuote() {

		String origContent = "// one \"word\" here";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.lastIndexOf('"'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertQuoteAction("test",
			RSyntaxTextAreaEditorKit.InsertQuoteAction.QuoteType.DOUBLE_QUOTE);
		ActionEvent e = createActionEvent(textArea, "\"");
		a.actionPerformedImpl(e, textArea);

		// A single double quote is inserted - we don't look for strings in comments
		Assertions.assertEquals("// one \"word\"\" here", textArea.getText());
		Assertions.assertEquals("// one \"word\"".length(), textArea.getCaretPosition());
	}
}
