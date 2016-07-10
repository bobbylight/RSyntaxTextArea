/*
 * 06/05/2016
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


/**
 * Adds some niceties to {@code AbstractTokenMakerTest}.  These two classes
 * should be merged when all unit tests in this package follow this
 * convention.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractTokenMakerTest2 extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	protected abstract TokenMaker createTokenMaker();


	/**
	 * Verifies that all tokens in an array have a specific token t ype.
	 *
	 * @param tokens The tokens.
	 * @param expectedType The expected token type.
	 */
	protected void assertAllTokensOfType(String[] tokens, int expectedType) {

		for (String token : tokens) {
			Segment segment = createSegment(token);
			TokenMaker tm = createTokenMaker();
			Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals("Token has unexpected type: orig=" + token +
					", actual=" + t, expectedType, t.getType());
		}

	}


}