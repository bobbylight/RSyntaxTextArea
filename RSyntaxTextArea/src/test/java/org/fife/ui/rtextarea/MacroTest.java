/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
class MacroTest {

	private static final String MACRO_XML = "<macro>\n" +
		"  <macroName>macroFile</macroName>\n" +
		"  <action id=\"default-typed\">abcdefg</action>\n" +
		"  <action id=\"default-typed\">123</action>\n" +
		"  ...\n" +
		"</macro>";


	@Test
	void testConstructor_zeroArg() {
		Assertions.assertNotNull(new Macro().getName());
	}


	@Test
	void testConstructor_fileArg() throws IOException {

		File file = File.createTempFile("rstaUnitTests", ".tmp");
		try (PrintWriter w = new PrintWriter(file)) {
			w.print(MACRO_XML);
		}
		file.deleteOnExit();

		Macro macro = new Macro(file);
		Assertions.assertEquals("macroFile", macro.getName());
		Assertions.assertEquals(2, macro.getMacroRecords().size());
	}


	@Test
	void testConstructor_stringName() {
		Macro macro = new Macro("foo");
		Assertions.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	void testConstructor_stringNameAndRecords_nullRecords() {
		Macro macro = new Macro("foo", null);
		Assertions.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	void testConstructor_stringNameAndRecords_nonNullRecords() {
		Macro macro = new Macro("foo", Collections.singletonList(
			new Macro.MacroRecord("foo", "bar")
		));
		Assertions.assertEquals(1, macro.getMacroRecords().size());
	}


	@Test
	void testAddMacroRecord_happyPath() {

		Macro macro = new Macro("foo");
		Assertions.assertEquals(0, macro.getMacroRecords().size());

		macro.addMacroRecord(new Macro.MacroRecord());
		Assertions.assertEquals(1, macro.getMacroRecords().size());
	}


	@Test
	void testAddMacroRecord_null() {

		Macro macro = new Macro("foo");
		Assertions.assertEquals(0, macro.getMacroRecords().size());

		macro.addMacroRecord(null);
		Assertions.assertEquals(0, macro.getMacroRecords().size());
	}


	@Test
	void testGetSetName() {
		Macro macro = new Macro("foo");
		Assertions.assertEquals("foo", macro.getName());
		macro.setName("bar");
		Assertions.assertEquals("bar", macro.getName());
	}


	@Test
	void testSaveToFile() throws IOException {

		File file = File.createTempFile("rstaUnitTests", ".tmp");
		file.deleteOnExit();

		Macro macro = new Macro("test");
		macro.saveToFile(file);
		Assertions.assertTrue(file.length() > 0);
	}
}
