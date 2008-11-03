/*
 * 01/27/2004
 *
 * RSyntaxTextArea.java - An extension of RTextArea that adds
 * the ability to syntax highlight certain programming languages.
 * Copyright (C) 2004 Robert Futrell
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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextAreaUI;


/**
 * An extension of <code>RTextArea</code> that adds syntax highlighting
 * of certain programming languages to its list of features.  Languages
 * currently supported include:
 * <ul>
 *    <li>Assembler (X86)
 *    <li>C
 *    <li>C++
 *    <li>CSS
 *    <li>C#
 *    <li>Fortran
 *    <li>Groovy
 *    <li>HTML
 *    <li>Java
 *    <li>JavaScript
 *    <li>JSP
 *    <li>Lua
 *    <li>Make
 *    <li>Perl
 *    <li>Ruby
 *    <li>SAS
 *    <li>SQL
 *    <li>Tcl
 *    <li>UNIX shell scripts
 *    <li>Windows batch
 *    <li>XML files
 * </ul>
 *
 * Other added features include:
 * <ul>
 *    <li>Bracket matching
 *    <li>Auto-indentation
 *    <li>Copy as RTF
 *    <li>Clickable hyperlinks (if the language parser being used supports it)
 * </ul>
 *
 * It is recommended that you use an instance of
 * {@link org.fife.ui.rtextarea.RTextScrollPane} instead of a regular
 * <code>JScrollPane</code> as this class allows you to add line numbers easily
 * to your text area.
 *
 * @author Robert Futrell
 * @version 0.3
 */
public class RSyntaxTextArea extends RTextArea implements SyntaxConstants {

	public static final String ANTIALIAS_PROPERTY			= "RSTA.antiAlias";
	public static final String AUTO_INDENT_PROPERTY			= "RSTA.autoIndent";
	public static final String BRACKET_MATCHING_PROPERTY		= "RSTA.bracketMatching";
	public static final String CLEAR_WHITESPACE_LINES_PROPERTY	= "RSTA.clearWhitespaceLines";
	public static final String FRACTIONAL_FONTMETRICS_PROPERTY	= "RSTA.fractionalFontMetrics";
	public static final String HYPERLINKS_ENABLED_PROPERTY		= "RSTA.hyperlinksEnabled";
	public static final String SYNTAX_SCHEME_PROPERTY			= "RSTA.syntaxScheme";
	public static final String SYNTAX_STYLE_PROPERTY			= "RSTA.syntaxStyle";
	public static final String VISIBLE_WHITESPACE_PROPERTY		= "RSTA.visibleWhitespace";

	private static final Color DEFAULT_BRACKET_MATCH_BG_COLOR		= new Color(234,234,255);
	private static final Color DEFAULT_BRACKET_MATCH_BORDER_COLOR	= new Color(0,0,128);


	/**
	 * The syntax style to be highlighting.
	 */
	private int syntaxStyle;

	/**
	 * The colors used for syntax highlighting.
	 */
	private SyntaxHighlightingColorScheme colorScheme;

	/**
	 * Handles code templates.
	 */
	private static CodeTemplateManager codeTemplateManager;

	/**
	 * Whether or not templates are enabled.
	 */
	private static boolean templatesEnabled;

	/**
	 * The rectangle surrounding the "matched bracket" if bracket matching
	 * is enabled.
	 */
Rectangle match;

	/**
	 * Colors used for the "matched bracket" if bracket matching is
	 * enabled.
	 */
	private Color matchedBracketBGColor;
	private Color matchedBracketBorderColor;

	/**
	 * Whether or not bracket matching is enabled.
	 */
	private boolean bracketMatchingEnabled;

	/**
	 * Whether or not auto-indent is on.
	 */
	private boolean autoIndentEnabled;

	/**
	 * Whether or not lines with nothing but whitespace are "made empty."
	 */
	private boolean clearWhitespaceLines;

	/**
	 * Whether we are displaying visible whitespace (spaces and tabs).
	 */
	private boolean whitespaceVisible;

	/**
	 * Whether hyperlinks are enabled (must be supported by the syntax
	 * scheme being used).
	 */
	private boolean hyperlinksEnabled;

	/**
	 * The color to use when painting hyperlinks.
	 */
	private Color hyperlinkFG;

	/**
	 * Mask used to determine if the correct key is being held down to scan
	 * for hyperlinks (ctrl, meta, etc.).
	 */
	private int linkScanningMask;

	/**
	 * Used during "Copy as RTF" operations.
	 */
	private RtfGenerator rtfGenerator;

	/**
	 * Metrics of the text area's font.
	 */
	private FontMetrics defaultFontMetrics;

	/**
	 * Manages running the parser.
	 */
	private ParserManager parserManager;

	/**
	 * List of highlights (of errors, warnings, etc.) from the parser.
	 */
	private List parserNoticeHighlights;

	/**
	 * Whether the editor is currently scanning for hyperlinks on mouse
	 * movement.
	 */
	private boolean isScanningForLinks;

	private int hoveredOverLinkOffset;

	/**
	 * Painter used to underline errors.
	 */
	private SquiggleUnderlineHighlightPainter parserErrorHighlightPainter =
						new SquiggleUnderlineHighlightPainter(Color.RED);


private int lineHeight;		// Height of a line of text; same for default, bold & italic.
private int maxAscent;

public int getMaxAscent() {
	return maxAscent;
}
private void refreshFontMetrics(Graphics2D g2d) {
	// It is assumed that any rendering hints are already applied to g2d.
	defaultFontMetrics = g2d.getFontMetrics(getFont());
	for (int i=0; i<colorScheme.syntaxSchemes.length; i++) {
		SyntaxScheme ss = colorScheme.syntaxSchemes[i];
		if (ss!=null) {
			ss.fontMetrics = ss.font==null ? null : g2d.getFontMetrics(ss.font);
		}
	}
	if (getLineWrap()==false) {
		// HORRIBLE HACK!  The un-wrapped view needs to refresh its cached
		// longest line information.
		((SyntaxView)getUI().getRootView(this).getView(0)).calculateLongestLine();
	}
}
private String aaHintFieldName;
private Object aaHint;
private boolean fractionalFontMetricsEnabled;


