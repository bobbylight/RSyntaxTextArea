/*
 * 11/04/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link IniTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class IniTokenMakerTest extends AbstractJFlexTokenMakerTest {

	@Override
	protected TokenMaker createTokenMaker() {
		return new IniTokenMaker();
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		TokenMaker tm = createTokenMaker();
		String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
		Assertions.assertEquals(";", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_yycharat() {
		Segment segment = createSegment("foobar");
		TokenMaker tm = createTokenMaker();
		tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertThrows(StringIndexOutOfBoundsException.class, () ->
			// assertion needed to appease spotbugsTest
			Assertions.assertEquals('\n', ((AbstractJFlexTokenMaker)tm).yycharat(0))
		);
	}


	@Test
	void testComment() {

		String[] commentLiterals = {
			"# Hello world",
			"; Hello world",
		};

		for (String code : commentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testNameValuePair_happyPath() {

		String code = "dialog.title=Options";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "dialog.title"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "Options"));

	}


	@Test
	void testNameValuePair_happyPath_withWhitespace() {

		String code = "dialog.title = Options";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "dialog.title"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "Options"));

	}


	@Test
	void testNameValuePair_equalSignInValue() {

		String code = "dialog.title=Options=hello";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "dialog.title"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "Options=hello"));
	}


	@Test
	void testSections() {

		String[] sectionLiterals = {
			"[",
			"[Unclosed, incomplete",
			"[section1]",
			"[*.dat]",
		};

		for (String code : sectionLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.PREPROCESSOR, token.getType());
		}

	}

	@Test
	void sectionIntoValue() {
		String code = "array = [1,2,3,4]";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "array"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null) {
			if (token.getType() != TokenTypes.NULL) {
				Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
			}
			token = token.getNextToken();
		}
	}

	@Test
	void doubleQuotedValues() {
		// comment-like string quoted -> not a comment
		String code = "color = \"#ff44aa\"";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "color"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null) {
			if (token.getType() != TokenTypes.NULL) {
				Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
			}
			token = token.getNextToken();
		}

		// double-quoted string with EOL comment
		code = "colors=\"#ff4455 #22ee4f\"# hex";
		segment = createSegment(code);
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "colors"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, '"'));
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, '"'));
		token = token.getNextToken();
		Assertions.assertTrue(token.isComment());

		// double-quoted string with escapes
		code = "s=\"\\\"#\"";
		segment = createSegment(code);
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "s"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.REGEX, "\\\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "#")); // still into the string, not a comment
	}

	@Test
	void singleQuotedValues() {
		// comment-like string quoted -> not a comment
		String code = "hex_color = '#ff44aa'";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "hex_color"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null) {
			if (token.getType() != TokenTypes.NULL) {
				Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
			}
			token = token.getNextToken();
		}

		// double-quoted string with EOL comment
		code = "color='#ff44aa'# hex";
		segment = createSegment(code);
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "color"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();

		for (int i = 0; i < 3; i++) {
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
			token = token.getNextToken();
		}

		Assertions.assertTrue(token.isComment());

		// double-quoted string with escapes
		code = "s='\\;\\\"#'";
		segment = createSegment(code);
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "s"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, '\''));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.REGEX, "\\;"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.REGEX, "\\\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "#")); // still into the string, not a comment
	}
}
