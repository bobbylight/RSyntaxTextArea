/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.EntityResolver;


/**
 * Unit tests for the {@link XmlParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class XmlParserTest {


	private void assertCleanParseResult_oneLineDocument(XmlParser parser,
			ParseResult res) {
		Assertions.assertEquals(parser, res.getParser());
		Assertions.assertEquals(0, res.getFirstLineParsed());
		Assertions.assertEquals(0, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assertions.assertEquals(0, notices.size());
	}


	@Test
	void testZeroArgConstructor() {
		XmlParser parser = new XmlParser();
		Assertions.assertNull(parser.getHyperlinkListener());
		Assertions.assertNull(parser.getImageBase());
	}


	@Test
	void testOneArgConstructor() {
		EntityResolver resolver = null;
		XmlParser parser = new XmlParser(resolver);
		Assertions.assertNull(parser.getHyperlinkListener());
		Assertions.assertNull(parser.getImageBase());
	}


	@Test
	void testIsValidating() {
		XmlParser parser = new XmlParser();
		Assertions.assertFalse(parser.isValidating());
		parser.setValidating(true);
		Assertions.assertTrue(parser.isValidating());
	}


	@Test
	void testParse_emptyDocument() {

		XmlParser parser = new XmlParser();

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		assertCleanParseResult_oneLineDocument(parser, res);

	}


	@Test
	void testParse_error_unclosedTag_nodtd() throws Exception {

		XmlParser parser = new XmlParser();

		// Include a DTD just for more code coverage
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?>\n" +
				"<books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assertions.assertEquals(parser, res.getParser());
		Assertions.assertEquals(0, res.getFirstLineParsed());
		Assertions.assertEquals(1, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assertions.assertEquals(1, notices.size());
		ParserNotice notice = notices.get(0);
		Assertions.assertEquals(ParserNotice.Level.ERROR, notice.getLevel());

	}


	@Test
	void testParse_error_unclosedTag_withDtd() throws Exception {

		XmlParser parser = new XmlParser();

		// Include a DTD just for more code coverage
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?>\n" +
				"<!DOCTYPE RSyntaxTheme SYSTEM \"theme.dtd\">\n" +
				"<books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assertions.assertEquals(parser, res.getParser());
		Assertions.assertEquals(0, res.getFirstLineParsed());
		Assertions.assertEquals(2, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assertions.assertEquals(1, notices.size());
		ParserNotice notice = notices.get(0);
		Assertions.assertEquals(ParserNotice.Level.ERROR, notice.getLevel());

	}


	@Test
	void testParse_happyPath() throws Exception {

		XmlParser parser = new XmlParser();

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_XML);
		doc.insertString(0, "<?xml version='1.0'?><books></books>", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		assertCleanParseResult_oneLineDocument(parser, res);

	}


	@Test
	void testSetValidating() {
		XmlParser parser = new XmlParser();
		Assertions.assertFalse(parser.isValidating());
		parser.setValidating(true);
		Assertions.assertTrue(parser.isValidating());
	}


}