	/**
	 * Creates a new <code>RSyntaxTextArea</code> with the following
	 * properties: no word wrap, INSERT_MODE, Plain white background, tab size
	 * of 5 spaces, no syntax highlighting, and default caret color,
	 * selection color, and syntax highlighting scmeme.
	 */
	public RSyntaxTextArea() {
		this(false, INSERT_MODE);
	}


	/**
	 * Creates a new <code>RSyntaxTextArea</code>.
	 *
	 * @param wordWrapEnabled Whether to use word wrap in this text area.
	 * @param textMode Either <code>INSERT_MODE</code> or
	 *        <code>OVERWRITE_MODE</code>.
	 */
	public RSyntaxTextArea(boolean wordWrapEnabled, int textMode) {

		// NOTE:  The font passed to the super constructor below isn't
		// actually used, so we can pass whatever we want (other than null,
		// which may cause NPE's in other methods).
		super(new Font("Monospaced", Font.PLAIN, 13),
					wordWrapEnabled, textMode);

		// Set some RSyntaxTextArea default values.
		syntaxStyle = RSyntaxTextArea.NO_SYNTAX_STYLE;
		setMatchedBracketBGColor(getDefaultBracketMatchBGColor());
		setMatchedBracketBorderColor(getDefaultBracketMatchBorderColor());
		setBracketMatchingEnabled(true);

		// Set auto-indent releated stuff.
		setAutoIndentEnabled(true);
		setClearWhitespaceLinesEnabled(true);

		setHyperlinksEnabled(true);
		setLinkScanningMask(InputEvent.CTRL_DOWN_MASK);
		setHyperlinkForeground(Color.BLUE);
		isScanningForLinks = false;

	}


	/**
	 * If code templates are enabled, registers the specified template.
	 *
	 * @param id The identifier for the template.
	 * @param beforeCaret The text to insert before the caret.
	 * @param afterCaret The text to insert after the caret.
	 */
	public static void addCodeTemplate(String id, String beforeCaret,
								String afterCaret) {
		if (getTemplatesEnabled()) {
			CodeTemplate template = new CodeTemplate(id, beforeCaret,
											afterCaret);
			getCodeTemplateManager().addTemplate(template);
		}
	}


	/**
	 * Adds a hyperlink listener to this text area.
	 *
	 * @param l The listener to add.
	 * @see #removeHyperlinkListener(HyperlinkListener)
	 */
	public void addHyperlinkListener(HyperlinkListener l) {
		listenerList.add(HyperlinkListener.class, l);
	}


	/**
	 * Updates the fontmetrics the first time we're displayed.
	 */
	public void addNotify() {
		super.addNotify();
		// We know we've just been connected to a screen resource (by
		// definition), so initialize our font metrics objects.
		refreshFontMetrics(getGraphics2D(getGraphics()));
	}


	/**
	 * Recalculates the height of a line in this text area and the
	 * maximum ascent of all fonts displayed.
	 */
	private void calculateLineHeight() {

		lineHeight = maxAscent = 0;

		// Each tokeh style.
		for (int i=0; i<colorScheme.syntaxSchemes.length; i++) {
			SyntaxScheme ss = colorScheme.syntaxSchemes[i];
			if (ss!=null && ss.font!=null) {
				FontMetrics fm = getFontMetrics(ss.font);
				int height = fm.getHeight();
				if (height>lineHeight)
					lineHeight = height;
				int ascent = fm.getMaxAscent();
				if (ascent>maxAscent)
					maxAscent = ascent;
			}
		}

		// The text area's (default) font).
		Font temp = getFont();
		FontMetrics fm = getFontMetrics(temp);
		int height = fm.getHeight();
		if (height>lineHeight) {
			lineHeight = height;
		}
		int ascent = fm.getMaxAscent();
		if (ascent>maxAscent) {
			maxAscent = ascent;
		}

	}


	/**
	 * Removes all highlights in the document from the parser.
	 */
	protected void clearParserNoticeHighlights() {
		Highlighter h = getHighlighter();
		if (h!=null && parserNoticeHighlights!=null) {
			int count = parserNoticeHighlights.size();
			for (int i=0; i<count; i++)
				h.removeHighlight(parserNoticeHighlights.get(i));
			parserNoticeHighlights.clear();
		}
		repaint();
	}


	/**
	 * Clones a token list.  This is necessary as tokens are reused in
	 * <code>RSyntaxDocument</code> so we can't simply use the ones we
	 * are handed from it.
	 *
	 * @param t The token list to clone.
	 * @return The clone of the token list.
	 */
	private Token cloneTokenList(Token t) {

		if (t==null) {
			return null;
		}

		Token clone = new DefaultToken();
		clone.copyFrom(t);
		Token cloneEnd = clone;

		while ((t=t.getNextToken())!=null) {
			Token temp = new DefaultToken();
			temp.copyFrom(t);
			cloneEnd.setNextToken(temp);
			cloneEnd = temp;
		}

		return clone;

	}


	/**
	 * Copies the currently selected text to the system clipboard, with
	 * any necessary style information (font, foreground color and background
	 * color).  Does nothing for <code>null</code> selections.
	 */
	public void copyAsRtf() {

		int selStart = getSelectionStart();
		int selEnd = getSelectionEnd();
		if (selStart==selEnd) {
			return;
		}

		// Make sure there is a system clipboard, and that we can write
		// to it.
		SecurityManager sm = System.getSecurityManager();
		if (sm!=null) {
			try {
				sm.checkSystemClipboardAccess();
			} catch (SecurityException se) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
				return;
			}
		}
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

