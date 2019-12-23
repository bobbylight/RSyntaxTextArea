/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.BadLocationException;

/**
 * Unit tests for the {@link Fold} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FoldTest extends AbstractRSyntaxTextAreaTest {

	private static final String CODE_WITH_CHILDREN = "{\n" +
		"  {\n" +
		"    println('hi');\n" +
		"  }\n" +
		"  {\n" +
		"    println('bye');\n" +
		"  }\n" +
		"}";


	@Test
	public void testContainsLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertFalse(fold.containsLine(0)); // First line is not included
		Assert.assertTrue(fold.containsLine(1));
		Assert.assertTrue(fold.containsLine(2));
		Assert.assertFalse(fold.containsLine(3));
	}


	@Test
	public void testContainsOrStartsOnLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertTrue(fold.containsOrStartsOnLine(0));
		Assert.assertTrue(fold.containsOrStartsOnLine(1));
		Assert.assertTrue(fold.containsOrStartsOnLine(2));
		Assert.assertFalse(fold.containsOrStartsOnLine(3));
	}


	@Test
	public void testCreateChild() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Fold child = fold.createChild(FoldType.COMMENT, 8);
		Assert.assertEquals(FoldType.COMMENT, child.getFoldType());
		Assert.assertEquals(8, child.getStartOffset());
		Assert.assertEquals(fold, child.getParent());
	}


	@Test
	public void testGetChildCount() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertEquals(0, fold.getChildCount());
	}


	@Test
	public void testGetChildren() {

		RSyntaxTextArea textArea = createTextArea(CODE_WITH_CHILDREN);

		Fold fold = new CurlyFoldParser().getFolds(textArea).get(0);
		Assert.assertEquals(2, fold.getChildren().size());
	}


	@Test
	public void testGetEndLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(textArea.getText().lastIndexOf('}'));

		Assert.assertEquals(textArea.getLineCount() - 1, fold.getEndLine());
	}


	@Test
	public void testGetHasChildFolds() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertFalse(fold.getHasChildFolds());
	}


	@Test
	public void testGetLastChild_noChildren() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertNull(fold.getLastChild());
	}


	@Test
	public void testGetLastChild_twoChildren() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea(CODE_WITH_CHILDREN);

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(CODE_WITH_CHILDREN.length() - 1);

		Fold firstChild = fold.createChild(FoldType.CODE, 2);
		firstChild.setEndOffset(CODE_WITH_CHILDREN.indexOf('}'));

		Fold secondChild = fold.createChild(FoldType.CODE, CODE_WITH_CHILDREN.indexOf('{', firstChild.getEndOffset()));
		secondChild.setEndOffset(CODE_WITH_CHILDREN.indexOf('}', secondChild.getStartOffset()));

		Assert.assertEquals(secondChild, fold.getLastChild());
	}


	@Test
	public void testGetLineCount() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		Assert.assertEquals(2, fold.getLineCount());
	}


	@Test
	public void testGetStartLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();

		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		Assert.assertEquals(0, fold.getStartLine());

		fold = new Fold(FoldType.CODE, textArea, textArea.getLineStartOffset(1));
		Assert.assertEquals(1, fold.getStartLine());
	}


	@Test
	public void testToString() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Fold fold = new Fold(FoldType.CODE, textArea, 0);
		fold.setEndOffset(21);

		String expected = "[Fold: startOffs=0, endOffs=21, collapsed=false]";
		Assert.assertEquals(expected, fold.toString());
	}
}
