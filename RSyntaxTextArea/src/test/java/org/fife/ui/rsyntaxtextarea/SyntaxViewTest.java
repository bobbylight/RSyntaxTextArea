/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import java.awt.*;


/**
 * Unit tests for the {@code SyntaxView} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SyntaxViewTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetNextVisualPositionFrom_north_onFirstLine() throws BadLocationException {

		String content = "This is the text";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, content);
		textArea.setCaretPosition(content.indexOf("is"));

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Position.Bias[] biasRet = new Position.Bias[1];

		// Pressing the up arrow on the top line moves the caret to offset 0
		int actual = view.getNextVisualPositionFrom(textArea.getCaretPosition(),
			Position.Bias.Backward, textArea.getBounds(), SwingConstants.NORTH, biasRet);
		Assertions.assertEquals(0, actual);
	}


	@Test
	void testGetNextVisualPositionFrom_south_onLastLine() throws BadLocationException {

		String content = "This is the text";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, content);
		textArea.setCaretPosition(content.indexOf("is"));

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Position.Bias[] biasRet = new Position.Bias[1];

		// Pressing the up arrow on the top line moves the caret to offset 0
		int actual = view.getNextVisualPositionFrom(textArea.getCaretPosition(),
			Position.Bias.Forward, textArea.getBounds(), SwingConstants.SOUTH, biasRet);
		Assertions.assertEquals(content.length(), actual);
	}


	@Test
	void testGetTokenListForPhysicalLineAbove_foldingEnabled() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2");

		int line2StartOffs = textArea.getLineStartOffset(2);

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Token token = view.getTokenListForPhysicalLineAbove(line2StartOffs);

		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "line"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "1"));
	}


	@Test
	void testGetTokenListForPhysicalLineAbove_noFolding() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2");
		textArea.setCodeFoldingEnabled(false);

		int line2StartOffs = textArea.getLineStartOffset(2);

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Token token = view.getTokenListForPhysicalLineAbove(line2StartOffs);

		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "line"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "1"));
	}


	@Test
	void testGetTokenListForPhysicalLineBelow_foldingEnabled() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2\nline 3");

		int line2StartOffs = textArea.getLineStartOffset(2);

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Token token = view.getTokenListForPhysicalLineBelow(line2StartOffs);

		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "line"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "3"));
	}


	@Test
	void testGetTokenListForPhysicalLineBelow_noFolding() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2\nline 3");
		textArea.setCodeFoldingEnabled(false);

		int line2StartOffs = textArea.getLineStartOffset(2);

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);
		Token token = view.getTokenListForPhysicalLineBelow(line2StartOffs);

		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "line"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "3"));
	}


	@Test
	void testModelToView_fiveArg_endOfText() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2\nline 3");
		textArea.setCodeFoldingEnabled(false);

		SyntaxView view = (SyntaxView)textArea.getUI().getRootView(textArea).getView(0);

		int offs0 = textArea.getText().indexOf("line 1");
		int offs1 = textArea.getText().length() + 1;
		Rectangle r = textArea.getVisibleRect();
		Shape result = view.modelToView(offs0, Position.Bias.Forward,
			offs1, Position.Bias.Backward,
			r);

		Assertions.assertNotNull(result);
	}


	@Test
	void testNextTabStop_offset0() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2\nline 3");
		textArea.setCodeFoldingEnabled(false);
		textArea.setCaretPosition(0);

		SyntaxView view = (SyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		Assertions.assertTrue(view.nextTabStop(0, 0) > 0);
	}


	@Test
	void testNextTabStop_tabSize0() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE,
			"line 0\nline 1\nline 2\nline 3");
		textArea.setCodeFoldingEnabled(false);
		textArea.setCaretPosition(0);
		textArea.setTabSize(0);

		SyntaxView view = (SyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		Assertions.assertEquals(0, view.nextTabStop(0, 0), 0.001);
	}


	@Test
	void testPaint_foldedFolds() {

		RSyntaxTextArea textArea = createTextArea();
		// Fold one child fold so there is a mix of expanded and collapsed folds,
		// and there is > 1 line at all
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		textArea.setCaretPosition(1);
		textArea.moveCaretPosition(textArea.getDocument().getLength() - 1);

		SyntaxView view = (SyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		view.paint(createTestGraphics(), textArea.getVisibleRect());
	}


	@Test
	void testPaint_noSelection() {

		RSyntaxTextArea textArea = createTextArea("one two three");
		textArea.setEOLMarkersVisible(true);

		SyntaxView view = (SyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		view.paint(createTestGraphics(), textArea.getVisibleRect());
	}


	@Test
	void testPaint_selection_selectionStartingInOneTokenAndEndingInAnother() {

		RSyntaxTextArea textArea = createTextArea("one two three");
		textArea.setCaretPosition(1);
		textArea.moveCaretPosition(5); // Between 't' and 'w'
		textArea.setEOLMarkersVisible(true);

		SyntaxView view = (SyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		view.paint(createTestGraphics(), textArea.getVisibleRect());
	}
}
