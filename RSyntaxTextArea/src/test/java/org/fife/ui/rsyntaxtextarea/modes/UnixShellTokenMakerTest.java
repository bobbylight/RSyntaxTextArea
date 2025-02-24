/*
 * 03/16/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link UnixShellTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class UnixShellTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new UnixShellTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		TokenMaker tm = createTokenMaker();
		String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {

		TokenMaker tm = createTokenMaker();

		for (int i=0; i<TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER ||
				i == TokenTypes.VARIABLE;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}

	}


	/**
	 * Checks for a token in a token map, and makes sure it is mapped to the
	 * expected token type.
	 *
	 * @param tm The token map.
	 * @param token The token.
	 * @param tokenType The expected token type.
	 */
	private void assertTokenMapContains(TokenMap tm, String token,
			int tokenType) {
		int actualType = tm.get(token.toCharArray(), 0, token.length() - 1);
		Assertions.assertEquals(tokenType, actualType);
	}


	@Test
	void testAddToken_identifier() {

		AddTokenCatchingUnixShellTokenMaker tm = new AddTokenCatchingUnixShellTokenMaker();
		String identifier = "foo";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);

		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.IDENTIFIER, tm.lastTokenType);

	}


	@Test
	void testAddToken_keyword() {

		AddTokenCatchingUnixShellTokenMaker tm = new AddTokenCatchingUnixShellTokenMaker();

		String identifier = "do";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);
		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, tm.lastTokenType);

		identifier = "while";
		seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);
		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, tm.lastTokenType);

	}


	@Test
	void testAddToken_unknown() {

		AddTokenCatchingUnixShellTokenMaker tm = new AddTokenCatchingUnixShellTokenMaker();

		String identifier = "foobar";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, -42, 0);
		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.IDENTIFIER, tm.lastTokenType);

	}


	@Test
	void testAddToken_whitespace() {

		AddTokenCatchingUnixShellTokenMaker tm = new AddTokenCatchingUnixShellTokenMaker();

		String identifier = " ";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.WHITESPACE, 0);
		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.WHITESPACE, tm.lastTokenType);

		identifier = "\t";
		seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.WHITESPACE, 0);
		Assertions.assertEquals(identifier, tm.lastTokenLexeme);
		Assertions.assertEquals(TokenTypes.WHITESPACE, tm.lastTokenType);

	}


	@Test
	void testGetTokenList_backtickStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "`A \\`backtick\\` literal`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`A \\`backtick\\` literal`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_backtickStart_variables() {

		TokenMaker tm = createTokenMaker();

		String text = "`My $name is very ${foo} ${adjective}`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`My "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$name"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, " is very "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${foo}"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${adjective}"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_backtickStart_variableEscaped() {

		TokenMaker tm = createTokenMaker();

		String text = "`\\$notAVariable \\${notAVariable}`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`\\$notAVariable \\${notAVariable}`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_backtickStart_endWithNonCurlyVariable() {

		TokenMaker tm = createTokenMaker();

		String text = "`Hello $name`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`Hello "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$name"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);
	}


	@Test
	void testGetTokenList_backtickStart_variablesUnclosed() {

		TokenMaker tm = createTokenMaker();

		String text = "`${unclosedVar";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${unclosedVar"));

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType()); // Marker token

	}


	@Test
	void testGetTokenList_doubleQuoteStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "\"A \\\"doubleQuote\\\" literal\\";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"A \\\"doubleQuote\\\" literal\\"));

		token = token.getNextToken();
		Assertions.assertNull(token);

	}


	@Test
	void testGetTokenList_doubleQuoteStart_variables() {

		TokenMaker tm = createTokenMaker();

		String text = "\"My $name is very ${foo} ${adjective}\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"My "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$name"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, " is very "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${foo}"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${adjective}"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_doubleQuoteStart_variableEscaped() {

		TokenMaker tm = createTokenMaker();

		String text = "\"\\$notAVariable \\${notAVariable}\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\\$notAVariable \\${notAVariable}\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_doubleQuoteStart_endWithNonCurlyVariable() {

		TokenMaker tm = createTokenMaker();

		String text = "\"Hello $name\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"Hello "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$name"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);
	}


	@Test
	void testGetTokenList_doubleQuoteStart_variablesUnclosed() {

		TokenMaker tm = createTokenMaker();

		String text = "\"${unclosedVar";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${unclosedVar"));

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType()); // Marker token

	}


	@Test
	void testGetTokenList_eolStart_bang() {

		TokenMaker tm = createTokenMaker();

		String text = "#!";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.PREPROCESSOR, "#!"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_eolStart_anythingElse() {

		TokenMaker tm = createTokenMaker();

		String text = "# this is a comment";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, "# this is a comment"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "foo\\";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, '\\'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_equals() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "foo=";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_LiteralBackQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "foo`cat foo.txt`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`cat foo.txt`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_LiteralDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "foo\"Hello world\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"Hello world\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_LiteralSingleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "foo'Hello world'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'Hello world'"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_operator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "foo|";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '|'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_separator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "foo(";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, '('));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_separator2() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "foo,";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, ','));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_slash() {

		TokenMaker tm = createTokenMaker();

		String text = "foo/";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo/"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_variable() {

		TokenMaker tm = createTokenMaker();

		String text = "foo$bar";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$bar"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_identifierStart_whiteSpace() {

		TokenMaker tm = createTokenMaker();

		String text = "foo ";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalCharStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "'A \\'char\\' literal with escapes'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'A \\'char\\' literal with escapes'"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "42\\";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, '\\'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_LiteralBackQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "42`cat 42.txt`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`cat 42.txt`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_LiteralDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "42\"Hello world\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"Hello world\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_LiteralSingleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "42'Hello world'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'Hello world'"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_operator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "42|";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '|'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_separator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "42(";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, '('));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_separator2() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "42,";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, ','));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_variable() {

		TokenMaker tm = createTokenMaker();

		String text = "42$bar";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$bar"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_literalNumberStart_whiteSpace() {

		TokenMaker tm = createTokenMaker();

		String text = "42 ";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "42"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_EolComment() {

		TokenMaker tm = createTokenMaker();

		String text = "# This is a comment";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_EscapedBackQuote() {

		TokenMaker tm = createTokenMaker();

		// Parsed as two identifiers: '\\' and '`'
		String text = "\\`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "\\"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_EscapedDollarSign() {

		TokenMaker tm = createTokenMaker();

		// Parsed as two identifiers: '\\' and '$'
		String text = "\\$";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "\\"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "$"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_EscapedDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		// Parsed as two identifiers: '\\' and '"'
		String text = "\\\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "\\"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_EscapedSingleQuote() {

		TokenMaker tm = createTokenMaker();

		// Parsed as two identifiers: '\\' and "'"
		String text = "\\'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "\\"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "'"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_Identifiers() {

		String code = "foo foo_bar ";
		code += ". , ;"; // "separators2"

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testGetTokenList_nullStart_LiteralBackQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "`cat foo.txt`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_LiteralDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "\"Hello world\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_LiteralSingleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "'Hello world'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_Numeric() {

		String code = "42 722";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testGetTokenList_nullStart_Operators() {

		String code = "= | > < &";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testGetTokenList_nullStart_Separators() {

		String code = "( ) [ ]";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testGetTokenList_nullStart_Variable() {

		TokenMaker tm = createTokenMaker();

		String text = "$PATH";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

		text = "${varName}";
		s = createSegment(text);
		token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, text));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_nullStart_WhiteSpace() {

		TokenMaker tm = createTokenMaker();

		String text = " ";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isWhitespace());
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, text));

		text = "\t";
		s = createSegment(text);
		token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isWhitespace());
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, text));

	}


	@Test
	void testGetTokenList_variableStart_backSlash() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo\\";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, '\\'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_equals() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "$foo=";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_LiteralBackQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo`cat foo.txt`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`cat foo.txt`"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_LiteralDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo\"Hello world\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"Hello world\""));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_LiteralSingleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo'Hello world'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'Hello world'"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_operator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "$foo|";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '|'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_separator() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "$foo(";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, '('));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_separator2() {

		TokenMaker tm = createTokenMaker();

		// Variable assignment
		String text = "$foo,";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, ','));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_slash() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo/";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, '/'));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_variable() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo$bar";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo$bar"));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_variableStart_whiteSpace() {

		TokenMaker tm = createTokenMaker();

		String text = "$foo ";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "$foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_EolComment() {

		TokenMaker tm = createTokenMaker();

		String text = " # This is a comment";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, text.trim()));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_LiteralBackQuote() {

		TokenMaker tm = createTokenMaker();

		String text = " `cat foo.txt`";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, text.trim()));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_LiteralDoubleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = " \"Hello world\"";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, text.trim()));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_LiteralSingleQuote() {

		TokenMaker tm = createTokenMaker();

		String text = " 'Hello world'";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, text.trim()));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_Variable() {

		TokenMaker tm = createTokenMaker();

		String text = " $PATH";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, text.trim()));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetTokenList_whitespaceStart_whitespace() {

		TokenMaker tm = createTokenMaker();

		String text = "  \t ";
		Segment s = createSegment(text);
		Token token = tm.getTokenList(s, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, "  \t "));

		token = token.getNextToken();
		Assertions.assertEquals(new TokenImpl(), token);

	}


	@Test
	void testGetWordsToHighlight() {

		TokenMaker tm = createTokenMaker();
		TokenMap tokens = ((UnixShellTokenMaker)tm).getWordsToHighlight();

		int reservedWord = TokenTypes.RESERVED_WORD;
		assertTokenMapContains(tokens, "case", reservedWord);
		assertTokenMapContains(tokens, "do", reservedWord);
		assertTokenMapContains(tokens, "done", reservedWord);
		assertTokenMapContains(tokens, "elif", reservedWord);
		assertTokenMapContains(tokens, "else", reservedWord);
		assertTokenMapContains(tokens, "esac", reservedWord);
		assertTokenMapContains(tokens, "fi", reservedWord);
		assertTokenMapContains(tokens, "for", reservedWord);
		assertTokenMapContains(tokens, "if", reservedWord);
		assertTokenMapContains(tokens, "in", reservedWord);
		assertTokenMapContains(tokens, "select", reservedWord);
		assertTokenMapContains(tokens, "then", reservedWord);
		assertTokenMapContains(tokens, "until", reservedWord);
		assertTokenMapContains(tokens, "while", reservedWord);

		int function = TokenTypes.FUNCTION;
		assertTokenMapContains(tokens, "addbib", function);
		assertTokenMapContains(tokens, "admin", function);
		assertTokenMapContains(tokens, "alias", function);
		assertTokenMapContains(tokens, "apropos", function);
		assertTokenMapContains(tokens, "ar", function);
		assertTokenMapContains(tokens, "at", function);
		assertTokenMapContains(tokens, "awk", function);
		assertTokenMapContains(tokens, "banner", function);
		assertTokenMapContains(tokens, "basename", function);
		assertTokenMapContains(tokens, "batch", function);
		assertTokenMapContains(tokens, "bg", function);
		assertTokenMapContains(tokens, "biff", function);
		assertTokenMapContains(tokens, "bin-mail", function);
		assertTokenMapContains(tokens, "binmail", function);
		assertTokenMapContains(tokens, "break", function);
		assertTokenMapContains(tokens, "cal", function);
		assertTokenMapContains(tokens, "calendar", function);
		assertTokenMapContains(tokens, "cancel", function);
		assertTokenMapContains(tokens, "cat", function);
		assertTokenMapContains(tokens, "cb", function);
		assertTokenMapContains(tokens, "cc", function);
		assertTokenMapContains(tokens, "cd", function);
		assertTokenMapContains(tokens, "cdc", function);
		assertTokenMapContains(tokens, "chdir", function);
		assertTokenMapContains(tokens, "checkeq", function);
		assertTokenMapContains(tokens, "checknr", function);
		assertTokenMapContains(tokens, "chfn", function);
		assertTokenMapContains(tokens, "chgrp", function);
		assertTokenMapContains(tokens, "chmod", function);
		assertTokenMapContains(tokens, "chown", function);
		assertTokenMapContains(tokens, "chsh", function);
		assertTokenMapContains(tokens, "clear", function);
		assertTokenMapContains(tokens, "cmp", function);
		assertTokenMapContains(tokens, "colcrt", function);
		assertTokenMapContains(tokens, "comb", function);
		assertTokenMapContains(tokens, "comm", function);
		assertTokenMapContains(tokens, "command", function);
		assertTokenMapContains(tokens, "compress", function);
		assertTokenMapContains(tokens, "continue", function);
		assertTokenMapContains(tokens, "cp", function);
		assertTokenMapContains(tokens, "cpio", function);
		assertTokenMapContains(tokens, "cpp", function);
		assertTokenMapContains(tokens, "crontab", function);
		assertTokenMapContains(tokens, "csh", function);
		assertTokenMapContains(tokens, "ctags", function);
		assertTokenMapContains(tokens, "curl", function);
		assertTokenMapContains(tokens, "cut", function);
		assertTokenMapContains(tokens, "cvs", function);
		assertTokenMapContains(tokens, "date", function);
		assertTokenMapContains(tokens, "dbx", function);
		assertTokenMapContains(tokens, "delta", function);
		assertTokenMapContains(tokens, "deroff", function);
		assertTokenMapContains(tokens, "df", function);
		assertTokenMapContains(tokens, "diff", function);
		assertTokenMapContains(tokens, "dtree", function);
		assertTokenMapContains(tokens, "du", function);
		assertTokenMapContains(tokens, "e", function);
		assertTokenMapContains(tokens, "echo", function);
		assertTokenMapContains(tokens, "ed", function);
		assertTokenMapContains(tokens, "edit", function);
		assertTokenMapContains(tokens, "enscript", function);
		assertTokenMapContains(tokens, "eqn", function);
		assertTokenMapContains(tokens, "error", function);
		assertTokenMapContains(tokens, "eval", function);
		assertTokenMapContains(tokens, "ex", function);
		assertTokenMapContains(tokens, "exec", function);
		assertTokenMapContains(tokens, "exit", function);
		assertTokenMapContains(tokens, "expand", function);
		assertTokenMapContains(tokens, "export", function);
		assertTokenMapContains(tokens, "expr", function);
		assertTokenMapContains(tokens, "false", function);
		assertTokenMapContains(tokens, "fc", function);
		assertTokenMapContains(tokens, "fg", function);
		assertTokenMapContains(tokens, "file", function);
		assertTokenMapContains(tokens, "find", function);
		assertTokenMapContains(tokens, "finger", function);
		assertTokenMapContains(tokens, "fmt", function);
		assertTokenMapContains(tokens, "fmt_mail", function);
		assertTokenMapContains(tokens, "fold", function);
		assertTokenMapContains(tokens, "ftp", function);
		assertTokenMapContains(tokens, "function", function);
		assertTokenMapContains(tokens, "gcore", function);
		assertTokenMapContains(tokens, "get", function);
		assertTokenMapContains(tokens, "getopts", function);
		assertTokenMapContains(tokens, "gprof", function);
		assertTokenMapContains(tokens, "grep", function);
		assertTokenMapContains(tokens, "groups", function);
		assertTokenMapContains(tokens, "gunzip", function);
		assertTokenMapContains(tokens, "gzip", function);
		assertTokenMapContains(tokens, "hashcheck", function);
		assertTokenMapContains(tokens, "hashmake", function);
		assertTokenMapContains(tokens, "head", function);
		assertTokenMapContains(tokens, "help", function);
		assertTokenMapContains(tokens, "history", function);
		assertTokenMapContains(tokens, "imake", function);
		assertTokenMapContains(tokens, "indent", function);
		assertTokenMapContains(tokens, "install", function);
		assertTokenMapContains(tokens, "jobs", function);
		assertTokenMapContains(tokens, "join", function);
		assertTokenMapContains(tokens, "kill", function);
		assertTokenMapContains(tokens, "last", function);
		assertTokenMapContains(tokens, "ld", function);
		assertTokenMapContains(tokens, "leave", function);
		assertTokenMapContains(tokens, "less", function);
		assertTokenMapContains(tokens, "let", function);
		assertTokenMapContains(tokens, "lex", function);
		assertTokenMapContains(tokens, "lint", function);
		assertTokenMapContains(tokens, "ln", function);
		assertTokenMapContains(tokens, "login", function);
		assertTokenMapContains(tokens, "look", function);
		assertTokenMapContains(tokens, "lookbib", function);
		assertTokenMapContains(tokens, "lorder", function);
		assertTokenMapContains(tokens, "lp", function);
		assertTokenMapContains(tokens, "lpq", function);
		assertTokenMapContains(tokens, "lpr", function);
		assertTokenMapContains(tokens, "lprm", function);
		assertTokenMapContains(tokens, "ls", function);
		assertTokenMapContains(tokens, "mail", function);
		assertTokenMapContains(tokens, "Mail", function);
		assertTokenMapContains(tokens, "make", function);
		assertTokenMapContains(tokens, "man", function);
		assertTokenMapContains(tokens, "md", function);
		assertTokenMapContains(tokens, "mesg", function);
		assertTokenMapContains(tokens, "mkdir", function);
		assertTokenMapContains(tokens, "mkstr", function);
		assertTokenMapContains(tokens, "more", function);
		assertTokenMapContains(tokens, "mount", function);
		assertTokenMapContains(tokens, "mv", function);
		assertTokenMapContains(tokens, "nawk", function);
		assertTokenMapContains(tokens, "neqn", function);
		assertTokenMapContains(tokens, "nice", function);
		assertTokenMapContains(tokens, "nm", function);
		assertTokenMapContains(tokens, "nroff", function);
		assertTokenMapContains(tokens, "od", function);
		assertTokenMapContains(tokens, "page", function);
		assertTokenMapContains(tokens, "passwd", function);
		assertTokenMapContains(tokens, "paste", function);
		assertTokenMapContains(tokens, "pr", function);
		assertTokenMapContains(tokens, "print", function);
		assertTokenMapContains(tokens, "printf", function);
		assertTokenMapContains(tokens, "printenv", function);
		assertTokenMapContains(tokens, "prof", function);
		assertTokenMapContains(tokens, "prs", function);
		assertTokenMapContains(tokens, "prt", function);
		assertTokenMapContains(tokens, "ps", function);
		assertTokenMapContains(tokens, "ptx", function);
		assertTokenMapContains(tokens, "pwd", function);
		assertTokenMapContains(tokens, "quota", function);
		assertTokenMapContains(tokens, "ranlib", function);
		assertTokenMapContains(tokens, "rcp", function);
		assertTokenMapContains(tokens, "rcs", function);
		assertTokenMapContains(tokens, "rcsdiff", function);
		assertTokenMapContains(tokens, "read", function);
		assertTokenMapContains(tokens, "readonly", function);
		assertTokenMapContains(tokens, "red", function);
		assertTokenMapContains(tokens, "return", function);
		assertTokenMapContains(tokens, "rev", function);
		assertTokenMapContains(tokens, "rlogin", function);
		assertTokenMapContains(tokens, "rm", function);
		assertTokenMapContains(tokens, "rmdel", function);
		assertTokenMapContains(tokens, "rmdir", function);
		assertTokenMapContains(tokens, "roffbib", function);
		assertTokenMapContains(tokens, "rsh", function);
		assertTokenMapContains(tokens, "rup", function);
		assertTokenMapContains(tokens, "ruptime", function);
		assertTokenMapContains(tokens, "rusers", function);
		assertTokenMapContains(tokens, "rwall", function);
		assertTokenMapContains(tokens, "rwho", function);
		assertTokenMapContains(tokens, "sact", function);
		assertTokenMapContains(tokens, "sccs", function);
		assertTokenMapContains(tokens, "sccsdiff", function);
		assertTokenMapContains(tokens, "script", function);
		assertTokenMapContains(tokens, "sed", function);
		assertTokenMapContains(tokens, "set", function);
		assertTokenMapContains(tokens, "setgroups", function);
		assertTokenMapContains(tokens, "setsenv", function);
		assertTokenMapContains(tokens, "sh", function);
		assertTokenMapContains(tokens, "shift", function);
		assertTokenMapContains(tokens, "size", function);
		assertTokenMapContains(tokens, "sleep", function);
		assertTokenMapContains(tokens, "sort", function);
		assertTokenMapContains(tokens, "sortbib", function);
		assertTokenMapContains(tokens, "spell", function);
		assertTokenMapContains(tokens, "split", function);
		assertTokenMapContains(tokens, "ssh", function);
		assertTokenMapContains(tokens, "strings", function);
		assertTokenMapContains(tokens, "strip", function);
		assertTokenMapContains(tokens, "stty", function);
		assertTokenMapContains(tokens, "su", function);
		assertTokenMapContains(tokens, "sudo", function);
		assertTokenMapContains(tokens, "symorder", function);
		assertTokenMapContains(tokens, "tabs", function);
		assertTokenMapContains(tokens, "tail", function);
		assertTokenMapContains(tokens, "talk", function);
		assertTokenMapContains(tokens, "tar", function);
		assertTokenMapContains(tokens, "tbl", function);
		assertTokenMapContains(tokens, "tee", function);
		assertTokenMapContains(tokens, "telnet", function);
		assertTokenMapContains(tokens, "test", function);
		assertTokenMapContains(tokens, "tftp", function);
		assertTokenMapContains(tokens, "time", function);
		assertTokenMapContains(tokens, "times", function);
		assertTokenMapContains(tokens, "touch", function);
		assertTokenMapContains(tokens, "trap", function);
		assertTokenMapContains(tokens, "troff", function);
		assertTokenMapContains(tokens, "true", function);
		assertTokenMapContains(tokens, "tsort", function);
		assertTokenMapContains(tokens, "tty", function);
		assertTokenMapContains(tokens, "type", function);
		assertTokenMapContains(tokens, "typeset", function);
		assertTokenMapContains(tokens, "ue", function);
		assertTokenMapContains(tokens, "ul", function);
		assertTokenMapContains(tokens, "ulimit", function);
		assertTokenMapContains(tokens, "umask", function);
		assertTokenMapContains(tokens, "unalias", function);
		assertTokenMapContains(tokens, "uncompress", function);
		assertTokenMapContains(tokens, "unexpand", function);
		assertTokenMapContains(tokens, "unget", function);
		assertTokenMapContains(tokens, "unifdef", function);
		assertTokenMapContains(tokens, "uniq", function);
		assertTokenMapContains(tokens, "units", function);
		assertTokenMapContains(tokens, "unset", function);
		assertTokenMapContains(tokens, "uptime", function);
		assertTokenMapContains(tokens, "users", function);
		assertTokenMapContains(tokens, "uucp", function);
		assertTokenMapContains(tokens, "uudecode", function);
		assertTokenMapContains(tokens, "uuencode", function);
		assertTokenMapContains(tokens, "uulog", function);
		assertTokenMapContains(tokens, "uuname", function);
		assertTokenMapContains(tokens, "uusend", function);
		assertTokenMapContains(tokens, "uux", function);
		assertTokenMapContains(tokens, "vacation", function);
		assertTokenMapContains(tokens, "val", function);
		assertTokenMapContains(tokens, "vedit", function);
		assertTokenMapContains(tokens, "vgrind", function);
		assertTokenMapContains(tokens, "vi", function);
		assertTokenMapContains(tokens, "view", function);
		assertTokenMapContains(tokens, "vtroff", function);
		assertTokenMapContains(tokens, "w", function);
		assertTokenMapContains(tokens, "wait", function);
		assertTokenMapContains(tokens, "wall", function);
		assertTokenMapContains(tokens, "wc", function);
		assertTokenMapContains(tokens, "wait", function);
		assertTokenMapContains(tokens, "what", function);
		assertTokenMapContains(tokens, "whatis", function);
		assertTokenMapContains(tokens, "whence", function);
		assertTokenMapContains(tokens, "whereis", function);
		assertTokenMapContains(tokens, "which", function);
		assertTokenMapContains(tokens, "who", function);
		assertTokenMapContains(tokens, "whoami", function);
		assertTokenMapContains(tokens, "write", function);
		assertTokenMapContains(tokens, "xargs", function);
		assertTokenMapContains(tokens, "xstr", function);
		assertTokenMapContains(tokens, "yacc", function);
		assertTokenMapContains(tokens, "yes", function);
		assertTokenMapContains(tokens, "zcat", function);
	}


	/**
	 * Overrides the default implementation to remember the last token's lexeme
	 * and type whenever an <code>addToken()</code> overload is called.
	 */
	private static final class AddTokenCatchingUnixShellTokenMaker
			extends UnixShellTokenMaker {

		private String lastTokenLexeme;
		private int lastTokenType;

		/**
		 * Overridden to cache the last token lexeme and type passed into this
		 * method.
		 */
		@Override
		public void addToken(char[] array, int start, int end, int tokenType,
				int startOffset, boolean hyperlink) {
			this.lastTokenLexeme = new String(array, start, end-start+1);
			this.lastTokenType = tokenType;
			super.addToken(array, start, end, tokenType, startOffset, hyperlink);
		}

	}


}
