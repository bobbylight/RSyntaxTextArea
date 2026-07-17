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
 * Unit tests for the XML fold parser.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class XmlFoldParserTest {

	private static List<Fold> getFolds(String code) {
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		FoldParser parser = new XmlFoldParser();
		return parser.getFolds(textArea);
	}


	@Test
	void testGetFolds_emptyDocument() {
		List<Fold> folds = getFolds("");
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_noTags() {
		List<Fold> folds = getFolds("just some plain text\nwith no markup at all\n");
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_tag_onOneLineIsNotAFold() {
		String code = "<body>foo</body>\n";
		List<Fold> folds = getFolds(code);
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_tag_twoLines() {
		String code = """
			<body>
			</body>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}


	@Test
	void testGetFolds_tag_manyLines() {
		String code = """
			<body>
			line 1
			line 2
			line 3
			</body>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(4, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}


	@Test
	void testGetFolds_tag_withAttributes() {
		String code = """
			<body id="foo" class="bar">
			</body>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, fold.getFoldType());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.indexOf("</"), fold.getEndOffset());
	}


	@Test
	void testGetFolds_selfClosingTag_isNeverAFold() {
		String code = """
			<body>
			<br/>
			</body>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(0, fold.getChildCount());
	}


	@Test
	void testGetFolds_selfClosingTag_withSpaceBeforeSlash() {
		String code = """
			<body>
			<br />
			</body>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(0, fold.getChildCount());
	}


	@Test
	void testGetFolds_selfClosingTag_atTopLevelCreatesNoFold() {
		String code = "<br/>\n";
		List<Fold> folds = getFolds(code);
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_nestedTags_bothMultiLine() {
		String code = """
			<html>
			<body>
			content
			</body>
			</html>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold outer = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, outer.getFoldType());
		Assertions.assertEquals(0, outer.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf("</"), outer.getEndOffset());
		Assertions.assertEquals(1, outer.getChildCount());

		Fold inner = outer.getChild(0);
		Assertions.assertEquals(FoldType.CODE, inner.getFoldType());
		Assertions.assertEquals(code.indexOf("<body>"), inner.getStartOffset());
		Assertions.assertEquals(code.indexOf("</body>"), inner.getEndOffset());
		Assertions.assertEquals(0, inner.getChildCount());
		Assertions.assertSame(outer, inner.getParent());
	}


	@Test
	void testGetFolds_nestedTags_onlyOuterSpansMultipleLines() {
		String code = """
			<html>
			<body>content</body>
			</html>
			""";
		List<Fold> folds = getFolds(code);

		// Inner "body" tag is entirely on one line, so it should not
		// become a fold but should not corrupt the outer fold either.
		Assertions.assertEquals(1, folds.size());
		Fold outer = folds.getFirst();
		Assertions.assertEquals(0, outer.getChildCount());
		Assertions.assertEquals(0, outer.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf("</"), outer.getEndOffset());
	}


	@Test
	void testGetFolds_nestedTags_onlyInnerSpansMultipleLines() {
		String code = """
			<html><body>
			content
			</body></html>
			""";
		List<Fold> folds = getFolds(code);

		// The outer "html" tag starts and ends on lines that, when taken
		// together with the fact that "body" is multi-line, still make
		// "html" span multiple lines overall, so both should fold.
		Assertions.assertEquals(1, folds.size());
		Fold outer = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, outer.getFoldType());
		Assertions.assertEquals(1, outer.getChildCount());

		Fold inner = outer.getChild(0);
		Assertions.assertEquals(code.indexOf("<body>"), inner.getStartOffset());
		Assertions.assertEquals(code.indexOf("</body>"), inner.getEndOffset());
	}


	@Test
	void testGetFolds_deeplyNestedTags() {
		String code = """
			<a>
			<b>
			<c>
			content
			</c>
			</b>
			</a>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold a = folds.getFirst();
		Assertions.assertEquals(1, a.getChildCount());

		Fold b = a.getChild(0);
		Assertions.assertEquals(1, b.getChildCount());

		Fold c = b.getChild(0);
		Assertions.assertEquals(0, c.getChildCount());
		Assertions.assertEquals(code.indexOf("<c>"), c.getStartOffset());
		Assertions.assertEquals(code.indexOf("</c>"), c.getEndOffset());
	}


	@Test
	void testGetFolds_siblingTags_bothMultiLine() {
		String code = """
			<root>
			<a>
			content 1
			</a>
			<b>
			content 2
			</b>
			</root>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold root = folds.getFirst();
		Assertions.assertEquals(2, root.getChildCount());

		Fold a = root.getChild(0);
		Assertions.assertEquals(code.indexOf("<a>"), a.getStartOffset());
		Assertions.assertEquals(code.indexOf("</a>"), a.getEndOffset());

		Fold b = root.getChild(1);
		Assertions.assertEquals(code.indexOf("<b>"), b.getStartOffset());
		Assertions.assertEquals(code.indexOf("</b>"), b.getEndOffset());
	}


	@Test
	void testGetFolds_siblingTags_atTopLevel() {
		String code = """
			<a>
			content 1
			</a>
			<b>
			content 2
			</b>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(2, folds.size());

		Fold a = folds.getFirst();
		Assertions.assertEquals(code.indexOf("<a>"), a.getStartOffset());
		Assertions.assertEquals(code.indexOf("</a>"), a.getEndOffset());

		Fold b = folds.get(1);
		Assertions.assertEquals(code.indexOf("<b>"), b.getStartOffset());
		Assertions.assertEquals(code.indexOf("</b>"), b.getEndOffset());
	}


	@Test
	void testGetFolds_manySiblingSingleLineTagsProduceNoFolds() {
		StringBuilder sb = new StringBuilder("<root>\n");
		for (int i = 0; i < 50; i++) {
			sb.append("<item>value").append(i).append("</item>\n");
		}
		sb.append("</root>\n");
		String code = sb.toString();

		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold root = folds.getFirst();
		Assertions.assertEquals(0, root.getChildCount());
		Assertions.assertEquals(0, root.getStartOffset());
		Assertions.assertEquals(code.lastIndexOf("</"), root.getEndOffset());
	}


	@Test
	void testGetFolds_selfClosingTagBetweenOtherTagsDoesNotBreakParent() {
		String code = """
			<root>
			<a/>
			<b>
			content
			</b>
			</root>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold root = folds.getFirst();
		Assertions.assertEquals(1, root.getChildCount());

		Fold b = root.getChild(0);
		Assertions.assertEquals(code.indexOf("<b>"), b.getStartOffset());
		Assertions.assertEquals(code.indexOf("</b>"), b.getEndOffset());
	}


	@Test
	void testGetFolds_xmlDeclaration_doesNotByItselfCreateFold() {
		String code = """
			<?xml version="1.0" encoding="UTF-8"?>
			<root>content</root>
			""";
		List<Fold> folds = getFolds(code);

		// Nothing here spans multiple lines, so there should be no folds.
		for (Fold fold : folds) {
			Assertions.assertNotEquals(-1, fold.getStartLine()); // Sanity: fold exists validly
		}
		// No multi-line construct present -> no folds expected.
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_comment_onOneLineIsNotAFold() {
		String code = "<!-- a comment all on one line -->\n";
		List<Fold> folds = getFolds(code);
		Assertions.assertEquals(0, folds.size());
	}


	@Test
	void testGetFolds_comment_twoLines() {
		String code = """
			<!-- line one
			line two -->
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(0, fold.getChildCount());
		Assertions.assertEquals(1, fold.getLineCount());
		Assertions.assertEquals(0, fold.getStartOffset());
		Assertions.assertEquals(code.length() - 2, fold.getEndOffset());
	}


	@Test
	void testGetFolds_comment_manyLines() {
		String code = """
			<!-- line one
			line two
			line three
			line four -->
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(FoldType.COMMENT, fold.getFoldType());
		Assertions.assertEquals(3, fold.getLineCount());
	}


	@Test
	void testGetFolds_comment_insideMultiLineTag() {
		String code = """
			<root>
			<!-- a comment
			spanning two lines -->
			</root>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold root = folds.getFirst();
		Assertions.assertEquals(1, root.getChildCount());

		Fold comment = root.getChild(0);
		Assertions.assertEquals(FoldType.COMMENT, comment.getFoldType());
		Assertions.assertEquals(code.indexOf("<!--"), comment.getStartOffset());
	}


	@Test
	void testGetFolds_multipleTopLevelComments() {
		String code = """
			<!-- comment one
			still one -->
			<root>content</root>
			<!-- comment two
			still two -->
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(2, folds.size());
		Assertions.assertEquals(FoldType.COMMENT, folds.get(0).getFoldType());
		Assertions.assertEquals(FoldType.COMMENT, folds.get(1).getFoldType());
	}


	@Test
	void testGetFolds_unclosedTag_doesNotThrow() {
		String code = """
			<root>
			<unclosed>
			content
			""";
		Assertions.assertDoesNotThrow(() -> getFolds(code));
	}


	@Test
	void testGetFolds_unmatchedClosingTag_doesNotThrow() {
		String code = """
			content
			</root>
			more content
			""";
		Assertions.assertDoesNotThrow(() -> getFolds(code));
	}


	@Test
	void testGetFolds_unclosedComment_doesNotThrow() {
		String code = """
			<!-- a comment that never ends
			more text
			even more text
			""";
		Assertions.assertDoesNotThrow(() -> getFolds(code));
	}


	@Test
	void testGetFolds_emptyElementWithOnlyWhitespaceBetweenTags() {
		String code = """
			<root>
			  \s
			</root>
			""";
		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold fold = folds.getFirst();
		Assertions.assertEquals(2, fold.getLineCount());
	}


	@Test
	void testGetFolds_cdataSection_doesNotBreakParsing() {
		String code = """
			<root>
			<![CDATA[ some <fake>tags</fake> in here ]]>
			</root>
			""";
		Assertions.assertDoesNotThrow(() -> {
			List<Fold> folds = getFolds(code);
			// Whatever the outcome, the top-level "root" tag should still
			// be recognized as a multi-line fold.
			Assertions.assertFalse(folds.isEmpty());
		});
	}


	@Test
	void testGetFolds_realisticDocument() {
		String code =
			"""
				<?xml version="1.0" encoding="UTF-8" ?>
				<!-- Books available in our library. -->
				<books>

				   <book>
				      <title>How to Win at Life</title>
				      <author>Donald Trump</author>
				      <publish-year>2004</publish-year>
				   </book>
				  \s
				   <book>
				      <title>Coding for Dummies</title>
				      <author>Nikhil Abraham</author>
				      <publish-year>2015</publish-year>
				   </book>
				  \s
				</books>
				""";

		List<Fold> folds = getFolds(code);

		Assertions.assertEquals(1, folds.size());
		Fold books = folds.getFirst();
		Assertions.assertEquals(FoldType.CODE, books.getFoldType());
		Assertions.assertEquals(2, books.getChildCount());

		Fold book1 = books.getChild(0);
		Assertions.assertEquals(0, book1.getChildCount());
		Assertions.assertEquals(code.indexOf("<book>"), book1.getStartOffset());

		Fold book2 = books.getChild(1);
		Assertions.assertEquals(0, book2.getChildCount());
		Assertions.assertEquals(code.lastIndexOf("<book>"), book2.getStartOffset());
	}
}
