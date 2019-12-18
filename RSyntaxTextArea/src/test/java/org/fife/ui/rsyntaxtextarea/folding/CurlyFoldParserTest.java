/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for the {@link CurlyFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class CurlyFoldParserTest {

	@Test
	public void testGetFolds_notJava_happyPath() {

		String code = "void foo() {\n" +
			"  /* This is a\n" +
			"     long multi-line comment */\n" +
			"  if (true) {\n" +
			"    // Do something\n" +
			"  }\n" +
			"  println(\"Hello world\");\n" +
			"}";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);

		CurlyFoldParser parser = new CurlyFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assert.assertEquals(1, folds.size());

		Fold topLevelFold = folds.get(0);
		Assert.assertEquals(FoldType.CODE, topLevelFold.getFoldType());
		int firstCurlyOffs = code.indexOf('{');
		int lastCurlyOffs = code.lastIndexOf('}');
		Assert.assertEquals(firstCurlyOffs, topLevelFold.getStartOffset());
		Assert.assertEquals(lastCurlyOffs, topLevelFold.getEndOffset());

		Assert.assertEquals(2, topLevelFold.getChildCount());

		Fold commentFold = topLevelFold.getChild(0);
		Assert.assertEquals(FoldType.COMMENT, commentFold.getFoldType());
		Assert.assertEquals(code.indexOf("/*"), commentFold.getStartOffset());
		Assert.assertEquals(code.indexOf("*/") + 1, commentFold.getEndOffset());

		Fold childFold = topLevelFold.getChild(1);
		Assert.assertEquals(FoldType.CODE, childFold.getFoldType());
		int childOpenCurlyOffs = code.indexOf('{', firstCurlyOffs + 1);
		int childCloseCurlyOffs = code.lastIndexOf('}', lastCurlyOffs - 1);
		Assert.assertEquals(childOpenCurlyOffs, childFold.getStartOffset());
		Assert.assertEquals(childCloseCurlyOffs, childFold.getEndOffset());
	}


	@Test
	public void testGetSetFoldableMultiLineComments() {
		CurlyFoldParser parser = new CurlyFoldParser();
		Assert.assertTrue(parser.getFoldableMultiLineComments());
		parser.setFoldableMultiLineComments(false);
		Assert.assertFalse(parser.getFoldableMultiLineComments());
	}
}
