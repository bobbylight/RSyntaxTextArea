/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.rsyntaxtextarea.OS;

import javax.swing.*;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility methods related to fonts. Using these methods is preferred over the {@code new Font()} constructor as they
 * ensure fallback fonts are properly configured for glyphs unsupported by the primary font.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class FontUtil {

	/**
	 * If a KV pair with this key is defined in the UI defaults as a Font,
	 * that font will be returned by {@link #getDefaultMonospacedFont()}. Note
	 * that if you use this method, you should consider using {@link #createFont(String, int, int)}
	 * to ensure it has a fallback for glyphs it doesn't support.
	 */
	public static final String DEFAULT_FONT_KEY = "org.fife.ui.rtextarea.defaultFont";


	/**
	 * Private constructor to prevent instantiation.
	 */
	private FontUtil() {
		// Private constructor to prevent instantiation.
	}


	/**
	 * Creates and returns a font with a default fallback font configured.
	 *
	 * @param family The font family.
	 * @param style The font style.
	 * @param size The font size.
	 * @return The font.
	 */
	public static Font createFont(String family, int style, int size) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		return sc.getFont(family, style, size);
	}


	/**
	 * Returns a new font with all properties the same as a base font, but
	 * with a new font family. This saves a few lines of code around creating
	 * a {@code Map}, since deriving a font with a new family can be
	 * tricky to do while avoiding overwriting all other
	 * {@code TextAttributes}.
	 *
	 * @param baseFont The font to start with.
	 * @param newFamily The new font family, or {@code null} for no change.
	 * @return The new font.
	 * @see #deriveFont(Font, String, Integer, Number)
	 */
	public static Font deriveFont(Font baseFont, String newFamily) {
		return deriveFont(baseFont, newFamily, null, null);
	}


	/**
	 * Returns a new font with all properties the same as a base font, but
	 * with optional changes. This saves a few lines of code around creating
	 * a <code>Map</code>, since deriving a font with a new family can be
	 * tricky to do while avoiding overwriting all other
	 * <code>TextAttributes</code>.
	 *
	 * @param baseFont The font to start with.
	 * @param newFamily The new font family, or {@code null} for no change.
	 * @param newStyle The new font style, or {@code null} for no change.
	 * @param newSize The new font size, or {@code null} for no change.
	 * @return The new font.
	 * @see #deriveFont(Font, String)
	 */
	public static Font deriveFont(Font baseFont, String newFamily,
								  Integer newStyle, Number newSize) {
		Map<TextAttribute, Object> newAttrs = new HashMap<>();
		if (newFamily != null) {
			newAttrs.put(TextAttribute.FAMILY, newFamily);
		}
		if (newSize != null) {
			newAttrs.put(TextAttribute.SIZE, newSize);
		}
		Font font = newAttrs.isEmpty() ? baseFont : baseFont.deriveFont(newAttrs);
		return newStyle == null ? font : font.deriveFont(newStyle);
	}


	/**
	 * Returns the default monospaced font for this operating system.
	 *
	 * @return The default font.
	 */
	public static Font getDefaultMonospacedFont() {

		Font font = UIManager.getFont(DEFAULT_FONT_KEY);
		if (font != null) {
			return font;
		}

		OS os = OS.get();

		if (os == OS.MAC_OS_X) {
			return getDefaultMonospaceFontMacOS();
		}
		else if (os == OS.WINDOWS) {
			return getDefaultMonospaceFontWindows();
		}
		else if (os == OS.LINUX) {
			return getDefaultMonospaceFontLinux();
		}

		return getDefaultMonospaceFontOther();
	}


	private static Font getDefaultMonospaceFontLinux() {

		Font font = FontUtil.createFont("Ubuntu Mono", Font.PLAIN, 12);

		if (!"Ubuntu Mono".equals(font.getFamily())) {
			font = FontUtil.createFont("DejaVu Sans Mono", Font.PLAIN, 12);
			if (!"DejaVu Sans Mono".equals(font.getFamily())) {
				font = getDefaultMonospaceFontOther();
			}
		}

		return font;
	}


	private static Font getDefaultMonospaceFontMacOS() {

		// Snow Leopard (1.6) uses Menlo as default monospaced font,
		// pre-Snow Leopard used Monaco.
		Font font = FontUtil.createFont("Menlo", Font.PLAIN, 12);

		if (!"Menlo".equals(font.getFamily())) {
			font = FontUtil.createFont("Monaco", Font.PLAIN, 12);
			if (!"Monaco".equals(font.getFamily())) {
				font = getDefaultMonospaceFontOther();
			}
		}

		return font;
	}


	private static Font getDefaultMonospaceFontOther() {
		return FontUtil.createFont(Font.MONOSPACED, Font.PLAIN, 13);
	}


	private static Font getDefaultMonospaceFontWindows() {

		// Cascadia Code was added in later Windows 10/11, default in VS
		// and VS Code. Consolas was added in Vista, used in older VS.
		Font font = FontUtil.createFont("Cascadia Code", Font.PLAIN, 13);

		if (!"Cascadia Code".equals(font.getFamily())) {
			font = FontUtil.createFont("Consolas", Font.PLAIN, 13);
			if (!"Consolas".equals(font.getFamily())) {
				font = getDefaultMonospaceFontOther();
			}
		}

		return font;
	}
}
