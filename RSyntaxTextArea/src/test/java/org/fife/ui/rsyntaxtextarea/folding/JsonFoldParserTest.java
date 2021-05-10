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
 * Unit tests for the {@link JsonFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JsonFoldParserTest {

	@Test
	public void testGetFolds_happyPath() {

		String code =
			"{\n" +
			"  \"celebrities\": [\n" +
			"    {\n" +
			"      \"name\": \"George Clooney\",\n" +
			"      \"dob\": \"1961-05-06T00:00:00\",\n" +
			"      \"profession\": \"actor\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"name\": \"Christina Applegate\",\n" +
			"      \"dob\": \"1971-11-25T00:00:00\",\n" +
			"      \"profession\": \"actor\"\n" +
			"    }\n," +
			"    { \"name\": \"One Line, Ignored\" }\n" +
			"  ]    \n" +
			"}\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assert.assertEquals(1, folds.size());

		Fold firstFold = folds.get(0);
		Assert.assertEquals(FoldType.CODE, firstFold.getFoldType());
		Assert.assertEquals(0, firstFold.getStartOffset());
		Assert.assertEquals(code.lastIndexOf('}'), firstFold.getEndOffset());

		Assert.assertEquals(1, firstFold.getChildCount());

		Fold celebrityArrayFold = firstFold.getChild(0);
		Assert.assertEquals(FoldType.CODE, celebrityArrayFold.getFoldType());
		Assert.assertEquals(code.indexOf("["), celebrityArrayFold.getStartOffset());
		Assert.assertEquals(code.indexOf("]"), celebrityArrayFold.getEndOffset());

		Assert.assertEquals(2, celebrityArrayFold.getChildCount());

		Fold clooneyFold = celebrityArrayFold.getChild(0);
		Assert.assertEquals(FoldType.CODE, clooneyFold.getFoldType());
		Assert.assertEquals(code.indexOf('{', code.indexOf('[')), clooneyFold.getStartOffset());
		Assert.assertEquals(code.indexOf('}', code.indexOf("Clooney")), clooneyFold.getEndOffset());

		Fold applegateFold = celebrityArrayFold.getChild(1);
		Assert.assertEquals(FoldType.CODE, applegateFold.getFoldType());
		Assert.assertEquals(code.indexOf('{', code.indexOf("Clooney")), applegateFold.getStartOffset());
		Assert.assertEquals(code.indexOf('}', code.indexOf("Applegate")), applegateFold.getEndOffset());
	}
}
