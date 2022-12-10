/*
 * 12/10/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link TokenImpl} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class TokenImplTest {


	@Test
	void testGetHTMLRepresentation_happyPath() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		// Don't bother checking font and other styles since it may be host-specific
		String actual = token.getHTMLRepresentation(textArea);
		Assertions.assertTrue(actual.startsWith("<font"));
		Assertions.assertTrue(actual.endsWith(">for</font>"));

	}


	@Test
	void testGetHTMLRepresentation_problemChars() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();

		char[] ch = " &\t<>'\"/".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, ch.length - 1, 0, TokenTypes.IDENTIFIER, 0);

		// Don't bother checking font and other styles since it may be host-specific
		String actual = token.getHTMLRepresentation(textArea);
		System.out.println(actual);
		Assertions.assertTrue(actual.startsWith("<font"));
		Assertions.assertTrue(actual.endsWith("> &amp;&#09;&lt;&gt;&#39;&#34;&#47;</font>"));

	}


	@Test
	void testIs_1arg_charArray_differentLengths() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is("while".toCharArray()));
	}


	@Test
	void testIs_1arg_charArray_sameLengthsButDifferent() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is("foo".toCharArray()));
	}


	@Test
	void testIs_1arg_charArray_same() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertTrue(token.is("for".toCharArray()));
	}


	@Test
	void testIs_2arg_charArray_differentLengths() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.IDENTIFIER, "while".toCharArray()));
	}


	@Test
	void testIs_2arg_charArray_sameLengthsButDifferent() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.IDENTIFIER, "foo".toCharArray()));
	}


	@Test
	void testIs_2arg_charArray_sameLexemeButDifferentType() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.RESERVED_WORD, "for".toCharArray()));
	}


	@Test
	void testIs_2arg_charArray_same() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertTrue(token.is(token.getType(), "for".toCharArray()));
	}


	@Test
	void testIs_2arg_string_differentLengths() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.IDENTIFIER, "while"));
	}


	@Test
	void testIs_2arg_string_sameLengthsButDifferent() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.IDENTIFIER, "foo"));
	}


	@Test
	void testIs_2arg_string_sameLexemeButDifferentType() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertFalse(token.is(TokenTypes.RESERVED_WORD, "for"));
	}


	@Test
	void testIs_2arg_string_same() {

		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.IDENTIFIER, 0);

		Assertions.assertTrue(token.is(token.getType(), "for"));
	}


	@Test
	void testIsComment_true() {

		char[] ch = "for".toCharArray();

		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.COMMENT_EOL, 0);
		Assertions.assertTrue(token.isComment());

		token = new TokenImpl(ch, 0, 2, 0, TokenTypes.COMMENT_MULTILINE, 0);
		Assertions.assertTrue(token.isComment());

		token = new TokenImpl(ch, 0, 2, 0, TokenTypes.COMMENT_DOCUMENTATION, 0);
		Assertions.assertTrue(token.isComment());

		token = new TokenImpl(ch, 0, 2, 0, TokenTypes.COMMENT_KEYWORD, 0);
		Assertions.assertTrue(token.isComment());

		token = new TokenImpl(ch, 0, 2, 0, TokenTypes.COMMENT_MARKUP, 0);
		Assertions.assertTrue(token.isComment());

		token = new TokenImpl(ch, 0, 2, 0, TokenTypes.MARKUP_COMMENT, 0);
		Assertions.assertTrue(token.isComment());
	}


	@Test
	void testComment_false() {
		char[] ch = "for".toCharArray();
		TokenImpl token = new TokenImpl(ch, 0, 2, 0, TokenTypes.RESERVED_WORD, 0);
		Assertions.assertFalse(token.isComment());
	}
}
