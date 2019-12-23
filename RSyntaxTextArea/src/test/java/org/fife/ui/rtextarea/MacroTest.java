/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;


/**
 * Unit tests for the {@link Macro} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MacroTest {

	private static final String MACRO_XML = "<macro>\n" +
		"  <macroName>macroFile</macroName>\n" +
		"  <action id=\"default-typed\">abcdefg</action>\n" +
		"  <action id=\"default-typed\">123</action>\n" +
		"  ...\n" +
		"</macro>";


	@Test
	public void testConstructor_zeroArg() {
		Assert.assertNotNull(new Macro().getName());
	}


	@Test
	public void testConstructor_fileArg() throws IOException {

		File file = File.createTempFile("rstaUnitTests", ".tmp");
		try (PrintWriter w = new PrintWriter(file)) {
			w.print(MACRO_XML);
		}
		file.deleteOnExit();

		Macro macro = new Macro(file);
		Assert.assertEquals("macroFile", macro.getName());
		Assert.assertEquals(2, macro.getMacroRecords().size());
	}


	@Test
	public void testConstructor_stringName() {
		Macro macro = new Macro("foo");
		Assert.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	public void testConstructor_stringNameAndRecords_nullRecords() {
		Macro macro = new Macro("foo", null);
		Assert.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	public void testConstructor_stringNameAndRecords_nonNullullRecords() {
		Macro macro = new Macro("foo", Collections.singletonList(
			new Macro.MacroRecord("foo", "bar")
		));
		Assert.assertEquals(1, macro.getMacroRecords().size());
	}


	@Test
	public void testAddMacroRecord_happyPath() {

		Macro macro = new Macro("foo");
		Assert.assertEquals(0, macro.getMacroRecords().size());

		macro.addMacroRecord(new Macro.MacroRecord());
		Assert.assertEquals(1, macro.getMacroRecords().size());
	}


	@Test
	public void testAddMacroRecord_null() {

		Macro macro = new Macro("foo");
		Assert.assertEquals(0, macro.getMacroRecords().size());

		macro.addMacroRecord(null);
		Assert.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	public void testGetSetName() {
		Macro macro = new Macro("foo");
		Assert.assertEquals("foo", macro.getName());
		macro.setName("bar");
		Assert.assertEquals("bar", macro.getName());
	}


	@Test
	public void testSaveToFile() throws IOException {

		File file = File.createTempFile("rstaUnitTests", ".tmp");
		file.deleteOnExit();

		Macro macro = new Macro("test");
		macro.saveToFile(file);
		Assert.assertTrue(file.length() > 0);
	}
}
