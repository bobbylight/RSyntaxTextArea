/*
 * 12/09/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


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
	void testIsSetReadOnly() {
		TextEditorPane textArea = new TextEditorPane();
		Assertions.assertFalse(textArea.isReadOnly());
		textArea.setReadOnly(true);
		Assertions.assertTrue(textArea.isReadOnly());
	}


	@Test
	void testLoad_noCharsetArg_localNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc);

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testLoad_charset_localNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, StandardCharsets.UTF_8);

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testLoad_string_loadNonExistentFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		// File didn't exist, check accordingly
		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testLoad_string_fileExists() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
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
	}


	@Test
	void testReload() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
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
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
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
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);

		textArea.saveAs(loc);

		// Assert file contents were saved and editor state is updated
		Assertions.assertTrue(file.length() > 0); // Allow for BOM vs. no BOM (varying defaults)
		Assertions.assertFalse(textArea.isDirty());
	}


	@Test
	void testSetEncoding_invalidArg_null() {
		Assertions.assertThrows(NullPointerException.class, () -> new TextEditorPane().setEncoding(null));
	}


	@Test
	void testSetEncoding_invalidArg_unsupportedCharset() {
		Assertions.assertThrows(UnsupportedCharsetException.class, () -> new TextEditorPane().setEncoding("xxx"));
	}


}
