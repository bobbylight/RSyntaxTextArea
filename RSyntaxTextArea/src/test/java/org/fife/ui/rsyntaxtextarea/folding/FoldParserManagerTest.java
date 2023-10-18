/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;


import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code FoldParserManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class FoldParserManagerTest {

	@Test
	void testAddFoldParserMapping() {

		FoldParserManager fpm = FoldParserManager.get();

		Assertions.assertNull(fpm.getFoldParser("text/foo"));
		fpm.addFoldParserMapping("text/foo", new CurlyFoldParser());

		Assertions.assertNotNull(fpm.getFoldParser("text/foo"));
	}

	@Test
	void testDefaultFoldParsersDefined() {

		FoldParserManager fpm = FoldParserManager.get();

		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_C));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CSHARP));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CLOJURE));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CSS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_D));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_DART));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_GO));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_GROOVY));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_HTACCESS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_HTML));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JAVA));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSON));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSP));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_KOTLIN));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LATEX));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LESS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LISP));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_MXML));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_NSIS));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PERL));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PHP));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PROTO));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PYTHON));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_RUST));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_SCALA));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_XML));
		Assertions.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_YAML));
	}
}
