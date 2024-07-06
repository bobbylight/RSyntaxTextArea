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
 * Unit tests for the {@link NsisFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class NsisFoldParserTest {

	@Test
	void testGetFolds_happyPath() {

		String code =
			"/*\n" +
			" * Multi-line comment.\n" +
			" */\n" +
			"# set the name of the installer\n" +
			"Outfile \"hello world.exe\"\n" +
			" \n" +
			"# create a default section.\n" +
			"Section\n" +
			" \n" +
			"# create a popup box, with an OK button and the text \"Hello world!\"\n" +
			"MessageBox MB_OK \"Hello world!\"\n" +
			" \n" +
			"SectionEnd\n" +
			"\n" +
			"Function Hello\n" +
			"  /*\n" +
			"   * Another multi-line comment.\n" +
			"   */\n" +
			"  DetailPrint \"Hello world\"\n" +
			"FunctionEnd";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NSIS);

		FoldParser parser = new NsisFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(3, folds.size());

		Fold firstFold = folds.get(0);
		Assertions.assertEquals(FoldType.COMMENT, firstFold.getFoldType());
		Assertions.assertEquals(code.indexOf("/*"), firstFold.getStartOffset());
		Assertions.assertEquals(code.indexOf("*/") + 1, firstFold.getEndOffset());

		Assertions.assertEquals(0, firstFold.getChildCount());

		Fold secondFold = folds.get(1);
		Assertions.assertEquals(FoldType.CODE, secondFold.getFoldType());
		Assertions.assertEquals(code.indexOf("Section"), secondFold.getStartOffset());
		Assertions.assertEquals(code.indexOf("SectionEnd"), secondFold.getEndOffset());

		Assertions.assertEquals(0, secondFold.getChildCount());

		Fold thirdFold = folds.get(2);
		Assertions.assertEquals(FoldType.CODE, thirdFold.getFoldType());
		Assertions.assertEquals(code.indexOf("Function"), thirdFold.getStartOffset());
		Assertions.assertEquals(code.indexOf("FunctionEnd"), thirdFold.getEndOffset());

		Assertions.assertEquals(1, thirdFold.getChildCount());
		Fold childCommentFold = thirdFold.getChild(0);
		Assertions.assertEquals(FoldType.COMMENT, childCommentFold.getFoldType());

		Assertions.assertFalse(childCommentFold.getHasChildFolds());
	}
}
