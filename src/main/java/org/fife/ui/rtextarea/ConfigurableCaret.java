/*
 * 12/21/2004
 *
 * ConfigurableCaret.java - The caret used by RTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;


/**
 * The caret used by {@link RTextArea}.  This caret has all of the properties
 * that <code>javax.swing.text.DefaultCaret</code> does, as well as adding the
 * following niceties:
 *
 * <ul>
 *   <li>This caret can render itself many different ways; see the
 *       {@link #setStyle(CaretStyle)} method and {@link CaretStyle} for
 *       more information.</li>
 *   <li>On Microsoft Windows and other operating systems that do not
 *       support system selection (i.e., selecting text, then pasting
 *       via the middle mouse button), clicking the middle mouse button
 *       will cause a regular paste operation to occur.  On systems
 *       that support system selection (i.e., all UNIX variants),
 *       the middle mouse button will behave normally.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.6
 */
public class ConfigurableCaret extends DefaultCaret {

	/**
	 * Action used to select a word on a double click.
	 */
	private static transient Action selectWord = null;

	/**
	 * Action used to select a line on a triple click.
	 */
	private static transient Action selectLine = null;

	/**
	 * holds last MouseEvent which caused the word selection.
	 */
	private transient MouseEvent selectedWordEvent = null;

	/**
	 * Used for fastest-possible retrieval of the character at the
	 * caret's position in the document.
	 */
	private transient Segment seg;

	/**
	 * Whether the caret is a vertical line, a horizontal line, or a block.
	 */
	private CaretStyle style;

	/**
	 * The selection painter.  By default this paints selections with the
	 * text area's selection color.
	 */
	private ChangeableHighlightPainter selectionPainter;

	private boolean alwaysVisible;

	/**
	 * Whether this caret will try to paste into the editor (assuming it is
	 * editable) on middle-mouse clicks.
	 */
	private boolean pasteOnMiddleMouseClick;

	/**
	 * Creates the caret using {@link CaretStyle#THICK_VERTICAL_LINE_STYLE}.
	 */
	public ConfigurableCaret() {
		this(CaretStyle.THICK_VERTICAL_LINE_STYLE);
	}


	/**
	 * Constructs a new <code>ConfigurableCaret</code>.
	 *
	 * @param style The style to use when painting the caret.  If this is
	 *        invalid, then {@link CaretStyle#THICK_VERTICAL_LINE_STYLE} is
	 *        used.
	 */
	public ConfigurableCaret(CaretStyle style) {
		seg = new Segment();
		setStyle(style);
		selectionPainter = new ChangeableHighlightPainter();
		pasteOnMiddleMouseClick = true;
	}


	/**
	 * Adjusts the caret location based on the MouseEvent.
	 */
	private void adjustCaret(MouseEvent e) {
		if ((e.getModifiers()&ActionEvent.SHIFT_MASK)!=0 && getDot()!=-1) {
			moveCaret(e);
		}
		else {
			positionCaret(e);
		}
	}


	/**
	 * Adjusts the focus, if necessary.
	 *
	 * @param inWindow if true indicates requestFocusInWindow should be used
	 */
	private void adjustFocus(boolean inWindow) {
		RTextArea textArea = getTextArea();
		if ((textArea != null) && textArea.isEnabled() &&
				textArea.isRequestFocusEnabled()) {
			if (inWindow) {
				textArea.requestFocusInWindow();
			}
			else {
				textArea.requestFocus();
			}
		}
	}


	/**
	 * Overridden to damage the correct width of the caret, since this caret
	 * can be different sizes.
	 *
	 * @param r The current location of the caret.
	 */
	@Override
	protected synchronized void damage(Rectangle r) {
		if (r != null) {
			validateWidth(r); // Check for "0" or "1" caret width
			x = r.x - 1;
			y = r.y;
			width = r.width + 4;
			height = r.height;
			repaint();
		}
	}


