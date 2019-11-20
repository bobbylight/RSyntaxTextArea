/*
 * 04/23/2009
 *
 * RSyntaxTextAreaHighlighter.java - Highlighter for RSyntaxTextAreas.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.fife.ui.rtextarea.RTextAreaHighlighter;
import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * The highlighter implementation used by {@link RSyntaxTextArea}s.  It knows to
 * always paint "marked occurrences" highlights below selection highlights,
 * and squiggle underline highlights above all other highlights.<p>
 *
 * Most of this code is copied from javax.swing.text.DefaultHighlighter;
 * unfortunately, we cannot re-use much of it since it is package private.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaHighlighter extends RTextAreaHighlighter {

	/**
	 * Marked occurrences in the document (to be painted separately from
	 * other highlights).
	 */
	private List<SyntaxLayeredHighlightInfoImpl> markedOccurrences;

	/**
	 * Highlights from document parsers.  These should be painted "on top of"
	 * all other highlights to ensure they are always above the selection.
	 */
	private List<SyntaxLayeredHighlightInfoImpl> parserHighlights;

	/**
	 * The default color used for parser notices when none is specified.
	 */
	private static final Color DEFAULT_PARSER_NOTICE_COLOR	= Color.RED;


	/**
	 * Constructor.
	 */
	public RSyntaxTextAreaHighlighter() {
		markedOccurrences = new ArrayList<>();
		parserHighlights = new ArrayList<>(0); // Often unused
	}


	/**
	 * Adds a special "marked occurrence" highlight.
	 *
	 * @param start
	 * @param end
	 * @param p
	 * @return A tag to reference the highlight later.
	 * @throws BadLocationException
	 * @see #clearMarkOccurrencesHighlights()
	 */
	Object addMarkedOccurrenceHighlight(int start, int end,
			SmartHighlightPainter p) throws BadLocationException {
		Document doc = textArea.getDocument();
		TextUI mapper = textArea.getUI();
		// Always layered highlights for marked occurrences.
		SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
		i.setPainter(p);
		i.setStartOffset(doc.createPosition(start));
		// HACK: Use "end-1" to prevent chars the user types at the "end" of
		// the highlight to be absorbed into the highlight (default Highlight
		// behavior).
		i.setEndOffset(doc.createPosition(end-1));
		markedOccurrences.add(i);
		mapper.damageRange(textArea, start, end);
		return i;
	}


	/**
	 * Adds a highlight from a parser.
	 *
	 * @param notice The notice from a {@link Parser}.
	 * @return A tag with which to reference the highlight.
	 * @throws BadLocationException
	 * @see #clearParserHighlights()
	 * @see #clearParserHighlights(Parser)
	 */
	HighlightInfo addParserHighlight(ParserNotice notice, HighlightPainter p)
								throws BadLocationException {

		Document doc = textArea.getDocument();
		TextUI mapper = textArea.getUI();

		int start = notice.getOffset();
		int end = 0;
		if (start==-1) { // Could just define an invalid line number
			int line = notice.getLine();
			Element root = doc.getDefaultRootElement();
			if (line>=0 && line<root.getElementCount()) {
				Element elem = root.getElement(line);
				start = elem.getStartOffset();
				end = elem.getEndOffset();
			}
		}
		else {
			end = start + notice.getLength();
		}

		// Always layered highlights for parser highlights.
		SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
		i.setPainter(p);
		i.setStartOffset(doc.createPosition(start));
		// HACK: Use "end-1" to prevent chars the user types at the "end" of
		// the highlight to be absorbed into the highlight (default Highlight
		// behavior).
		i.setEndOffset(doc.createPosition(end-1));
		i.notice = notice;//i.color = notice.getColor();

		parserHighlights.add(i);
		mapper.damageRange(textArea, start, end);
		return i;

	}


	/**
	 * Removes all "marked occurrences" highlights from the view.
	 *
	 * @see #addMarkedOccurrenceHighlight(int, int, SmartHighlightPainter)
	 */
	void clearMarkOccurrencesHighlights() {
		// Don't remove via an iterator; since our List is an ArrayList, this
		// implies tons of System.arrayCopy()s
		for (HighlightInfo info : markedOccurrences) {
			repaintListHighlight(info);
		}
		markedOccurrences.clear();
	}


	/**
	 * Removes all parser highlights.
	 *
	 * @see #addParserHighlight(ParserNotice, javax.swing.text.Highlighter.HighlightPainter)
	 */
	void clearParserHighlights() {
		// Don't remove via an iterator; since our List is an ArrayList, this
		// implies tons of System.arrayCopy()s
		for (SyntaxLayeredHighlightInfoImpl parserHighlight : parserHighlights) {
			repaintListHighlight(parserHighlight);
		}
		parserHighlights.clear();
	}


	/**
	 * Removes all of the highlights for a specific parser.
	 *
	 * @param parser The parser.
	 */
	public void clearParserHighlights(Parser parser) {

		Iterator<SyntaxLayeredHighlightInfoImpl> i = parserHighlights.iterator();
		while (i.hasNext()) {

			SyntaxLayeredHighlightInfoImpl info = i.next();

			if (info.notice.getParser()==parser) {
			    if (info.width > 0 && info.height > 0) {
			    	textArea.repaint(info.x, info.y, info.width, info.height);
			    }
				i.remove();
			}

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deinstall(JTextComponent c) {
		super.deinstall(c);
		markedOccurrences.clear();
		parserHighlights.clear();
	}


	/**
	 * Returns a list of "marked occurrences" in the text area.  If there are
	 * no marked occurrences, this will be an empty list.
	 *
	 * @return The list of marked occurrences, or an empty list if none.  The
	 *         contents of this list will be of type {@link DocumentRange}.
	 */
	public List<DocumentRange> getMarkedOccurrences() {
		List<DocumentRange> list = new ArrayList<>(markedOccurrences.size());
		for (HighlightInfo info : markedOccurrences) {
			int start = info.getStartOffset();
			int end = info.getEndOffset() + 1; // HACK
			if (start <= end) {
				// Occasionally a Marked Occurrence can have a lost end offset
				// but not start offset (replacing entire text content with
				// new content, and a marked occurrence is on the last token
				// in the document).
				DocumentRange range = new DocumentRange(start, end);
				list.add(range);
			}
		}
		return list;
	}


	/**
	 * Paints the "marked occurrences" highlights, then any other standard
	 * layered highlights (e.g. the text selection).
	 *
	 * @param g The graphics context.
	 * @param lineStart The starting offset of the line.
	 * @param lineEnd The end offset of the line.
	 * @param viewBounds The bounds of the view.
	 * @param editor The parent text component.
	 * @param view The view instance being rendered.
	 * @see #paintParserHighlights(Graphics, int, int, Shape, JTextComponent, View)
	 */
	@Override
	public void paintLayeredHighlights(Graphics g, int lineStart, int lineEnd,
						Shape viewBounds, JTextComponent editor, View view) {
		paintListLayered(g, lineStart,lineEnd, viewBounds, editor, view, markedOccurrences);
		super.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor, view);
	}


	/**
	 * Paints any highlights from {@code Parser}s.
	 *
	 * @param g The graphics context.
	 * @param lineStart The starting offset of the line.
	 * @param lineEnd The end offset of the line.
	 * @param viewBounds The bounds of the view.
	 * @param editor The parent text component.
	 * @param view The view instance being rendered.
	 * @see #paintLayeredHighlights(Graphics, int, int, Shape, JTextComponent, View)
	 */
	public void paintParserHighlights(Graphics g, int lineStart, int lineEnd,
									   Shape viewBounds, JTextComponent editor, View view) {
		paintListLayered(g, lineStart,lineEnd, viewBounds, editor, view, parserHighlights);
	}


	/**
	 * Removes a parser highlight from this view.
	 *
	 * @param tag The reference to the highlight.
	 * @see #addParserHighlight(ParserNotice, javax.swing.text.Highlighter.HighlightPainter)
	 */
	void removeParserHighlight(HighlightInfo tag) {
		repaintListHighlight(tag);
		parserHighlights.remove(tag);
	}


	/**
	 * Highlight info implementation used for parser notices and marked
	 * occurrences.
	 */
	private static class SyntaxLayeredHighlightInfoImpl extends
			LayeredHighlightInfoImpl {

		private ParserNotice notice;//Color color; // Used only by Parser highlights.

		@Override
		public Color getColor() {
			//return color;
			Color color = null;
			if (notice!=null) {
				color = notice.getColor();
				if (color==null) {
					color = DEFAULT_PARSER_NOTICE_COLOR;
				}
			}
			return color;
		}

		@Override
		public String toString() {
			return "[SyntaxLayeredHighlightInfoImpl: " +
					"startOffs=" + getStartOffset() +
					", endOffs=" + getEndOffset() +
					", color=" + getColor() +
					"]";
		}

	}


}
