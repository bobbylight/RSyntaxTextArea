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
 * Unit tests for the {@link CurlyFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CurlyFoldParserTest {

	@Test
	void testGetFolds_java_importsBeforeClass() {

		String code = "import java.io.*;\n" +
			"import java.net.*;\n" +
			"import javax.swing.*;\n" +
			"public class Example {}\n";


		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		CurlyFoldParser parser = new CurlyFoldParser(true, true);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold importFold = folds.get(0);
		Assertions.assertEquals(0, importFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf(";"), importFold.getEndOffset());

		Assertions.assertFalse(importFold.getHasChildFolds());
	}

	@Test
	void testGetFolds_java_importsBeforeComment() {

		String code = "import java.io.*;\n" +
			"import java.net.*;\n" +
			"import javax.swing.*;\n" +
			"/* test comment */\n";


		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		CurlyFoldParser parser = new CurlyFoldParser(true, true);
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());
		Fold importFold = folds.get(0);
		Assertions.assertEquals(0, importFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf(";"), importFold.getEndOffset());

		Assertions.assertFalse(importFold.getHasChildFolds());
	}


	@Test
	void testGetFolds_notJava_happyPath() {

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

		Assertions.assertEquals(1, folds.size());

		Fold topLevelFold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, topLevelFold.getFoldType());
		int firstCurlyOffs = code.indexOf('{');
		int lastCurlyOffs = code.lastIndexOf('}');
		Assertions.assertEquals(firstCurlyOffs, topLevelFold.getStartOffset());
		Assertions.assertEquals(lastCurlyOffs, topLevelFold.getEndOffset());

		Assertions.assertEquals(2, topLevelFold.getChildCount());

		Fold commentFold = topLevelFold.getChild(0);
		Assertions.assertEquals(FoldType.COMMENT, commentFold.getFoldType());
		Assertions.assertEquals(code.indexOf("/*"), commentFold.getStartOffset());
		Assertions.assertEquals(code.indexOf("*/") + 1, commentFold.getEndOffset());

		Fold childFold = topLevelFold.getChild(1);
		Assertions.assertEquals(FoldType.CODE, childFold.getFoldType());
		int childOpenCurlyOffs = code.indexOf('{', firstCurlyOffs + 1);
		int childCloseCurlyOffs = code.lastIndexOf('}', lastCurlyOffs - 1);
		Assertions.assertEquals(childOpenCurlyOffs, childFold.getStartOffset());
		Assertions.assertEquals(childCloseCurlyOffs, childFold.getEndOffset());
	}


	@Test
	void testGetSetFoldableMultiLineComments() {
		CurlyFoldParser parser = new CurlyFoldParser();
		Assertions.assertTrue(parser.getFoldableMultiLineComments());
		parser.setFoldableMultiLineComments(false);
		Assertions.assertFalse(parser.getFoldableMultiLineComments());
	}
}
