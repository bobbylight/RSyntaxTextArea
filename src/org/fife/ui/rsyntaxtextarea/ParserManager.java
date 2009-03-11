/*
 * 09/26/2005
 *
 * ParserManager.java - Manages the parsing of an RSyntaxTextArea's document,
 * if necessary.
 * Copyright (C) 2005 Robert Futrell
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.io.DocumentReader;


/**
 * Manages running a parser object for an <code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @version 0.2
 */
class ParserManager implements DocumentListener, ActionListener {

	private RSyntaxTextArea textArea;
	private Parser parser;
	private Timer timer;

	private static final boolean DEBUG_PARSING	= true;

	/**
	 * The default delay between the last key press and when the document
	 * is parsed, in milliseconds.
	 */
	private static final int DEFAULT_DELAY_MS		= 1250;


	/**
	 * Constructor.
	 *
	 * @param textArea The text area whose document the parser will be
	 *        parsing.
	 */
	public ParserManager(RSyntaxTextArea textArea) {
		this(DEFAULT_DELAY_MS, textArea);
	}


	/**
	 * Constructor.
	 *
	 * @param delay The delay between the last key press and when the document
	 *        is parsed.
	 * @param textArea The text area whose document the parser will be
	 *        parsing.
	 */
	public ParserManager(int delay, RSyntaxTextArea textArea) {
		this.textArea = textArea;
		timer = new Timer(delay, this);
		timer.setRepeats(false);
	}


	public void actionPerformed(ActionEvent e) {

		if (parser==null) { // Sanity check
			return;
		}

		long begin = 0;
		if (DEBUG_PARSING) {
			begin = System.currentTimeMillis();
		}

		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		doc.readLock();
		try {
			DocumentReader r = new DocumentReader(doc);
			parser.parse(r);
			r.close();
		} finally {
			doc.readUnlock();
		}

		if (DEBUG_PARSING) {
			float time = (System.currentTimeMillis()-begin)/1000f;
			System.err.println("Parsing took: "+time+" seconds");
		}

		textArea.refreshParserNoticeHighlights(parser.getNoticeIterator());

	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void changedUpdate(DocumentEvent e) {
	}


	/**
	 * Returns the delay between the last "concurrent" edit and when the
	 * document is reparsed.
	 *
	 * @return The delay, in milliseconds.
	 * @see #setDelay(int)
	 */
	public int getDelay() {
		return timer.getDelay();
	}


	/**
	 * Returns the parser.
	 *
	 * @return The parser.
	 * @see #setParser(Parser)
	 */
	public Parser getParser() {
		return parser;
	}


	/**
	 * Returns the tool tip to display for a mouse event at the given
	 * location.  This method is overridden to give a registered parser a
	 * chance to display a tool tip (such as an error description when the
	 * mouse is over an error highlight).
	 *
	 * @param e The mouse event.
	 */
	public String getToolTipText(MouseEvent e) {
//		try {
			int pos = textArea.viewToModel(e.getPoint());
			/*
			Highlighter.Highlight[] highlights = textArea.getHighlighter().
												getHighlights();
			for (int i=0; i<highlights.length; i++) {
				Highlighter.Highlight h = highlights[i];
				//if (h instanceof ParserNoticeHighlight) {
				//	ParserNoticeHighlight pnh = (ParserNoticeHighlight)h;
					int start = h.getStartOffset();
					int end = h.getEndOffset();
					if (start<=pos && end>=pos) {
						//return pnh.getMessage();
						return textArea.getText(start, end-start);
					}
				//}
			}
			*/
			Iterator i = parser.getNoticeIterator();
			if (i!=null) {
				while (i.hasNext()) {
					ParserNotice notice = (ParserNotice)i.next();
					if (notice.containsPosition(pos)) {
						return notice.getMessage();
					}
				}
			}
//		} catch (BadLocationException ble) {
//			ble.printStackTrace();	// Should never happen.
//		}
		return null;
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void handleDocumentEvent(DocumentEvent e) {
		timer.restart();
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void insertUpdate(DocumentEvent e) {
		handleDocumentEvent(e);
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void removeUpdate(DocumentEvent e) {
		handleDocumentEvent(e);
	}


	/**
	 * Sets the delay between the last "concurrent" edit and when the document
	 * is re-parsed.
	 *
	 * @param millis The new delay, in milliseconds.  This must be greater
	 *        than <code>0</code>.
	 * @see #getDelay()
	 */
	public void setDelay(int millis) {
		boolean running = timer.isRunning();
		timer.stop();
		timer.setDelay(millis);
		if (running) {
			timer.start();
		}
	}


	/**
	 * Sets the parser.
	 *
	 * @param parser The new parser.  If this is <code>null</code>, no more
	 *        parsing is done.
	 * @see #getParser()
	 */
	public void setParser(Parser parser) {

		// Clean up old timer, if necessary.
		if (this.parser!=null) {
			timer.stop();
			// Don't unregister the text area as the user might have
			// registered it themselves to supply tool tips other ways, such
			// as ToolTipSupplier.
			//ToolTipManager.sharedInstance().unregisterComponent(textArea);
			textArea.getDocument().removeDocumentListener(this);
		}

		// Set the new parser.
		this.parser = parser;

		// Set up timer, if necessary.
		if (parser!=null) {
			ToolTipManager.sharedInstance().registerComponent(textArea);
			textArea.getDocument().addDocumentListener(this);
			timer.start();
		}

	}


}