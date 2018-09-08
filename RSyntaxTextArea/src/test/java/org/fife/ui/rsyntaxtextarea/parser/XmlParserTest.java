/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.EntityResolver;


/**
 * Unit tests for the {@link XmlParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlParserTest {


	private void assertCleanParseResult_oneLineDocument(XmlParser parser,
			ParseResult res) {
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(0, notices.size());
	}


	@Test
	public void testZeroArgConstructor() {
		XmlParser parser = new XmlParser();
		Assert.assertNull(parser.getHyperlinkListener());
		Assert.assertNull(parser.getImageBase());
	}


	@Test
	public void testOneArgConstructor() {
		EntityResolver resolver = null;
		XmlParser parser = new XmlParser(resolver);
		Assert.assertNull(parser.getHyperlinkListener());
		Assert.assertNull(parser.getImageBase());
	}


	@Test
	public void testIsValidating() {
		XmlParser parser = new XmlParser();
		Assert.assertFalse(parser.isValidating());
		parser.setValidating(true);
		Assert.assertTrue(parser.isValidating());
	}


	@Test
	public void testParse_emptyDocument() throws Exception {

		XmlParser parser = new XmlParser();

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		assertCleanParseResult_oneLineDocument(parser, res);

	}


	@Test
	public void testParse_error_unclosedTag_nodtd() throws Exception {

		XmlParser parser = new XmlParser();

		// Include a DTD just for more code coverage
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?>\n" +
				"<books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(1, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(1, notices.size());
		ParserNotice notice = notices.get(0);
		Assert.assertEquals(ParserNotice.Level.ERROR, notice.getLevel());

	}


	@Test
	public void testParse_error_unclosedTag_withDtd() throws Exception {

		XmlParser parser = new XmlParser();

		// Include a DTD just for more code coverage
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?>\n" +
				"<!DOCTYPE RSyntaxTheme SYSTEM \"theme.dtd\">\n" +
				"<books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(2, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(1, notices.size());
		ParserNotice notice = notices.get(0);
		Assert.assertEquals(ParserNotice.Level.ERROR, notice.getLevel());

	}


	@Test
	public void testParse_happyPath() throws Exception {

		XmlParser parser = new XmlParser();

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?><books></books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		assertCleanParseResult_oneLineDocument(parser, res);

	}


	@Test
	public void testSetValidating() {
		XmlParser parser = new XmlParser();
		Assert.assertFalse(parser.isValidating());
		parser.setValidating(true);
		Assert.assertTrue(parser.isValidating());
	}


}