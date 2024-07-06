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
import java.awt.*;
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
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ConfigurableCaret().deinstall(new JTextArea()));
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
	void testGetSetSelectionVisible() {

		ConfigurableCaret caret = new ConfigurableCaret();
		RTextArea textArea = new RTextArea();
		caret.install(textArea);

		caret.setSelectionVisible(true); // e.g. made visible
		Assertions.assertTrue(caret.isSelectionVisible());
		caret.setSelectionVisible(false); // Ignored on purpose
		Assertions.assertTrue(caret.isSelectionVisible());

		caret.deinstall(textArea);
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
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ConfigurableCaret().install(new JTextArea()));
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
	void testMouseClicked_doNothing_eventAlreadyConsumed() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		e.consume();
		caret.mouseClicked(e);
	}


	@Test
	void testMouseClicked_doNothing_notMiddleMouseButton() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseClicked(e);
	}


	@Test
	void testMouseClicked_doNothing_pasteOnMiddleMouseClickFalse() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.setPasteOnMiddleMouseClick(false);
		caret.install(textArea);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		caret.mouseClicked(e);
	}


	@Test
	void testMouseClicked_doNothing_middleClickButDoubleClick() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 2, false, MouseEvent.BUTTON2);
		caret.mouseClicked(e);
	}


	@Test
	void testMouseClicked_doNothing_textAreaNotEditable() {

		RTextArea textArea = new RTextArea();
		textArea.setEditable(false);
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		caret.mouseClicked(e);
	}


	@Test
	void testMouseClicked_doNothing_textAreaNotEnabled() {

		RTextArea textArea = new RTextArea();
		textArea.setEnabled(false);
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic middle-mouse button click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		caret.mouseClicked(e);
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
	void testMouseDragged_doNothing_eventAlreadyConsumed() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button drag event
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		e.consume();
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_doNothing_notLeftMouseButton() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic middle-mouse button drag event
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON2);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_doNothing_charSelectionType() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button press
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 20,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_doNothing_textAreaHasNoSize() {

		RTextArea textArea = new RTextArea();
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button double-click (to enable word selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 2, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 20,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_wordSelectionType_draggedForward() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSize(new Dimension(80, 80));
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button double-click (to enable word selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 2, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 20,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_wordSelectionType_draggedBackward() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSize(new Dimension(80, 80));
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button double-click (to enable word selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 20,
			10, 2, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_wordSelectionType_draggedWithinPriorSelection() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSize(new Dimension(80, 80));
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);
		caret.setDot(0);
		caret.moveDot(textArea.getDocument().getLength());

		// Synthetic left-mouse button double-click (to enable word selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 5,
			5, 2, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_lineSelectionType_draggedForward() {

		RTextArea textArea = new RTextArea("Hello world\nHello world");
		textArea.setSize(new Dimension(80, 80));
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button triple-click (to enable line selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 3, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 10,
			40, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMouseDragged_lineSelectionType_draggedBackward() {

		RTextArea textArea = new RTextArea("Hello world\nHello world");
		textArea.setSize(new Dimension(80, 80));
		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button triple-click (to enable line selection)
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 10,
			40, 3, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// Synthetic left-mouse button drag event
		e = new MouseEvent(textArea, 0, 0, 0, 10,
			10, 1, false, MouseEvent.BUTTON1);
		caret.mouseDragged(e);
	}


	@Test
	void testMousePressed_leftButton_doubleClickSelectsWord() {

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
	void testMousePressed_leftButton_tripleClickSelectsLine() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button triple click
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 3, false, MouseEvent.BUTTON1);
		caret.mousePressed(e);

		// The entire line was selected
		Assertions.assertEquals("foo bar", textArea.getSelectedText());
	}


	@Test
	void testMousePressed_rightButton_doNothing_eventConsumed() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic right-mouse button press
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 1, false, MouseEvent.BUTTON3);
		e.consume();
		caret.mousePressed(e);
	}


	@Test
	void testMousePressed_rightButton_doNothing_textAreaNotEnabled() {

		RTextArea textArea = createTextArea("foo bar");
		textArea.setEnabled(false);

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic right-mouse button press
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 1, false, MouseEvent.BUTTON3);
		caret.mousePressed(e);
	}


	@Test
	void testMousePressed_rightButton_doNothing_textAreaNotRequestFocusEnabled() {

		RTextArea textArea = createTextArea("foo bar");
		textArea.setRequestFocusEnabled(false);

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic right-mouse button press
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 1, false, MouseEvent.BUTTON3);
		caret.mousePressed(e);
	}


	@Test
	void testMousePressed_rightButton_success_textAreaRequestsFocus() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic right-mouse button press
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 1, false, MouseEvent.BUTTON3);
		caret.mousePressed(e);
	}


	@Test
	void testMouseReleased() {

		RTextArea textArea = createTextArea("foo bar");

		ConfigurableCaret caret = new ConfigurableCaret();
		caret.install(textArea);

		// Synthetic left-mouse button release
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2,
			2, 1, false, MouseEvent.BUTTON1);
		caret.mouseReleased(e);
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
