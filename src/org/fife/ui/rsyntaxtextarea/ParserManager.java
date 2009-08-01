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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ToolTipInfo;



/**
 * Manages running a parser object for an <code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @version 0.2
 */
class ParserManager implements DocumentListener, ActionListener,
								HyperlinkListener {

	private RSyntaxTextArea textArea;
	private List parsers;
	private Timer timer;
	private Map noticesToHighlights;
	private Parser parserForTip;
/*
	private Position firstOffsetModded;
	private Position lastOffsetModded;
*/
	/**
	 * Painter used to underline errors.
	 */
	private SquiggleUnderlineHighlightPainter parserErrorHighlightPainter =
						new SquiggleUnderlineHighlightPainter(Color.RED);

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
		textArea.getDocument().addDocumentListener(this);
		parsers = new ArrayList(1); // Usually small
		timer = new Timer(delay, this);
		timer.setRepeats(false);
	}


	/**
	 * Called when the timer fires (e.g. it's time to parse the document).
	 * 
	 * @param e The event.
	 */
	public void actionPerformed(ActionEvent e) {

		// Sanity check - should have >1 parser if event is fired.
		int parserCount = getParserCount();
		if (parserCount==0) {
			return;
		}

		long begin = 0;
		if (DEBUG_PARSING) {
			begin = System.currentTimeMillis();
		}

//		clearParserNoticeHighlights();
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*
		Element root = doc.getDefaultRootElement();
		int firstLine = firstOffsetModded==null ? 0 : root.getElementIndex(firstOffsetModded.getOffset());
		int lastLine = lastOffsetModded==null ? root.getElementCount()-1 : root.getElementIndex(lastOffsetModded.getOffset());
		firstOffsetModded = lastOffsetModded = null;
		System.out.println("[DEBUG]: Lines to parse: " + firstLine + "-" + lastLine);
*/
		String style = textArea.getSyntaxEditingStyle();
		doc.readLock();
		try {
			for (int i=0; i<parserCount; i++) {
				Parser parser = getParser(i);
				ParseResult res = parser.parse(doc, style);
				addParserNoticeHighlights(res);
			}
		} finally {
			doc.readUnlock();
		}

		if (DEBUG_PARSING) {
			float time = (System.currentTimeMillis()-begin)/1000f;
			System.err.println("Total parsing time: " + time + " seconds");
		}

	}


	/**
	 * Adds a parser for the text area.
	 *
	 * @param parser The new parser.  If this is <code>null</code>, nothing
	 *        happens.
	 * @see #getParser(int)
	 * @see #removeParser(Parser)
	 */
	public void addParser(Parser parser) {
		if (parser!=null && !parsers.contains(parser)) {
			timer.stop();
			parsers.add(parser);
			if (parsers.size()==1) {
				// Okay to call more than once.
				ToolTipManager.sharedInstance().registerComponent(textArea);
			}
			timer.restart();
		}
	}


	/**
	 * Adds highlights for a list of parser notices.
	 *
	 * @param res The result of a parsing.
	 * @see #clearParserNoticeHighlights()
	 */
	private void addParserNoticeHighlights(ParseResult res) {

		if (noticesToHighlights==null) {
			noticesToHighlights = new HashMap();
		}

		removeParserNotices(res);

		List notices = res.getNotices();
		if (notices.size()>0) { // Guaranteed non-null

			RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)
													textArea.getHighlighter();

			for (Iterator i=notices.iterator(); i.hasNext(); ) {
				ParserNotice notice = (ParserNotice)i.next();
				try {
					Object highlight = h.addParserHighlight(notice,
											parserErrorHighlightPainter);
					noticesToHighlights.put(notice, highlight);
				} catch (BadLocationException ble) { // Never happens
					ble.printStackTrace();
				}
			}

		}

	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void changedUpdate(DocumentEvent e) {
	}


	/**
	 * Removes all parsers.
	 *
	 * @see #addParser(Parser)
	 */
	public void clearParsers() {
		timer.stop();
		parsers.clear();
	}


	void clearParserNoticeHighlights() {
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)
											textArea.getHighlighter();
		if (h!=null) {
			h.clearParserHighlights();
		}
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
	 * Returns the specified parser.
	 *
	 * @param index The index of the parser.
	 * @return The parser.
	 * @see #getParserCount()
	 * @see #addParser(Parser)
	 * @see #removeParser(Parser)
	 */
	public Parser getParser(int index) {
		return (Parser)parsers.get(index);
	}


	/**
	 * Returns the number of registered parsers.
	 *
	 * @return The number of registered parsers.
	 */
	public int getParserCount() {
		return parsers.size();
	}


	/**
	 * Returns the tool tip to display for a mouse event at the given
	 * location.  This method is overridden to give a registered parser a
	 * chance to display a tool tip (such as an error description when the
	 * mouse is over an error highlight).
	 *
	 * @param e The mouse event.
	 * @return The tool tip to display, and possibly a hyperlink event handler. 
	 */
	public ToolTipInfo getToolTipText(MouseEvent e) {

		String tip = null;
		HyperlinkListener listener = null;
		parserForTip = null;

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
			if (noticesToHighlights!=null) {
				for (Iterator j=noticesToHighlights.keySet().iterator();
						j.hasNext(); ) {
					ParserNotice notice = (ParserNotice)j.next();
					if (notice.containsPosition(pos)) {
						tip = notice.getToolTipText();
						parserForTip = notice.getParser();
						if (notice.getParser() instanceof HyperlinkListener) {
							listener = (HyperlinkListener)notice.getParser();
						}
						break;
					}
				}
			}
//		} catch (BadLocationException ble) {
//			ble.printStackTrace();	// Should never happen.
//		}

		URL imageBase = parserForTip==null ? null :
							parserForTip.getImageBase();
		return new ToolTipInfo(tip, listener, imageBase);

	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void handleDocumentEvent(DocumentEvent e) {
		if (parsers.size()>0) {
			timer.restart();
		}
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void insertUpdate(DocumentEvent e) {
/*
		// Keep track of the first and last offset modified.  Some parsers are
		// smart and will only re-parse this section of the file.
		try {
			int offs = e.getOffset();
			if (firstOffsetModded==null || offs<firstOffsetModded.getOffset()) {
				firstOffsetModded = e.getDocument().createPosition(offs);
			}
			offs = e.getOffset() + e.getLength();
			if (lastOffsetModded==null || offs>lastOffsetModded.getOffset()) {
				lastOffsetModded = e.getDocument().createPosition(offs);
			}
		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Shouldn't happen
		}
*/
		handleDocumentEvent(e);

	}


	/**
	 * Removes any currently stored notices from the same Parser as in the
	 * results.
	 *
	 * @param res The results.
	 */
	private void removeParserNotices(ParseResult res) {

		if (noticesToHighlights!=null) {

			Parser parser = res.getParser();
			RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)
												textArea.getHighlighter();

			for (Iterator i=noticesToHighlights.entrySet().iterator();
					i.hasNext(); ) {
				Map.Entry entry = (Map.Entry)i.next();
				ParserNotice notice = (ParserNotice)entry.getKey();
				if (notice.getParser()==parser) {
					h.removeParserHighlight(entry.getValue());
					i.remove();
				}
			}

		}

	}


	/**
	 * Removes a parser.
	 *
	 * @param parser The parser to remove.
	 * @return Whether the parser was found.
	 * @see #addParser(Parser)
	 * @see #getParser(int)
	 */
	public boolean removeParser(Parser parser) {
		return parsers.remove(parser);
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void removeUpdate(DocumentEvent e) {
/*
		// Keep track of the first and last offset modified.  Some parsers are
		// smart and will only re-parse this section of the file.  Note that
		// for removals, only the line at the removal start needs to be
		// re-parsed.
		try {
			int offs = e.getOffset();
			if (firstOffsetModded==null || offs<firstOffsetModded.getOffset()) {
				firstOffsetModded = e.getDocument().createPosition(offs);
			}
			if (lastOffsetModded==null || offs>lastOffsetModded.getOffset()) {
				lastOffsetModded = e.getDocument().createPosition(offs);
			}
		} catch (BadLocationException ble) { // Neer happens
			ble.printStackTrace();
		}
*/
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
	 * Called when the user clicks a hyperlink in a {@link FocusableTip}.
	 * 
	 *
	 * @param e The event.
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (parserForTip!=null && parserForTip.getHyperlinkListener()!=null) {
			parserForTip.getHyperlinkListener().linkClicked(textArea, e);
		}
	}


}