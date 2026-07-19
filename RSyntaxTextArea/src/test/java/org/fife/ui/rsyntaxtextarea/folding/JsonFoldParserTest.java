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
			"""
				{
				  "celebrities": [
				    {
				      "name": "George Clooney",
				      "dob": "1961-05-06T00:00:00",
				      "profession": "actor"
				    },
				    {
				      "name": "Christina Applegate",
				      "dob": "1971-11-25T00:00:00",
				      "profession": "actor"
				    }
				,\
				    { "name": "One Line, Ignored" }
				  ]   \s
				}
				""";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());

		Fold firstFold = folds.getFirst();
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


	@Test
	void testGetFolds_noContent() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_singleLineObject_notFoldable() {

		String code = "{ \"name\": \"George Clooney\", \"profession\": \"actor\" }\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_topLevelArray() {

		String code =
			"""
				[
				  "one",
				  "two"
				]
				""";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());

		Fold arrayFold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, arrayFold.getFoldType());
		Assertions.assertEquals(0, arrayFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf(']'), arrayFold.getEndOffset());
		Assertions.assertEquals(0, arrayFold.getChildCount());
	}


	@Test
	void testGetFolds_nestedArrays() {

		String code =
			"""
				[
				  [
				    1, 2
				  ],
				  [
				    3, 4
				  ]
				]
				""";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());

		Fold outerFold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, outerFold.getFoldType());
		Assertions.assertEquals(0, outerFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf(']'), outerFold.getEndOffset());
		Assertions.assertEquals(2, outerFold.getChildCount());

		Fold firstInner = outerFold.getChild(0);
		Assertions.assertEquals(code.indexOf('[', 1), firstInner.getStartOffset());
		Assertions.assertEquals(code.indexOf(']'), firstInner.getEndOffset());

		Fold secondInner = outerFold.getChild(1);
		Assertions.assertEquals(code.indexOf('[', firstInner.getEndOffset()), secondInner.getStartOffset());
		Assertions.assertEquals(code.indexOf(']', secondInner.getStartOffset()), secondInner.getEndOffset());
	}


	@Test
	void testGetFolds_mismatchedDelimitersDoNotCloseWrongFoldType() {

		// A ']' should never close a fold opened by '{', and a '}' should
		// never close a fold opened by '['.  Verify the object fold here
		// stays open despite an (invalid, but still tokenized) stray ']'
		// appearing before its closing '}'.
		String code =
			"""
				{
				  "arr": [ 1, 2 ]
				  ,"more": "stuff"
				}
				""";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(1, folds.size());

		Fold objectFold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, objectFold.getFoldType());
		Assertions.assertEquals(0, objectFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf('}'), objectFold.getEndOffset());
		// The single-line "arr" array should not have produced a child fold.
		Assertions.assertEquals(0, objectFold.getChildCount());
	}


	@Test
	void testGetFolds_multipleTopLevelFolds() {

		String code =
			"""
				{
				  "first": true
				}
				{
				  "second": true
				}
				""";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);

		FoldParser parser = new JsonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(2, folds.size());

		Fold firstFold = folds.getFirst();
		Assertions.assertEquals(0, firstFold.getStartOffset());
		Assertions.assertEquals(code.indexOf('}'), firstFold.getEndOffset());

		Fold secondFold = folds.get(1);
		Assertions.assertEquals(code.indexOf('{', firstFold.getEndOffset()), secondFold.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf('}'), secondFold.getEndOffset());
	}
}
