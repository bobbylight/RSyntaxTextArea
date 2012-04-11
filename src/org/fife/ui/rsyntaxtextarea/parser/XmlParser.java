/*
 * 08/16/2008
 *
 * XMLParser.java - Simple XML parser.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.fife.io.DocumentReader;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;


/**
 * A parser for XML documents.  Adds squiggle underlines for any XML errors
 * found (though most XML parsers don't really have error recovery and so only
 * can find one error at a time).<p>
 *
 * This class isn't actually used by RSyntaxTextArea anywhere, but you can
 * install and use it yourself.  Doing so is as simple as:
 * 
 * <pre>
 * XmlParser xmlParser = new XmlParser();
 * textArea.addParser(xmlParser);
 * </pre>
 *
 * Also note that a single instance of this class can be installed on
 * multiple instances of <code>RSyntaxTextArea</code>.
 *
 * For a more complete XML parsing solution, see the
 * <a href="http://svn.fifesoft.com/viewvc-1.0.5/bin/cgi/viewvc.cgi/RSTALanguageSupport/trunk/?root=RSyntaxTextArea">RSTALanguageSupport
 * project</a>'s <code>XmlLanguageSupport</code> class.
 * 
 * @author Robert Futrell
 * @version 1.1
 */
public class XmlParser extends AbstractParser {

	private SAXParserFactory spf;
	private DefaultParseResult result;


	public XmlParser() {
		result = new DefaultParseResult(this);
		try {
			spf = SAXParserFactory.newInstance();
		} catch (FactoryConfigurationError fce) {
			fce.printStackTrace();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public ParseResult parse(RSyntaxDocument doc, String style) {

		result.clearNotices();
		Element root = doc.getDefaultRootElement();
		result.setParsedLines(0, root.getElementCount()-1);

		if (spf==null) {
			return result;
		}

		try {
			SAXParser sp = spf.newSAXParser();
			Handler handler = new Handler(doc);
			DocumentReader r = new DocumentReader(doc);
			InputSource input = new InputSource(r);
			sp.parse(input, handler);
			r.close();
		} catch (SAXParseException spe) {
			// A fatal parse error - ignore; a ParserNotice was already created.
		} catch (Exception e) {
			e.printStackTrace();
			result.addNotice(new DefaultParserNotice(this,
					"Error parsing XML: " + e.getMessage(), 0, -1, -1));
		}

		return result;

	}


	/**
	 * Callback notified when errors are found in the XML document.  Adds a
	 * notice to be squiggle-underlined.
	 */
	private class Handler extends DefaultHandler {

		private Document doc;

		private Handler(Document doc) {
			this.doc = doc;
		}

		private void doError(SAXParseException e, int level) {
			int line = e.getLineNumber() - 1;
			Element root = doc.getDefaultRootElement();
			Element elem = root.getElement(line);
			int offs = elem.getStartOffset();
			int len = elem.getEndOffset() - offs;
			if (line==root.getElementCount()-1) {
				len++;
			}
			DefaultParserNotice pn = new DefaultParserNotice(XmlParser.this,
											e.getMessage(), line, offs, len);
			pn.setLevel(level);
			result.addNotice(pn);
		}

		public void error(SAXParseException e) {
			doError(e, ParserNotice.ERROR);
		}

		public void fatalError(SAXParseException e) {
			doError(e, ParserNotice.ERROR);
		}

		public void warning(SAXParseException e) {
			doError(e, ParserNotice.WARNING);
		}

	}


}