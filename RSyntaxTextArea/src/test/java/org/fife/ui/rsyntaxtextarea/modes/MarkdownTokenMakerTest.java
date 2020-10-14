/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link MarkdownTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MarkdownTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new MarkdownTokenMaker();
	}


	@Test
	public void testGetCurlyBracesDenoteCodeBlocks() {
		Assert.assertFalse(createTokenMaker().getCurlyBracesDenoteCodeBlocks(0));
	}


	@Test
	@Override
	public void testGetLineCommentStartAndEnd() {
		Assert.assertNull(createTokenMaker().getLineCommentStartAndEnd(0));
	}


	@Test
	public void testMarkdownHeaders() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"# Heading 1",
			"## Heading 2",
			"### Heading 3"
		);
	}


	@Test
	public void testMarkdownSyntaxHighlightingStarting_noLanguageSpecified() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"```",
			"``` This is a block paragraph on a single line ```"
		);
	}


	@Test
	public void testMarkdownSyntaxHighlightingStarting_languageSpecified() {

		TokenMaker tm = createTokenMaker();
		Token t = tm.getTokenList(createSegment("```javascript"), TokenTypes.NULL, 0);

		Assert.assertTrue(t.is(TokenTypes.PREPROCESSOR, "```"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.VARIABLE, "javascript"));
	}


	@Test
	public void testMarkdownUnorderedListItem_minus() {
		testMarkdownUnorderedListItemImpl("- ");
	}


	@Test
	public void testMarkdownUnorderedListItem_star() {
		testMarkdownUnorderedListItemImpl("* ");
	}


	@Test
	public void testMarkdownUnorderedListItem_plus() {
		testMarkdownUnorderedListItemImpl("+ ");
	}


	private void testMarkdownUnorderedListItemImpl(String code) {

		TokenMaker tm = createTokenMaker();
		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);

		Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, t.getType());
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
	}


	@Test
	public void testMarkdownOrderedListItem_singleDigit() {
		testMarkdownOrderedListItemImpl("1. ");
	}


	@Test
	public void testMarkdownOrderedListItem_multipleDigits() {
		testMarkdownOrderedListItemImpl("572. ");
	}


	private void testMarkdownOrderedListItemImpl(String code) {

		TokenMaker tm = createTokenMaker();
		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);

		Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, t.getType());
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "."));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
	}


	@Test
	public void testMarkdownBoldItalic() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"***",
			"***word***",
			"***multiple bolded and italicized words***",
			"___",
			"___word___",
			"___multiple bolded and italicized words___"
		);
	}


	@Test
	public void testMarkdownBold() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2,
			"**",
			"**word**",
			"**multiple bolded words**",
			"__",
			"__word__",
			"__multiple bolded words__"
		);
	}


	@Test
	public void testMarkdownItalic() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"*",
			"*word*",
			"*multiple italicized words*",
			"_",
			"_word__",
			"_multiple italicized words_"
		);
	}


	@Test
	public void testMarkdownStrikethrough() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"~~",
			"~~word~~",
			"~~multiple strikethrough words~~"
		);
	}


	@Test
	public void testMarkdownOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"~"
		);
	}


	@Test
	public void testMarkdownBlockQuote_beginningOfLine() {

		TokenMaker tm = createTokenMaker();

		String code = "> block quote";
		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);
		Assert.assertTrue(t.is(TokenTypes.COMMENT_EOL, code));
	}


	@Test
	public void testMarkdownBlockQuote_firstNonWhitespaceToken() {

		TokenMaker tm = createTokenMaker();

		String code = "   > block quote";
		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);

		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, "   "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.COMMENT_EOL, "> block quote"));
	}


	@Test
	public void testMarkdownBlockQuote_badSinceNotFirstNonWhitespaceToken() {

		TokenMaker tm = createTokenMaker();

		String code = "first > block quote";
		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);

		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "first"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, ">"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "block"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "quote"));
	}


	@Test
	public void testMarkdownUrl() {
		testMarkdownImageOrUrlImpl("[test label](https://google.com)");
	}


	@Test
	public void testMarkdownImage() {
		testMarkdownImageOrUrlImpl("![test label](https://google.com)");
	}


	private void testMarkdownImageOrUrlImpl(String code) {

		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);
		Assert.assertEquals(TokenTypes.REGEX, t.getType());
		t = t.getNextToken();
		Assert.assertEquals(TokenTypes.ANNOTATION, t.getType());
	}


	@Test
	public void testMarkdownShorthandUrl() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"<https://google.com>",
			"<www.google.com>",
			"<www.fake.com/some/path?value=42>"
		);
	}


	@Test
	public void testMarkdownEmailAddress() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"<fake.address@google.com>"
		);
	}


	@Test
	public void testMarkdownHr_happyPath() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"---"
		);
	}


	@Test
	public void testMarkdownHr_error_wordAdjacentToToken() {

		TokenMaker tm = createTokenMaker();
		String code = "---test";

		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "---"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "test"));
	}


	@Test
	public void testMarkdownHr_error_firstButNotAloneOnLine() {

		TokenMaker tm = createTokenMaker();
		String code = "--- test";

		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "---"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(Token.WHITESPACE, " "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "test"));
	}


	@Test
	public void testMarkdownHr_error_notFirstOnLine() {

		TokenMaker tm = createTokenMaker();
		String code = "test ---";

		Token t = tm.getTokenList(createSegment(code), TokenTypes.NULL, 0);
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "test"));
		t = t.getNextToken();
		Assert.assertTrue(t.is(Token.WHITESPACE, " "));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "---"));
	}


	@Test
	public void testHtml_happyPath_tagWithAttributes() {

		String code = "<body onload=\"doSomething()\" data-extra='true'>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assert.assertTrue("Unexpected token: " + token, token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "data-extra"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'true'"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	public void testHtml_happyPath_closedTag() {

		String code = "<img src='foo.png'/>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "img"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "src"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assert.assertTrue("Unexpected token: " + token, token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'foo.png'"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "/>"));

	}


	@Test
	public void testHtml_happyPath_closingTag() {

		String code = "</body>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));
	}


	@Test
	public void testHtml_allowedTagNames() {

		String[] tagNames = {
			"a", "abbr", "acronym", "address", "applet", "area", "article",
			"aside", "audio", "b", "base", "basefont", "bdo", "bgsound", "big",
			"blink", "blockquote", "body", "br", "button", "canvas", "caption",
			"center", "cite", "code", "col", "colgroup", "command", "comment",
			"dd", "datagrid", "datalist", "datatemplate", "del", "details",
			"dfn", "dialog", "dir", "div", "dl", "dt", "em", "embed",
			"eventsource", "fieldset", "figure", "font", "footer", "form",
			"frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6",
			"head", "header", "hr", "html", "i", "iframe", "ilayer", "img",
			"input", "ins", "isindex", "kbd", "keygen", "label", "layer",
			"legend", "li", "link", "map", "mark", "marquee", "menu", "meta",
			"meter", "multicol", "nav", "nest", "nobr", "noembed", "noframes",
			"nolayer", "noscript", "object", "ol", "optgroup", "option",
			"output", "p", "param", "plaintext", "pre", "progress", "q", "rule",
			"s", "samp", "section", "select", "server", "small",
			"source", "spacer", "span", "strike", "strong",
			"sub", "sup", "table", "tbody", "td", "textarea", "tfoot", "th",
			"thead", "time", "title", "tr", "tt", "u", "ul", "var", "video"
		};

		TokenMaker tm = createTokenMaker();
		for (String tagName : tagNames) {

			for (int i = 0; i < tagName.length(); i++) {

				if (i > 0) {
					tagName = tagName.substring(0, i).toUpperCase() +
						tagName.substring(i);
				}

				String code = "<" + tagName;
				Segment segment = createSegment(code);
				Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
				Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
				token = token.getNextToken();
				Assert.assertEquals("Not a valid markdown tag name token: " + token,
					token.getType(), TokenTypes.MARKUP_TAG_NAME);
			}

		}
	}


	@Test
	public void testHtml_loneIdentifier() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"123"
		);
	}
}
