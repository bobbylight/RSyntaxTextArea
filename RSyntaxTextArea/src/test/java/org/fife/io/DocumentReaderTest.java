/*
 * 03/13/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.io;

import javax.swing.text.PlainDocument;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link DocumentReader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DocumentReaderTest {


	@Test
	public void testClose() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		r.close();
	}


	@Test
	public void testMark() throws Exception {

		PlainDocument doc = new PlainDocument();
		doc.insertString(0, "0123456789", null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.mark(5);
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());
		r.reset();
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());

		r.close();

	}


	@Test
	public void testMarkSupported() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		Assert.assertTrue(r.markSupported());
		r.close();
	}


	@Test
	public void testRead_intArg() throws Exception {

		PlainDocument doc = new PlainDocument();
		doc.insertString(0, "0123456789", null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());
		Assert.assertEquals('3', r.read());
		Assert.assertEquals('4', r.read());
		Assert.assertEquals('5', r.read());
		Assert.assertEquals('6', r.read());
		Assert.assertEquals('7', r.read());
		Assert.assertEquals('8', r.read());
		Assert.assertEquals('9', r.read());
		Assert.assertEquals(-1, r.read());
		Assert.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	public void testRead_charArrayArg_allAtOnce() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assert.assertEquals(10, r.read(array));
		Assert.assertEquals(content, new String(array));

		Assert.assertEquals(-1, r.read(array));
		Assert.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	public void testRead_charArrayArg_multipleReads() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[3];
		Assert.assertEquals(3, r.read(array));
		Assert.assertEquals("012", new String(array));

		Assert.assertEquals(3, r.read(array));
		Assert.assertEquals("345", new String(array));

		Assert.assertEquals(3, r.read(array));
		Assert.assertEquals("678", new String(array));

		Assert.assertEquals(1, r.read(array));
		Assert.assertEquals('9', array[0]);

		Assert.assertEquals(-1, r.read(array));
		Assert.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	public void testRead_3Arg_allAtOnce() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assert.assertEquals(10, r.read(array, 0, array.length));
		Assert.assertEquals(content, new String(array));

		Assert.assertEquals(-1, r.read(array, 0, array.length));
		Assert.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	public void testRead_3Arg_multipleReads() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		char[] array = new char[10];
		Assert.assertEquals(3, r.read(array, 0, 3));
		Assert.assertEquals("012", new String(array, 0, 3));

		Assert.assertEquals(3, r.read(array, 0, 3));
		Assert.assertEquals("345", new String(array, 0, 3));

		Assert.assertEquals(3, r.read(array, 0, 3));
		Assert.assertEquals("678", new String(array, 0, 3));

		Assert.assertEquals(1, r.read(array, 0, 3));
		Assert.assertEquals('9', array[0]);

		Assert.assertEquals(-1, r.read(array, 0, 3));
		Assert.assertEquals(-1, r.read());

		r.close();

	}


	@Test
	public void testReady() {
		PlainDocument doc = new PlainDocument();
		DocumentReader r = new DocumentReader(doc);
		Assert.assertTrue(r.ready());
		r.close();
	}


	@Test
	public void testReset_NoMarkedOffset() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());
		r.reset();
		Assert.assertEquals('0', r.read());
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());

		r.close();

	}


	@Test
	public void testReset_MarkedOffset() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.mark(5);
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());
		r.reset();
		Assert.assertEquals('1', r.read());
		Assert.assertEquals('2', r.read());

		r.close();

	}


	@Test
	public void testSeek_WithinDocument() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.seek(6);
		Assert.assertEquals('6', r.read());
		Assert.assertEquals('7', r.read());
		Assert.assertEquals('8', r.read());
		Assert.assertEquals('9', r.read());
		Assert.assertEquals(-1, r.read());
		
		r.close();

	}


	@Test
	public void testSeek_PastDocumentEnd() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.seek(1000);
		Assert.assertEquals(-1, r.read());
		
		r.close();

	}


	@Test
	public void testSkip_WithinDocument() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.skip(6);
		Assert.assertEquals('7', r.read());
		Assert.assertEquals('8', r.read());
		Assert.assertEquals('9', r.read());
		Assert.assertEquals(-1, r.read());
		
		r.close();

	}


	@Test
	public void testSkip_PastDocumentEnd() throws Exception {

		String content = "0123456789";
		PlainDocument doc = new PlainDocument();
		doc.insertString(0, content, null);
		DocumentReader r = new DocumentReader(doc);

		Assert.assertEquals('0', r.read());
		r.skip(1000);
		Assert.assertEquals(-1, r.read());
		
		r.close();

	}


}