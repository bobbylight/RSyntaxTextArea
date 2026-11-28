/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitTest {


	/**
	 * Reads the content into a new, empty document.
	 *
	 * @param content The content to read.
	 * @param chunkSize The maximum number of chars returned per {@code read()}
	 *        call, or {@code -1} for no limit.  Small values exercise the
	 *        logic handling line endings split across buffer boundaries.
	 * @return The document.
	 */
	private static Document read(String content, int chunkSize)
			throws IOException, BadLocationException {
		Document doc = new PlainDocument();
		read(doc, 0, content, chunkSize);
		return doc;
	}


	private static void read(Document doc, int pos, String content, int chunkSize)
			throws IOException, BadLocationException {
		Reader r = new StringReader(content);
		if (chunkSize > 0) {
			r = new ChunkedReader(r, chunkSize);
		}
		new RTextAreaEditorKit().read(r, doc, pos);
	}


	private static String getText(Document doc) throws BadLocationException {
		return doc.getText(0, doc.getLength());
	}


	private static Object getEol(Document doc) {
		return doc.getProperty(DefaultEditorKit.EndOfLineStringProperty);
	}


	@Test
	void testRead_emptyContent() throws Exception {
		Document doc = read("", -1);
		Assertions.assertEquals("", getText(doc));
		Assertions.assertEquals("\n", getEol(doc));
	}


	@Test
	void testRead_noNewlines() throws Exception {
		Document doc = read("hello world", -1);
		Assertions.assertEquals("hello world", getText(doc));
		Assertions.assertEquals("\n", getEol(doc));
	}


	@Test
	void testRead_lf() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2, 3 }) {
			Document doc = read("one\ntwo\n\nthree", chunkSize);
			Assertions.assertEquals("one\ntwo\n\nthree", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\n", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_crlf() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2, 3 }) {
			Document doc = read("one\r\ntwo\r\n\r\nthree", chunkSize);
			Assertions.assertEquals("one\ntwo\n\nthree", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r\n", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_crlf_atStartAndEnd() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2 }) {
			Document doc = read("\r\nmiddle\r\n", chunkSize);
			Assertions.assertEquals("\nmiddle\n", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r\n", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_cr() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2, 3 }) {
			Document doc = read("one\rtwo\rthree", chunkSize);
			Assertions.assertEquals("one\ntwo\nthree", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_cr_consecutive() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2, 3 }) {
			Document doc = read("one\r\r\rtwo", chunkSize);
			Assertions.assertEquals("one\n\n\ntwo", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_cr_atEndOfInput() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2 }) {
			Document doc = read("one\r", chunkSize);
			Assertions.assertEquals("one\n", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_cr_consecutiveAtEndOfInput() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2 }) {
			Document doc = read("one\r\r", chunkSize);
			Assertions.assertEquals("one\n\n", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_cr_onlyChar() throws Exception {
		Document doc = read("\r", -1);
		Assertions.assertEquals("\n", getText(doc));
		Assertions.assertEquals("\r", getEol(doc));
	}


	@Test
	void testRead_mixedCrAndCrlf_crlfWins() throws Exception {
		for (int chunkSize : new int[] { -1, 1, 2 }) {
			Document doc = read("one\rtwo\r\nthree", chunkSize);
			Assertions.assertEquals("one\ntwo\nthree", getText(doc), "chunkSize: " + chunkSize);
			Assertions.assertEquals("\r\n", getEol(doc), "chunkSize: " + chunkSize);
		}
	}


	@Test
	void testRead_mixedLfAndCr() throws Exception {
		Document doc = read("one\ntwo\rthree", -1);
		Assertions.assertEquals("one\ntwo\nthree", getText(doc));
		Assertions.assertEquals("\r", getEol(doc));
	}


	@Test
	void testRead_intoNonEmptyDocument_insertsAtPositionAndLeavesEolPropertyAlone()
			throws Exception {

		Document doc = new PlainDocument();
		doc.insertString(0, "startend", null);
		Object origEol = getEol(doc);

		read(doc, 5, "\r\nmiddle\r\n", -1);

		Assertions.assertEquals("start\nmiddle\nend", getText(doc));
		Assertions.assertEquals(origEol, getEol(doc));
	}


	@Test
	void testRead_largerThanReadBuffer() throws Exception {

		// Ensure a CRLF straddles the internal 32K read buffer boundary
		String prefix = "x".repeat(32767);
		Document doc = read(prefix + "\r\nend", -1);

		Assertions.assertEquals(prefix + "\nend", getText(doc));
		Assertions.assertEquals("\r\n", getEol(doc));
	}


	/**
	 * A reader that returns at most {@code chunkSize} characters per
	 * {@code read()} call.
	 */
	private static final class ChunkedReader extends Reader {

		private final Reader delegate;
		private final int chunkSize;

		ChunkedReader(Reader delegate, int chunkSize) {
			this.delegate = delegate;
			this.chunkSize = chunkSize;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			return delegate.read(cbuf, off, Math.min(len, chunkSize));
		}

		@Override
		public void close() throws IOException {
			delegate.close();
		}
	}
}
