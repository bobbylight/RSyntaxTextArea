/*
 * 08/13/2009
 *
 * TipUtil.java - Utility methods for homemade tool tips.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
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
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.html.HTMLDocument;

import org.fife.ui.rsyntaxtextarea.HtmlUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rtextarea.RTextArea;


/**
 * Static utility methods for focusable tips.  Many of these methods
 * are useful when you want to make a popup {@code JWindow} look like
 * a tool tip.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class TipUtil {

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
		for (GraphicsDevice device : devices) {
			GraphicsConfiguration[] configs = device.getConfigurations();
			for (GraphicsConfiguration config : configs) {
				Rectangle gcBounds = config.getBounds();
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
	 * @see #getToolTipBackground(RTextArea)
	 * @see #getToolTipBorder()
	 */
	public static Color getToolTipBackground() {
		return getToolTipBackground(null);
	}


	/**
	 * Returns the default background color to use for tool tip windows.
	 *
	 * @param textArea The text area that will be the parent component of
	 *         the tool tip.  If this is non-{@code null}, its background
	 *         color is taken into consideration when determining the color
	 *         to return (it will match the editor's background color if
	 *         necessary to facilitate proper contrast for tool tips rendering
	 *         code).  If this is {@code null}, the tool tip background for
	 *         the current Look and Feel will be returned.
	 * @return The default background color.
	 * @see #getToolTipBackground()
	 * @see #getToolTipBorder(RTextArea)
	 */
	public static Color getToolTipBackground(RTextArea textArea) {

		// If the parent component is a text area, we assume the tool tip will
		// display text area content.  In this case, use its background to
		// ensure contrast between foreground and background.
		// The only exception here is if the background of the editor is white,
		// in which case we still use the default tool tip color (e.g. yellow
		// on Windows) since contrast there is high enough and it looks a little
		// more "native"
		if (textArea != null && !Color.WHITE.equals(textArea.getBackground())) {
			return textArea.getBackground();
		}

		Color c = UIManager.getColor("ToolTip.background");

		// Tooltip.background is wrong color on Nimbus (!)
		boolean isNimbus = isNimbusLookAndFeel();
		if (c==null || isNimbus) {
			c = UIManager.getColor("info"); // Used by Nimbus (and others)
			if (c==null || (isNimbus && isDerivedColor(c))) {
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
	 * Returns the border used by tool tips in this look and feel.
	 *
	 * @return The border.
	 * @see #getToolTipBorder(RTextArea)
	 * @see #getToolTipBackground()
	 */
	public static Border getToolTipBorder() {
		return getToolTipBorder(null);
	}


	/**
	 * Returns the border used by tool tips in this look and feel.
	 *
	 * @param textArea The text area that will be the parent component of
	 *         the tool tip.  If this is non-{@code null}, its background
	 *         color is taken into consideration when determining the color
	 *         to return (it will coordinate with the editor's background
	 *         color if necessary to facilitate proper contrast for tool
	 *         tips rendering code).  If this is {@code null}, the tool
	 *         tip background for the current Look and Feel will be returned.
	 * @return The border.
	 * @see #getToolTipBorder()
	 * @see #getToolTipBackground(RTextArea)
	 */
	public static Border getToolTipBorder(RTextArea textArea) {

		// If the parent component is a text area, we assume the tool tip will
		// display text area content.  In this case, use its background to
		// ensure contrast between foreground and background.
		// The only exception here is if the background of the editor is white,
		// in which case we still use the default tool tip color (e.g. yellow
		// on Windows) since contrast there is high enough and it looks a little
		// more "native"
		if (textArea != null && !Color.WHITE.equals(textArea.getBackground())) {
			Color color = textArea.getBackground();
			if (color != null) {
				return BorderFactory.createLineBorder(color.brighter());
			}
		}

		Border border = UIManager.getBorder("ToolTip.border");

		if (border==null || isNimbusLookAndFeel()) {
			border = UIManager.getBorder("nimbusBorder");
			if (border==null) {
				border = BorderFactory.createLineBorder(SystemColor.controlDkShadow);
			}
		}

		return border;

	}


	/**
	 * Returns whether a color is a Nimbus DerivedColor, which is troublesome
	 * in that it doesn't use its RGB values (uses HSB instead?) and so
	 * querying them is useless.
	 *
	 * @param c The color to check.
	 * @return Whether it is a DerivedColor.
	 */
	private static boolean isDerivedColor(Color c) {
		return c!=null && c.getClass().getName().endsWith(".DerivedColor");
	}


	/**
	 * Returns whether the Nimbus Look and Feel is installed.
	 *
	 * @return Whether the current LAF is Nimbus.
	 */
	private static boolean isNimbusLookAndFeel() {
		return UIManager.getLookAndFeel().getName().equals("Nimbus");
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
		boolean isNimbus = isNimbusLookAndFeel();
		if (isNimbus) {
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

		// Set the foreground color.  Important because when rendering HTML,
		// default foreground becomes black, which may not match all LAF's
		// (e.g. Substance).
		Color fg = UIManager.getColor("Label.foreground");
		if (fg==null || (isNimbus && isDerivedColor(fg))) {
			fg = SystemColor.textText;
		}
		textArea.setForeground(fg);

		// Make it use the "tool tip" background color.
		textArea.setBackground(TipUtil.getToolTipBackground());

		// Force JEditorPane to use a certain font even in HTML.
		// All standard LookAndFeels, even Nimbus (!), define Label.font.
		Font font = UIManager.getFont("Label.font");
		if (font == null) { // Try to make a sensible default
			font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		}
		HTMLDocument doc = (HTMLDocument) textArea.getDocument();
		setFont(doc, font, fg);

		// Always add link foreground rule.  Unfortunately these CSS rules
		// stack each time the LaF is changed (how can we overwrite them
		// without clearing out the important "standard" ones?).
		Color linkFG = RSyntaxUtilities.getHyperlinkForeground();
		doc.getStyleSheet().addRule(
				"a { color: " + HtmlUtil.getHexString(linkFG) + "; }");

	}


	/**
	 * Sets the default font for an HTML document (e.g., in a tool tip
	 * displaying HTML).  This is here because when rendering HTML,
	 * {@code setFont()} is not honored.
	 *
	 * @param doc The document to modify.
	 * @param font The font to use.
	 * @param fg The default foreground color.
	 */
	private static void setFont(HTMLDocument doc, Font font, Color fg) {
		doc.getStyleSheet().addRule(
				"body { font-family: " + font.getFamily() +
						"; font-size: " + font.getSize() + "pt" +
						"; color: " + HtmlUtil.getHexString(fg) + "; }");
	}


}
