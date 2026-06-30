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
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;


/**
 * Unit tests for the {@link HtmlFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HtmlFoldParserTest {

	private static String syntaxStyle(HtmlFoldParser.Language language) {
		return switch (language) {
			case HTML -> SyntaxConstants.SYNTAX_STYLE_HTML;
			case PHP -> SyntaxConstants.SYNTAX_STYLE_PHP;
			case JSP -> SyntaxConstants.SYNTAX_STYLE_JSP;
		};
	}

	@Test
	void testGetFolds_code_php_oneLine() {

		String code = "<?php some php code\n" +
			"php code ending ?>\n";

		String language = SyntaxConstants.SYNTAX_STYLE_PHP;
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(language);

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.Language.PHP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
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

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.Language.PHP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
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

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.Language.PHP);
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

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.Language.JSP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
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

		HtmlFoldParser parser = new HtmlFoldParser(HtmlFoldParser.Language.JSP);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(0, folds.size());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_comments_oneLine(HtmlFoldParser.Language language) {

		String code = "<!-- start\n" +
			"end -->\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_comments_twoLines(HtmlFoldParser.Language language) {

		String code = "<!-- start\n" +
			"a second line\n" +
			"end -->\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(2, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_comments_onOneLineIsNotAFold(HtmlFoldParser.Language language) {

		String code = "<!-- all on one line -->\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);
		Assertions.assertEquals(0, folds.size());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_tags_oneLine(HtmlFoldParser.Language language) {

		String code = "<body>\n" +
			"</body>\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_tags_twoLines(HtmlFoldParser.Language language) {

		String code = "<body>\n" +
			"this is content\n" +
			"</body>\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(2, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}

	@ParameterizedTest
	@EnumSource(HtmlFoldParser.Language.class)
	void testGetFolds_tags_onOneLineIsNotAFold(HtmlFoldParser.Language language) {

		String code = "<body>foo</body>\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(syntaxStyle(language));

		HtmlFoldParser parser = new HtmlFoldParser(language);
		List<Fold> folds = parser.getFolds(textArea);
		Assertions.assertEquals(0, folds.size());
	}
}
