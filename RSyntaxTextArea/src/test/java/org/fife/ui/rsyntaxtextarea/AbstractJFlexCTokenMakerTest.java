/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.modes.CTokenMaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@code AbstractJFlexCTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class AbstractJFlexCTokenMakerTest extends AbstractRSyntaxTextAreaTest {

	private Action insertBreakAction;


	@BeforeEach
	void setUp() {

		// Kind of cheating here, knowing the implementation of this action
		CTokenMaker tm = new CTokenMaker();
		insertBreakAction = tm.getInsertBreakAction();

	}


	@Test
	void testCStyleInsertBreakAction_doNothing_notEditable() {

		String code = "/* Comment */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals(code, textArea.getText());
	}


	@Test
	void testCStyleInsertBreakAction_doNothing_notEnabled() {

		String code = "/* Comment */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals(code, textArea.getText());
	}


	@Test
	void testCStyleInsertBreakAction_defaultBehaviorIfNotInMlc_endOfLine() {

		String code = "/* Comment */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(textArea.getDocument().getLength());

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals(code + '\n', textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(),
			textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_defaultBehaviorIfNotInMlc_startOfLine() {

		String code = "/* Comment */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals('\n' + code, textArea.getText());
		Assertions.assertEquals(1, textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_singleLine_happyPath() {

		String code = "/* Comment */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(2);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		String expected = "/*\nComment */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.indexOf("Comment"), textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_beforeMlc_multiLine_happyPath() {

		String code = "/* \n * Comment\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		String expected = '\n' + code;
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(1, textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_multiLine_caretAtEndOfLine() {

		String code = "/*\n * Comment\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(13);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		String expected = "/*\n * Comment\n * \n */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.lastIndexOf(" * ") + 3, textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_multiLine_caretInMiddleOfLine() {

		String code = "/*\n * Comment\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(9);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		String expected = "/*\n * Com\n * ment\n */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.indexOf("ment"), textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_multiLine_caretInMiddleOfLine_afterWhitespace() {

		String code = "/*\n * This rocks\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(code.indexOf('r')); // space immediately preceding

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		// Note the trailing space before the new line break is currently preserved.
		// This should arguably be trimmed.
		String expected = "/*\n * This \n * rocks\n */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.indexOf('r'), textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_multiLine_caretInMiddleOfLine_beforeWhitespace() {

		String code = "/*\n * This rocks\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(code.indexOf(" r")); // Before the space between words

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		// Note the space is currently kept on the "prior" line, before the new one.
		// This should arguably be trimmed.
		String expected = "/*\n * This \n * rocks\n */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.indexOf('r'), textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_multiLine_caretInLeadingMlcPart() {

		String code = "/*\n * Comment\n */";
		RSyntaxTextArea textArea = createTextArea(code);
		textArea.setCaretPosition(code.indexOf(" *")); // Second line, offset 0

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		String expected = "/*\n * Comment\n * \n */";
		Assertions.assertEquals(expected, textArea.getText());
		Assertions.assertEquals(expected.lastIndexOf(" * ") + 3, textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inMlc_incomplete_atEndOfFirstLine() {

		RSyntaxTextArea textArea = createTextArea("/*");
		textArea.setCaretPosition(2);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals("/*\n * \n */", textArea.getText());
		Assertions.assertEquals(6, textArea.getCaretPosition());
	}


	@Test
	void testCStyleInsertBreakAction_inDocComment_incomplete_atEndOfFirstLine() {

		RSyntaxTextArea textArea = createTextArea(
			SyntaxConstants.SYNTAX_STYLE_JAVA, "/**");
		textArea.setCaretPosition(3);

		ActionEvent e = new ActionEvent(textArea, 0, DefaultEditorKit.insertBreakAction);
		insertBreakAction.actionPerformed(e);

		Assertions.assertEquals("/**\n * \n */", textArea.getText());
		Assertions.assertEquals(7, textArea.getCaretPosition());
	}
}
