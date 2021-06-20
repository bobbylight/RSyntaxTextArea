package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.antlr.MultiLineTokenInfo;
import org.fife.ui.rsyntaxtextarea.modes.antlr.TestLexer;
import org.junit.Assert;
import org.junit.Test;

public class AntlrTokenMakerTest extends AbstractTokenMakerTest2 {

	@Test
	public void testTokens() {
		Segment segment = createSegment("+ / * /* test */ /** javadoc */ \"short\" \"\"\"long\"\"\"");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.OPERATOR, t.getType());
		Assert.assertEquals("+", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.OPERATOR, t.getType());
		Assert.assertEquals("/", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.OPERATOR, t.getType());
		Assert.assertEquals("*", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, t.getType());
		Assert.assertEquals("/* test */", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, t.getType());
		Assert.assertEquals("/** javadoc */", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType());
		Assert.assertEquals("\"short\"", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType());
		Assert.assertEquals("\"\"\"long\"\"\"", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testEmpty() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testEmptyInMultiline() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.COMMENT_MULTILINE, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteMultilineBegin() {
		Segment segment = createSegment("/* incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, t.getType());
		Assert.assertEquals("/* incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteMultilineMiddle() {
		Segment segment = createSegment("incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.COMMENT_MULTILINE, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, t.getType());
		Assert.assertEquals("incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteMultilineEnd() {
		Segment segment = createSegment("incomplete */");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.COMMENT_MULTILINE, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, t.getType());
		Assert.assertEquals("incomplete */", t.getLexeme());
		t = t.getNextToken();
		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteDocBegin() {
		Segment segment = createSegment("/** incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, t.getType());
		Assert.assertEquals("/** incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteDocMiddle() {
		Segment segment = createSegment("incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, t.getType());
		Assert.assertEquals("incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteDocEnd() {
		Segment segment = createSegment("incomplete */");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, t.getType());
		Assert.assertEquals("incomplete */", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteLongStringBegin() {
		Segment segment = createSegment("\"\"\" incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType());
		Assert.assertEquals("\"\"\" incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteLongStringMiddle() {
		Segment segment = createSegment("incomplete");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType());
		Assert.assertEquals("incomplete", t.getLexeme());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testIncompleteLongStringEnd() {
		Segment segment = createSegment("incomplete \"\"\"");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType());
		Assert.assertEquals("incomplete \"\"\"", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);
	}

	@Test
	public void testErrorHandling() {
		Segment segment = createSegment("+ invalid");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		assertTokenText(segment, t);

		Assert.assertEquals(TokenTypes.OPERATOR, t.getType());
		Assert.assertEquals("+", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.WHITESPACE, t.getType());
		Assert.assertEquals(" ", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.ERROR_IDENTIFIER, t.getType());
		Assert.assertEquals("invalid", t.getLexeme());
		t = t.getNextToken();

		Assert.assertEquals(TokenTypes.NULL, t.getType());
		t = t.getNextToken();
		Assert.assertNull(t);

	}

	/**
	 * asserts that the token sequence can be used to exactly reconstruct the source segment
	 *
	 * @param segment    the source segment
	 * @param startToken the token sequence
	 */
	private void assertTokenText(Segment segment, Token startToken) {
		StringBuilder sb = new StringBuilder();
		for (Token t = startToken; t != null; t = t.getNextToken()) {
			if (t.isPaintable()) {
				sb.append(t.getLexeme());
			}
		}
		Assert.assertEquals(segment.toString(), sb.toString());
	}

	@Override
	protected TokenMaker createTokenMaker() {
		return new TestAntlrTokenMaker();
	}

	@Override
	public void testGetLineCommentStartAndEnd() {
		// unused
	}

	static class TestAntlrTokenMaker extends AntlrTokenMaker {
		public TestAntlrTokenMaker() {
			super(new MultiLineTokenInfo(0, TokenTypes.COMMENT_DOCUMENTATION, "/**", "*/"),
				new MultiLineTokenInfo(0, TokenTypes.COMMENT_MULTILINE, "/*", "*/"),
				new MultiLineTokenInfo(0, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"\"\"", "\"\"\""));
		}

		@Override
		protected int convertType(int type) {
			switch (type) {
				case TestLexer.T__0:
				case TestLexer.MUL:
				case TestLexer.DIV:
					return TokenTypes.OPERATOR;
				case TestLexer.STRING_LITERAL:
					return TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
				case TestLexer.COMMENT:
					return TokenTypes.COMMENT_MULTILINE;
				case TestLexer.COMMENT_DOC:
					return TokenTypes.COMMENT_DOCUMENTATION;
				case TestLexer.WS:
					return TokenTypes.WHITESPACE;
				default:
					throw new IllegalArgumentException("Unsupported type " + type);
			}
		}

		@Override
		protected Lexer createLexer(String text) {
			return new TestLexer(CharStreams.fromString(text)) {
				@Override
				public void skip() {
					// We need the skipped tokens in the stream, move them to the HIDDEN channel instead
					setChannel(Lexer.HIDDEN);
				}
			};
		}
	}
}
