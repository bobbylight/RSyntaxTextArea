/*
 * 03/16/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link WindowsBatchTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class WindowsBatchTokenMakerTest extends AbstractTokenMakerTest {


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
		Assert.assertEquals("Token mapped to unexpected type: " + token,
				tokenType, actualType);
	}


	@Test
	public void testAddToken_identifier() {

		AddTokenCatchingWindowsBatchTokenMaker tm = new AddTokenCatchingWindowsBatchTokenMaker();
		String identifier = "foo";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);

		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(TokenTypes.IDENTIFIER, tm.lastTokenType);

	}


	@Test
	public void testAddToken_keyword() {

		AddTokenCatchingWindowsBatchTokenMaker tm = new AddTokenCatchingWindowsBatchTokenMaker();

		String identifier = "do";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);
		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(TokenTypes.RESERVED_WORD, tm.lastTokenType);

		identifier = "echo";
		seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.IDENTIFIER, 0);
		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(TokenTypes.RESERVED_WORD, tm.lastTokenType);

	}


	@Test
	public void testAddToken_unknown() {

		AddTokenCatchingWindowsBatchTokenMaker tm = new AddTokenCatchingWindowsBatchTokenMaker();

		String identifier = "foobar";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, -42, 0);
		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(-42, tm.lastTokenType);

	}


	@Test
	public void testAddToken_whitespace() {

		AddTokenCatchingWindowsBatchTokenMaker tm = new AddTokenCatchingWindowsBatchTokenMaker();

		String identifier = " ";
		Segment seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(TokenTypes.WHITESPACE, tm.lastTokenType);

		identifier = "\t";
		seg = createSegment(identifier);
		tm.addToken(seg, 0, identifier.length()-1, TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(identifier, tm.lastTokenLexeme);
		Assert.assertEquals(TokenTypes.WHITESPACE, tm.lastTokenType);

	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		WindowsBatchTokenMaker tm = new WindowsBatchTokenMaker();
		String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
		Assert.assertEquals("rem ", startAndEnd[0]);
		Assert.assertEquals(null, startAndEnd[1]);
	}


	@Test
	public void testGetMarkOccurrencesOfTokenType() {

		WindowsBatchTokenMaker tm = new WindowsBatchTokenMaker();

		for (int i=0; i<TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER ||
					i == TokenTypes.VARIABLE;
			Assert.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}

	}


	@Test
	public void testGetWordsToHighlight() {

		WindowsBatchTokenMaker tm = new WindowsBatchTokenMaker();
		TokenMap tokens = tm.getWordsToHighlight();

		int reservedWord = TokenTypes.RESERVED_WORD;
		assertTokenMapContains(tokens, "goto", reservedWord);
		assertTokenMapContains(tokens, "if", reservedWord);
		assertTokenMapContains(tokens, "shift", reservedWord);
		assertTokenMapContains(tokens, "start", reservedWord);

		assertTokenMapContains(tokens, "ansi.sys", reservedWord);
		assertTokenMapContains(tokens, "append", reservedWord);
		assertTokenMapContains(tokens, "arp", reservedWord);
		assertTokenMapContains(tokens, "assign", reservedWord);
		assertTokenMapContains(tokens, "assoc", reservedWord);
		assertTokenMapContains(tokens, "at", reservedWord);
		assertTokenMapContains(tokens, "attrib", reservedWord);
		assertTokenMapContains(tokens, "break", reservedWord);
		assertTokenMapContains(tokens, "cacls", reservedWord);
		assertTokenMapContains(tokens, "call", reservedWord);
		assertTokenMapContains(tokens, "cd", reservedWord);
		assertTokenMapContains(tokens, "chcp", reservedWord);
		assertTokenMapContains(tokens, "chdir", reservedWord);
		assertTokenMapContains(tokens, "chkdsk", reservedWord);
		assertTokenMapContains(tokens, "chknfts", reservedWord);
		assertTokenMapContains(tokens, "choice", reservedWord);
		assertTokenMapContains(tokens, "cls", reservedWord);
		assertTokenMapContains(tokens, "cmd", reservedWord);
		assertTokenMapContains(tokens, "color", reservedWord);
		assertTokenMapContains(tokens, "comp", reservedWord);
		assertTokenMapContains(tokens, "compact", reservedWord);
		assertTokenMapContains(tokens, "control", reservedWord);
		assertTokenMapContains(tokens, "convert", reservedWord);
		assertTokenMapContains(tokens, "copy", reservedWord);
		assertTokenMapContains(tokens, "ctty", reservedWord);
		assertTokenMapContains(tokens, "date", reservedWord);
		assertTokenMapContains(tokens, "debug", reservedWord);
		assertTokenMapContains(tokens, "defrag", reservedWord);
		assertTokenMapContains(tokens, "del", reservedWord);
		assertTokenMapContains(tokens, "deltree", reservedWord);
		assertTokenMapContains(tokens, "dir", reservedWord);
		assertTokenMapContains(tokens, "diskcomp", reservedWord);
		assertTokenMapContains(tokens, "diskcopy", reservedWord);
		assertTokenMapContains(tokens, "do", reservedWord);
		assertTokenMapContains(tokens, "doskey", reservedWord);
		assertTokenMapContains(tokens, "dosshell", reservedWord);
		assertTokenMapContains(tokens, "drivparm", reservedWord);
		assertTokenMapContains(tokens, "echo", reservedWord);
		assertTokenMapContains(tokens, "edit", reservedWord);
		assertTokenMapContains(tokens, "edlin", reservedWord);
		assertTokenMapContains(tokens, "emm386", reservedWord);
		assertTokenMapContains(tokens, "erase", reservedWord);
		assertTokenMapContains(tokens, "exist", reservedWord);
		assertTokenMapContains(tokens, "exit", reservedWord);
		assertTokenMapContains(tokens, "expand", reservedWord);
		assertTokenMapContains(tokens, "extract", reservedWord);
		assertTokenMapContains(tokens, "fasthelp", reservedWord);
		assertTokenMapContains(tokens, "fc", reservedWord);
		assertTokenMapContains(tokens, "fdisk", reservedWord);
		assertTokenMapContains(tokens, "find", reservedWord);
		assertTokenMapContains(tokens, "for", reservedWord);
		assertTokenMapContains(tokens, "format", reservedWord);
		assertTokenMapContains(tokens, "ftp", reservedWord);
		assertTokenMapContains(tokens, "graftabl", reservedWord);
		assertTokenMapContains(tokens, "help", reservedWord);
		assertTokenMapContains(tokens, "ifshlp.sys", reservedWord);
		assertTokenMapContains(tokens, "in", reservedWord);
		assertTokenMapContains(tokens, "ipconfig", reservedWord);
		assertTokenMapContains(tokens, "keyb", reservedWord);
		assertTokenMapContains(tokens, "kill", reservedWord);
		assertTokenMapContains(tokens, "label", reservedWord);
		assertTokenMapContains(tokens, "lh", reservedWord);
		assertTokenMapContains(tokens, "loadfix", reservedWord);
		assertTokenMapContains(tokens, "loadhigh", reservedWord);
		assertTokenMapContains(tokens, "lock", reservedWord);
		assertTokenMapContains(tokens, "md", reservedWord);
		assertTokenMapContains(tokens, "mem", reservedWord);
		assertTokenMapContains(tokens, "mkdir", reservedWord);
		assertTokenMapContains(tokens, "mklink", reservedWord);
		assertTokenMapContains(tokens, "mode", reservedWord);
		assertTokenMapContains(tokens, "more", reservedWord);
		assertTokenMapContains(tokens, "move", reservedWord);
		assertTokenMapContains(tokens, "msav", reservedWord);
		assertTokenMapContains(tokens, "msd", reservedWord);
		assertTokenMapContains(tokens, "mscdex", reservedWord);
		assertTokenMapContains(tokens, "nbtstat", reservedWord);
		assertTokenMapContains(tokens, "net", reservedWord);
		assertTokenMapContains(tokens, "netstat", reservedWord);
		assertTokenMapContains(tokens, "nlsfunc", reservedWord);
		assertTokenMapContains(tokens, "not", reservedWord);
		assertTokenMapContains(tokens, "nslookup", reservedWord);
		assertTokenMapContains(tokens, "path", reservedWord);
		assertTokenMapContains(tokens, "pathping", reservedWord);
		assertTokenMapContains(tokens, "pause", reservedWord);
		assertTokenMapContains(tokens, "ping", reservedWord);
		assertTokenMapContains(tokens, "power", reservedWord);
		assertTokenMapContains(tokens, "print", reservedWord);
		assertTokenMapContains(tokens, "prompt", reservedWord);
		assertTokenMapContains(tokens, "pushd", reservedWord);
		assertTokenMapContains(tokens, "popd", reservedWord);
		assertTokenMapContains(tokens, "qbasic", reservedWord);
		assertTokenMapContains(tokens, "rd", reservedWord);
		assertTokenMapContains(tokens, "ren", reservedWord);
		assertTokenMapContains(tokens, "rename", reservedWord);
		assertTokenMapContains(tokens, "rmdir", reservedWord);
		assertTokenMapContains(tokens, "route", reservedWord);
		assertTokenMapContains(tokens, "sc", reservedWord);
		assertTokenMapContains(tokens, "scandisk", reservedWord);
		assertTokenMapContains(tokens, "scandreg", reservedWord);
		assertTokenMapContains(tokens, "set", reservedWord);
		assertTokenMapContains(tokens, "setx", reservedWord);
		assertTokenMapContains(tokens, "setver", reservedWord);
		assertTokenMapContains(tokens, "share", reservedWord);
		assertTokenMapContains(tokens, "shutdown", reservedWord);
		assertTokenMapContains(tokens, "smartdrv", reservedWord);
		assertTokenMapContains(tokens, "sort", reservedWord);
		assertTokenMapContains(tokens, "subset", reservedWord);
		assertTokenMapContains(tokens, "switches", reservedWord);
		assertTokenMapContains(tokens, "sys", reservedWord);
		assertTokenMapContains(tokens, "time", reservedWord);
		assertTokenMapContains(tokens, "tracert", reservedWord);
		assertTokenMapContains(tokens, "tree", reservedWord);
		assertTokenMapContains(tokens, "type", reservedWord);
		assertTokenMapContains(tokens, "undelete", reservedWord);
		assertTokenMapContains(tokens, "unformat", reservedWord);
		assertTokenMapContains(tokens, "unlock", reservedWord);
		assertTokenMapContains(tokens, "ver", reservedWord);
		assertTokenMapContains(tokens, "verify", reservedWord);
		assertTokenMapContains(tokens, "vol", reservedWord);
		assertTokenMapContains(tokens, "xcopy", reservedWord);

	}


	/**
	 * Overrides the default implementation to remember the last token's lexeme
	 * and type whenever an <code>addToken()</code> overload is called.
	 */
	private static class AddTokenCatchingWindowsBatchTokenMaker
			extends WindowsBatchTokenMaker {

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