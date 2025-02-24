/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;


/**
 * Unit tests for the {@link HtmlFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HtmlFoldParserTest {

	private static final String[] SUPPORTED_LANGUAGES = {
		SyntaxConstants.SYNTAX_STYLE_HTML,
		SyntaxConstants.SYNTAX_STYLE_PHP,
		SyntaxConstants.SYNTAX_STYLE_JSP,
	};

	@Test
	void testConstructor_invalidLanguageIndex_tooSmall() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new HtmlFoldParser(-42)
		);
	}

	@Test
	void testConstructor_invalidLanguageIndex_tooLarge() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new HtmlFoldParser(42)
		);
	}

	@Test
	void testGetFolds_code_php_oneLine() {

		String code = "<?php some php code\n" +
			"php code ending ?>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_PHP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.LANGUAGE_PHP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@Test
	void testGetFolds_code_php_twoLines() {

		String code = "<?php some php code\n" +
			"more code\n" +
			"php code ending ?>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_PHP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.LANGUAGE_PHP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(2, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@Test
	void testGetFolds_code_php_onOneLineIsNotAFold() {

		String code = "<?php all on one line ?>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_PHP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.LANGUAGE_PHP);
		List<Fold> folds = parser.getFolds(textArea);
		Assertions.assertEquals(0, folds.size());
	}

	@Test
	void testGetFolds_comments_jsp_oneLine() {

		String code = "<%-- line one\n" +
			"line 2 --%>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_JSP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.LANGUAGE_JSP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@Test
	void testGetFolds_comments_jsp_allOnOneLineIsNotAFold() {

		String code = "<%-- all on one line --%>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_JSP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.LANGUAGE_JSP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(0, folds.size());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_comments_oneLine(int languageIndex) {

		String code = "<!-- start\n" +
			"end -->\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_comments_twoLines(int languageIndex) {

		String code = "<!-- start\n" +
			"a second line\n" +
			"end -->\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(2, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_comments_onOneLineIsNotAFold(int languageIndex) {

		String code = "<!-- all on one line -->\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);
		Assertions.assertEquals(0, folds.size());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_tags_oneLine(int languageIndex) {

		String code = "<body>\n" +
			"</body>\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_tags_twoLines(int languageIndex) {

		String code = "<body>\n" +
			"this is content\n" +
			"</body>\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(2, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}

	@ParameterizedTest
	@ValueSource(ints = {
		HtmlFoldParser.LANGUAGE_HTML,
		HtmlFoldParser.LANGUAGE_PHP,
		HtmlFoldParser.LANGUAGE_JSP
	})
	void testGetFolds_tags_onOneLineIsNotAFold(int languageIndex) {

		String code = "<body>foo</body>\n";

		String language = SUPPORTED_LANGUAGES[languageIndex + 1];
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(languageIndex);
		List<Fold> folds = parser.getFolds(textArea);
		Assertions.assertEquals(0, folds.size());
	}
}
