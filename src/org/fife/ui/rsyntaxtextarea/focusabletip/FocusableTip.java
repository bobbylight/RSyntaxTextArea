/*
 * 07/29/2009
 *
 * FocusableTip.java - A focusable tool tip, like those in Eclipse.
 * Copyright (C) 2009 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.focusabletip;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;


/**
 * A focusable tool tip, similar to those found in Eclipse.  The user
 * can click in the tip and it becomes a "real," resizable window.
 *
 * @author Robert Futrell
 * @vesrion 1.0
 */
public class FocusableTip {

	private JTextArea textArea;
	private TipWindow tipWindow;
	private URL imageBase;
	private TextAreaListener textAreaListener;
	private HyperlinkListener hyperlinkListener;
	private String lastText;
	private Rectangle tipVisibleBounds;

	private static final String MSG =
		"org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip";
	private static final ResourceBundle msg = ResourceBundle.getBundle(MSG);


	public FocusableTip(JTextArea textArea, HyperlinkListener listener) {
		setTextArea(textArea);
		this.hyperlinkListener = listener;
		textAreaListener = new TextAreaListener();
		tipVisibleBounds = new Rectangle();
	}


	private void computeTipVisibleBounds(int start, int end) {
		// Compute area that the mouse can move in without hiding the
		// tip window. Note that Java 1.4 can only detect mouse events
		// in Java windows, not globally.
		try {

			Rectangle r = tipWindow.getBounds();
			Point p = r.getLocation();
			SwingUtilities.convertPointFromScreen(p, textArea);
			r.setLocation(p);
			tipVisibleBounds.setBounds(r);
			tipVisibleBounds = tipVisibleBounds.union(textArea.modelToView(start));
			tipVisibleBounds = tipVisibleBounds.union(textArea.modelToView(end));

		} catch (BadLocationException ble) { // Never happens
			ble.printStackTrace();
			tipVisibleBounds.setBounds(-1, -1, 0, 0);
		}

	}


	private void createAndShowTipWindow(final MouseEvent e, final String text) {

		Window owner = SwingUtilities.getWindowAncestor(textArea);
		tipWindow = new TipWindow(owner, this, text);
		tipWindow.setHyperlinkListener(hyperlinkListener);

		// TODO: Position tip window better (handle RTL, edges of screen, etc.).
		// Wrap in an invokeLater() to work around a JEditorPane issue where it
		// doesn't return its proper preferred size until after it is displayed.
		// See http://forums.sun.com/thread.jspa?forumID=57&threadID=574810
		// for a discussion.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tipWindow.fixSize();
				Point p = e.getPoint();
				SwingUtilities.convertPointToScreen(p, textArea);
				int x = p.x - 10;
				int y = p.y - tipWindow.getHeight() - 10;
				tipWindow.setLocation(x, y);
				tipWindow.setVisible(true);
				textAreaListener.install(textArea);
				lastText = text;
			}
		});

	}


	/**
	 * Returns the base URL to use when loading images in this focusable tip.
	 *
	 * @return The base URL to use.
	 * @see #setImageBase(URL)
	 */
	public URL getImageBase() {
		return imageBase;
	}


	/**
	 * Returns localized text for the given key.
	 *
	 * @param key The key into the resource bundle.
	 * @return The localized text.
	 */
	static String getString(String key) {
		return msg.getString(key);
	}


	void possiblyDisposeOfTipWindow() {
		if (tipWindow != null) {
			tipWindow.dispose();
			tipWindow = null;
			textAreaListener.uninstall();
			tipVisibleBounds.setBounds(-1, -1, 0, 0);
			lastText = null;
			textArea.requestFocus();
		}
	}


	void removeListeners() {
		System.out.println("DEBUG: Removing text area listeners");
		textAreaListener.uninstall();
	}


	/**
	 * Sets the base URL to use when loading images in this focusable tip.
	 * 
	 * @param url The base URL to use.
	 * @see #getImageBase()
	 */
	public void setImageBase(URL url) {
		imageBase = url;
	}


	private void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
		// Is okay to do multiple times.
		ToolTipManager.sharedInstance().registerComponent(textArea);
	}


	public void toolTipRequested(MouseEvent e, String text, int start, int end) {

		if (text == null || text.length() == 0) {
			possiblyDisposeOfTipWindow();
			lastText = text;
			return;
		}

		if (lastText == null || text.length() != lastText.length()
				|| !text.equals(lastText)) {
			possiblyDisposeOfTipWindow();
			createAndShowTipWindow(e, text);
			computeTipVisibleBounds(start, end);
		}

	}


	private class TextAreaListener extends MouseInputAdapter implements
			CaretListener, ComponentListener, FocusListener, KeyListener {

		public void caretUpdate(CaretEvent e) {
			Object source = e.getSource();
			if (source == textArea) {
				possiblyDisposeOfTipWindow();
			}
		}

		public void componentHidden(ComponentEvent e) {
			handleComponentEvent(e);
		}

		public void componentMoved(ComponentEvent e) {
			handleComponentEvent(e);
		}

		public void componentResized(ComponentEvent e) {
			handleComponentEvent(e);
		}

		public void componentShown(ComponentEvent e) {
			handleComponentEvent(e);
		}

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			// Only dispose of tip if it wasn't the TipWindow that was clicked
			Component c = e.getOppositeComponent();
			boolean tipClicked = (c instanceof TipWindow) ||
					(SwingUtilities.getWindowAncestor(c) instanceof TipWindow);
			if (!tipClicked) {
				possiblyDisposeOfTipWindow();
			}
		}

		private void handleComponentEvent(ComponentEvent e) {
			System.out.println("DEBUG: ComponentEvent!");
			possiblyDisposeOfTipWindow();
		}

		public void install(JTextArea textArea) {
			textArea.addCaretListener(this);
			textArea.addComponentListener(this);
			textArea.addFocusListener(this);
			textArea.addKeyListener(this);
			textArea.addMouseListener(this);
			textArea.addMouseMotionListener(this);
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
				possiblyDisposeOfTipWindow();
			}
			else if (e.getKeyCode()==KeyEvent.VK_F2) {
System.out.println("here - " + tipWindow.getFocusableWindowState());
				if (tipWindow!=null && !tipWindow.getFocusableWindowState()) {
					tipWindow.actionPerformed(null);
					e.consume(); // Don't do bookmarking stuff
					System.out.println("hi!");
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			// possiblyDisposeOfTipWindow();
		}

		public void mouseMoved(MouseEvent e) {
			if (tipVisibleBounds==null ||
					!tipVisibleBounds.contains(e.getPoint())) {
				possiblyDisposeOfTipWindow();
			}
		}

		public void uninstall() {
			textArea.removeCaretListener(this);
			textArea.removeComponentListener(this);
			textArea.removeFocusListener(this);
			textArea.removeKeyListener(this);
			textArea.removeMouseListener(this);
			textArea.removeMouseMotionListener(this);
		}

	}

}