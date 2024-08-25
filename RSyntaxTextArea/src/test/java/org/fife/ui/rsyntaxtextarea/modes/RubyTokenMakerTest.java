/*
 * 06/21/2015
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

import java.io.InputStream;


/**
 * Unit tests for the {@link RubyTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RubyTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new RubyTokenMaker();
	}


	protected TokenMaker createTokenMaker(InputStream in) {
		return new RubyTokenMaker(in);
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.VARIABLE;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testBacktickLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"`Hello world`",
			"`Hello world", // Unterminated string literals not flagged as errors yet
			"`Hello \\q world`", // Any escapes are ignored
			"``"
		);
	}


	@Test
	void testBacktickLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			TokenTypes.LITERAL_BACKQUOTE,
			"Hello world",
			"Hello world`"
		);
	}


	@Test
	void testBooleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			"true",
			"false"
		);
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'Hello world'",
			"'Hello world", // Unterminated char literals not flagged as errors yet
			"'Hello \\q world'", // Any escapes are ignored
			"''"
		);
	}


	@Test
	void testCharLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from prior line",
			"continued from prior line'"
		);
	}


	@Test
	void testDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"=begin Hello world =end"
		);
	}


	@Test
	void testDocComments_URL() {
		String[] docCommentLiterals = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"https://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : docCommentLiterals) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
			Assertions.assertTrue(token.isHyperlink(), "Not a hyperlink: " + token);
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(literal, token.getLexeme());
		}

	}


	@Test
	void testDocComments_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			 TokenTypes.COMMENT_DOCUMENTATION,
			"continued from prior line =end"
		);
	}


	@Test
	void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testFloatingPointLiterals() {

		String[] floats = {
			"3e10", "3e+10", "3e-10",
			"3E10", "3E+10", "3E-10",
			"3e1_0",
			"3E1_0",
		};

		for (String code : floats) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_FLOAT, code));
		}

	}


	@Test
	void testFunctions() {

		String[] functions = {
			"Array", "Float", "Integer", "String", "at_exit", "autoload",
			"binding", "caller", "catch", "chop", "chop!", "chomp", "chomp!",
			"eval", "exec", "exit", "exit!", "fail", "fork", "format", "gets",
			"global_variables", "gsub", "gsub!", "iterator?", "lambda", "load",
			"local_variables", "loop", "open", "p", "print", "printf", "proc",
			"putc", "puts", "raise", "rand", "readline", "readlines", "require",
			"select", "sleep", "split", "sprintf", "srand", "sub", "sub!",
			"syscall", "system", "test", "trace_var", "trap", "untrace_var",
		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	void testHeredoc_EOF() {
		// Note that the terminating "EOF" should be on another line in real
		// Ruby scripts, but our lexer does not discern that.
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,

			"<<EOF Hello world EOF",
			"<<EOF Hello world unclosed",

			"<<\"EOF\" Hello world EOF",
			"<<\"EOF\" Hello world unclosed",
			"<< \"EOF\" Hello world EOF",
			"<<   \t\"EOF\" Hello world EOF",

			"<<'EOF' Hello world EOF",
			"<<'EOF' Hello world unclosed",
			"<< 'EOF' Hello world EOF",
			"<<   \t'EOF' Hello world EOF",

			"<<`EOF` Hello world EOF",
			"<<`EOF` Hello world unclosed",
			"<< `EOF` Hello world EOF",
			"<<   \t`EOF` Hello world EOF"
		);
	}


	@Test
	void testHeredoc_EOF_continuedFromPriorLine() {
		// Note that the terminating "EOF" should be on another line in real
		// Ruby scripts, but our lexer does not discern that.
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			RubyTokenMaker.INTERNAL_HEREDOC_EOF,
			"continued from prior line",
			"continued from prior line EOF",
			"EOF"
		);
	}


	@Test
	void testHeredoc_EOT() {
		// Note that the terminating "EOT" should be on another line in real
		// Ruby scripts, but our lexer does not discern that.
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,

			"<<EOT Hello world EOT",
			"<<EOT Hello world unclosed",

			"<<\"EOT\" Hello world unclosed",
			"<<\"EOT\" Hello world EOT",
			"<< \"EOT\" Hello world EOT",
			"<<   \t\"EOT\" Hello world EOT",

			"<<'EOT' Hello world EOT",
			"<<'EOT' Hello world unclosed",
			"<< 'EOT' Hello world EOT",
			"<<   \t'EOT' Hello world EOT",

			"<<`EOT` Hello world unclosed",
			"<<`EOT` Hello world EOT",
			"<< `EOT` Hello world EOT",
			"<<   \t`EOT` Hello world EOT"
		);
	}


	@Test
	void testHeredoc_EOT_continuedFromPriorLine() {
		// Note that the terminating "EOT" should be on another line in real
		// Ruby scripts, but our lexer does not discern that.
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			RubyTokenMaker.INTERNAL_HEREDOC_EOT,
			"continued from prior line",
			"continued from prior line EOT",
			"EOT"
		);
	}


	@Test
	void testHexLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
			"0x1",
			"0xfe",
			"0x333333333333",
			"0xf_e",
			"0x333_33_3" // Underscores
		);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"_foo",
			"foo9",
			"_foo9"
		);
	}


	@Test
	void testIntegerLiterals() {

		String[] binaryInts = {
			"0b0", "0b111", "0b001", "0b10_01",
		};
		for (String code : binaryInts) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
		}

		String[] octalInts = {
			"0", "0777", "017", "0_54", "07_7____7",
		};
		for (String code : octalInts) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
		}

		String[] decimalInts = {
			"1", "333", "3_3____3",
			"0d1", "0d333", "0d3_3___3",
		};
		for (String code : decimalInts) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, code));
		}

	}


	@Test
	void testKeywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"alias", "BEGIN", "begin", "break", "case", "class", "def",
			"defined", "do", "else", "elsif", "END", "end", "ensure", "for",
			"if", "in", "module", "next", "nil", "private", "protected", "public", "redo", "rescue", "retry",
			"return", "self", "super", "then", "undef", "unless", "until",
			"when", "while", "yield"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"and", "or", "not",
			"::", ".", "-", "+", "!", "~", "*", "/", "%", "<<", ">>", "&", "|", "^",
			">", ">=", "<", "<=", "<=>", "==", "===", "!=", "=~", "!~", "&&", "||",
			"..", "...", "=", "+=", "-=", "*=", "/=", "%="
		);
	}


	@Test
	void testPredefinedVariables() {

		// ("$"([!@&`\'+0-9~=/\,;.<>_*$?:\"]|"DEBUG"|"FILENAME"|"LOAD_PATH"|"stderr"|"stdin"|"stdout"|"VERBOSE"|([\-][0adFiIlpwv])))
		assertAllTokensOfType(TokenTypes.VARIABLE,
			"$!", "$@",
			"$DEBUG", "$FILENAME", "$LOAD_PATH", "$stderr", "$stdin", "$stdout", "$VERBOSE"
		);
	}


	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(", ")", "{", "}", "[", "]"
		);
	}


	@Test
	void testStringLiterals() {

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,

			"\"Hello world\"",
			"\"Hello world", // Unterminated strings not flagged as errors yet
			"\"Hello \\q world\"", // Any escapes are ignored
			"\"\"",

			"%(\"Hello world\")",
			"%(\"Hello world", // Unterminated not yet flagged as errors
			"%Q(\"Hello world\")",
			"%q(\"Hello world\")",
			"%W(\"Hello world\")",
			"%w(\"Hello world\")",
			"%x(\"Hello world\")",

			"%{\"Hello world\"}",
			"%{\"Hello world", // Unterminated not yet flagged as errors
			"%Q{\"Hello world\"}",
			"%q{\"Hello world\"}",
			"%W{\"Hello world\"}",
			"%w{\"Hello world\"}",
			"%x{\"Hello world\"}",

			"%[\"Hello world\"]",
			"%[\"Hello world", // Unterminated not yet flagged as errors
			"%Q[\"Hello world\"]",
			"%q[\"Hello world\"]",
			"%W[\"Hello world\"]",
			"%w[\"Hello world\"]",
			"%x[\"Hello world\"]",

			"%<\"Hello world\">",
			"%<\"Hello world", // Unterminated not yet flagged as errors
			"%Q<\"Hello world\">",
			"%q<\"Hello world\">",
			"%W<\"Hello world\">",
			"%w<\"Hello world\">",
			"%x<\"Hello world\">",

			"%!\"Hello world\"!",
			"%!\"Hello world", // Unterminated not yet flagged as errors
			"%Q!\"Hello world\"!",
			"%q!\"Hello world\"!",
			"%W!\"Hello world\"!",
			"%w!\"Hello world\"!",
			"%x!\"Hello world\"!",

			"%/\"Hello world\"/",
			"%/\"Hello world", // Unterminated not yet flagged as errors
			"%Q/\"Hello world\"/",
			"%q/\"Hello world\"/",
			"%W/\"Hello world\"/",
			"%w/\"Hello world\"/",
			"%x/\"Hello world\"/"
		);
	}


	@Test
	void testStringLiterals_continuedFromPriorLine() {

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from prior line",
			"continued from prior line\""
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_PAREN,
			"continued from prior line",
			"continued from prior line\")"
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_CURLY_BRACE,
			"continued from prior line",
			"continued from prior line\"}"
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_SQUARE_BRACKET,
			"continued from prior line",
			"continued from prior line\"]"
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_LT,
			"continued from prior line",
			"continued from prior line\">"
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_BANG,
			"continued from prior line",
			"continued from prior line\"!"
		);

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RubyTokenMaker.INTERNAL_STRING_Q_SLASH,
			"continued from prior line",
			"continued from prior line\"/"
		);
	}


	@Test
	public void testSymbols() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			":foo",
			":_foo",
			":_foo3"
		);
	}


	@Test
	void testVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
			"$foo",
			"@foo",
			"@@foo"
		);
	}


	@Test
	public void testWhitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"\t",
			"\f",
			"  ",
			"\t\t",
			" \t "
		);
	}
}
