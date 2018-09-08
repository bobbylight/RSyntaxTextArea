/*
 * 09/20/2016
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
 * Unit tests for the {@link LatexTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LatexTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new LatexTokenMaker();
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"% Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testEolComments_escapedPercentNotAComment() {
		String code = "\\% not-comment";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, '\\'));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, '%'));
	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("%", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}
}