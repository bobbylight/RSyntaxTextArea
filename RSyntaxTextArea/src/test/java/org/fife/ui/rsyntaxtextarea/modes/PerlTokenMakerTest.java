/*
 * 09/20/2015
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
 * Unit tests for the {@link PerlTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class PerlTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new PerlTokenMaker();
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
			boolean expected = i  == TokenTypes.IDENTIFIER || i == TokenTypes.FUNCTION ||
				i == TokenTypes.VARIABLE;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testBacktickLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"``",
			"`hi`",
			"`\\u00fe`",
			"`\\``"
		);
	}


	@Test
	void testBacktickLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			TokenTypes.LITERAL_BACKQUOTE,
			"continued from prior line",
			"continued from prior line\""
		);
	}


	@Test
	void testBacktickLiterals_unclosed() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"`",
			"`hi there",
			"`\\u00fe",
			"`\\` unclosed"
		);
	}


	@Test
	void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'",
			"'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Invalid char literal: " + token);
		}

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
	void testCharLiterals_unclosed() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'",
			"'hi there",
			"'\\u00fe",
			"'\\' unclosed"
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

		String code =
			// Basic doubles
			"3.0 4.2 3.0 4.2 .111 " +
			// Basic floats ending in f, F, d, or D
			"3.f 3.F 3.0f 3.0F .111f .111F " +
			// lower-case exponent, no sign
			"3.e7f 3.e7F 3.0e7f 3.0e7F .111e7f .111e7F " +
			// Upper-case exponent, no sign
			"3.E7f 3.E7F 3.0E7f 3.0E7F .111E7f .111E7F " +
			// Lower-case exponent, positive
			"3.e+7f 3.e+7F 3.0e+7f 3.0e+7F .111e+7f .111e+7F " +
			// Upper-case exponent, positive
			"3.E+7f 3.E+7F 3.0E+7f 3.0E+7F .111E+7f .111E+7F " +
			// Lower-case exponent, negative
			"3.e-7f 3.e-7F 3.0e-7f 3.0e-7F .111e-7f .111e-7F " +
			// Upper-case exponent, negative
			"3.E-7f 3.E-7F 3.0E-7f 3.0E-7F .111E-7f .111E-7F";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType(), "Invalid floating point: " + token);
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
	void testHeredoc_eot_doubleQuoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<\"EOT\"",
			"<<\"EOT\" this  is heredoc",
			"<<\"EOT\" this is heredoc EOT",
			"<<\"EOT\" this is heredoc with escaped chars \\EEOT",
			"<<\"EOT\" heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_eot_doubleQuoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			// State is reused here since it's identical in these two cases
			PerlTokenMaker.INTERNAL_HEREDOC_EOT_UNQUOTED,
			"more heredoc",
			"more heredoc",
			"more heredoc with escaped chars \\EEOT",
			"more heredoc with $ dollar sign not part of variable",
			"unterminated EOT",
			"EOT"
		);
	}


	@Test
	void testHeredoc_eot_singleQuoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<'EOT'",
			"<<'EOT' this  is heredoc",
			"<<'EOT' this is heredoc EOT",
			"<<'EOT' this is heredoc with escaped chars \\EEOT",
			"<<'EOT' heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_eot_singleQuoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			PerlTokenMaker.INTERNAL_HEREDOC_EOT_SINGLE_QUOTED,
			"more heredoc",
			"more heredoc",
			"more heredoc with escaped chars \\EEOT",
			"more heredoc with $ dollar sign not part of variable",
			"unterminated EOT",
			"EOT"
		);
	}


	@Test
	void testHeredoc_eot_unquoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<EOT",
			"<<EOT this  is heredoc",
			"<<EOT this is heredoc EOT",
			"<<EOT this is heredoc with escaped chars \\EEOT",
			"<<EOT heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_eot_unquoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			PerlTokenMaker.INTERNAL_HEREDOC_EOT_UNQUOTED,
			"more heredoc",
			"more heredoc EOT",
			"more heredoc with escaped chars \\EEOT",
			"more heredoc with $ dollar sign not part of variable",
			"EOT"
		);
	}


	@Test
	void testHeredoc_doubleQuoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<\"EOF\"",
			"<<\"EOF\" this  is heredoc",
			"<<\"EOF\" this is heredoc EOF",
			"<<\"EOF\" this is heredoc with escaped chars \\EEOF",
			"<<\"EOF\" heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_doubleQuoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			// State is reused here since it's identical in these two cases
			PerlTokenMaker.INTERNAL_HEREDOC_EOF_UNQUOTED,
			"more heredoc",
			"more heredoc",
			"more heredoc with escaped chars \\EEOF",
			"more heredoc with $ dollar sign not part of variable",
			"unterminated EOF",
			"EOF"
		);
	}


	@Test
	void testHeredoc_singleQuoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<'EOF'",
			"<<'EOF' this  is heredoc",
			"<<'EOF' this is heredoc EOF",
			"<<'EOF' this is heredoc with escaped chars \\EEOF",
			"<<'EOF' heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_singleQuoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			PerlTokenMaker.INTERNAL_HEREDOC_EOF_SINGLE_QUOTED,
			"more heredoc",
			"more heredoc",
			"more heredoc with escaped chars \\EEOF",
			"more heredoc with $ dollar sign not part of variable",
			"unterminated EOF",
			"EOF"
		);
	}


	@Test
	void testHeredoc_unquoted() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"<<EOF",
			"<<EOF this  is heredoc",
			"<<EOF this is heredoc EOF",
			"<<EOF this is heredoc with escaped chars \\EEOF",
			"<<EOF heredoc with $ dollar sign not part of variable"
		);
	}


	@Test
	void testHeredoc_unquoted_fromPriorLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			PerlTokenMaker.INTERNAL_HEREDOC_EOF_UNQUOTED,
			"more heredoc",
			"more heredoc EOF",
			"more heredoc with escaped chars \\EEOF",
			"more heredoc with $ dollar sign not part of variable",
			"EOF"
		);
	}


	@Test
	void testHeredoc_unqoutedAndDoubleQuote_withVariable() {

		String[] eofStarts = { "<<EOF", "<<\"EOF\"", "<<EOT", "<<\"EOT\"" };
		String[] variables = { "$foo", "@foo", "%foo", "${foo}", "$$foo", "@$foo", "%$foo" };
		for (String eofStart : eofStarts) {
			for (String variable : variables) {

				String code = eofStart + " " + variable + " more heredoc";
				Segment segment = createSegment(code);
				TokenMaker tm = createTokenMaker();
				Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

				Assertions.assertTrue(token.is(TokenTypes.PREPROCESSOR, eofStart + " "));
				token = token.getNextToken();
				Assertions.assertTrue(token.is(TokenTypes.VARIABLE, variable));
				token = token.getNextToken();
				Assertions.assertTrue(token.is(TokenTypes.PREPROCESSOR, " more heredoc"));
				Assertions.assertFalse(token.getNextToken().isPaintable());
			}
		}
	}


	@Test
	void testHeredoc_singleQuote_variablesNotHighlighted() {

		String[] eofStarts = { "<<'EOF'", "<<'EOT'" };
		String[] variables = { "$foo", "@foo", "%foo", "${foo}", "$$foo", "@$foo", "%$foo" };
		for (String eofStart : eofStarts) {
			for (String variable : variables) {

				String code = eofStart + " " + variable + " more heredoc";
				Segment segment = createSegment(code);
				TokenMaker tm = createTokenMaker();
				Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

				// All text is heredoc, no variables
				Assertions.assertTrue(token.is(TokenTypes.PREPROCESSOR, code));
				Assertions.assertFalse(token.getNextToken().isPaintable());
			}
		}
	}

	/* TODO: Start highlighting these!
	@Test
	public void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1u 0xfeu 0x333333333333u 0X1u 0Xfeu 0X33333333333u 0xFEu 0XFEu " +
				"0x1U 0xfeU 0x333333333333U 0X1U 0XfeU 0X33333333333U 0xFEU 0XFEU " +
				"0x1lu 0xfelu 0x333333333333lu 0X1lu 0Xfelu 0X33333333333lu 0xFElu 0XFElu " +
				"0x1LU 0xfeLU 0x333333333333LU 0X1LU 0XfeLU 0X33333333333LU 0xFELU 0XFELU " +
				"0x1ul 0xfeul 0x333333333333ul 0X1ul 0Xfeul 0X33333333333ul 0xFEul 0XFEul " +
				"0x1UL 0xfeUL 0x333333333333UL 0X1UL 0XfeUL 0X33333333333UL 0xFEUL 0XFEUL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Invalid hex literal: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertTrue(token.getType() == TokenTypes.NULL);

	}
	*/

	@Test
	void testStandardFunctions() {

		String[] functions = {
			"abs",
			"accept",
			"alarm",
			"atan2",
			"bind",
			"binmode",
			"bless",
			"caller",
			"chdir",
			"chmod",
			"chomp",
			"chop",
			"chown",
			"chr",
			"chroot",
			"close",
			"closedir",
			"connect",
			"cos",
			"crypt",
			"dbmclose",
			"dbmopen",
			"defined",
			"delete",
			"die",
			"dump",
			"each",
			"endgrent",
			"endhostent",
			"endnetent",
			"endprotoent",
			"endpwent",
			"endservent",
			"eof",
			"eval",
			"exec",
			"exists",
			"exit",
			"exp",
			"fcntl",
			"fileno",
			"flock",
			"fork",
			"formline",
			"getc",
			"getgrent",
			"getgrgid",
			"getgrnam",
			"gethostbyaddr",
			"gethostbyname",
			"gethostent",
			"getlogin",
			"getnetbyaddr",
			"getnetbyname",
			"getnetent",
			"getpeername",
			"getpgrp",
			"getppid",
			"getpriority",
			"getprotobyname",
			"getprotobynumber",
			"getprotoent",
			"getpwent",
			"getpwnam",
			"getpwuid",
			"getservbyname",
			"getservbyport",
			"getservent",
			"getsockname",
			"getsockopt",
			"glob",
			"gmtime",
			"goto",
			"grep",
			"hex",
			"index",
			"int",
			"ioctl",
			"join",
			"keys",
			"kill",
			//"last",
			"lc",
			"lcfirst",
			"length",
			"link",
			"listen",
			"local",
			"localtime",
			"log",
			"lstat",
			"map",
			"mkdir",
			"msgctl",
			"msgget",
			"msgrcv",
			"msgsnd",
			"my",
			//"next",
			"no",
			"oct",
			"open",
			"opendir",
			"ord",
			"our",
			"pack",
			"package",
			"pipe",
			"pop",
			"pos",
			"print",
			"printf",
			"prototype",
			"push",
			"quotemeta",
			"rand",
			"read",
			"readdir",
			"readline",
			"readlink",
			"readpipe",
			"recv",
			//"redo",
			"ref",
			"rename",
			"require",
			"reset",
			"return",
			"reverse",
			"rewinddir",
			"rindex",
			"rmdir",
			"scalar",
			"seek",
			"seekdir",
			"select",
			"semctl",
			"semget",
			"semop",
			"send",
			"sethostent",
			"setgrent",
			"setnetent",
			"setpgrp",
			"setpriority",
			"setprotoent",
			"setpwent",
			"setservent",
			"setsockopt",
			"shift",
			"shmctl",
			"shmget",
			"shmread",
			"shmwrite",
			"shutdown",
			"sin",
			"sleep",
			"socket",
			"socketpair",
			"sort",
			"splice",
			"split",
			"sprintf",
			"sqrt",
			"srand",
			"stat",
			"study",
			//"sub",
			"substr",
			"symlink",
			"syscall",
			"sysopen",
			"sysread",
			"sysseek",
			"system",
			"syswrite",
			"tell",
			"telldir",
			"tie",
			"tied",
			"time",
			"times",
			"truncate",
			"uc",
			"ucfirst",
			"umask",
			"undef",
			"unlink",
			"unpack",
			"unshift",
			"untie",
			"use",
			"utime",
			"values",
			"vec",
			"wait",
			"waitpid",
			"wantarray",
			"warn",
			"write",
		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType(), "Not a function: " + token);
		}

	}


	@Test
	void testKeywords() {

		String code = "and cmp continue do else elsif eq esac for foreach ge " +
				"if last le ne next not or redo sub unless until while xor";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType(), "Not a keyword: " + token);
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
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == >> ~ | &&";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType(), "Not an operator: " + token);
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
	void testPod() {
		String[] podCommandsExceptCut = {
			"pod", "head1", "head2", "head3", "head4", "over", "item", "back",
			"begin", "end", "for", "encoding"
		};

		for (String podCommand : podCommandsExceptCut) {
			String code = "=" + podCommand + " random other text";
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, "=" + podCommand));
			token = token.getNextToken();
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, " random other text"));
			Assertions.assertFalse(token.getNextToken().isPaintable());
		}
	}


	@Test
	void testPod_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			PerlTokenMaker.INTERNAL_POD,
			"foo bar",
			"=cut"
		);
	}


	@Test
	void testPod_endsWithCut() {
		String code = "=cut for";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, PerlTokenMaker.INTERNAL_POD, 0);
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, "=cut"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "for"));
		Assertions.assertFalse(token.getNextToken().isPaintable());
	}


	@Test
	void testPreprocessor() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"#! This is text"
		);
	}


	@Test
	void testRegex_followingCertainOperators() {
		assertAllSecondTokensAreRegexes(
			"=/foo/",
			"(/foo/",
			",/foo/",
			"?/foo/",
			":/foo/",
			"[/foo/",
			"!/foo/",
			"&/foo/",
			"=/foo/",
			"==/foo/",
			"!=/foo/",
			"<<=/foo/",
			">>=/foo/",
			"~/foo/",
			"!~/foo/"
		);
	}


	@Test
	void testRegex_notWhenFollowingCertainTokens() {
		assertAllSecondTokensAreNotRegexes(
			"^/foo/",
			">>/foo/",
			"<</foo/",
			"--/foo/",
			"4/foo/"
		);
	}


	@Test
	void testRegex_startOfLine() {
		assertAllTokensOfType(TokenTypes.REGEX,
			"/foo/",
			"/foo/msixpogcadlu",
			"/foo/m",
			"/foo/s",
			"/foo/i",
			"/foo/x",
			"/foo/p",
			"/foo/o",
			"/foo/g",
			"/foo/c",
			"/foo/a",
			"/foo/d",
			"/foo/l",
			"/foo/u",
			"/foo(captured)/",
			"/foo(?:captured)/",
			"/foo\\/bar/",
			"m/foo/msixpodualgc",
			"m/foo/",
			"m!foo!msixpodualgc",
			"m!foo!",
			"m|foo|msixpodualgc",
			"m|foo|",
			"m\\foo\\msixpodualgc",
			"m\\foo\\",
			"s/foo/bar/msixpodualgcer",
			"s/foo/bar/",
			"s!foo!bar!msixpodualgcer",
			"s!foo!bar!",
			"s|foo|bar|msixpodualgcer",
			"s|foo|bar|",
			"tr/foo/bar/cdsr",
			"tr/foo/bar/",
			"tr!foo!bar!cdsr",
			"tr!foo!bar!",
			"tr|foo|bar|cdsr",
			"tr|foo|bar|",
			"tr\\foo\\bar\\cdsr",
			"tr\\foo\\bar\\",
			"y/foo/bar/cdsr",
			"y/foo/bar/",
			"y!foo!bar!cdsr",
			"y!foo!bar!",
			"y|foo|bar|cdsr",
			"y|foo|bar|",
			"y\\foo\\bar\\cdsr",
			"y\\foo\\bar\\",
			"qr/foo/msixpodual",
			"qr/foo/",
			"qr!foo!msixpodual",
			"qr!foo!",
			"qr|foo|msixpodual",
			"qr|foo|",
			"qr\\foo\\msixpodual",
			"qr\\foo\\"
		);
	}


	@Test
	void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assertions.assertEquals(separators[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	void testStringLiterals_continuedFromPriorLine() {

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from prior line",
			"continued from prior line\""
		);
	}


	@Test
	void testStringLiterals_unclosed() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"",
			"\"hi there",
			"\"\\u00fe",
			"\"\\\" unclosed"
		);
	}


	@Test
	void testVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
		"$foo",
			"@foo",
			"%foo",
			"${foo}",
			"$$foo",
			"@$foo",
			"%$foo"
		);
	}


}
