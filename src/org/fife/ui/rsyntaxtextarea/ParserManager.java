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

import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.io.DocumentReader;


/**
 * Manages running a parser object for an <code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @version 0.1
 */
class ParserManager implements DocumentListener {

	private RSyntaxTextArea textArea;
	private Parser parser;
	private Timer timer;
	private TimerTask task;
	private boolean needsReparsing;

	private static final boolean DEBUG_PARSING	= true;

	private static final int TIMER_DELAY_MS		= 3000;


	/**
	 * Constructor.
	 *
	 * @param textArea The text area whose document the parser will be
	 *        parsing.
	 */
	public ParserManager(RSyntaxTextArea textArea) {
		this.textArea = textArea;
		ToolTipManager.sharedInstance().registerComponent(textArea);
		textArea.getDocument().addDocumentListener(this);
		// TODO: Name this timer in 1.5.
		timer = new Timer();//"ParserManager-timer");
	}


	/**
	 * Cancels the currently-running parsing task, if any.
	 */
	private void cancelTask() {
		if (task!=null) {
			task.cancel();
			task = null;
		}
	}


	/**
	 * Called when the document is modified.
	 *
	 * @param e The document event.
	 */
	public void changedUpdate(DocumentEvent e) {
		handleDocumentEvent(e);
	}


	/**
	 * Returns the parser.
	 *
	 * @return The parser.
	 * @see #setParser
	 */
	public Parser getParser() {
		return parser;
	}


	/**
	 * Returns the tooltip to display for a mouse event at the given
	 * location.  This method is overridden to give a registered parser a
	 * chance to display a tooltip (such as an error description when the
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
			for (Iterator i=parser.getNoticeIterator(); i.hasNext(); ) {
				ParserNotice notice = (ParserNotice)i.next();
				if (notice.containsPosition(pos)) {
					return notice.getMessage();
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
		if (!needsReparsing) {
			needsReparsing = true;
		}
		else {
			cancelTask();
			scheduleTask();
		}
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
	 * Schedules the parsing task for the appropraite time and delay.
	 */
	private void scheduleTask() {
		task = new ParseDocumentTask();
		timer.schedule(task, TIMER_DELAY_MS);
	}


	/**
	 * Sets the parser.
	 *
	 * @param parser The new parser.
	 * @see #getParser
	 */
	public void setParser(Parser parser) {

		// Clean up old timer, if necessary.
		cancelTask();

		// Set the new parser.
		this.parser = parser;

		// Set up timer, if necessary.
		if (parser!=null) {
			needsReparsing = true;
			scheduleTask();
		}

	}


	/**
	 * Terminates the timer used to schedule parsing.  This method is called
	 * by <code>RSyntaxTextArea</code> when it is being removed from a GUI
	 * (via <code>removeNotify()</code>) to "clean up."
	 */
	public void stopParsing() {
		textArea.getDocument().removeDocumentListener(this);
		ToolTipManager.sharedInstance().unregisterComponent(textArea);
		setParser(null);
	}


	private class ParseDocumentTask extends TimerTask {

		public void run() {

			if (!needsReparsing) {
				if (DEBUG_PARSING) {
					System.err.println("Parsing skipped; not needed");
				}
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
				needsReparsing = false;
				r.close();
			} finally {
				doc.readUnlock();
			}

			if (DEBUG_PARSING) {
				float time = (System.currentTimeMillis()-begin)/1000f;
				System.err.println("Parsing took: "+time+" seconds");
			}

			// Update the GUI on the EDT.
			SwingUtilities.invokeLater(new UpdateTextAreaRunnable());

		}

	}


	/**
	 * Notifies the <code>RSyntaxTextArea</code> that it needs to update
	 * its parser highlights.
	 */
	class UpdateTextAreaRunnable implements Runnable {

		public void run() {
			textArea.refreshParserNoticeHighlights(parser.getNoticeIterator());
		}

	}


}