/*
 * 09/20/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link PerlTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PerlTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	private TokenMaker createTokenMaker() {
		return new PerlTokenMaker();
	}


	@Test
	public void testCharLiterals() {

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
			Assert.assertEquals("Invalid char literal: " + token, TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testFloatingPointLiterals() {

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
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Invalid floating point: " + token, TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

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
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Invalid hex literal: " + token, TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}
*/

	@Test
	public void testStandardFunctions() {

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
			Assert.assertEquals("Not a function: " + token,
					TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	public void testKeywords() {

		String code = "and cmp continue do else elsif eq esac for foreach ge " +
				"if last le ne next not or redo sub unless until while xor";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not a keyword: " + token, TokenTypes.RESERVED_WORD, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == >> ~ | &&";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not an operator: " + token, TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assert.assertEquals(separators[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assert.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}