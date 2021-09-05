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
 * Unit tests for the {@link LinesWithContentFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LinesWithContentFoldParserTest {

	@Test
	void testHappyPath() {

		String code = "; this is a block of code\n" +
			"LDA #$01\n" +
			"STA $0200\n" +
			"LDA #$05\n" +
			"\n" +
			"; a single line of content is ignored\n" +
			"\n" +
			"LDA #$01\n" +
			"STA $0200\n" +
			"LDA #$05";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);

		LinesWithContentFoldParser parser = new LinesWithContentFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(2, folds.size());

		Fold firstFold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, firstFold.getFoldType());
		Assertions.assertEquals(0, firstFold.getStartOffset());
		// End of fold is the end of hte last line containing content
		Assertions.assertEquals(code.indexOf("\n\n"), firstFold.getEndOffset());

		Fold secondFold = folds.get(1);
		Assertions.assertEquals(FoldType.CODE, secondFold.getFoldType());
		Assertions.assertEquals(code.lastIndexOf("\n\n") + 2, secondFold.getStartOffset());
		Assertions.assertEquals(Integer.MAX_VALUE, secondFold.getEndOffset());
	}
}
