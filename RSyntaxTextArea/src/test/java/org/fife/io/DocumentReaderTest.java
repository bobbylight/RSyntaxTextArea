/*
 * 03/13/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.PlainDocument;


/**
 * Unit tests for the {@link DocumentReader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DocumentReaderTest {


	@Test
	void testClose() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		r.close();
	}


	@Test
	void testMark() throws Exception {

		PlainDocument doc = new PlainDocument();
		doc.insertString(0, "0123456789", null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		r.mark(5);
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());
		r.reset();
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());

		r.close();

	}


	@Test
	void testMarkSupported() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		Assertions.assertTrue(r.markSupported());
		r.close();
	}


	@Test
	void testRead_intArg() throws Exception {

		PlainDocument doc = new PlainDocument();
		doc.insertString(0, "0123456789", null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());
		Assertions.assertEquals('3', r.read());
		Assertions.assertEquals('4', r.read());
		Assertions.assertEquals('5', r.read());
		Assertions.assertEquals('6', r.read());
		Assertions.assertEquals('7', r.read());
		Assertions.assertEquals('8', r.read());
		Assertions.assertEquals('9', r.read());
		Assertions.assertEquals(-1, r.read());
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testRead_charArrayArg_allAtOnce() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assertions.assertEquals(10, r.read(array));
		Assertions.assertEquals(content, new String(array));

		Assertions.assertEquals(-1, r.read(array));
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testRead_charArrayArg_multipleReads() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[3];
		Assertions.assertEquals(3, r.read(array));
		Assertions.assertEquals("012", new String(array));

		Assertions.assertEquals(3, r.read(array));
		Assertions.assertEquals("345", new String(array));

		Assertions.assertEquals(3, r.read(array));
		Assertions.assertEquals("678", new String(array));

		Assertions.assertEquals(1, r.read(array));
		Assertions.assertEquals('9', array[0]);

		Assertions.assertEquals(-1, r.read(array));
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testRead_3Arg_allAtOnce() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assertions.assertEquals(10, r.read(array, 0, array.length));
		Assertions.assertEquals(content, new String(array));

		Assertions.assertEquals(-1, r.read(array, 0, array.length));
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testRead_3Arg_multipleReads() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assertions.assertEquals(3, r.read(array, 0, 3));
		Assertions.assertEquals("012", new String(array, 0, 3));

		Assertions.assertEquals(3, r.read(array, 0, 3));
		Assertions.assertEquals("345", new String(array, 0, 3));

		Assertions.assertEquals(3, r.read(array, 0, 3));
		Assertions.assertEquals("678", new String(array, 0, 3));

		Assertions.assertEquals(1, r.read(array, 0, 3));
		Assertions.assertEquals('9', array[0]);

		Assertions.assertEquals(-1, r.read(array, 0, 3));
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testReady() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		Assertions.assertTrue(r.ready());
		r.close();
	}


	@Test
	void testReset_NoMarkedOffset() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());
		r.reset();
		Assertions.assertEquals('0', r.read());
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());

		r.close();

	}


	@Test
	void testReset_MarkedOffset() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		r.mark(5);
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());
		r.reset();
		Assertions.assertEquals('1', r.read());
		Assertions.assertEquals('2', r.read());

		r.close();

	}


	@Test
	void testSeek_WithinDocument() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		r.seek(6);
		Assertions.assertEquals('6', r.read());
		Assertions.assertEquals('7', r.read());
		Assertions.assertEquals('8', r.read());
		Assertions.assertEquals('9', r.read());
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testSeek_PastDocumentEnd() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		r.seek(1000);
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testSkip_WithinDocument() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		Assertions.assertEquals(6, r.skip(6));
		Assertions.assertEquals('7', r.read());
		Assertions.assertEquals('8', r.read());
		Assertions.assertEquals('9', r.read());
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	void testSkip_PastDocumentEnd() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assertions.assertEquals('0', r.read());
		r.skip(1000);
		Assertions.assertEquals(-1, r.read());

		r.close();

	}


}
