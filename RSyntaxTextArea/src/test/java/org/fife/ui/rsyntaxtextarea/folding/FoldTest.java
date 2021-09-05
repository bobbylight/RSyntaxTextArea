/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.BadLocationException;

/**
 * Unit tests for the {@link Fold} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class FoldTest extends AbstractRSyntaxTextAreaTest {

	private static final String CODE_WITH_CHILDREN = "{\n" +
		"  {\n" +
		"    println('hi');\n" +
		"  }\n" +
		"  {\n" +
		"    println('bye');\n" +
		"  }\n" +
		"}";


	@Test
	void testContainsLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertFalse(fold.containsLine(0)); // First line is not included
		Assertions.assertTrue(fold.containsLine(1));
		Assertions.assertTrue(fold.containsLine(2));
		Assertions.assertFalse(fold.containsLine(3));
	}


	@Test
	void testContainsOrStartsOnLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertTrue(fold.containsOrStartsOnLine(0));
		Assertions.assertTrue(fold.containsOrStartsOnLine(1));
		Assertions.assertTrue(fold.containsOrStartsOnLine(2));
		Assertions.assertFalse(fold.containsOrStartsOnLine(3));
	}


	@Test
	void testCreateChild() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Fold child = fold.createChild(FoldType.COMMENT, 8);
		Assertions.assertEquals(FoldType.COMMENT, child.getFoldType());
		Assertions.assertEquals(8, child.getStartOffset());
		Assertions.assertEquals(fold, child.getParent());
	}


	@Test
	void testGetChildCount() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertEquals(0, fold.getChildCount());
	}


	@Test
	void testGetChildren() {

		RSyntaxTextArea textArea = createTextArea(CODE_WITH_CHILDREN);

		Fold fold = new CurlyFoldParser().getFolds(textArea).get(0);
		Assertions.assertEquals(2, fold.getChildren().size());
	}


	@Test
	void testGetEndLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(textArea.getText().lastIndexOf('}'));

		Assertions.assertEquals(textArea.getLineCount() - 1, fold.getEndLine());
	}


	@Test
	void testGetHasChildFolds() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertFalse(fold.getHasChildFolds());
	}


	@Test
	void testGetLastChild_noChildren() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertNull(fold.getLastChild());
	}


	@Test
	void testGetLastChild_twoChildren() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(CODE_WITH_CHILDREN);

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(CODE_WITH_CHILDREN.length() - 1);

		Fold firstChild = fold.createChild(FoldType.CODE, 2);
		firstChild.setEndOffset(CODE_WITH_CHILDREN.indexOf('}'));

		Fold secondChild = fold.createChild(FoldType.CODE, CODE_WITH_CHILDREN.indexOf('{', firstChild.getEndOffset()));
		secondChild.setEndOffset(CODE_WITH_CHILDREN.indexOf('}', secondChild.getStartOffset()));

		Assertions.assertEquals(secondChild, fold.getLastChild());
	}


	@Test
	void testGetLineCount() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assertions.assertEquals(2, fold.getLineCount());
	}


	@Test
	void testGetStartLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		Assertions.assertEquals(0, fold.getStartLine());

		fold = new Fold(FoldType.CODE, textArea, textArea.getLineStartOffset(1));
		Assertions.assertEquals(1, fold.getStartLine());
	}


	@Test
	void testToString() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		String expected = "[Fold: startOffs=0, endOffs=21, collapsed=false]";
		Assertions.assertEquals(expected, fold.toString());
	}
}
