/*
 * 08/13/2009
 *
 * TipUtil.java - Utility methods for homemade tool tips.
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

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemColor;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.html.HTMLDocument;


/**
 * Static utility methods for focusable tips.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class TipUtil {


	private TipUtil() {
	}


	/**
	 * Returns the screen coordinates for the monitor that contains the
	 * specified point.  This is useful for setups with multiple monitors,
	 * to ensure that popup windows are positioned properly.
	 *
	 * @param x The x-coordinate, in screen coordinates.
	 * @param y The y-coordinate, in screen coordinates.
	 * @return The bounds of the monitor that contains the specified point.
	 */
	public static Rectangle getScreenBoundsForPoint(int x, int y) {
		GraphicsEnvironment env = GraphicsEnvironment.
										getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		for (int i=0; i<devices.length; i++) {
			GraphicsConfiguration[] configs = devices[i].getConfigurations();
			for (int j=0; j<configs.length; j++) {
				Rectangle gcBounds = configs[j].getBounds();
				if (gcBounds.contains(x, y)) {
					return gcBounds;
				}
			}
		}
		// If point is outside all monitors, default to default monitor (?)
		return env.getMaximumWindowBounds();
	}


	/**
	 * Returns the default background color to use for tool tip windows.
	 *
	 * @return The default background color.
	 */
	public static Color getToolTipBackground() {

		Color c = UIManager.getColor("ToolTip.background");

		// Tooltip.background is wrong color on Nimbus (!)
		if (c==null || UIManager.getLookAndFeel().getName().equals("Nimbus")) {
			c = UIManager.getColor("info"); // Used by Nimbus (and others)
			if (c==null) {
				c = SystemColor.info; // System default
			}
		}

		// Workaround for a bug (?) with Nimbus - calling JLabel.setBackground()
		// with a ColorUIResource does nothing, must be a normal Color
		if (c instanceof ColorUIResource) {
			c = new Color(c.getRGB());
		}

		return c;

	}


	/**
	 * Tweaks a <code>JEditorPane</code> so it can be used to render the
	 * content in a focusable pseudo-tool tip.  It is assumed that the editor
	 * pane is using an <code>HTMLDocument</code>.
	 *
	 * @param textArea The editor pane to tweak.
	 */
	public static void tweakTipEditorPane(JEditorPane textArea) {

		// Jump through a few hoops to get things looking nice in Nimbus
		if (UIManager.getLookAndFeel().getName().equals("Nimbus")) {
			Color selBG = textArea.getSelectionColor();
			Color selFG = textArea.getSelectedTextColor();
			textArea.setUI(new javax.swing.plaf.basic.BasicEditorPaneUI());
			textArea.setSelectedTextColor(selFG);
			textArea.setSelectionColor(selBG);
		}

		textArea.setEditable(false); // Required for links to work!
		textArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// Make selection visible even though we are not (initially) focusable.
		textArea.getCaret().setSelectionVisible(true);

		// Make it use the "tool tip" background color.
		textArea.setBackground(TipUtil.getToolTipBackground());

		// Force JEditorPane to use a certain font even in HTML.
		// All standard LookAndFeels, even Nimbus (!), define Label.font.
		Font font = UIManager.getFont("Label.font");
		if (font == null) { // Try to make a sensible default
			font = new Font("SansSerif", Font.PLAIN, 12);
		}
		HTMLDocument doc = (HTMLDocument) textArea.getDocument();
		doc.getStyleSheet().addRule(
				"body { font-family: " + font.getFamily() + "; font-size: "
						+ font.getSize() + "pt; }");

	}


}