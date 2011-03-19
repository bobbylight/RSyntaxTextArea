/*
 * 08/16/2008
 *
 * XMLParser.java - Simple XML parser.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.fife.io.DocumentReader;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;


/**
 * A parser for XML documents.
 *
 * @author Robert Futrell
 * @version 1.0
 */
/*
 * TODO: Figure out why this is buggy...
 */
public class XMLParser extends AbstractParser {

	private SAXParserFactory spf;
	private RSyntaxTextArea textArea;
	private DefaultParseResult result;


	public XMLParser(RSyntaxTextArea textArea) {
		this.textArea = textArea;
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
			Handler handler = new Handler();
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


	private class Handler extends DefaultHandler {

		private void doError(SAXParseException e) {
			int line = e.getLineNumber() - 1;
			try {
				int offs = textArea.getLineStartOffset(line);
				int len = textArea.getLineEndOffset(line) - offs + 1;
				ParserNotice pn = new DefaultParserNotice(XMLParser.this,
											e.getMessage(), line, offs, len);
				result.addNotice(pn);
				System.err.println(">>> " + offs + "-" + len + " -> "+ pn);
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}

		public void error(SAXParseException e) throws SAXException {
			doError(e);
		}

		public void fatalError(SAXParseException e) throws SAXException {
			doError(e);
		}

		public void warning(SAXParseException e) throws SAXException {
			doError(e);
		}

	}


}