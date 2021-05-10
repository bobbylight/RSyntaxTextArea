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
 * Unit tests for the {@link YamlFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class YamlFoldParserTest {

	@Test
	public void testGetFolds_happyPath() {

		String code =
			"men:\n" +
			"  - George Clooney\n" +
			"  - Matt Damon\n" +
			"  extra:\n" +
			"    - Another\n" +
			"    - Item\n" +
			"women:\n" +
			"  - Sally Field\n" +
			"  - Christina Applegate\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);

		YamlFoldParser parser = new YamlFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assert.assertEquals(2, folds.size());

		Fold firstFold = folds.get(0);
		Assert.assertEquals(FoldType.CODE, firstFold.getFoldType());
		Assert.assertEquals(0, firstFold.getStartOffset());
		Assert.assertEquals(code.indexOf("Item"), firstFold.getEndOffset());

		Assert.assertEquals(1, firstFold.getChildCount());

		Fold childFold = firstFold.getChild(0);
		Assert.assertEquals(FoldType.CODE, childFold.getFoldType());
		Assert.assertEquals(code.indexOf("  extra"), childFold.getStartOffset());
		Assert.assertEquals(code.indexOf("Item"), childFold.getEndOffset());

		Fold secondFold = folds.get(1);
		Assert.assertEquals(FoldType.CODE, secondFold.getFoldType());
		Assert.assertEquals(code.indexOf("women"), secondFold.getStartOffset());
		Assert.assertEquals(Integer.MAX_VALUE, secondFold.getEndOffset());
	}
}
