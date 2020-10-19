/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;


import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@code FoldParserManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FoldParserManagerTest {

	@Test
	public void testAddFoldParserMapping() {

		FoldParserManager fpm = FoldParserManager.get();

		Assert.assertNull(fpm.getFoldParser("text/foo"));
		fpm.addFoldParserMapping("text/foo", new CurlyFoldParser());

		Assert.assertNotNull(fpm.getFoldParser("text/foo"));
	}

	@Test
	public void testDefaultFoldParsersDefined() {

		FoldParserManager fpm = FoldParserManager.get();

		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_C));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CSHARP));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CLOJURE));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_CSS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_D));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_DART));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_GO));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_GROOVY));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_HTACCESS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_HTML));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JAVA));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSON));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_JSP));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LATEX));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LESS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_LISP));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_MXML));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_NSIS));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PERL));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PHP));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_PYTHON));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_SCALA));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_XML));
		Assert.assertNotNull(fpm.getFoldParser(SyntaxConstants.SYNTAX_STYLE_YAML));
	}
}
