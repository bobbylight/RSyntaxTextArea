/*
 * 12/23/2008
 *
 * SizeGrip.java - A size grip component that sits at the bottom of the window,
 * allowing the user to easily resize that window.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;


/**
 * A component that allows its parent window to be resizable, similar to the
 * size grip seen on status bars.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@SuppressWarnings("checkstyle:magicnumber")
class SizeGrip extends JPanel {


	SizeGrip() {
		MouseHandler adapter = new MouseHandler();
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
		setPreferredSize(new Dimension(16, 16));
	}


	/**
	 * Overridden to ensure that the cursor for this component is appropriate
	 * for the orientation.
	 *
	 * @param o The new orientation.
	 */
	@Override
	public void applyComponentOrientation(ComponentOrientation o) {
		possiblyFixCursor(o.isLeftToRight());
		super.applyComponentOrientation(o);
	}


	/**
	 * Paints this panel.
	 *
	 * @param g The graphics context.
	 */
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Dimension dim = getSize();

		Color c1 = UIManager.getColor("Label.disabledShadow");
		Color c2 = UIManager.getColor("Label.disabledForeground");
		ComponentOrientation orientation = getComponentOrientation();

		if (orientation.isLeftToRight()) {
			int width = dim.width  -= 3;
			int height = dim.height -= 3;
			g.setColor(c1);
			g.fillRect(width-9,height-1, 3,3);
			g.fillRect(width-5,height-1, 3,3);
			g.fillRect(width-1,height-1, 3,3);
			g.fillRect(width-5,height-5, 3,3);
			g.fillRect(width-1,height-5, 3,3);
			g.fillRect(width-1,height-9, 3,3);
			g.setColor(c2);
			g.fillRect(width-9,height-1, 2,2);
			g.fillRect(width-5,height-1, 2,2);
			g.fillRect(width-1,height-1, 2,2);
			g.fillRect(width-5,height-5, 2,2);
			g.fillRect(width-1,height-5, 2,2);
			g.fillRect(width-1,height-9, 2,2);
		}
		else {
			int height = dim.height -= 3;
			g.setColor(c1);
			g.fillRect(10,height-1, 3,3);
			g.fillRect(6,height-1, 3,3);
			g.fillRect(2,height-1, 3,3);
			g.fillRect(6,height-5, 3,3);
			g.fillRect(2,height-5, 3,3);
			g.fillRect(2,height-9, 3,3);
			g.setColor(c2);
			g.fillRect(10,height-1, 2,2);
			g.fillRect(6,height-1, 2,2);
			g.fillRect(2,height-1, 2,2);
			g.fillRect(6,height-5, 2,2);
			g.fillRect(2,height-5, 2,2);
			g.fillRect(2,height-9, 2,2);
		}

	}


	/**
	 * Ensures that the cursor for this component is appropriate for the
	 * orientation.
	 *
	 * @param ltr Whether the current component orientation is LTR.
	 */
	private void possiblyFixCursor(boolean ltr) {
		int cursor = Cursor.NE_RESIZE_CURSOR;
		if (ltr) {
			cursor = Cursor.NW_RESIZE_CURSOR;
		}
		if (cursor!=getCursor().getType()) {
			setCursor(Cursor.getPredefinedCursor(cursor));
		}
	}


	/**
	 * Listens for mouse events on this panel and resizes the parent window
	 * appropriately.
	 */
	private final class MouseHandler extends MouseInputAdapter {

		/*
		 * NOTE: We use SwingUtilities.convertPointToScreen() instead of just using
		 * the locations relative to the corner component because the latter proved
		 * buggy - stretch the window too wide and some kind of arithmetic error
		 * started happening somewhere - our window would grow way too large.
		 */

		private Point origPos;

		@Override
		public void mouseDragged(MouseEvent e) {
			Point newPos = e.getPoint();
			SwingUtilities.convertPointToScreen(newPos, SizeGrip.this);
			int xDelta = newPos.x - origPos.x;
			int yDelta = newPos.y - origPos.y;
			Window wind = SwingUtilities.getWindowAncestor(SizeGrip.this);
			if (wind!=null) { // Should always be true
				if (getComponentOrientation().isLeftToRight()) {
					int w = wind.getWidth();
					if (newPos.x>=wind.getX()) {
						w += xDelta;
					}
					int h = wind.getHeight();
					if (newPos.y>=wind.getY()) {
						h += yDelta;
					}
					wind.setSize(w,h);
				}
				else { // RTL
					int newW = Math.max(1, wind.getWidth()-xDelta);
					int newH = Math.max(1, wind.getHeight()+yDelta);
					wind.setBounds(newPos.x, wind.getY(), newW, newH);
				}
				// invalidate()/validate() needed pre-1.6.
				wind.invalidate();
				wind.validate();
			}
			origPos.setLocation(newPos);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			origPos = e.getPoint();
			SwingUtilities.convertPointToScreen(origPos, SizeGrip.this);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			origPos = null;
		}

	}


}