	/**
	 * Called when the UI is being removed from the
	 * interface of a JTextComponent.  This is used to
	 * unregister any listeners that were attached.
	 *
	 * @param c The text component.  If this is not an
	 *        <code>RTextArea</code>, an <code>Exception</code>
	 *        will be thrown.
	 */
	@Override
	public void deinstall(JTextComponent c) {
		if (!(c instanceof RTextArea)) {
			throw new IllegalArgumentException(
					"c must be instance of RTextArea");
		}
		super.deinstall(c);
		c.setNavigationFilter(null);
	}


	/**
	 * Returns whether this caret will paste the contents of the clipboard into
	 * the editor (assuming it is editable) on middle-mouse-button clicks.
	 *
	 * @return Whether a paste operation will be performed.
	 * @see #setPasteOnMiddleMouseClick(boolean)
	 */
	public boolean getPasteOnMiddleMouseClick() {
		return pasteOnMiddleMouseClick;
	}


	/**
	 * Gets the text editor component that this caret is bound to.
	 *
	 * @return The <code>RTextArea</code>.
	 */
	protected RTextArea getTextArea() {
		return (RTextArea)getComponent();
	}


	/**
	 * Returns whether this caret's selection uses rounded edges.
	 *
	 * @return Whether this caret's edges are rounded.
	 * @see #setRoundedSelectionEdges
	 */
	public boolean getRoundedSelectionEdges() {
		return ((ChangeableHighlightPainter)getSelectionPainter()).
								getRoundedEdges();
	}


	/**
	 * Gets the painter for the Highlighter.  This is overridden to return
	 * our custom selection painter.
	 *
	 * @return The painter.
	 */
	@Override
	protected Highlighter.HighlightPainter getSelectionPainter() {
		return selectionPainter;
	}


	/**
	 * Gets the current style of this caret.
	 *
	 * @return The caret's style.
	 * @see #setStyle(CaretStyle)
	 */
	public CaretStyle getStyle() {
		return style;
	}


	/**
	 * Installs this caret on a text component.
	 *
	 * @param c The text component.  If this is not an {@link RTextArea},
	 *        an <code>Exception</code> will be thrown.
	 */
	@Override
	public void install(JTextComponent c) {
		if (!(c instanceof RTextArea)) {
			throw new IllegalArgumentException(
					"c must be instance of RTextArea");
		}
		super.install(c);
		c.setNavigationFilter(new FoldAwareNavigationFilter());
	}


