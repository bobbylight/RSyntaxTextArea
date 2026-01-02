/*
 * 12/09/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;


/**
 * Unit tests for {@link TextEditorPane}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class TextEditorPaneTest {


	@Test
	void testConstructor_zeroArg() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertEquals(TextEditorPane.INSERT_MODE, textArea.getTextMode());
		Assertions.assertFalse(textArea.getLineWrap());
	}


	@Test
	void testConstructor_oneArg() {
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.OVERWRITE_MODE);
		Assertions.assertEquals(TextEditorPane.OVERWRITE_MODE, textArea.getTextMode());
		Assertions.assertFalse(textArea.getLineWrap());
	}


	@Test
	void testGetFileName_localFile() throws IOException {
		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, loc);
		Assertions.assertEquals(file.getName(), textArea.getFileName());
	}


	@Test
	void testGetFileName_remoteFile() throws IOException {
		FileLocation loc = FileLocation.create("https://google.com/foo.txt");
		FileLocation spyLoc = Mockito.spy(loc);
		InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
		Mockito.doReturn(testInputStream).when(spyLoc).getInputStream();
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, spyLoc);
		Assertions.assertEquals(loc.getFileFullPath(), textArea.getFileFullPath());
	}


	@Test
	void testGetLastSaveOrLoadTime_localFileThatExists() throws IOException {
		File tempFile = createTempFile();
		FileLocation loc = FileLocation.create(tempFile);
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, loc);
		Assertions.assertEquals(tempFile.lastModified(), textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testGetLastSaveOrLoadTime_localFileThatDoesNotExist() {
		Assertions.assertEquals(TextEditorPane.LAST_MODIFIED_UNKNOWN, new TextEditorPane().getLastSaveOrLoadTime());
	}


	@Test
	void testGetLastSaveOrLoadTime_remoteFile() throws IOException {
		FileLocation loc = FileLocation.create("https://google.com/foo.txt");
		FileLocation spyLoc = Mockito.spy(loc);
		InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
		Mockito.doReturn(testInputStream).when(spyLoc).getInputStream();
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, spyLoc);
		Assertions.assertEquals(TextEditorPane.LAST_MODIFIED_UNKNOWN, textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testGetLineSeparator() {
		TextEditorPane textArea = new TextEditorPane();
		textArea.setLineSeparator("\n");
		Assertions.assertEquals("\n", textArea.getLineSeparator());
		textArea.setLineSeparator("\r\n");
		Assertions.assertEquals("\r\n", textArea.getLineSeparator());
	}


	@Test
	void testGetSetEncoding() {

		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());

		textArea.setEncoding("UTF-16");
		Assertions.assertEquals("UTF-16", textArea.getEncoding());
		Assertions.assertTrue(textArea.isDirty());
		textArea.setDirty(false);

		textArea.setEncoding("UTF-8");
		Assertions.assertEquals("UTF-8", textArea.getEncoding());
		Assertions.assertTrue(textArea.isDirty());

	}


	@Test
	void testInsertUpdate_setsDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());
		textArea.insertUpdate(null);
		Assertions.assertTrue(textArea.isDirty());
	}


	@Test
	void testIsLocal() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertTrue(textArea.isLocal());
	}


	@Test
	void testIsLocalAndExists() throws IOException {
		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, loc);
		Assertions.assertTrue(textArea.isLocalAndExists());
		Assertions.assertTrue(file.delete());
		Assertions.assertFalse(textArea.isLocalAndExists());
	}


	@Test
	void testIsModifiedOutsideEditor() throws IOException {

		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);
		FileLocation spyLoc = Mockito.spy(loc);

		// Simulate the file's "last modified" timestamp changing
		Mockito.doReturn(100L, 150L).when(spyLoc).getActualLastModified();

		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, loc);
		Assertions.assertFalse(textArea.isModifiedOutsideEditor());

		try (PrintWriter w = new PrintWriter(file)) {
			w.println("lorem ipsum");
		}
		Assertions.assertTrue(textArea.isModifiedOutsideEditor());
	}


	@Test
	void testIsSetReadOnly() {
		boolean[] called = { false };
		TextEditorPane textArea = new TextEditorPane();
		textArea.addPropertyChangeListener(TextEditorPane.READ_ONLY_PROPERTY,
			e -> called[0] = true);
		Assertions.assertFalse(textArea.isReadOnly());
		textArea.setReadOnly(true);
		Assertions.assertTrue(textArea.isReadOnly());
		Assertions.assertTrue(called[0]);
	}


	@Test
	void testLoad_noCharsetArg_localNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		FileLocation loc = FileLocation.create(createTempFile());
		textArea.load(loc);

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(loc.getActualLastModified(), textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testLoad_charset_localNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		FileLocation loc = FileLocation.create(createTempFile());
		textArea.load(loc, StandardCharsets.UTF_8);

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(loc.getActualLastModified(), textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testLoad_stringCharset_loadNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		FileLocation loc = FileLocation.create(createTempFile());
		textArea.load(loc, "utf-8");

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(loc.getActualLastModified(), textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testLoad_stringCharset_fileDoesNotExist() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = createTempFile();
		Assertions.assertTrue(file.delete());
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		// Verify the editor's contents are cleared and its state is as expected.
		Assertions.assertEquals("", textArea.getText());
		Assertions.assertEquals("utf-8", textArea.getEncoding());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(0, textArea.getCaretPosition());
		Assertions.assertEquals(loc.getActualLastModified(), textArea.getLastSaveOrLoadTime());
	}

	@Test
	void testLoad_stringCharset_fileExists() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file that already exists and has content.
		File file = createTempFile();
		try (PrintWriter w = new PrintWriter(file)) {
			w.println("lorem ipsum");
		}
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		// Verify file loaded properly and properties are as expected
		Assertions.assertEquals("lorem ipsum\n", textArea.getText());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(0, textArea.getCaretPosition());
		Assertions.assertEquals(loc.getActualLastModified(), textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testReload_localFile_exists() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = createTempFile();
		try (PrintWriter w = new PrintWriter(file)) {
			w.println("lorem ipsum");
		}
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		// Verify file loaded properly and properties are as expected
		Assertions.assertEquals("lorem ipsum\n", textArea.getText());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(0, textArea.getCaretPosition());

		// Modify it
		textArea.append("added");

		textArea.reload();

		// Verify things are back to the way they were before the edit
		Assertions.assertEquals("lorem ipsum\n", textArea.getText());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
		Assertions.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	void testReload_localFile_doesNotExist_newAndUnsaved() {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo");

		// For local, unsaved files, reload does nothing
		Assertions.assertDoesNotThrow(textArea::reload);
		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertTrue(textArea.canUndo());
		Assertions.assertTrue(textArea.isDirty());
		Assertions.assertEquals(TextEditorPane.LAST_MODIFIED_UNKNOWN, textArea.getLastSaveOrLoadTime());
	}


	@Test
	void testReload_localFile_doesNotExist_fileDeletedFromUnderUs() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");
		textArea.append("lorem ipsum");
		textArea.save();

		Assertions.assertTrue(file.delete());
		Assertions.assertThrows(IOException.class, textArea::reload);
	}


	@Test
	void testReload_remoteFile() throws IOException {

		FileLocation loc = FileLocation.create("https://google.com/foo.txt");
		FileLocation spyLoc = Mockito.spy(loc);
		InputStream testInputStream = new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8));
		Mockito.doReturn(testInputStream).when(spyLoc).getInputStream();

		TextEditorPane textArea = new TextEditorPane(TextEditorPane.INSERT_MODE, false, spyLoc);
		Assertions.assertEquals("Hello world", textArea.getText());

		// Modify the text area's contents, then reload
		textArea.append("added");
		testInputStream.reset(); // Allow it to be read again
		Assertions.assertDoesNotThrow(textArea::reload);

		Assertions.assertEquals("Hello world", textArea.getText());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testRemoveUpdate_setsDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());
		textArea.removeUpdate(null);
		Assertions.assertTrue(textArea.isDirty());
	}


	@Test
	void testSave_loadNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();

		// Load a file location that doesn't exist yet
		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		textArea.append("lorem ipsum");
		textArea.save();

		// Assert file contents were saved and editor state is updated
		Assertions.assertEquals(11, file.length());
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testSaveAs() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("lorem ipsum");

		// Load a file location that doesn't exist yet
		File file = createTempFile();
		FileLocation loc = FileLocation.create(file);

		textArea.saveAs(loc);

		// Assert file contents were saved and editor state is updated
		Assertions.assertTrue(file.length() > 0); // Allow for BOM vs. no BOM (varying defaults)
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testSaveAs_nullFileLoc() {
		Assertions.assertThrows(NullPointerException.class, () -> new TextEditorPane().saveAs(null));
	}


	@Test
	void testSetEncoding_invalidArg_null() {
		Assertions.assertThrows(NullPointerException.class, () -> new TextEditorPane().setEncoding(null));
	}


	@Test
	void testSetEncoding_invalidArg_unsupportedCharset() {
		Assertions.assertThrows(UnsupportedCharsetException.class, () -> new TextEditorPane().setEncoding("xxx"));
	}


	@Test
	void testSetLineSeparator_null() {
		Assertions.assertThrows(NullPointerException.class, () -> new TextEditorPane().setLineSeparator(null));
	}


	@Test
	void testSetLineSeparator_invaliedValue() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new TextEditorPane().setLineSeparator("x"));
	}


	@Test
	void testSetLineSeparator_validValues() {
		String[] validSeparators = { "\r", "\n", "\r\n" };
		TextEditorPane textArea = new TextEditorPane();

		for (String sep : validSeparators) {
			textArea.setLineSeparator(sep);
			Assertions.assertEquals(sep, textArea.getLineSeparator());
		}
	}


	@Test
	void testSetLineSeparator_notSettingDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());
		textArea.setLineSeparator("\r", false);
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testSetLineSeparator_settingDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());
		textArea.setLineSeparator("\r", true);
		Assertions.assertTrue(textArea.isDirty());
	}


	@Test
	void testSetLineSeparator_settingDirtyFlagByDefault() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isDirty());
		textArea.setLineSeparator("\r");
		Assertions.assertTrue(textArea.isDirty());
	}


	private static File createTempFile() throws IOException {
		File tempFile = File.createTempFile("unitTest", ".tmp");
		tempFile.deleteOnExit();
		return tempFile;
	}


}
