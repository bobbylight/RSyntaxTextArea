/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.modes.XMLTokenMaker;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for this action.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCloseMarkupTagActionTest extends AbstractRSyntaxTextAreaTest {

	private boolean xmlDefaultClosingTags;

	@BeforeEach
	void setUp() {
		xmlDefaultClosingTags = XMLTokenMaker.getCompleteCloseMarkupTags();
	}


	@AfterEach
	void tearDown() {
		XMLTokenMaker.setCompleteCloseTags(xmlDefaultClosingTags);
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo><");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo></foo>";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_nested() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo><bar></bar><self-closed/><");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo><bar></bar><self-closed/></foo>";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_attributesAreSkipped() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo attr=\"value/with slashes\"><");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo attr=\"value/with slashes\"></foo>";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_spaceBeforeTagName() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"< foo><");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "< foo></foo>";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_butDisabledForLanguage() {

		XMLTokenMaker.setCompleteCloseTags(false);
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo><");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo></";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_butDisabledForTextArea() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo><");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setCloseMarkupTags(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo></";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_butThereIsSelection() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<foo><X");
		textArea.setCaretPosition(textArea.getDocument().getLength() - 1);
		textArea.moveCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "<foo></";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_typingClosingTag_butNoOpeningTag() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML,
			"<");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "</";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_markup_emptyDocument() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "");

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "/";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_markup_nonEmptyDocument_slashWillNotBeInClosingTag() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "foo ");

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "foo /";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_nonMarkup_emptyDocument() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, "");

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "/";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEditable() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void getGetMacroId() {
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction, a.getMacroID());
	}
}
