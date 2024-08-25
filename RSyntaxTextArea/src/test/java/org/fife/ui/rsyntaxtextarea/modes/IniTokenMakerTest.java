/*
 * 11/04/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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
		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () ->
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
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "Options"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "hello"));

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


}
