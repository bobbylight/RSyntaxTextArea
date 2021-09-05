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
 * Unit tests for the {@link JsonFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class JsonFoldParserTest {

	@Test
	void testGetFolds_happyPath() {

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

		Assertions.assertEquals(1, folds.size());

		Fold firstFold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, firstFold.getFoldType());
		Assertions.assertEquals(0, firstFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf('}'), firstFold.getEndOffset());

		Assertions.assertEquals(1, firstFold.getChildCount());

		Fold celebrityArrayFold = firstFold.getChild(0);
		Assertions.assertEquals(FoldType.CODE, celebrityArrayFold.getFoldType());
		Assertions.assertEquals(code.indexOf("["), celebrityArrayFold.getStartOffset());
		Assertions.assertEquals(code.indexOf("]"), celebrityArrayFold.getEndOffset());

		Assertions.assertEquals(2, celebrityArrayFold.getChildCount());

		Fold clooneyFold = celebrityArrayFold.getChild(0);
		Assertions.assertEquals(FoldType.CODE, clooneyFold.getFoldType());
		Assertions.assertEquals(code.indexOf('{', code.indexOf('[')), clooneyFold.getStartOffset());
		Assertions.assertEquals(code.indexOf('}', code.indexOf("Clooney")), clooneyFold.getEndOffset());

		Fold applegateFold = celebrityArrayFold.getChild(1);
		Assertions.assertEquals(FoldType.CODE, applegateFold.getFoldType());
		Assertions.assertEquals(code.indexOf('{', code.indexOf("Clooney")), applegateFold.getStartOffset());
		Assertions.assertEquals(code.indexOf('}', code.indexOf("Applegate")), applegateFold.getEndOffset());
	}
}
