/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@code AbstractJFlexCTokenMaker.CStyleInsertBreakAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class AbstractJFlexCTokenMakerCStyleInsertBreakActionTest extends AbstractRSyntaxTextAreaTest {

	private Action insertBreakAction;

	@BeforeEach
	void setUp() {
		insertBreakAction = new JavaTokenMaker().getInsertBreakAction();
	}

	@Test
	void test_notEditable() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals("hello world", textArea.getText());
	}

	@Test
	void test_notEnabled() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals("hello world", textArea.getText());
	}

	@Test
	void test_notinDocComment_inCode_defaultIndentingBehavior() {

		String origContent = "  public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(true); // default
		textArea.setClearWhitespaceLinesEnabled(true); // default

		textArea.setCaretPosition(origContent.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// Both the new line and the closing curly's line are indented the same amount as
		// the opening curly's line. Also the caret's line has an extra indentation (defaults
		// to tab)
		String expectedResultContent = origContent + "\n  \t\n  }\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(origContent.length() + "\n  \t".length(), textArea.getCaretPosition());
	}

	@Test
	void test_notinDocComment_inWhitespaceBeforeMlcStart() {

		String code = "  /**";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals("\n /**", textArea.getText());
		Assertions.assertEquals(2, textArea.getCaretPosition());
	}

	@Test
	void test_inMLC_unclosed_firstLine() {

		String code = "/*";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New empty line, indented properly, added for the caret and another line closing the MLC also added
		Assertions.assertEquals("/*\n * \n */", textArea.getText());
		Assertions.assertEquals("/*\n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_unclosed_firstLine() {

		String code = "/**";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New empty line, indented properly, added for the caret and another line closing the MLC also added
		Assertions.assertEquals("/**\n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_unclosed_firstLine_contentAfterStart() {

		String code = "/** This is an MLC";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New empty line, indented properly, added for the caret and another line closing the MLC also added.
		// Note indentation for new text (caret position) matches starting offset of the text
		// on the first line.
		Assertions.assertEquals("/** This is an MLC\n *  \n */", textArea.getText());
		Assertions.assertEquals("/** This is an MLC\n *  ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_unclosed_secondLine() {

		String code = "/**\n * ";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New empty line, indented properly, added for the caret and another line closing the MLC also added
		Assertions.assertEquals("/**\n * \n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * \n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_unclosed_secondLine_contentAfterStar() {

		String code = "/**\n * Comment line 1";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New empty line, indented properly, added for the caret and another line closing the MLC also added
		Assertions.assertEquals("/**\n * Comment line 1\n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * Comment line 1\n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_closed_firstLine() {

		String code = "/**\n */";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New line with a single asterisk is added and indented properly
		Assertions.assertEquals("/**\n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_closed_firstLine_contentAfterStar() {

		String code = "/** Some text\n */";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New line with a single asterisk is added and indented properly.
		// Note indentation for new text (caret position) matches starting offset of the text
		// on the first line.
		Assertions.assertEquals("/** Some text\n *  \n */", textArea.getText());
		Assertions.assertEquals("/** Some text\n *  ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_closed_secondLine() {

		String code = "/**\n * \n */";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.lastIndexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New line with a single asterisk is added and indented properly
		Assertions.assertEquals("/**\n * \n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * \n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_closed_secondLine_contentAfterStar() {

		String code = "/**\n * Comment line 1\n */";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.lastIndexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New line with a single asterisk is added and indented properly
		Assertions.assertEquals("/**\n * Comment line 1\n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * Comment line 1\n * ".length(), textArea.getCaretPosition());
	}

	@Test
	void test_inDocComment_closed_secondLine_caretBeforeLeadingWhitespaceAndStar() {

		String code = "/**\n * \n */";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setCaretPosition(code.indexOf('\n') + 1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		insertBreakAction.actionPerformed(e);

		// New line with a single asterisk is added and indented properly
		Assertions.assertEquals("/**\n * \n * \n */", textArea.getText());
		Assertions.assertEquals("/**\n * \n * ".length(), textArea.getCaretPosition());
	}
}
