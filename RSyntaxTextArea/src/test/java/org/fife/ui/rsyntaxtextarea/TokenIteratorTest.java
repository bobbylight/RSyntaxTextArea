/*
 * 03/28/2014
 *
 * TokenIteratorTest.java - Unit tests for the iterator returned from an
 * RSyntaxDocument.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Element;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests for the iterator returned from an {@link RSyntaxDocument}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TokenIteratorTest {

	private static final RSyntaxTextAreaEditorKit kit =
			new RSyntaxTextAreaEditorKit();


	/**
	 * Verifies that using an {@link RSyntaxDocument}'s iterator returns the
	 * same set of tokens as manually getting the token list for each line.
	 */
	@Test
	public void testBasicIteration() throws Exception {

		RSyntaxDocument doc = null;

		// A well-formed Java document.
		doc = loadResource("TokenIteratorTest_JavaBasic.txt",
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		assertIteratorMatchesList(doc);

		// An unterminated Javadoc comment.
		doc = loadResource("TokenIteratorTest_UnterminatedJavadoc.txt",
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		assertIteratorMatchesList(doc);

		// A single line.
		doc.replace(0, doc.getLength(), "one two three", null);
		assertIteratorMatchesList(doc);

		// A single-line unterminated MLC.
		doc.replace(0, doc.getLength(), "/* Unterminated MLC", null);
		assertIteratorMatchesList(doc);

	}


	/**
	 * Tests empty documents, documents with lots of blank lines, etc.
	 */
	@Test
	public void testEmptyLines() throws Exception {

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);

		// An empty document.
		doc.remove(0, doc.getLength());
		assertIteratorMatchesList(doc);

		// A document with nothing but empty lines.
		doc.insertString(0, "\n\n\n\n", null);
		assertIteratorMatchesList(doc);

		// A document with nothing lots of empty lines before text.
		doc.insertString(0, "\n\n\n\nfor if while\n\n\n\n", null);
		assertIteratorMatchesList(doc);

	}


	/**
	 * Loads a text resource from the classpath into an instance of
	 * {@link RSyntaxDocument}.
	 *
	 * @param res The resource.
	 * @param syntax The syntax style to load with.
	 * @return The document.
	 * @throws Exception If anything goes wrong.
	 */
	private RSyntaxDocument loadResource(String res, String syntax)
			throws Exception {
		RSyntaxDocument doc = new RSyntaxDocument(syntax);
		BufferedReader r = new BufferedReader(new InputStreamReader(
						getClass().getResourceAsStream(res)));
		kit.read(r, doc, 0);
		r.close();
		return doc;
	}


	/**
	 * Compares the document's iterator's returned tokens against the expected
	 * token list for the document.  This method will cause a calling test to
	 * fail if the iterator doesn't return the current token list.
	 *
	 * @param doc The document.
	 */
	private static final void assertIteratorMatchesList(RSyntaxDocument doc) {

		List<Token> expected = getTokens(doc);
		int index = 0;
		//System.out.println("---");
		for (Token t : doc) {
			//System.out.println(t);
			assertEquals(expected.get(index), t);
			index++;
		}
		assertEquals(expected.size(), index);

	}


	/**
	 * Returns the set of expected paintable tokens from a document.
	 *
	 *  @param doc The document.
	 *  @return The list of tokens, in the order in which they appear.
	 */
	private static final List<Token> getTokens(RSyntaxDocument doc) {

		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();
		List<Token> list = new ArrayList<Token>();

		for (int i=0; i<lineCount; i++) {
			Token t = doc.getTokenListForLine(i);
			while (t!=null && t.isPaintable()) {
				list.add(new TokenImpl(t)); // Copy since Tokens are pooled
				t = t.getNextToken();
			}
		}

		return list;

	}


}