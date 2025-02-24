/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;


/**
 * Unit tests for the {@link RTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaTest {

	private IconGroup origIconGroup;


	@BeforeEach
	void setUp() {
		origIconGroup = RTextArea.getIconGroup();
	}


	@AfterEach
	void tearDown() {
		if (origIconGroup != null) {
			RTextArea.setIconGroup(origIconGroup);
		}
		RTextArea.loadMacro(null);
	}


	@Test
	void testAddLineHighlight_happyPath() throws BadLocationException {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		Object tag = textArea.addLineHighlight(1, Color.BLUE);
		Assertions.assertNotNull(tag);
	}


	@Test
	void testAddLineHighlight_errorOnInvalidLine() {
		RTextArea textArea = new RTextArea();
		Assertions.assertThrows(BadLocationException.class,
			() -> textArea.addLineHighlight(1, Color.BLUE));
	}


	@Test
	void testCanRedo() {
		RTextArea textArea = new RTextArea();
		Assertions.assertFalse(textArea.canRedo());
		textArea.replaceSelection("Hi");
		Assertions.assertFalse(textArea.canRedo());
		textArea.undoLastAction();
		Assertions.assertTrue(textArea.canRedo());
		textArea.redoLastAction();
		Assertions.assertFalse(textArea.canRedo());
	}


	@Test
	void testCanUndo() {
		RTextArea textArea = new RTextArea();
		Assertions.assertFalse(textArea.canUndo());
		textArea.replaceSelection("Hi");
		Assertions.assertTrue(textArea.canUndo());
		textArea.undoLastAction();
		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testCreateDefaultModel() {
		RTextArea textArea = new RTextArea();
		Assertions.assertInstanceOf(RDocument.class, textArea.createDefaultModel());
	}


	@Test
	void testDiscardAllEdits() {
		RTextArea textArea = new RTextArea();
		textArea.replaceSelection("Hi");
		Assertions.assertTrue(textArea.canUndo());
		textArea.discardAllEdits();
		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testGetAction_invalidActionValue() {
		Assertions.assertNull(RTextArea.getAction(-1));
		Assertions.assertNull(RTextArea.getAction(1000));
	}


	@Test
	void testGetAction_validActionValues() {

		// Actions are lazily instantiated on first RSTA creation
		new RTextArea();

		Assertions.assertNotNull(RTextArea.getAction(RTextArea.COPY_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.CUT_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.DELETE_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.PASTE_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.REDO_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION));
		Assertions.assertNotNull(RTextArea.getAction(RTextArea.UNDO_ACTION));
	}


	@Test
	void testGetCurrentMacro_nothingBeingRecorded() {
		Assertions.assertNull(RTextArea.getCurrentMacro());
	}


	@Test
	void testGetPopupMenu() {
		RTextArea textArea = new RTextArea();
		Assertions.assertNotNull(textArea.getPopupMenu());
	}


	@Test
	void testGetSetIconGroup() {
		RTextArea.setIconGroup(new IconGroup("test", "group"));
	}


	@Test
	void testGetSetToolTipSupplier() {
		RTextArea textArea = new RTextArea();
		Assertions.assertNull(textArea.getToolTipSupplier());
		textArea.setToolTipSupplier((textArea1, e) -> null);
		Assertions.assertNotNull(textArea.getToolTipSupplier());
	}


	@Test
	void testGetToolTipText_noToolTipSupplier() {
		RTextArea textArea = new RTextArea();
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 1, false);
		Assertions.assertNull(textArea.getToolTipText(e));
	}


	@Test
	void testGetToolTipText_withToolTipSupplier() {

		String tip = "Tool tip text";
		RTextArea textArea = new RTextArea();
		textArea.setToolTipSupplier((textArea1, e) -> tip);

		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 1, false);
		Assertions.assertEquals(tip, textArea.getToolTipText(e));
	}


	@Test
	void testMarkAllOnOccurrenceSearches() {
		RTextArea textArea = new RTextArea();
		Assertions.assertTrue(textArea.getMarkAllOnOccurrenceSearches());
		textArea.setMarkAllOnOccurrenceSearches(false);
		Assertions.assertFalse(textArea.getMarkAllOnOccurrenceSearches());
	}


	@Test
	void testRead_withDesc() throws IOException {

		StringReader sr = new StringReader("Test content");
		RTextArea textArea = new RTextArea();
		textArea.read(sr, "desc");

		Assertions.assertEquals("desc", textArea.getDocument().
			getProperty(Document.StreamDescriptionProperty));
		Assertions.assertEquals("Test content", textArea.getText());
	}


	@Test
	void testRecordingMacro_happyPath() {
		Assertions.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.beginRecordingMacro();
		Assertions.assertTrue(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assertions.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	void testRecordingMacro_endWhileNotRecording() {
		Assertions.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assertions.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	void testRemoveAllLineHighlights() throws BadLocationException {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.addLineHighlight(0, Color.BLUE);

		LineHighlightManager lhm = textArea.getLineHighlightManager();
		Assertions.assertEquals(1, lhm.getCurrentLineHighlightTags().size());

		textArea.removeAllLineHighlights();
		Assertions.assertEquals(0, lhm.getCurrentLineHighlightTags().size());
	}


	@Test
	void testRemoveLineHighlight() throws BadLocationException {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		Object tag = textArea.addLineHighlight(0, Color.BLUE);

		LineHighlightManager lhm = textArea.getLineHighlightManager();
		Assertions.assertEquals(1, lhm.getCurrentLineHighlightTags().size());

		textArea.removeLineHighlight(tag);
		Assertions.assertEquals(0, lhm.getCurrentLineHighlightTags().size());
	}


	@Test
	void testReplaceRange_error_endBeforeStart() {
		RTextArea textArea = new RTextArea();
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> textArea.replaceRange("replacement", 2, 1));
	}


	@Test
	void testReplaceRange_happyPath() {
		RTextArea textArea = new RTextArea("111");
		textArea.replaceRange("2", 1, 2);
		Assertions.assertEquals("121", textArea.getText());
	}


	@Test
	void testReplaceSelection_null() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setSelectionStart(1);
		textArea.setSelectionEnd(3);
		textArea.replaceSelection(null);
		Assertions.assertEquals("le 1\nline 2", textArea.getText());
	}


	@Test
	void testReplaceSelection_tabsEmulatedWithWhiteSpace_insertMode() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t");
		Assertions.assertEquals("    line 1\nline 2", textArea.getText());
	}


	@Test
	void testReplaceSelection_tabsEmulatedWithWhiteSpace_unevenOffset() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(1);
		textArea.replaceSelection("\t");
		Assertions.assertEquals("l   ine 1\nline 2", textArea.getText());
	}


	@Test
	void testReplaceSelection_tabsEmulatedWithWhiteSpace_twoTabs() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t\t");
		Assertions.assertEquals("        line 1\nline 2", textArea.getText());
	}


	@Test
	void testReplaceSelection_tabsEmulatedWithWhiteSpace_mixedContent() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t\nadded");
		Assertions.assertEquals("    \naddedline 1\nline 2", textArea.getText());
	}


	@Test
	void testReplaceSelection_tabsEmulatedWithWhiteSpace_overwriteMode() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTextMode(RTextArea.OVERWRITE_MODE);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t");
		Assertions.assertEquals("     1\nline 2", textArea.getText());
	}


	@Test
	void testSetActionProperties_charMnemonic_doNothingForInvalidAction() {
		RTextArea.setActionProperties(-1, "foo", 'x', null);
	}


	@Test
	void testSetActionProperties_intMnemonic_doNothingForInvalidAction() {
		RTextArea.setActionProperties(-1, "foo", -1, null);
	}


	@Test
	void testSetCaret() {
		RTextArea textArea = new RTextArea();
		Caret newCaret = new ConfigurableCaret();
		textArea.setCaret(newCaret);
		Assertions.assertEquals(newCaret, textArea.getCaret());
	}


	@Test
	void testSetCaretStyle_nullDoesntThrowException() {
		RTextArea textArea = new RTextArea();
		textArea.setCaretStyle(RTextArea.INSERT_MODE, null);
	}


	@Test
	void testSetDocument_errorIfIncorrectType() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RTextArea textArea = new RTextArea();
			textArea.setDocument(new DefaultStyledDocument());
		});
	}


	@Test
	void testSetPopupMenu() {
		RTextArea textArea = new RTextArea();
		JPopupMenu popup = new JPopupMenu();
		textArea.setPopupMenu(popup);
		Assertions.assertEquals(popup, textArea.getPopupMenu());
	}


	@Test
	void setTextMode_invalidMode() {
		RTextArea textArea = new RTextArea();
		Assertions.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
		textArea.setTextMode(-7);
		Assertions.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
	}
}