		// Create the RTF selection.
		RtfGenerator gen = getRTFGenerator();
		Token tokenList = getTokenListFor(selStart, selEnd);
		for (Token t=tokenList; t!=null; t=t.getNextToken()) {
			if (t.isPaintable()) {
				if (t.textCount==1 && t.text[t.textOffset]=='\n') {
					gen.appendNewline();
				}
				else {
					Font font = getFontForTokenType(t.type);
					Color bg = getBackgroundForTokenType(t.type);
					boolean underline = getUnderlineForToken(t);
					// Small optimization - don't print fg color if this
					// is a whitespace color.  Saves on RTF size.
					if (t.isWhitespace()) {
						gen.appendToDocNoFG(t.getLexeme(), font, bg, underline);
					}
					else {
						Color fg = getForegroundForToken(t);
						gen.appendToDoc(t.getLexeme(), font, fg, bg, underline);
					}
				}
			}
		}

		// Set the system clipboard contents to the RTF selection.
		RtfTransferable contents = new RtfTransferable(gen.getRtf().getBytes());
		//System.out.println("*** " + new String(gen.getRtf().getBytes()));
		try {
			cb.setContents(contents, null);
		} catch (IllegalStateException ise) {
			UIManager.getLookAndFeel().provideErrorFeedback(null);
			return;
		}

	}


	/**
	 * Returns the document to use for an <code>RSyntaxTextArea</code>
	 *
	 * @return The document.
	 */
	protected Document createDefaultModel() {
		return new RSyntaxDocument(NO_SYNTAX_STYLE);
	}


	/**
	 * Returns the caret event/mouse listener for <code>RTextArea</code>s.
	 *
	 * @return The caret event/mouse listener.
	 */
	protected RTAMouseListener createMouseListener() {
		return new RSyntaxTextAreaMutableCaretEvent(this);
	}


	/**
	 * Returns the a real UI to install on this text area.
	 *
	 * @return The UI.
	 */
	protected RTextAreaUI createRTextAreaUI() {
		return new RSyntaxTextAreaUI(this);
	}


	/**
	 * If the caret is on a bracket, this method finds the matching bracket,
	 * and if it exists, highlights it.
	 */
	protected final void doBracketMatching() {

		// We always need to repaint the "matched bracket" highlight if it
		// exists.
		if (match!=null)
			repaint(match);

		// If a matching bracket is found, get its bounds and paint it!
		int pos = RSyntaxUtilities.getMatchingBracketPosition(this);
		if (pos>-1) {
			try {
				match = modelToView(pos);
				repaint(match);
			} catch (BadLocationException ble) {
				// Shouldn't happen.
				ble.printStackTrace();
			}
		}
		else {
			// Set match to null so the old value isn't still repainted.
			match = null;
		}

	}


	/**
	 * Notifies all listeners that a caret change has occured.
	 *
	 * @param e The caret event.
	 */
	protected void fireCaretUpdate(CaretEvent e) {
		super.fireCaretUpdate(e);
		if (bracketMatchingEnabled)
			doBracketMatching();
	}


	/**
	 * Notifies all listeners that have registered interest for notification
	 * on this event type.  The listener list is processed last to first.
	 *
	 * @param e The event to fire.
	 * @see EventListenerList
	 */
	public void fireHyperlinkUpdate(HyperlinkEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==HyperlinkListener.class) {
				((HyperlinkListener)listeners[i+1]).hyperlinkUpdate(e);
			}          
		}
	}


	/**
	 * Returns the background color for tokens of the specified type.
	 *
	 * @param type The type of token.
	 * @return The background color to use for that token type.  If
	 *         this value is <code>null</code> then this token type
	 *         has no special background color.
	 * @see #getForegroundForToken(Token)
	 */
	public Color getBackgroundForTokenType(int type) {
		// NOTE: Defaulting to this.getBackground() if syntax
		// scheme's background is null causes VERY slow painting
		// on OSX... it appears to repaint the entire text area
		// with every redraw???
		return colorScheme.syntaxSchemes[type].background;
	}


	/**
	 * Returns the number of code templates registered, or
	 * <code>-1</code> if code templates are not enabled.
	 *
	 * @return The number of code templates registered.
	 */
	public static int getCodeTemplateCount() {
		return getTemplatesEnabled() ?
			codeTemplateManager.getTemplateCount() : -1;
	}


	/**
	 * Returns the code template manager for all instances of
	 * <code>RSyntaxTextArea</code>.  Note that if templates
	 * are not enabled, this will return <code>null</code>.
	 *
	 * @return The code template manager.
	 * @see #enableTemplates
	 */
	static CodeTemplateManager getCodeTemplateManager() {
		return codeTemplateManager;
	}


	/**
	 * Returns the default bracket-match background color.
	 *
	 * @return The color.
	 * @see #getDefaultBracketMatchBorderColor
	 */
	public static final Color getDefaultBracketMatchBGColor() {
		return DEFAULT_BRACKET_MATCH_BG_COLOR;
	}


	/**
	 * Returns the default bracket-match border color.
	 *
	 * @return The color.
	 * @see #getDefaultBracketMatchBGColor
	 */
	public static final Color getDefaultBracketMatchBorderColor() {
		return DEFAULT_BRACKET_MATCH_BORDER_COLOR;
	}


	/**
	 * Returns the "default" syntax highlighting color scheme.  The colors
	 * used are somewhat standard among syntax highlighting text editors.
	 *
	 * @return The default syntax highlighting color scheem.
	 */
	public SyntaxHighlightingColorScheme getDefaultSyntaxHighlightingColorScheme() {
		return new SyntaxHighlightingColorScheme(true);
	}


	/**
	 * Returns the font for tokens of the specified type.
	 *
	 * @param type The type of token.
	 * @return The font to use for that token type.
	 * @see #getFontMetricsForTokenType(int)
	 */
	public Font getFontForTokenType(int type) {
		Font f = colorScheme.syntaxSchemes[type].font;
		return f!=null ? f : getFont();
	}


	/**
	 * Returns the font metrics for tokens of the specified type.
	 *
	 * @param type The type of token.
	 * @return The font metrics to use for that token type.
	 * @see #getFontForTokenType(int)
	 */
	public FontMetrics getFontMetricsForTokenType(int type) {
		FontMetrics fm = colorScheme.syntaxSchemes[type].fontMetrics;
		return fm!=null ? fm : defaultFontMetrics;
	}


	/**
	 * Returns the foreground color to use when painting a token.
	 *
	 * @param t The token.
	 * @return The foreground color to use for that token.  This
	 *         value is never <code>null</code>.
	 * @see #getBackgroundForTokenType(int)
	 */
	public Color getForegroundForToken(Token t) {
		if (getHyperlinksEnabled() && t.isHyperlink() &&
				hoveredOverLinkOffset==t.offset) {
			return hyperlinkFG;
		}
		Color fg = colorScheme.syntaxSchemes[t.type].foreground;
		return fg!=null ? fg : getForeground();
	}


	/**
	 * Returns whether fractional fontmetrics are enabled for this text area.
	 *
	 * @return Whether fractional fontmetrics are enabled.
	 * @see #setFractionalFontMetricsEnabled
	 * @see #getTextAntiAliasHint
	 */
	public boolean getFractionalFontMetricsEnabled() {
		return fractionalFontMetricsEnabled;
	}


	/**
	 * Returns a <code>Graphics2D</code> version of the specified graphics
	 * that has been initialized with the proper rendering hints.
	 *
	 * @param g The graphics context for which to get a
	 *        <code>Graphics2D</code>.
	 * @return The <code>Graphics2D</code>.
	 */
	private final Graphics2D getGraphics2D(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (aaHint!=null) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							aaHint);
		}
		if (fractionalFontMetricsEnabled) {
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
							RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		return g2d;
	}


	/**
	 * Returns the color to use when painting hyperlinks.
	 *
	 * @return The color to use when painting hyperlinks.
	 * @see #setHyperlinkForeground(Color)
	 * @see #getHyperlinksEnabled()
	 */
	public Color getHyperlinkForeground() {
		return hyperlinkFG;
	}


	/**
	 * Returns whether hyperlinks are enabled for this text area.
	 *
	 * @return Whether hyperlinks are enabled for this text area.
	 * @see #setHyperlinksEnabled(boolean)
	 */
	public boolean getHyperlinksEnabled() {
		return hyperlinksEnabled;
	}


	/**
	 * Returns the height to use for a line of text in this text area.
	 *
	 * @return The height of a line of text in this text area.
	 */
	public int getLineHeight() {
		//System.err.println("... getLineHeight() returning " + lineHeight);
		return lineHeight;
	}


	/**
	 * Gets the color used as the background for a matched bracket.
	 *
	 * @return The color used.
	 * @see #setMatchedBracketBGColor
	 * @see #getMatchedBracketBorderColor
	 */
	public final Color getMatchedBracketBGColor() {
		return matchedBracketBGColor;
	}


	/**
	 * Gets the color used as the border for a matched bracket.
	 *
	 * @return The color used.
	 * @see #setMatchedBracketBorderColor
	 * @see #getMatchedBracketBGColor
	 */
	public final Color getMatchedBracketBorderColor() {
		return matchedBracketBorderColor;
	}


	/**
	 * Returns the matched bracket's rectangle, or <code>null</code> if there
	 * is currently no matched bracket.  Note that this shouldn't ever be
	 * called by the user.
	 *
	 * @return The rectangle surrounding the matched bracket.
	 */
	public final Rectangle getMatchRectangle() {
		return match;
	}


	/**
	 * Returns the RTF generator for this text area, lazily creating it
	 * if necessary.
	 *
	 * @return The RTF generator.
	 */
	private RtfGenerator getRTFGenerator() {
		if (rtfGenerator==null) {
			rtfGenerator = new RtfGenerator();
		}
		else {
			rtfGenerator.reset();
		}
		return rtfGenerator;
	}


	/**
	 * Returns what type of syntax highlighting this editor is doing.
	 *
	 * @return The style being used, such as
	 *         <code>RSyntaxTextArea.JAVA_SYNTAX_STYLE</code>.
	 * @see #setSyntaxEditingStyle
	 */
	public int getSyntaxEditingStyle() {
		return syntaxStyle;
	}


	/**
	 * Returns all of the colors currently being used in syntax highlighting
	 * by this text component.
	 *
	 * @return An instance of <code>SyntaxHighlightingColorScheme</code> that
	 *         represents the colors currently being used for syntax
	 *         highlighting.
	 * @see #setSyntaxHighlightingColorScheme
	 */
	public SyntaxHighlightingColorScheme getSyntaxHighlightingColorScheme() {
		return colorScheme;
	}


	/**
	 * Returns whether or not templates are enabled for all instances
	 * of <code>RSyntaxTextArea</code>.
	 *
	 * @return Whether templates are enabled.
	 * @see #saveTemplates
	 * @see #setTemplateDirectory
	 * @see #setTemplatesEnabled
	 */
	public synchronized static boolean getTemplatesEnabled() {
		return templatesEnabled;
	}


	/**
	 * Returns the rendering hint used when antialiasing text in this
	 * editor.
	 *
	 * @return The name of a field in <code>java.awt.RenderingHints</code>,
	 *         or <code>null</code> if no text antialiasing is being done.
	 * @see #setTextAntiAliasHint(String)
	 * @see #getFractionalFontMetricsEnabled()
	 */
	public String getTextAntiAliasHint() {
		return aaHintFieldName;
	}


	/**
	 * Returns a token list for the given range in the document.
	 *
	 * @param startOffs The starting offset in the document.
	 * @param endOffs The end offset in the document.
	 * @return The first token in the token list.
	 */
	private Token getTokenListFor(int startOffs, int endOffs) {

		Token tokenList = null;
		Token lastToken = null;

		Element map = getDocument().getDefaultRootElement();
		int startLine = map.getElementIndex(startOffs);
		int endLine = map.getElementIndex(endOffs);

		for (int line=startLine; line<=endLine; line++) {
			Token t = getTokenListForLine(line);
			t = cloneTokenList(t);
			if (tokenList==null) {
				tokenList = t;
				lastToken = tokenList;
				while (lastToken.getNextToken()!=null &&
						lastToken.getNextToken().isPaintable()) {
					lastToken = lastToken.getNextToken();
				}
			}
			else {
				lastToken.setNextToken(t);
				while (lastToken.getNextToken()!=null &&
						lastToken.getNextToken().isPaintable()) {
					lastToken = lastToken.getNextToken();
				}
			}
			if (line<endLine) {
				// Document offset MUST be correct to prevent exceptions
				// in getTokenListFor()
				int docOffs = map.getElement(line).getEndOffset()-1;
				t = new DefaultToken(new char[] { '\n' }, 0,0, docOffs,
								Token.WHITESPACE);
				lastToken.setNextToken(t);
				lastToken = t;
			}
		}

		// Trim the beginning and end of the token list so that it starts
		// at startOffs and ends at endOffs.

		// Be careful and check that startOffs is actually in the list.
		// startOffs can be < the token list's start if the end "newline"
		// character of a line is the first character selected (the token
		// list returned for that line will be null, so the first token in
		// the final token list will be from the next line and have a
		// starting offset > startOffs?).
		if (startOffs>=tokenList.offset) {
			while (!tokenList.containsPosition(startOffs)) {
				tokenList = tokenList.getNextToken();
			}
			tokenList.makeStartAt(startOffs);
		}

		Token temp = tokenList;
		// Be careful to check temp for null here.  It is possible that no
		// token contains endOffs, if endOffs is at the end of a line.
		while (temp!=null && !temp.containsPosition(endOffs)) {
			temp = temp.getNextToken();
		}
		if (temp!=null) {
			temp.textCount = endOffs - temp.offset;
			temp.setNextToken(null);
		}

		return tokenList;

	}


	/**
	 * Returns a list of tokens representing the given line.
	 *
	 * @param line The line number to get tokens for.
	 * @return A linked list of tokens representing the line's text.
	 */
	public Token getTokenListForLine(int line) {
		return ((RSyntaxDocument)getDocument()).getTokenListForLine(line);
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
		String toolTip = parserManager==null ? null :
						parserManager.getToolTipText(e);
		return toolTip!=null ? toolTip : super.getToolTipText(e);
	}


	/**
	 * Returns whether the specified token should be underlined.
	 * A token is underlined if its syntax style includes underlining,
	 * or if it is a hyperlink ahd hyperlinks are enabled.
	 *
	 * @param t The token.
	 * @return Whether the specified token should be underlined.
	 */
	public boolean getUnderlineForToken(Token t) {
		return (t.isHyperlink() && getHyperlinksEnabled()) ||
				colorScheme.syntaxSchemes[t.type].underline;
	}


	/**
	 * Returns whether or not auto-indent is enabled.
	 *
	 * @return Whether or not auto-indent is enabled.
	 * @see #setAutoIndentEnabled
	 */
	public boolean isAutoIndentEnabled() {
		return autoIndentEnabled;
	}


	/**
	 * Returns whether or not bracket matching is enabled.
	 *
	 * @return <code>true</code> iff bracket matching is enabled.
	 * @see #setBracketMatchingEnabled
	 */
	public final boolean isBracketMatchingEnabled() {
		return bracketMatchingEnabled;
	}


	/**
	 * Returns whether or not lines containing nothing but whitespace are made
	 * into blank lines when Enter is pressed in them.
	 *
	 * @return Whether or not whitespace-only lines are cleared when
	 *         the user presses Enter on them.
	 * @see #setClearWhitespaceLinesEnabled
	 */
	public boolean isClearWhitespaceLinesEnabled() {
		return clearWhitespaceLines;
	}


	/**
	 * Returns whether whitespace (spaces and tabs) is visible.
	 *
	 * @return Whether whitespace is visible.
	 * @see #setWhitespaceVisible
	 */
	public boolean isWhitespaceVisible() {
		return whitespaceVisible;
	}


	/**
	 * Returns the token at the specified position in the model.
	 *
	 * @param offs The position in the model.
	 * @return The token, or <code>null</code> if no token is at that
	 *         position.
	 * @see #viewToToken(Point)
	 */
	private Token modelToToken(int offs) {
		if (offs>=0) {
			try {
				int line = getLineOfOffset(offs);
				Token t = getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {
					if (t.containsPosition(offs)) {
						return t;
					}
					t = t.getNextToken();
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Never happens
			}
		}
		return null;
	}


	/**
	 * The <code>paintComponent</code> method is overridden so we
	 * apply any necessary rendering hints to the Graphics object.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(getGraphics2D(g));
	}


	/**
	 * Refreshes the highlights in the text area from the parser.
	 *
	 * @param parserNoticeIterator An iterator over new parser notices.
	 * @see #clearParserNoticeHighlights
	 */
	void refreshParserNoticeHighlights(Iterator parserNoticeIterator) {
		clearParserNoticeHighlights();
		if (parserNoticeHighlights==null)
			parserNoticeHighlights = new ArrayList();
		Highlighter h = getHighlighter();
		while (parserNoticeIterator.hasNext()) {
			ParserNotice notice = (ParserNotice)parserNoticeIterator.next();
			int start = notice.getOffset();
			int length = notice.getLength();
			try {
				parserNoticeHighlights.add(h.addHighlight(start,start+length,
									parserErrorHighlightPainter));
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
	}


	/**
	 * Removes a hyperlink listener from this text area.
	 *
	 * @param l The listener to remove.
	 * @see #addHyperlinkListener(HyperlinkListener)
	 */
	public void removeHyperlinkListener(HyperlinkListener l) {
		listenerList.remove(HyperlinkListener.class, l);
	}


	/**
	 * Overridden so we stop this text area's parser, if any.
	 */
	public void removeNotify() {
		if (parserManager!=null) {
			parserManager.stopParsing();
		}
		super.removeNotify();
	}


	/**
	 * Sets the colors used for syntax highlighting to their defaults.
	 */
	public void restoreDefaultSyntaxHighlightingColorScheme() {
		setSyntaxHighlightingColorScheme(getDefaultSyntaxHighlightingColorScheme());
	}


	/**
	 * Attempts to save all currently-known templates to the current template
	 * directory, as set by <code>setTemplateDirectory</code>.  Templates
	 * will be saved as XML files with names equal to their abbreviations; for
	 * example, a template that expands on the word "forb" will be saved as
	 * <code>forb.xml</code>.
	 *
	 * @return Whether or not the save was successful.  The save will
	 *         be unsuccessful if the template directory does not exist or
	 *         if it has not been set (i.e., you have not yet called
	 *         <code>setTemplateDirectory</code>).
	 * @see #getTemplatesEnabled
	 * @see #setTemplateDirectory
	 * @see #setTemplatesEnabled
	 */
	public synchronized static boolean saveTemplates() {
		if (!getTemplatesEnabled() || codeTemplateManager==null)
			return false;
		return codeTemplateManager.saveTemplates();
	}


	/**
	 * Sets whether or not auto-indent is enabled.  This fires a property
	 * change event of type <code>AUTO_INDENT_PROPERTY</code>.
	 *
	 * @param enabled Whether or not auto-indent is enabled.
	 * @see #isAutoIndentEnabled
	 */
	public void setAutoIndentEnabled(boolean enabled) {
		if (autoIndentEnabled!=enabled) {
			autoIndentEnabled = enabled;
			firePropertyChange(AUTO_INDENT_PROPERTY, !enabled, enabled);
		}
	}


	/**
	 * Sets whether bracket matching is enabled.  This fires a property change
	 * event of type <code>BRACKET_MATCHING_PROPERTY</code>.
	 *
	 * @param enabled Whether or not bracket matching should be enabled.
	 * @see #isBracketMatchingEnabled
	 */
	public void setBracketMatchingEnabled(boolean enabled) {
		if (enabled!=bracketMatchingEnabled) {
			bracketMatchingEnabled = enabled;
			repaint();
			firePropertyChange(BRACKET_MATCHING_PROPERTY, !enabled, enabled);
		}
	}


	/**
	 * Sets whether or not lines containing nothing but whitespace are made
	 * into blank lines when Enter is pressed in them.  This method fires
	 * a property change event of type
	 * <code>CLEAR_WHITESPACE_LINES_PROPERTY</code>.
	 *
	 * @param enabled Whether or not whitespace-only lines are cleared when
	 *        the user presses Enter on them.
	 * @see #isClearWhitespaceLinesEnabled
	 */
	public void setClearWhitespaceLinesEnabled(boolean enabled) {
		if (enabled!=clearWhitespaceLines) {
			clearWhitespaceLines = enabled;
			firePropertyChange(CLEAR_WHITESPACE_LINES_PROPERTY,
							!enabled, enabled);
		}
	}


	/**
	 * Sets the document used by this text area.  This is overridden so that
	 * only instances of {@link RSyntaxDocument} are accepted; for all
	 * others, an exception will be thrown.
	 *
	 * @param document The new document for this text area.
	 * @throws IllegalArgumentException If the document is not an
	 *         <code>RSyntaxDocument</code>.
	 */
	public void setDocument(Document document) {
		if (!(document instanceof RSyntaxDocument))
			throw new IllegalArgumentException("Documents for " +
					"RSyntaxTextArea must be instances of " +
					"RSyntaxDocument!");
		super.setDocument(document);
	}


	/**
	 * Sets the font used by this text area.  Note that this method does not
	 * alter the appearance of an <code>RSyntaxTextArea</code> since it uses
	 * different fonts for each token type.
	 *
	 * @param font The font.
	 */
	public void setFont(Font font) {
		Font old = super.getFont();
		super.setFont(font); // Do this first.
		// We must be connected to a screen resource for our
		// graphics to be non-null.
		if (isDisplayable()) {
			refreshFontMetrics(getGraphics2D(getGraphics()));
			calculateLineHeight();
			// Updates the margin line.
			updateMarginLineX();
			// Force the current line highlight to be repainted, even
			// though the caret's location hasn't changed.
			forceCurrentLineHighlightRepaint();
			// Get line number border in text area to repaint again
			// since line heights have updated.
			firePropertyChange("font", old, font);
			// So parent JScrollPane will have its scrollbars updated.
			revalidate();
		}
	}


	/**
	 * Sets whether fractional fontmetrics are enabled.  This method fires
	 * a property change event of type {@link #FRACTIONAL_FONTMETRICS_PROPERTY}.
	 *
	 * @param enabled Whether fractional fontmetrics are enabled.
	 * @see #getFractionalFontMetricsEnabled()
	 */
	public void setFractionalFontMetricsEnabled(boolean enabled) {
		if (fractionalFontMetricsEnabled!=enabled) {
			fractionalFontMetricsEnabled = enabled;
			// We must be connected to a screen resource for our graphics to be
			// non-null.
			if (isDisplayable()) {
				refreshFontMetrics(getGraphics2D(getGraphics()));
			}
			firePropertyChange(FRACTIONAL_FONTMETRICS_PROPERTY,
											!enabled, enabled);
		}
	}


	/**
	 * Sets the color to use when painting hyperlinks.
	 *
	 * @param fg The color to use when painting hyperlinks.
	 * @throws NullPointerException If <code>fg</code> is <code>null</code>.
	 * @see #getHyperlinkForeground()
	 * @see #setHyperlinksEnabled(boolean)
	 */
	public void setHyperlinkForeground(Color fg) {
		if (fg==null) {
			throw new NullPointerException("fg cannot be null");
		}
		hyperlinkFG = fg;
	}


	/**
	 * Sets whether hyperlinks are enabled for this text area.  This method
	 * fires a property change event of type
	 * {@link #HYPERLINKS_ENABLED_PROPERTY}.
	 *
	 * @param enabled Whether hyperlinks are enabled.
	 * @see #getHyperlinksEnabled()
	 */
	public void setHyperlinksEnabled(boolean enabled) {
		if (this.hyperlinksEnabled!=enabled) {
			this.hyperlinksEnabled = enabled;
			repaint();
			firePropertyChange(HYPERLINKS_ENABLED_PROPERTY, !enabled, enabled);
		}
	}


	/**
	 * Sets the mask for the key used to toggle whether we are scanning for
	 * hyperlinks with mouse hovering.
	 *
	 * @param mask The mask to use.  This should be a value such as
	 *        {@link InputEvent#CTRL_DOWN_MASK} or
	 *        {@link InputEvent#META_DOWN_MASK}.
	 *        For invalid values, behavior is undefined.
	 * @see InputEvent
	 */
	public void setLinkScanningMask(int mask) {
		if (mask==InputEvent.CTRL_DOWN_MASK ||
				mask==InputEvent.META_DOWN_MASK ||
				mask==InputEvent.ALT_DOWN_MASK ||
				mask==InputEvent.SHIFT_DOWN_MASK) {
			linkScanningMask = mask;
		}
	}


	/**
	 * Sets the color used as the background for a matched bracket.
	 *
	 * @param color The color to use.
	 * @see #getMatchedBracketBGColor
	 * @see #setMatchedBracketBorderColor
	 */
	public void setMatchedBracketBGColor(Color color) {
		matchedBracketBGColor = color;
		if (match!=null)
			repaint();
	}


	/**
	 * Sets the color used as the border for a matched bracket.
	 *
	 * @param color The color to use.
	 * @see #getMatchedBracketBorderColor
	 * @see #setMatchedBracketBGColor
	 */
	public void setMatchedBracketBorderColor(Color color) {
		matchedBracketBorderColor = color;
		if (match!=null)
			repaint();
	}


	public void setParser(Parser parser) {
		if (parserManager==null)
			parserManager = new ParserManager(this);
		clearParserNoticeHighlights();
		parserManager.setParser(parser);
	}


	/**
	 * Sets what type of syntax highlighting this editor is doing.  This method
	 * fires a property change of type <code>SYNTAX_STYLE_PROPERTY</code>.
	 *
	 * @param style The syntax editing style to use, for example,
	 *        <code>RSyntaxTextArea.NO_SYNTAX_STYLE</code> or
	 *        <code>RSyntaxArea.JAVA_SYNTAX_STYLE</code>.
	 * @see #getSyntaxEditingStyle
	 */
	public void setSyntaxEditingStyle(int style) {

		if (style<NO_SYNTAX_STYLE || style>MAX_SYNTAX_STYLE_NUMBER)
			style = NO_SYNTAX_STYLE;

		if (style != syntaxStyle) {
			int oldStyle = syntaxStyle;
			syntaxStyle = style;
			((RSyntaxDocument)getDocument()).setSyntaxStyle(style);
			firePropertyChange(SYNTAX_STYLE_PROPERTY, oldStyle, syntaxStyle);
		}

	}


	/**
	 * Sets all of the colors used in syntax highlighting to the colors
	 * specified.  This uses a shallow copy of the color scheme so that
	 * multiple text areas can share the same color scheme and have their
	 * properties changed simultaneously.<p>
	 *
	 * This method fires a property change event of type
	 * <code>SYNTAX_SCHEME_PROPERTY</code>.
	 *
	 * @param colorScheme The instance of
	 *        <code>SyntaxHighlightingColorScheme</code> to use.
	 * @see #getSyntaxHighlightingColorScheme
	 */
	public void setSyntaxHighlightingColorScheme(final
							SyntaxHighlightingColorScheme colorScheme) {

		// NOTE:  We don't check whether colorScheme is the same as the
		// current scheme because DecreaseFontSizeAction and
		// IncreaseFontSizeAction need it this way.
		// FIXME:  Find a way around this.

		SyntaxHighlightingColorScheme old = this.colorScheme;
		this.colorScheme = colorScheme;

		// Recalculate the line height.  We do this here instead of in
		// refreshFontMetrics() as this method is called less often and we
		// don't need the rendering hints to get the font's height.
		calculateLineHeight();

		if (isDisplayable()) {
			refreshFontMetrics(getGraphics2D(getGraphics()));
		}

		// Updates the margin line.
		updateMarginLineX();

		// Force the current line highlight to be repainted, even though
		// the caret's location hasn't changed.
		forceCurrentLineHighlightRepaint();

		// So encompassing JScrollPane will have its scrollbars updated.
		revalidate();

		firePropertyChange(SYNTAX_SCHEME_PROPERTY, old, this.colorScheme);

	}


	/**
	 * If templates are enabled, all currently-known templates are forgotten
	 * and all templates are loaded from all files in the specified directory
	 * ending in "*.xml".  If templates aren't enabled, nothing happens.
	 *
	 * @param dir The directory containing files ending in extension
	 *        <code>.xml</code> that contain templates to load.
	 * @return <code>true</code> if the load was successful;
	 *         <code>false</code> if either templates aren't currently
	 *         enabled or the load failed somehow (most likely, the
	 *         directory doesn't exist).	 
	 * @see #getTemplatesEnabled
	 * @see #setTemplatesEnabled
	 * @see #saveTemplates
	 */
	public synchronized static boolean setTemplateDirectory(String dir) {
		if (getTemplatesEnabled() && dir!=null) {
			File directory = new File(dir);
			if (directory.isDirectory()) {
				return codeTemplateManager.
						setTemplateDirectory(directory)>-1;
			}
			else {
				boolean created = directory.mkdir();
				if (created) {
					return codeTemplateManager.
						setTemplateDirectory(directory)>-1;
				}
			}
		}
		return false;
	}


	/**
	 * Enables or disables templates.<p>
	 *
	 * Templates are a set of "shorthand identifiers" that you can configure
	 * so that you only have to type a short identifier (such as "forb") to
	 * insert a larger amount of code into the document (such as:<p>
	 *
	 * <pre>
	 *   for (&lt;caret&gt;) {
	 *
	 *   }
	 * </pre>
	 *
	 * Templates are a shared resource among all instances of
	 * <code>RSyntaxTextArea</code>; that is, templates can only be
	 * enabled/disabled for all text areas globally, not individually, and
	 * all text areas have access of the same templates.  This should not
	 * be an issue; rather, it should be beneficial as it promotes
	 * uniformity among all text areas in an application.
	 *
	 * @param enabled Whether or not templates should be enabled.
	 * @see #getTemplatesEnabled
	 */
	public synchronized static void setTemplatesEnabled(boolean enabled) {
		if (enabled!=templatesEnabled) {
			templatesEnabled = enabled;
			if (enabled)
				codeTemplateManager = new CodeTemplateManager();
			else
				codeTemplateManager = null;
		}
	}


	/**
	 * Sets the rendering hint to use when antialiasing text in this
	 * editor.
	 *
	 * @param aaHintFieldName The name of a field in
	 *        <code>java.awt.RenderingHints</code>.  If an unknown or
	 *        unsupported field name is specified (such as a 1.6+ hint
	 *        being specified when this is a 1.4/1.5 JVM), <code>null</code>
	 *        is used instead.  A value of <code>null</code> means "no
	 *        antialiasing."
	 * @see #getTextAntiAliasHint()
	 */
	public void setTextAntiAliasHint(String aaHintFieldName) {

		//System.out.println("Trying to set AA hint to: " + aaHintFieldName);

		// If the new AA hint is null, disable text antialiasing.
		if (aaHintFieldName==null && this.aaHintFieldName!=null) {
			String old = this.aaHintFieldName;
			this.aaHint = null;
			this.aaHintFieldName = null;
			// We must be connected to a screen resource for our graphics
			// to be non-null.
			if (isDisplayable()) {
				refreshFontMetrics(getGraphics2D(getGraphics()));
			}
			firePropertyChange(ANTIALIAS_PROPERTY, old, null);
			repaint();
		}

		// Otherwise, if they're speicfying a new hint type, use it instead.
		else if (aaHintFieldName!=null &&
				!aaHintFieldName.equals(this.aaHintFieldName)) {
			String old = this.aaHintFieldName;
			try {
				Field f = RenderingHints.class.
								getDeclaredField(aaHintFieldName);
				this.aaHint = f.get(null);
				this.aaHintFieldName = aaHintFieldName;
			} catch (RuntimeException re) {
				// Re-throw (keep FindBugs happy)
			} catch (/*NoSuchField|IllegalAccess*/Exception e) {
				this.aaHint = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
				this.aaHintFieldName = "VALUE_TEXT_ANTIALIAS_OFF";
			}
			// We must be connected to a screen resource for our graphics
			// to be non-null.
			if (isDisplayable()) {
				refreshFontMetrics(getGraphics2D(getGraphics()));
			}
			firePropertyChange(ANTIALIAS_PROPERTY, old,this.aaHintFieldName);
			repaint();
		}

		//System.out.println("... Actual new value: " + this.aaHintFieldName);

	}


	/**
	 * Sets whether whitespace is visible.  This method fires a property change
	 * of type <code>VISIBLE_WHITESPACE_PROPERTY</code>.
	 *
	 * @param visible Whether whitespace should be visible.
	 * @see #isWhitespaceVisible
	 */
	public void setWhitespaceVisible(boolean visible) {
		if (whitespaceVisible!=visible) {
			whitespaceVisible = visible;
			((RSyntaxDocument)getDocument()).setWhitespaceVisible(
													visible, this);
			repaint();
			firePropertyChange(VISIBLE_WHITESPACE_PROPERTY,
							!visible, visible);
		}
	}


	/**
	 * Returns the token at the specified position in the view.
	 *
	 * @param p The position in the view.
	 * @return The token, or <code>null</code> if no token is at that
	 *         position.
	 * @see #modelToToken(int)
	 */
	/*
	 * TODO: This is a little inefficient.  This should convert view
	 * coordinates to the underlying token (if any).  The way things currently
	 * are, we're calling getTokenListForLine() twice (once in viewToModel()
	 * and once here).
	 */
	private Token viewToToken(Point p) {
		return modelToToken(viewToModel(p));
	}


	/**
	 * Handles hyperlinks.
	 */
	private class RSyntaxTextAreaMutableCaretEvent
					extends RTextAreaMutableCaretEvent {

		protected RSyntaxTextAreaMutableCaretEvent(RTextArea textArea) {
			super(textArea);
		}

		public void mouseClicked(MouseEvent e) {
			if (getHyperlinksEnabled() && isScanningForLinks &&
					hoveredOverLinkOffset>-1) {
				Token t = modelToToken(hoveredOverLinkOffset);
				URL url = null;
				String desc = null;
				try {
					String temp = t.getLexeme();
					// URI's need "http://" prefix for web URL's to work.
					if (temp.startsWith("www.")) {
						temp = "http://" + temp;
					}
					url = new URL(temp);
				} catch (MalformedURLException mue) {
					desc = mue.getMessage();
				}
				HyperlinkEvent he = new HyperlinkEvent(this,
						HyperlinkEvent.EventType.ACTIVATED,
						url, desc);
				fireHyperlinkUpdate(he);
			}
		}

		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
			if (getHyperlinksEnabled()) {
				if ((e.getModifiersEx()&linkScanningMask)!=0) {
					isScanningForLinks = true;
					Token t = viewToToken(e.getPoint());
					Cursor c2 = null;
					if (t!=null && t.isHyperlink()) {
						hoveredOverLinkOffset = t.offset;
						c2 = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					}
					else {
						c2 = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
						hoveredOverLinkOffset = -1;
					}
					if (getCursor()!=c2) {
						setCursor(c2);
						// TODO: Repaint just the affected line(s).
						repaint(); // Link either left or went into.
					}
				}
				else {
					if (isScanningForLinks) {
						Cursor c = getCursor();
						isScanningForLinks = false;
						hoveredOverLinkOffset = -1;
						if (c!=null && c.getType()==Cursor.HAND_CURSOR) {
							setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
							repaint(); // TODO: Repaint just the affected line.
						}
					}
				}
			}
		}

	}


}