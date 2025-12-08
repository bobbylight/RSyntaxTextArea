/*
 * 09/20/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link LatexTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LatexTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new LatexTokenMaker();
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, TokenTypes.NULL, false,
			"% Hello world"
		);
	}


	@Test
	void testEolComments_escapedPercentNotAComment() {
		String code = "\\% not-comment";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertFalse(token.isComment());
	}


	@Test
	void testPackageWithOptions() {
		String code = "\\usepackage[i]{babel}";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.PREPROCESSOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.REGEX, token.getType());
	}

	@Test
	void testLabel() {
		String code = "\\label{L}";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.REGEX, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());


		code = "\\label {L}";
		segment = createSegment(code);
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType());

		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.REGEX, token.getType());

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("%", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}
}
