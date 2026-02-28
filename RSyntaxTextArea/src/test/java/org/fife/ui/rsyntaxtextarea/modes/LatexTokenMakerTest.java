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

import java.util.List;
import java.util.Map;


/**
 * Unit tests for the {@link LatexTokenMaker} class.
 *
 * @author Robert Futrell
 * @author Mattia Marelli
 * @version 1.1
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
	void testMultilineOptions() {
		String code = "\\usepackage[a,b";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token tk = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals(TokenTypes.RESERVED_WORD, tk.getType());

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "["));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.PREPROCESSOR, "a"));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.OPERATOR, ","));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.PREPROCESSOR, "b"));

		code = "c]{package}";
		segment = createSegment(code);
		tk = tm.getTokenList(segment, LatexTokenMaker.STATE_OPT, 0);
		Assertions.assertTrue(tk.is(TokenTypes.PREPROCESSOR, "c"));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "]"));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "{"));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.REGEX, "package"));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "}"));
	}


	void testMathEnv(String markStart, String markEnd) {
		String code = String.format("%s\\alpha%s", markStart, markEnd);
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token tk = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, markStart));

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.is(TokenTypes.FUNCTION, "\\alpha"));

		tk = tk.getNextToken();
		Assertions.assertEquals(1, tk.getLanguageIndex()); // math
		Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, markEnd));
	}


	@Test
	void testMath() {
		List.of(
			Map.entry("$", "$"),
			Map.entry("$$", "$$"),
			Map.entry("\\[", "\\]"),
			Map.entry("\\(", "\\)")
		).forEach(e -> testMathEnv(e.getKey(), e.getValue()));
	}


	@Test
	void testHyperlink() {
		String code = "% https://google.com";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token tk = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(tk.isComment());

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.isComment());

		tk = tk.getNextToken();
		Assertions.assertTrue(tk.isHyperlink());
	}


	@Test
	void testSpecialCommands() {
		TokenMaker tm = createTokenMaker();
		List.of("newcommand", "renewcommand", "newenvironment", "renewenvironment",
				"newcounter", "newlength", "def", "usepackage")
			.forEach(s -> {
				Segment segment = createSegment("\\" + s + "{}");
				Token tk = tm.getTokenList(segment, TokenTypes.NULL, 0);
				Assertions.assertTrue(tk.is(TokenTypes.RESERVED_WORD, "\\" + s));

				tk = tk.getNextToken();
				Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "{"));

				tk = tk.getNextToken();
				Assertions.assertTrue(tk.is(TokenTypes.SEPARATOR, "}"));
			});
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
