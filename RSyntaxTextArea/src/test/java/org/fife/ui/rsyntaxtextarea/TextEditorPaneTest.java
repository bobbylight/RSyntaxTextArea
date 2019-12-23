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

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for {@link TextEditorPane}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class TextEditorPaneTest {


	@Test
	public void testConstructor_zeroArg() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertEquals(TextEditorPane.INSERT_MODE, textArea.getTextMode());
		Assert.assertFalse(textArea.getLineWrap());
	}


	@Test
	public void testConstructor_oneArg() {
		TextEditorPane textArea = new TextEditorPane(TextEditorPane.OVERWRITE_MODE);
		Assert.assertEquals(TextEditorPane.OVERWRITE_MODE, textArea.getTextMode());
		Assert.assertFalse(textArea.getLineWrap());
	}


	@Test
	public void testGetSetEncoding() {

		TextEditorPane textArea = new TextEditorPane();
		Assert.assertFalse(textArea.isDirty());

		textArea.setEncoding("UTF-16");
		Assert.assertEquals("UTF-16", textArea.getEncoding());
		Assert.assertTrue(textArea.isDirty());
		textArea.setDirty(false);

		textArea.setEncoding("UTF-8");
		Assert.assertEquals("UTF-8", textArea.getEncoding());
		Assert.assertTrue(textArea.isDirty());

	}


	@Test
	public void testInsertUpdate_setsDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertFalse(textArea.isDirty());
		textArea.insertUpdate(null);
		Assert.assertTrue(textArea.isDirty());
	}


	@Test
	public void testIsLocal() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertTrue(textArea.isLocal());
	}


	@Test
	public void testIsSetReadOnly() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertFalse(textArea.isReadOnly());
		textArea.setReadOnly(true);
		Assert.assertTrue(textArea.isReadOnly());
	}


	@Test
	public void testLoad_noCharsetArg_localNonExistantFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc);

		// File didn't exist, check accordingly
		Assert.assertTrue(textArea.getText().isEmpty());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
	}


	@Test
	public void testLoad_charset_localNonExistantFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, StandardCharsets.UTF_8);

		// File didn't exist, check accordingly
		Assert.assertTrue(textArea.getText().isEmpty());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
	}


	@Test
	public void testLoad_string_loadNonExistantFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("foo"); // Just to add to the undo stack

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.delete();
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		// File didn't exist, check accordingly
		Assert.assertTrue(textArea.getText().isEmpty());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
	}


	@Test
	public void testLoad_string_fileExists() throws IOException {

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
		Assert.assertEquals("lorem ipsum\n", textArea.getText());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
		Assert.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	public void testReload() throws IOException {

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
		Assert.assertEquals("lorem ipsum\n", textArea.getText());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
		Assert.assertEquals(0, textArea.getCaretPosition());

		// Modify it
		textArea.append("added");

		textArea.reload();

		// Verify things are back to the way they were before the edit
		Assert.assertEquals("lorem ipsum\n", textArea.getText());
		Assert.assertFalse(textArea.canUndo());
		Assert.assertFalse(textArea.isDirty());
		Assert.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	public void testRemoveUpdate_setsDirtyFlag() {
		TextEditorPane textArea = new TextEditorPane();
		Assert.assertFalse(textArea.isDirty());
		textArea.removeUpdate(null);
		Assert.assertTrue(textArea.isDirty());
	}


	@Test
	public void testSave_loadNonExistantFile() throws IOException {

		TextEditorPane textArea = new TextEditorPane();

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);
		textArea.load(loc, "utf-8");

		textArea.append("lorem ipsum");
		textArea.save();

		// Assert file contents were saved and editor state is updated
		Assert.assertEquals(11, file.length());
		Assert.assertFalse(textArea.isDirty());
	}


	@Test
	public void testSaveAs() throws IOException {

		TextEditorPane textArea = new TextEditorPane();
		textArea.append("lorem ipsum");

		// Load a file location that doesn't exist yet
		File file = File.createTempFile("unitTest", ".tmp");
		file.deleteOnExit();
		FileLocation loc = FileLocation.create(file);

		textArea.saveAs(loc);

		// Assert file contents were saved and editor state is updated
		Assert.assertTrue(file.length() > 0); // Allow for BOM vs. no BOM (varying defaults)
		Assert.assertFalse(textArea.isDirty());
	}


	@Test(expected = NullPointerException.class)
	public void testSetEncoding_invalidArg_null() {
		new TextEditorPane().setEncoding(null);
	}


	@Test(expected = UnsupportedCharsetException.class)
	public void testSetEncoding_invalidArg_unsupportedCharset() {
		new TextEditorPane().setEncoding("xxx");
	}


}
