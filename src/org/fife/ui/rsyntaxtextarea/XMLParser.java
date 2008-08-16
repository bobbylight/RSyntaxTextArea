package org.fife.ui.rsyntaxtextarea;

import java.io.Reader;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;



/**
 * A parser for XML documents.
 *
 * @author Robert Futrell
 * @version 1.0
 */
/*
 * TODO: Figure out why this is buggy...
 */
public class XMLParser implements Parser {

	private SAXParserFactory spf;
	private RSyntaxTextArea textArea;
	private ArrayList noticeList = new ArrayList(1);


	public XMLParser(RSyntaxTextArea textArea) {
		this.textArea = textArea;
		try {
			spf = SAXParserFactory.newInstance();
		} catch (FactoryConfigurationError fce) {
			fce.printStackTrace();
		}
	}


	public Iterator getNoticeIterator() {
		return noticeList.iterator();
	}


	public void parse(Reader r) {

		noticeList.clear();

		if (spf==null) {
			return;
		}

		try {
			SAXParser sp = spf.newSAXParser();
			Handler handler = new Handler();
			InputSource input = new InputSource(r);
			sp.parse(input, handler);
		} catch (SAXParseException spe) {
			// A fatal parse error - ignore; a ParserNotice was already created.
		} catch (Exception e) {
			e.printStackTrace();
			noticeList.add(new ParserNotice("Error parsing XML: " + e.getMessage(), -1, -1));
		}

	}


	private class Handler extends DefaultHandler {

		private void doError(SAXParseException e) {
			int line = e.getLineNumber() - 1;
			try {
				int offs = textArea.getLineStartOffset(line);
				int len = textArea.getLineEndOffset(line) - offs + 1;
				ParserNotice pn = new ParserNotice(e.getMessage(), offs,len,
					e.getLineNumber(), e.getColumnNumber());
				noticeList.add(pn);
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