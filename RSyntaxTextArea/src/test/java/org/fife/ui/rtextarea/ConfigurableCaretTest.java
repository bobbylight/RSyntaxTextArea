/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;



import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;


/**
 * Unit tests for the {@link ConfigurableCaret} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class ConfigurableCaretTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testDeistall_rTextAreaRequired() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ConfigurableCaret().deinstall(new JTextArea());
		});
	}


	@Test
	void testGetSetPasteOnMiddleClick() {
		ConfigurableCaret caret = new ConfigurableCaret();
		Assertions.assertTrue(caret.getPasteOnMiddleMouseClick());
		caret.setPasteOnMiddleMouseClick(false);
		Assertions.assertFalse(caret.getPasteOnMiddleMouseClick());
	}


	@Test
	void testGetSetRoundedSelectionEdges() {
		ConfigurableCaret caret = new ConfigurableCaret();
		Assertions.assertFalse(caret.getRoundedSelectionEdges());
		caret.setRoundedSelectionEdges(true);
		Assertions.assertTrue(caret.getRoundedSelectionEdges());
	}


	@Test
	void testGetSetStyle() {
		ConfigurableCaret caret = new ConfigurableCaret();
		Assertions.assertEquals(CaretStyle.THICK_VERTICAL_LINE_STYLE, caret.getStyle());
		caret.setStyle(CaretStyle.BLOCK_STYLE);
		Assertions.assertEquals(CaretStyle.BLOCK_STYLE, caret.getStyle());
	}


	@Test
	void testSetStyle_null() {
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.setStyle(null);
		Assertions.assertEquals(CaretStyle.THICK_VERTICAL_LINE_STYLE, caret.getStyle());
	}


	@Test
	void testGetTextArea() {

		ConfigurableCaret caret = new ConfigurableCaret();
		Assertions.assertNull(caret.getTextArea());

		caret.install(new RTextArea());
		Assertions.assertNotNull(caret.getTextArea());

		caret.deinstall(caret.getTextArea());
	}


	@Test
	void testInstall_rTextAreaRequired() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ConfigurableCaret().install(new JTextArea());
		});
	}


	@Test
	void testInstallDeinstall() {

		RTextArea textArea = new RTextArea();

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);
		caret.deinstall(textArea);
	}


	@Test
	void testIsSetAlwaysVisible() {

		ConfigurableCaret caret = new ConfigurableCaret();
		Assertions.assertFalse(caret.isAlwaysVisible());
		caret.setAlwaysVisible(true);
		Assertions.assertTrue(caret.isAlwaysVisible());
	}


	@Test
	@Disabled("This test doesn't work in CI as the system clipboard will be blank")
	void testMouseClicked_pasteOnMiddleMouseDown() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		textArea.getToolkit().getSystemClipboard().setContents(
			new StringSelection("foo"), null);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		caret.mouseClicked(e);

		// Clipboard contents should be inserted into the document
		Assertions.assertEquals("foo", textArea.getText());
	}


	@Test
	void testMousePressed_doubleClickSelectsWord() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button double click.  Note that what word that
		// gets selected is based off of caret position, not mouse X/Y, since
		// the caret gets updated before this is called, so that value is used.
		// Thus we must set the caret position to the first word if we want to
		// mimic the user clicking on it.
		textArea.setCaretPosition(0);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 0,
			2, 2, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// The word at the x, y coordinates was selected
		Assertions.assertEquals("foo", textArea.getSelectedText());
	}


	@Test
	void testMousePressed_tripleClickSelectsLine() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button double click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 3, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// The entire line was selected
		Assertions.assertEquals("foo bar", textArea.getSelectedText());
	}


	@Test
	void testPaint() {
		for (CaretStyle style : CaretStyle.values()) {
			testPaintImpl(style);
		}
	}


	private void testPaintImpl(CaretStyle caretStyle) {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.setStyle(caretStyle);
		caret.install(textArea);
		caret.setVisible(true);

		caret.paint(createTestGraphics());

		caret.deinstall(textArea);
	}


	@Test
	void testSetCaretPosition_skipsOverFold() {

		String code = "{\n" +
			"  {\n" +
			"    printf('hi');\n" +
			"  }\n" +
			"  printf('bye');\n" +
			"}";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, code);

		// Ensure caret is at the end of the document, and try to go "backward"
		// to offset 10
		ConfigurableCaret caret = (ConfigurableCaret)textArea.getCaret();
		caret.setDot(code.length());
		int newCaretOffs = 10;

		// Collapse the "middle" fold, where the caret will be
		textArea.getFoldManager().reparse();
		Fold fold = textArea.getFoldManager().getDeepestFoldContaining(newCaretOffs);
		fold.setCollapsed(true);

		caret.setDot(newCaretOffs);

		// Caret actually gets placed at offset 5, which is just after the
		// opening curly of the closed fold that contains the target offset
		// of 10.
		Assertions.assertEquals(5, caret.getDot());
	}


	@Test
	void testSetCaretPosition_skipsOverFold_forward() {

		String code = "{\n" +
			"  {\n" +
			"    printf('hi');\n" +
			"  }\n" +
			"  printf('bye');\n" +
			"}";

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, code);

		// Ensure caret is at the beginning of the document, and try to go "forward"
		// to offset 10
		ConfigurableCaret caret = (ConfigurableCaret)textArea.getCaret();
		caret.setDot(0);
		int newCaretOffs = 10;

		// Collapse the "middle" fold, where the caret will be
		textArea.getFoldManager().reparse();
		Fold fold = textArea.getFoldManager().getDeepestFoldContaining(newCaretOffs);
		fold.setCollapsed(true);

		caret.setDot(newCaretOffs);

		// Caret actually gets placed at offset 28, the start of the
		// line *after* the last one in the collapsed fold.
		Assertions.assertEquals(28, caret.getDot());
	}
}
