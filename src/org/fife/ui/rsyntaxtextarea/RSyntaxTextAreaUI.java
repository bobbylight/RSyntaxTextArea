/*
 * 02/24/2004
 *
 * RSyntaxTextAreaUI.java - UI for an RSyntaxTextArea.
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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.text.*;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextAreaUI;


/**
 * UI used by <code>RSyntaxTextArea</code>.  This allows us to implement
 * syntax highlighting.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public class RSyntaxTextAreaUI extends RTextAreaUI {

	private static final String SHARED_ACTION_MAP_NAME	= "RSyntaxTextAreaUI.actionMap";
	private static final String SHARED_INPUT_MAP_NAME		= "RSyntaxTextAreaUI.inputMap";
	private static final EditorKit defaultKit			= new RSyntaxTextAreaEditorKit();


	public static ComponentUI createUI(JComponent ta) {
		return new RSyntaxTextAreaUI(ta);
	}


	/**
	 * Constructor.
	 */
	public RSyntaxTextAreaUI(JComponent rSyntaxTextArea) {
		super(rSyntaxTextArea);
	}


    /**
     * Creates the view for an element.
     *
     * @param elem The element.
     * @return The view.
     */
	public View create(Element elem) {
		RTextArea c = getRTextArea();
		if (c instanceof RSyntaxTextArea) {
			RSyntaxTextArea area = (RSyntaxTextArea) c;
			View v;
			if (area.getLineWrap())
				v = new WrappedSyntaxView(elem);
			else
				v = new SyntaxView(elem);
			return v;
		}
		return null;
	}


	/**
	 * Creates the highlighter to use for syntax text areas.
	 *
	 * @return The highlighter.
	 */
	protected Highlighter createHighlighter() {
		return new RSyntaxTextAreaHighlighter();
	}


	/**
	 * Returns the name to use to cache/fetch the shared action map.  This
	 * should be overridden by subclasses if the subclass has its own custom
	 * editor kit to install, so its actions get picked up.
	 *
	 * @return The name of the cached action map.
	 */
	protected String getActionMapName() {
		return SHARED_ACTION_MAP_NAME;
	}


	/**
	 * Fetches the EditorKit for the UI.
	 *
	 * @param tc The text component for which this UI is installed.
	 * @return The editor capabilities.
	 * @see javax.swing.plaf.TextUI#getEditorKit
	 */
	public EditorKit getEditorKit(JTextComponent tc) {
		return defaultKit;
	}


	/**
	 * Get the InputMap to use for the UI.<p>  
	 *
	 * This method is not named <code>getInputMap()</code> because there is
	 * a package-private method in <code>BasicTextAreaUI</code> with that name.
	 * Thus, creating a new method with that name causes certain compilers to
	 * issue warnings that you are not actually overriding the original method
	 * (since it is package-private).
	 */
	protected InputMap getRTextAreaInputMap() {
		InputMap map = new InputMapUIResource();
		InputMap shared = (InputMap)UIManager.get(SHARED_INPUT_MAP_NAME);
		if (shared==null) {
			shared = new RSyntaxTextAreaDefaultInputMap();
			UIManager.put(SHARED_INPUT_MAP_NAME, shared);
		}
		//KeyStroke[] keys = shared.allKeys();
		//for (int i=0; i<keys.length; i++)
		//	System.err.println(keys[i] + " -> " + shared.get(keys[i]));
		map.setParent(shared);
		return map;
	}


	/**
	 * Paints the text area's background.
	 *
	 * @param g The graphics component on which to paint.
	 */
	protected void paintBackground(Graphics g) {
		super.paintBackground(g);
		paintMatchedBracket(g);
	}


	/**
	 * Paints the "matched bracket", if any.
	 *
	 * @param g The graphics context.
	 */
	protected void paintMatchedBracket(Graphics g) {
		// We must add "-1" to the height because otherwise we'll paint below
		// the region that gets invalidated.
		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (rsta.isBracketMatchingEnabled()) {
			Rectangle match = rsta.match;
			if (match!=null) {
				if (rsta.getAnimateBracketMatching()) {
					g.setColor(rsta.getMatchedBracketBGColor());
					g.fillRoundRect(match.x,match.y, match.width,match.height-1, 5,5);
					g.setColor(rsta.getMatchedBracketBorderColor());
					g.drawRoundRect(match.x,match.y, match.width,match.height-1, 5,5);
				}
				else {
					g.setColor(rsta.getMatchedBracketBGColor());
					g.fillRect(match.x,match.y, match.width,match.height-1);
					g.setColor(rsta.getMatchedBracketBorderColor());
					g.drawRect(match.x,match.y, match.width,match.height-1);
				}
			}
		}
	}

	/**
	 * Gets called whenever a bound property is changed on this UI's
	 * <code>RSyntaxTextArea</code>.
	 *
	 * @param e The property change event.
	 */
	protected void propertyChange(PropertyChangeEvent e) {

		String name = e.getPropertyName();

		// If they change the syntax scheme, we must do this so that
		// WrappedSyntaxView(_TEST) updates its child views properly.
		if (name.equals(RSyntaxTextArea.SYNTAX_SCHEME_PROPERTY)) {
			modelChanged();
		}

		// Everything else is general to all RTextAreas.
		else {
			super.propertyChange(e);
		}

	}


	/**
	 * Updates the view.  This should be called when the underlying
	 * <code>RSyntaxTextArea</code> changes its syntax editing style.
	 */
	public void refreshSyntaxHighlighting() {
		modelChanged();
	}


	/**
	 * Returns the y-coordinate of the line containing a specified offset.<p>
	 *
	 * This is faster than calling <code>modelToView(offs).y</code>, so it is
	 * preferred if you do not need the actual bounding box.
	 *
	 * @param offs The offset info the document.
	 * @return The y-coordinate of the top of the offset, or <code>-1</code> if
	 *         this text area doesn't yet have a positive size.
	 * @throws BadLocationException If <code>offs</code> isn't a valid offset
	 *         into the document.
	 */
	public int yForLineContaining(int offs) throws BadLocationException {
		Rectangle alloc = getVisibleEditorRect();
		if (alloc!=null) {
			RSTAView view = (RSTAView)getRootView(textArea).getView(0);
			return view.yForLineContaining(alloc, offs);
		}
		return -1;
	}


}