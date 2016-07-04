/*
 * 11/14/2003
 *
 * RTextScrollPane.java - A JScrollPane that will only accept RTextAreas
 * so that it can display line numbers, fold indicators, and icons.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JScrollPane;


/**
 * An extension of <code>JScrollPane</code> that will only take
 * <code>RTextArea</code>s (or <code>javax.swing.JLayer</code>s decorating
 * <code>RTextArea</code>s) for its view.  This class has the ability to show:
 * <ul>
 *    <li>Line numbers
 *    <li>Per-line icons (for bookmarks, debugging breakpoints, error markers, etc.)
 *    <li>+/- icons to denote code folding regions.
 * </ul>
 *
 * The actual "meat" of these extras is contained in the {@link Gutter} class.
 * Each <code>RTextScrollPane</code> has a <code>Gutter</code> instance that
 * it uses as its row header.  The gutter is only made visible when one of its
 * features is being used (line numbering, folding, and/or icons).
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextScrollPane extends JScrollPane {

	private Gutter gutter;


	/**
	 * Constructor.  If you use this constructor, you must call
	 * {@link #setViewportView(Component)} and pass in an {@link RTextArea}
	 * for this scroll pane to render line numbers properly.
	 */
	public RTextScrollPane() {
		this(null, true);
	}

	/**
	 * Creates a scroll pane.  A default value will be used for line number
	 * color (gray), and the current line's line number will be highlighted.
	 *
	 * @param textArea The text area this scroll pane will contain.
	 */
	public RTextScrollPane(RTextArea textArea) {
		this(textArea, true);
	}

	/**
	 * Creates a scroll pane.  A default value will be used for line number
	 * color (gray), and the current line's line number will be highlighted.
	 *
	 * @param comp The component this scroll pane should display.  This should
	 *        be an instance of {@link RTextArea},
	 *        <code>javax.swing.JLayer</code> (or the older
	 *        <code>org.jdesktop.jxlayer.JXLayer</code>), or <code>null</code>.
	 *        If this argument is <code>null</code>, you must call
	 *        {@link #setViewportView(Component)}, passing in an instance of
	 *        one of the types above.
	 */
	public RTextScrollPane(Component comp) {
		this(comp, true);
	}

	/**
	 * Creates a scroll pane.  A default value will be used for line number
	 * color (gray), and the current line's line number will be highlighted.
	 *
	 * @param textArea The text area this scroll pane will contain.  If this is
	 *        <code>null</code>, you must call
	 *        {@link #setViewportView(Component)}, passing in an
	 *        {@link RTextArea}.
	 * @param lineNumbers Whether line numbers should be enabled.
	 */
	public RTextScrollPane(RTextArea textArea, boolean lineNumbers) {
		this(textArea, lineNumbers, Color.GRAY);
	}

	/**
	 * Creates a scroll pane.  A default value will be used for line number
	 * color (gray), and the current line's line number will be highlighted.
	 *
	 * @param comp The component this scroll pane should display.  This should
	 *        be an instance of {@link RTextArea},
	 *        <code>javax.swing.JLayer</code> (or the older
	 *        <code>org.jdesktop.jxlayer.JXLayer</code>), or <code>null</code>.
	 *        If this argument is <code>null</code>, you must call
	 *        {@link #setViewportView(Component)}, passing in an instance of
	 *        one of the types above.
	 * @param lineNumbers Whether line numbers should be enabled.
	 */
	public RTextScrollPane(Component comp, boolean lineNumbers) {
		this(comp, lineNumbers, Color.GRAY);
	}


	/**
	 * Creates a scroll pane.
	 *
	 * @param comp The component this scroll pane should display.  This should
	 *        be an instance of {@link RTextArea},
	 *        <code>javax.swing.JLayer</code> (or the older
	 *        <code>org.jdesktop.jxlayer.JXLayer</code>), or <code>null</code>.
	 *        If this argument is <code>null</code>, you must call
	 *        {@link #setViewportView(Component)}, passing in an instance of
	 *        one of the types above.
	 * @param lineNumbers Whether line numbers are initially enabled.
	 * @param lineNumberColor The color to use for line numbers.
	 */
	public RTextScrollPane(Component comp, boolean lineNumbers,
							Color lineNumberColor) {

		super(comp);

		RTextArea textArea = getFirstRTextAreaDescendant(comp);

		// Create the gutter for this document.
		Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
		gutter = new Gutter(textArea);
		gutter.setLineNumberFont(defaultFont);
		gutter.setLineNumberColor(lineNumberColor);
		setLineNumbersEnabled(lineNumbers);

		// Set miscellaneous properties.
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}


	/**
	 * Ensures the gutter is visible if it's showing anything.
	 */
	private void checkGutterVisibility() {
		int count = gutter.getComponentCount();
		if (count==0) {
			if (getRowHeader()!=null && getRowHeader().getView()==gutter) {
				setRowHeaderView(null);
			}
		}
		else {
			if (getRowHeader()==null || getRowHeader().getView()==null) {
				setRowHeaderView(gutter);
			}
		}
	}


	/**
	 * Returns the gutter.
	 *
	 * @return The gutter.
	 */
	public Gutter getGutter() {
		return gutter;
	}


	/**
	 * Returns <code>true</code> if the line numbers are enabled and visible.
	 *
	 * @return Whether or not line numbers are visible.
	 * @see #setLineNumbersEnabled(boolean)
	 */
	public boolean getLineNumbersEnabled() {
		return gutter.getLineNumbersEnabled();
	}


	/**
	 * Returns the text area being displayed.
	 *
	 * @return The text area.
	 * @see #setViewportView(Component)
	 */
	public RTextArea getTextArea() {
		return (RTextArea)getViewport().getView();
	}


	/**
	 * Returns whether the fold indicator is enabled.
	 *
	 * @return Whether the fold indicator is enabled.
	 * @see #setFoldIndicatorEnabled(boolean)
	 */
	public boolean isFoldIndicatorEnabled() {
		return gutter.isFoldIndicatorEnabled();
	}


	/**
	 * Returns whether the icon row header is enabled.
	 *
	 * @return Whether the icon row header is enabled.
	 * @see #setIconRowHeaderEnabled(boolean)
	 */
	public boolean isIconRowHeaderEnabled() {
		return gutter.isIconRowHeaderEnabled();
	}


	/**
	 * Toggles whether the fold indicator is enabled.
	 *
	 * @param enabled Whether the fold indicator should be enabled.
	 * @see #isFoldIndicatorEnabled()
	 */
	public void setFoldIndicatorEnabled(boolean enabled) {
		gutter.setFoldIndicatorEnabled(enabled);
		checkGutterVisibility();
	}


	/**
	 * Toggles whether the icon row header (used for breakpoints, bookmarks,
	 * etc.) is enabled.
	 *
	 * @param enabled Whether the icon row header is enabled.
	 * @see #isIconRowHeaderEnabled()
	 */
	public void setIconRowHeaderEnabled(boolean enabled) {
		gutter.setIconRowHeaderEnabled(enabled);
		checkGutterVisibility();
	}


	/**
	 * Toggles whether or not line numbers are visible.
	 *
	 * @param enabled Whether or not line numbers should be visible.
	 * @see #getLineNumbersEnabled()
	 */
	public void setLineNumbersEnabled(boolean enabled) {
		gutter.setLineNumbersEnabled(enabled);
		checkGutterVisibility();
	}


	/**
	 * Sets the view for this scroll pane.  This must be an {@link RTextArea}.
	 *
	 * @param view The new view.
	 * @see #getTextArea()
	 */
	@Override
	public void setViewportView(Component view) {

		RTextArea rtaCandidate = null;

		if (!(view instanceof RTextArea)) {
			rtaCandidate = getFirstRTextAreaDescendant(view);
			if (rtaCandidate==null) {
				throw new IllegalArgumentException(
				"view must be either an RTextArea or a JLayer wrapping one");
			}
		}
		else {
			rtaCandidate = (RTextArea)view;
		}
		super.setViewportView(view);
		if (gutter!=null) {
			gutter.setTextArea(rtaCandidate);
		}
	}


	/**
	 * Returns the first descendant of a component that is an
	 * <code>RTextArea</code>.  This is primarily here to support
	 * <code>javax.swing.JLayer</code>s that wrap <code>RTextArea</code>s.
	 *
	 * @param comp The component to recursively look through.
	 * @return The first descendant text area, or <code>null</code> if none
	 *         is found.
	 */
	private static RTextArea getFirstRTextAreaDescendant(Component comp) {
		Stack<Component> stack = new Stack<Component>();
		stack.add(comp);
		while (!stack.isEmpty()) {
			Component current = stack.pop();
			if (current instanceof RTextArea) {
				return (RTextArea)current;
			}
			if (current instanceof Container) {
				Container container = (Container)current;
				stack.addAll(Arrays.asList(container.getComponents()));
			}
		}
		return null;
	}


}
