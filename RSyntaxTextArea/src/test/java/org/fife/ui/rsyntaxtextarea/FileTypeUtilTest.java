/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Unit tests for the {@code FileTypeUtil} class.
 *
 * @author Robrert Futrell
 * @version 1.0
 */
class FileTypeUtilTest extends AbstractRSyntaxTextAreaTest {


	private static RSyntaxTextArea plainTextArea(String text) {
		return createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, text);
	}


	@Test
	void testFileFilterToPattern_lettersOnly() {
		String fileFilter = "makefile";
		Assertions.assertEquals("^makefile$", FileTypeUtil.fileFilterToPattern(fileFilter).pattern());
	}


	@Test
	void testFileFilterToPattern_wildcardWithExtension() {
		String fileFilter = "*.java";
		Assertions.assertEquals("^.*\\.java$", FileTypeUtil.fileFilterToPattern(fileFilter).pattern());
	}


	@Test
	void testFileFilterToPattern_anyCharMatch() {
		String fileFilter = "any?char";
		Assertions.assertEquals("^any.char$", FileTypeUtil.fileFilterToPattern(fileFilter).pattern());
	}


	@Test
	void testFileFilterToPattern_dollarSignsAreEscaped() {
		String fileFilter = "*.$money";
		Assertions.assertEquals("^.*\\.\\$money$", FileTypeUtil.fileFilterToPattern(fileFilter).pattern());
	}


	@Test
	void testGetDefaultContentTypeToFilterMap() {
		Assertions.assertNotNull(FileTypeUtil.get().getDefaultContentTypeToFilterMap());
	}


	@Test
	void testGuessContentType_textAreaArg_shellScripts() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/bin/sh\necho hi",
			"#!/bin/bash\necho hi",
			"#!/bin/zsh\necho hi",
			"#!/usr/bin/env bash\necho hi",
			"#!/usr/local/bin/bash\necho hi",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_perl() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env perl\nprint(\"Hello world!\n\");",
			"#!/usr/bin/env perl -w\nprint(\"Hello world!\n\");",
			"#!/usr/bin/perl\nprint(\"Hello world!\n\");",
			"#!/usr/bin/perl -w\nprint(\"Hello world!\n\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_PERL, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_php() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env php\nprint(\"Hello world!\n\");",
			"#!/usr/bin/php\nprint(\"Hello world!\n\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_PHP, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_python() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env python\nprint(\"Hello world!\");",
			"#!/usr/bin/python\nprint(\"Hello world!\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_lua() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env lua\nprint(\"Hello world!\");",
			"#!/usr/bin/lua\nprint(\"Hello world!\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_LUA, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_ruby() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env ruby\nprint(\"Hello world!\n\");",
			"#!/usr/bin/ruby\nprint(\"Hello world!\n\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_RUBY, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_unknownProgramInShebang() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"#!/usr/bin/env xxx\nprint(\"Hello world!\n\");",
			"#!/usr/bin/xxx\nprint(\"Hello world!\n\");",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_html() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"<!doctype html>",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_textAreaArg_xml() {

		FileTypeUtil util = FileTypeUtil.get();

		String[] texts = {
			"<?xml?>",
			"<?xml version=\"1.0\"?>",
			"<?xml version=\"1.0\" ?>",
			"<?xml version='1.0'?>",
			"<?xml version='1.0' ?>",
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>",
		};

		for (String text : texts) {
			RSyntaxTextArea textArea = plainTextArea(text);
			Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, util.guessContentType(textArea));
		}
	}


	@Test
	void testGuessContentType_fileArg_knownTypes() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("makefile");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, util.guessContentType(file));

		file = new File("test.java");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file));

		file = new File("test.rs");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_RUST, util.guessContentType(file));

		file = new File("test.xml");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, util.guessContentType(file));
	}


	@Test
	void testGuessContentType_fileArg_knownTypes_ignoreBackupExtensions() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("test.java.bak");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file));

		file = new File("test.java.orig");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file));

		file = new File("test.java.oLD");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file));
	}


	@Test
	void testGuessContentType_fileArg_unknownTypes() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("test.unknown");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(file));
	}


	@Test
	void testGuessContentType_fileMapArgs_knownTypes() {

		FileTypeUtil util = FileTypeUtil.get();

		Map<String, List<String>> filters = new HashMap<>();
		filters.put(SyntaxConstants.SYNTAX_STYLE_JAVA, Collections.singletonList("*.unusual"));

		File file = new File("test.unusual");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, filters));
	}


	@Test
	void testGuessContentType_fileMapArgs_unknownTypes() {

		FileTypeUtil util = FileTypeUtil.get();

		Map<String, List<String>> filters = new HashMap<>();
		filters.put(SyntaxConstants.SYNTAX_STYLE_JAVA, Collections.singletonList("*.unusual"));

		File file = new File("test.java");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(file, filters));
	}


	@Test
	void testGuessContentType_fileBooleanArgs_ignoreBackupExtensions() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("test.java.bak");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, true));
	}


	@Test
	void testGuessContentType_fileMapArgs_dontIgnoreBackupExtensions() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("test.java.bak");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(file, false));
	}


	@Test
	void testGuessContentType_threeArg_nullFile() {

		FileTypeUtil util = FileTypeUtil.get();

		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(null, null, true));
	}


	@Test
	void testGuessContentType_threeArg_defaultFilters() {

		FileTypeUtil util = FileTypeUtil.get();

		File file = new File("test.java");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, null, true));

		file = new File("test.unknown");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(file, null, true));
	}


	@Test
	void testGuessContentType_threeArg_customFilters_ignoreBackupExtension() {

		FileTypeUtil util = FileTypeUtil.get();

		Map<String, List<String>> filters = new HashMap<>();
		filters.put(SyntaxConstants.SYNTAX_STYLE_JAVA, Collections.singletonList("*.unusual"));

		// With no backup extension
		File file = new File("test.unusual");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, filters, true));

		// With backup extension
		file = new File("test.unusual.orig");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, filters, true));
	}


	@Test
	void testGuessContentType_threeArg_customFilters_dontIgnoreBackupExtension() {

		FileTypeUtil util = FileTypeUtil.get();

		Map<String, List<String>> filters = new HashMap<>();
		filters.put(SyntaxConstants.SYNTAX_STYLE_JAVA, Collections.singletonList("*.unusual"));

		// With no backup extension
		File file = new File("test.unusual");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, util.guessContentType(file, filters, false));

		// With backup extension
		file = new File("test.unusual.orig");
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, util.guessContentType(file, filters, false));
	}


	@Test
	void testStripBackupExtensions_null() {
		Assertions.assertNull(FileTypeUtil.stripBackupExtensions(null));
	}


	@Test
	void testStripBackupExtensions_hasBackupExtension() {
		Assertions.assertEquals("test.js", FileTypeUtil.stripBackupExtensions("test.js.bak"));
		Assertions.assertEquals("test.js", FileTypeUtil.stripBackupExtensions("test.js.old"));
		Assertions.assertEquals("test.js", FileTypeUtil.stripBackupExtensions("test.js.orig"));
	}


	@Test
	void testStripBackupExtensions_noBackupExtension() {
		Assertions.assertEquals("test.js", FileTypeUtil.stripBackupExtensions("test.js"));
	}
}
