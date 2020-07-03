package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;


/**
 * Unit tests for the {@link RTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaTest {

	private IconGroup origIconGroup;


	@Before
	public void setUp() {
		origIconGroup = RTextArea.getIconGroup();
	}


	@After
	public void tearDown() {
		if (origIconGroup != null) {
			RTextArea.setIconGroup(origIconGroup);
		}
	}


	@Test
	public void testCanRedo() {
		RTextArea textArea = new RTextArea();
		Assert.assertFalse(textArea.canRedo());
		textArea.replaceSelection("Hi");
		Assert.assertFalse(textArea.canRedo());
		textArea.undoLastAction();
		Assert.assertTrue(textArea.canRedo());
		textArea.redoLastAction();
		Assert.assertFalse(textArea.canRedo());
	}


	@Test
	public void testCanUndo() {
		RTextArea textArea = new RTextArea();
		Assert.assertFalse(textArea.canUndo());
		textArea.replaceSelection("Hi");
		Assert.assertTrue(textArea.canUndo());
		textArea.undoLastAction();
		Assert.assertFalse(textArea.canUndo());
	}


	@Test
	public void testCreateDefaultModel() {
		RTextArea textArea = new RTextArea();
		Assert.assertTrue(textArea.createDefaultModel() instanceof RDocument);
	}


	@Test
	public void testDiscardAllEdits() {
		RTextArea textArea = new RTextArea();
		textArea.replaceSelection("Hi");
		Assert.assertTrue(textArea.canUndo());
		textArea.discardAllEdits();
		Assert.assertFalse(textArea.canUndo());
	}


	@Test
	public void testGetPopupMenu() {
		RTextArea textArea = new RTextArea();
		Assert.assertNotNull(textArea.getPopupMenu());
	}


	@Test
	public void testGetSetIconGroup() {
		RTextArea.setIconGroup(new IconGroup("test", "group"));
	}


	@Test
	public void testGetSetToolTipSupplier() {
		RTextArea textArea = new RTextArea();
		Assert.assertNull(textArea.getToolTipSupplier());
		textArea.setToolTipSupplier((textArea1, e) -> null);
		Assert.assertNotNull(textArea.getToolTipSupplier());
	}


	@Test
	public void testRecordingMacro_happyPath() {
		Assert.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.beginRecordingMacro();
		Assert.assertTrue(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assert.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	public void testRecordingMacro_endWhileNotRecording() {
		Assert.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assert.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	public void testReplaceSelection_null() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setSelectionStart(1);
		textArea.setSelectionEnd(3);
		textArea.replaceSelection(null);
		Assert.assertEquals("le 1\nline 2", textArea.getText());
	}


	@Test
	public void testReplaceSelection_tabsEmulatedWithWhiteSpace_insertMode() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t");
		Assert.assertEquals("    line 1\nline 2", textArea.getText());
	}


	@Test
	public void testReplaceSelection_tabsEmulatedWithWhiteSpace_unevenOffset() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(1);
		textArea.replaceSelection("\t");
		Assert.assertEquals("l   ine 1\nline 2", textArea.getText());
	}


	@Test
	public void testReplaceSelection_tabsEmulatedWithWhiteSpace_twoTabs() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t\t");
		Assert.assertEquals("        line 1\nline 2", textArea.getText());
	}


	@Test
	public void testReplaceSelection_tabsEmulatedWithWhiteSpace_mixedContent() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t\nadded");
		Assert.assertEquals("    \naddedline 1\nline 2", textArea.getText());
	}


	@Test
	public void testReplaceSelection_tabsEmulatedWithWhiteSpace_overwriteMode() {
		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setTabsEmulated(true);
		textArea.setTextMode(RTextArea.OVERWRITE_MODE);
		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.replaceSelection("\t");
		Assert.assertEquals("     1\nline 2", textArea.getText());
	}


	@Test
	public void testMarkAllOnOccurrenceSearches() {
		RTextArea textArea = new RTextArea();
		Assert.assertTrue(textArea.getMarkAllOnOccurrenceSearches());
		textArea.setMarkAllOnOccurrenceSearches(false);
		Assert.assertFalse(textArea.getMarkAllOnOccurrenceSearches());
	}


	@Test
	public void testSetCaretStyle_nullDoesntThrowException() {
		RTextArea textArea = new RTextArea();
		textArea.setCaretStyle(RTextArea.INSERT_MODE, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDocument_errorIfIncorrectType() {
		RTextArea textArea = new RTextArea();
		textArea.setDocument(new DefaultStyledDocument());
	}


	@Test
	public void testSetPopupMenu() {
		RTextArea textArea = new RTextArea();
		JPopupMenu popup = new JPopupMenu();
		textArea.setPopupMenu(popup);
		Assert.assertEquals(popup, textArea.getPopupMenu());
	}


	@Test
	public void setTextMode_invalidMode() {
		RTextArea textArea = new RTextArea();
		Assert.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
		textArea.setTextMode(-7);
		Assert.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
	}
}
