/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * Unit tests for the {@link LatexFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LatexFoldParserTest {

	@Test
	void testGetFolds_happyPath() {

		String code =
			"\\documentclass[12pt]{article}\n" +
			"\\usepackage{lingmacros}\n" +
			"\\usepackage{tree-dvips}\n" +
			"\\begin{document}\n" +
			"\n" +
			"\\section*{Notes for My Paper}\n" +
			"\n" +
			"Don't forget to include examples of topicalization.\n" +
			"They look like this:\n" +
			"\n" +
			"\\end{document}";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);

		FoldParser parser = new LatexFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());

		Fold firstFold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, firstFold.getFoldType());
		Assertions.assertEquals(code.indexOf("\\begin"), firstFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf("\\end"), firstFold.getEndOffset());

		Assertions.assertEquals(0, firstFold.getChildCount());
	}
}
