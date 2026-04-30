/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;

import org.fife.ui.rsyntaxtextarea.demo.IndentAnalyzer.IndentResult;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link IndentAnalyzer} class.
 *
 * @author RSyntaxTextArea Demo
 * @version 1.0
 */
class IndentAnalyzerTest {

	@Test
	void testDefaultConstructor() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		Assertions.assertEquals(4, analyzer.getSpacesPerIndent());
	}


	@Test
	void testParameterizedConstructor() {
		IndentAnalyzer analyzer = new IndentAnalyzer(2);
		Assertions.assertEquals(2, analyzer.getSpacesPerIndent());
	}


	@Test
	void testAnalyzeEmptyText() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		IndentResult result = analyzer.analyze("", 0, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isEmpty());
	}


	@Test
	void testAnalyzeNullText() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		IndentResult result = analyzer.analyze(null, 0, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isEmpty());
	}


	@Test
	void testAnalyzeInvalidLineNumber() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {}";
		IndentResult result = analyzer.analyze(text, -1, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isEmpty());

		result = analyzer.analyze(text, 100, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isEmpty());
	}


	@Test
	void testAnalyzeUnsupportedLanguage() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "var x = 1;";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		Assertions.assertFalse(result.isSupported());
		Assertions.assertFalse(result.isEmpty());
	}


	@Test
	void testJavaSimpleClass() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    \n" +
			"}";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testJavaMethodInsideClass() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    public void method() {\n" +
			"        \n" +
			"    }\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(2, result.getIndentLevel());
	}


	@Test
	void testJavaIfBlock() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    public void method() {\n" +
			"        if (true) {\n" +
			"            \n" +
			"        }\n" +
			"    }\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 3, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(3, result.getIndentLevel());
	}


	@Test
	void testJavaClosingBraceReducesIndent() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    public void method() {\n" +
			"        if (true) {\n" +
			"            doSomething();\n" +
			"        }\n" +
			"    }\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 4, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testJavaBracesInStringsIgnored() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    String s = \"{not a block}\";\n" +
			"    \n" +
			"}";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testJavaBracesInCommentsIgnored() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    // This is a comment with { braces }\n" +
			"    /* Another { comment } */\n" +
			"    \n" +
			"}";
		IndentResult result = analyzer.analyze(text, 3, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testJavaFirstLine() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "package com.example;\n" +
			"public class Test {\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(0, result.getIndentLevel());
	}


	@Test
	void testPythonSimpleFunction() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "def hello():\n" +
			"    \n" +
			"print('done')";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testPythonIfStatement() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "if True:\n" +
			"    \n" +
			"else:\n" +
			"    pass";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testPythonNestedBlocks() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "def outer():\n" +
			"    if True:\n" +
			"        \n" +
			"    return";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(2, result.getIndentLevel());
	}


	@Test
	void testPythonEmptyLineAfterColon() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "def test():\n" +
			"\n" +
			"    pass";
		IndentResult result = analyzer.analyze(text, 1, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testPythonCommentLineAfterColon() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "def test():\n" +
			"    # This is a comment\n" +
			"    \n" +
			"pass";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}


	@Test
	void testPythonFirstLine() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "import sys\n" +
			"print('hello')";
		IndentResult result = analyzer.analyze(text, 0, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(0, result.getIndentLevel());
	}


	@Test
	void testPythonWithTabs() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "def test():\n" +
			"\tif True:\n" +
			"\t\t\n" +
			"\tpass";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(2, result.getIndentLevel());
	}


	@Test
	void testJavaCurrentLineReturned() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    public void method() {\n" +
			"        System.out.println(\"hello\");\n" +
			"    }\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 2, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals("        System.out.println(\"hello\");", result.getCurrentLine());
	}


	@Test
	void testExceptionHandling() {
		IndentAnalyzer analyzer = new IndentAnalyzer() {
			@Override
			public IndentResult analyze(String text, int caretLine, String syntaxStyle) {
				throw new RuntimeException("Test exception");
			}
		};

		try {
			analyzer.analyze("test", 0, SyntaxConstants.SYNTAX_STYLE_JAVA);
			Assertions.fail("Should have thrown exception");
		} catch (RuntimeException e) {
			Assertions.assertEquals("Test exception", e.getMessage());
		}
	}


	@Test
	void testJavaMultiLineCommentWithBraces() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    /*\n" +
			"     * This is a { comment }\n" +
			"     */\n" +
			"    public void test() {\n" +
			"        \n" +
			"    }\n" +
			"}";
		IndentResult result = analyzer.analyze(text, 5, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(2, result.getIndentLevel());
	}


	@Test
	void testJavaCharLiteralWithBrace() {
		IndentAnalyzer analyzer = new IndentAnalyzer();
		String text = "public class Test {\n" +
			"    char c = '{';\n" +
			"    char d = '}';\n" +
			"    \n" +
			"}";
		IndentResult result = analyzer.analyze(text, 3, SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertTrue(result.isSupported());
		Assertions.assertEquals(1, result.getIndentLevel());
	}

}