	/**
	 * Returns whether this caret is always visible (as opposed to
	 * blinking, or not visible when the editor's window is not focused).
	 * This can be used by popup windows that want the caret's location
	 * to still be visible for contextual purposes while they are displayed.
	 *
	 * @return Whether this caret is always visible.
	 * @see #setAlwaysVisible(boolean)
	 */
	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}


	/**
	 * Called when the mouse is clicked.  If the click was generated from
	 * button1, a double click selects a word, and a triple click the
	 * current line.
	 *
	 * @param e the mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

		if (! e.isConsumed()) {

			RTextArea textArea = getTextArea();
			int nclicks = e.getClickCount();

			if (SwingUtilities.isLeftMouseButton(e)) {
				if (nclicks>2) {
					nclicks %= 2; // Alternate selecting word/line.
					switch (nclicks) {
						case 0:
							selectWord(e);
							selectedWordEvent = null;
							break;
						case 1:
							Action a = null;
							ActionMap map = textArea.getActionMap();
							if (map != null) {
								a = map.get(RTextAreaEditorKit.selectLineAction);
							}
							if (a == null) {
								if (selectLine == null) {
									selectLine = new RTextAreaEditorKit.SelectLineAction();
								}
								a = selectLine;
							}
							a.actionPerformed(new ActionEvent(textArea,
										ActionEvent.ACTION_PERFORMED,
										null, e.getWhen(), e.getModifiers()));
					}
				}
			}

			else if (SwingUtilities.isMiddleMouseButton(e) &&
					getPasteOnMiddleMouseClick()) {
				if (nclicks == 1 && textArea.isEditable() && textArea.isEnabled()) {
					// Paste the system selection, if it exists (e.g., on UNIX
					// platforms, the user can select text, the middle-mouse click
					// to paste it; this doesn't work on Windows).  If the system
					// doesn't support system selection, just do a normal paste.
					JTextComponent c = (JTextComponent) e.getSource();
					if (c != null) {
						try {
							Toolkit tk = c.getToolkit();
							Clipboard buffer = tk.getSystemSelection();
							// If the system supports system selections, (e.g. UNIX),
							// try to do it.
							if (buffer != null) {
								adjustCaret(e);
								TransferHandler th = c.getTransferHandler();
								if (th != null) {
									Transferable trans = buffer.getContents(null);
									if (trans != null) {
										th.importData(c, trans);
									}
								}
								adjustFocus(true);
							}
							// If the system doesn't support system selections
							// (e.g. Windows), just do a normal paste.
							else {
								textArea.paste();
							}
						} catch (HeadlessException he) {
							// do nothing... there is no system clipboard
						}
					} // if (c!=null)
				} // if (nclicks == 1 && component.isEditable() && component.isEnabled())
			} // else if (SwingUtilities.isMiddleMouseButton(e))

		} // if (!c.isConsumed())

	}


	/**
	 * Overridden to also focus the text component on right mouse clicks.
	 *
	 * @param e The mouse event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (!e.isConsumed() && SwingUtilities.isRightMouseButton(e)) {
			JTextComponent c = getComponent();
			if (c!=null && c.isEnabled() && c.isRequestFocusEnabled()) {
				c.requestFocusInWindow();
			}
		}
	}


	/**
	 * Paints the cursor.
	 *
	 * @param g The graphics context in which to paint.
	 */
	@Override
	public void paint(Graphics g) {

		// If the cursor is currently visible...
		if (isVisible() || alwaysVisible) {

			try {

				RTextArea textArea = getTextArea();
				g.setColor(textArea.getCaretColor());
				TextUI mapper = textArea.getUI();
				Rectangle r = mapper.modelToView(textArea, getDot());

				// "Correct" the value of rect.width (takes into
				// account caret being at EOL (and thus rect.width==1),
				// etc.
				// We do this even for LINE_STYLE because
				// if they change from that caret to block/underline,
				// the first time they do so width==1, so it will take
				// one caret flash to paint correctly (wider).  If we
				// do this every time, then it's painted correctly the
				// first blink.
				validateWidth(r);

				// This condition is most commonly hit when code folding is
				// enabled and the user collapses a fold above the caret
				// position.  If our cached x/y/w/h aren't updated, this caret
				// appears to stop blinking because the wrong line range gets
				// damaged.  This check keeps us in sync.
				if (width>0 && height>0 &&
						!contains(r.x, r.y, r.width, r.height)) {
					Rectangle clip = g.getClipBounds();
					if (clip != null && !clip.contains(this)) {
						// Clip doesn't contain the old location, force it
						// to be repainted lest we leave a caret around.
						repaint();
					}
					// This will potentially cause a repaint of something
					// we're already repainting, but without changing the
					// semantics of damage we can't really get around this.
					damage(r);
				}

				// Need to subtract 2 from height, otherwise
				// the caret will expand too far vertically.
				r.height -= 2;

				switch (style) {

					// Draw a big rectangle, and xor the foreground color.
					case BLOCK_STYLE:
						Color textAreaBg = textArea.getBackground();
						if (textAreaBg==null) {
							textAreaBg = Color.white;
						}
						g.setXORMode(textAreaBg);
						// fills x==r.x to x==(r.x+(r.width)-1), inclusive.
						g.fillRect(r.x,r.y, r.width,r.height);
						break;

					// Draw a rectangular border.
					case BLOCK_BORDER_STYLE:
						// fills x==r.x to x==(r.x+(r.width-1)), inclusive.
						g.drawRect(r.x,r.y, r.width-1,r.height);
						break;

					// Draw an "underline" below the current position.
					case UNDERLINE_STYLE:
						textAreaBg = textArea.getBackground();
						if (textAreaBg==null) {
							textAreaBg = Color.white;
						}
						g.setXORMode(textAreaBg);
						int y = r.y + r.height;
						g.drawLine(r.x,y, r.x+r.width-1,y);
						break;

					// Draw a vertical line.
					default:
					case VERTICAL_LINE_STYLE:
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						break;

					// A thicker vertical line.
					case THICK_VERTICAL_LINE_STYLE:
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						r.x++;
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						break;

				} // End of switch (style).

			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}

		} // End of if (isVisible()).

	}


	/**
	 * Selects word based on a mouse event.
	 */
	private void selectWord(MouseEvent e) {
		if (selectedWordEvent != null &&
				selectedWordEvent.getX() == e.getX() &&
				selectedWordEvent.getY() == e.getY()) {
			// We've already the done selection for this.
			return;
		}
		Action a = null;
		RTextArea textArea = getTextArea();
		ActionMap map = textArea.getActionMap();
		if (map != null) {
			a = map.get(RTextAreaEditorKit.selectWordAction);
		}
		if (a == null) {
			if (selectWord == null) {
				selectWord = new RTextAreaEditorKit.SelectWordAction();
			}
			a = selectWord;
		}
		a.actionPerformed(new ActionEvent(textArea,
							ActionEvent.ACTION_PERFORMED,
							null, e.getWhen(), e.getModifiers()));
		selectedWordEvent = e;
	}


	/**
	 * Toggles whether this caret should always be visible (as opposed to
	 * blinking, or not visible when the editor's window is not focused).
	 * This can be used by popup windows that want the caret's location
	 * to still be visible for contextual purposes while they are displayed.
	 *
	 * @param alwaysVisible Whether this caret should always be visible.
	 * @see #isAlwaysVisible()
	 */
	public void setAlwaysVisible(boolean alwaysVisible) {
		if (alwaysVisible != this.alwaysVisible) {
			this.alwaysVisible = alwaysVisible;
			if (!isVisible()) {
				// Force painting of caret since super class's "flasher" timer
				// won't fire when the window doesn't have focus
				repaint();
			}
		}
	}


	/**
	 * Sets whether this caret will paste the contents of the clipboard into
	 * the editor (assuming it is editable) on middle-mouse-button clicks.
	 *
	 * @param paste Whether a paste operation will be performed.
	 * @see #getPasteOnMiddleMouseClick()
	 */
	public void setPasteOnMiddleMouseClick(boolean paste) {
		pasteOnMiddleMouseClick = paste;
	}


	/**
	 * Sets whether this caret's selection should have rounded edges.
	 *
	 * @param rounded Whether it should have rounded edges.
	 * @see #getRoundedSelectionEdges()
	 */
	public void setRoundedSelectionEdges(boolean rounded) {
		((ChangeableHighlightPainter)getSelectionPainter()).
								setRoundedEdges(rounded);
	}


	/**
	 * Overridden to always render the selection, even when the text component
	 * loses focus.
	 *
	 * @param visible Whether the selection should be visible.  This parameter
	 *        is ignored.
	 */
	@Override
	public void setSelectionVisible(boolean visible) {
		super.setSelectionVisible(true);
	}


	/**
	 * Sets the style used when painting the caret.
	 *
	 * @param style The style to use.  This should not be <code>null</code>.
	 * @see #getStyle()
	 */
	public void setStyle(CaretStyle style) {
		if (style==null) {
			style = CaretStyle.THICK_VERTICAL_LINE_STYLE;
		}
		if (style!=this.style) {
			this.style = style;
			repaint();
		}
	}


	/**
	 * Helper function used by the block and underline carets to ensure the
	 * width of the painted caret is valid.  This is done for the following
	 * reasons:
	 *
	 * <ul>
	 *   <li>The <code>View</code> classes in the javax.swing.text package
	 *       always return a width of "1" when <code>modelToView</code> is
	 *       called.  We'll be needing the actual width.</li>
	 *   <li>Even in smart views, such as <code>RSyntaxTextArea</code>'s
	 *       <code>SyntaxView</code> and <code>WrappedSyntaxView</code> that
	 *       return the width of the current character, if the caret is at the
	 *       end of a line for example, the width returned from
	 *       <code>modelToView</code> will be 0 (as the width of unprintable
	 *       characters such as '\n' is calculated as 0).  In this case, we'll
	 *       use a default width value.</li>
	 * </ul>
	 *
	 * @param rect The rectangle returned by the current
	 *        <code>View</code>'s <code>modelToView</code>
	 *        method for the caret position.
	 */
	private void validateWidth(Rectangle rect) {

		// If the width value > 1, we assume the View is
		// a "smart" view that returned the proper width.
		// So only worry about this stuff if width <= 1.
		if (rect!=null && rect.width<=1) {

			// The width is either 1 (most likely, we're using a "dumb" view
			// like those in javax.swing.text) or 0 (most likely, we're using
			// a "smart" view like org.fife.ui.rsyntaxtextarea.SyntaxView,
			// we're at the end of a line, and the width of '\n' is being
			// computed as 0).

			try {

				// Try to get a width for the character at the caret
				// position.  We use the text area's font instead of g's
				// because g's may vary in an RSyntaxTextArea.
				RTextArea textArea = getTextArea();
				textArea.getDocument().getText(getDot(),1, seg);
				Font font = textArea.getFont();
				FontMetrics fm = textArea.getFontMetrics(font);
				rect.width = fm.charWidth(seg.array[seg.offset]);

				// This width being returned 0 likely means that it is an
				// unprintable character (which is almost 100% to be a
				// newline char, i.e., we're at the end of a line).  So,
				// just use the width of a space.
				if (rect.width==0) {
					rect.width = fm.charWidth(' ');
				}

			} catch (BadLocationException ble) {
				// This shouldn't ever happen.
				ble.printStackTrace();
				rect.width = 8;
			}

		} // End of if (rect!=null && rect.width<=1).

	}


	/**
	 * Keeps the caret out of folded regions in edge cases where it doesn't
	 * happen automatically, for example, when the caret moves automatically in
	 * response to Document.insert() and Document.remove() calls.  Most keyboard
	 * shortcuts already take folding into account, as do viewToModel() and
	 * modelToView(), so this filter usually does not do anything.<p>
	 *
	 * Common cases: backspacing to visible line of collapsed region.
	 */
	private class FoldAwareNavigationFilter extends NavigationFilter {

		@Override
	    public void setDot(FilterBypass fb, int dot, Position.Bias bias) {

	    	RTextArea textArea = getTextArea();
	        if (textArea instanceof RSyntaxTextArea) {

	        	RSyntaxTextArea rsta = (RSyntaxTextArea)getTextArea();
	        	if (rsta.isCodeFoldingEnabled()) {

	        		int lastDot = getDot();
	        		FoldManager fm = rsta.getFoldManager();
	        		int line = 0;
	        		try {
	        			line = textArea.getLineOfOffset(dot);
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}

	        		if (fm.isLineHidden(line)) {

	        			//System.out.println("filterBypass: avoiding hidden line");
	        			try {
		        			if (dot>lastDot) { // Moving to further line
		        				int lineCount = textArea.getLineCount();
		        				while (++line<lineCount &&
		        						fm.isLineHidden(line));
		            			if (line<lineCount) {
		            				dot = textArea.getLineStartOffset(line);
		            			}
		            			else { // No lower lines visible
		            				UIManager.getLookAndFeel().
		            						provideErrorFeedback(textArea);
		            				return;
		            			}
		        			}
		        			else if (dot<lastDot) { // Moving to earlier line
		        				while (--line>=0 && fm.isLineHidden(line));
		        				if (line>=0) {
		        					dot = textArea.getLineEndOffset(line) - 1;
		        				}
		        			}
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}

	        		}

	        	}

	        }

	        super.setDot(fb, dot, bias);

	    }

		@Override
	    public void moveDot(FilterBypass fb, int dot, Position.Bias bias) {
	        super.moveDot(fb, dot, bias);
	    }

	}


}