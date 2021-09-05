package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;


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
		Assertions.assertTrue(textArea.createDefaultModel() instanceof RDocument);
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
	void testMarkAllOnOccurrenceSearches() {
		RTextArea textArea = new RTextArea();
		Assertions.assertTrue(textArea.getMarkAllOnOccurrenceSearches());
		textArea.setMarkAllOnOccurrenceSearches(false);
		Assertions.assertFalse(textArea.getMarkAllOnOccurrenceSearches());
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
