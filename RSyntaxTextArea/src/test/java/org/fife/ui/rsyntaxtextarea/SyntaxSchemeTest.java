/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Unit tests for the {@link SyntaxScheme} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SyntaxSchemeTest {


	@Test
	void testClone() {

		SyntaxScheme scheme = new SyntaxScheme(true);
		SyntaxScheme scheme2 = (SyntaxScheme)scheme.clone();

		Assertions.assertEquals(scheme, scheme2);
	}


	@Test
	void testGetSetStyle() {

		SyntaxScheme scheme = new SyntaxScheme(true);

		Assertions.assertNotNull(scheme.getStyle(TokenTypes.COMMENT_EOL));
		Style style = new Style(Color.RED, Color.BLUE);

		scheme.setStyle(TokenTypes.COMMENT_EOL, style);
		Style style2 = scheme.getStyle(TokenTypes.COMMENT_EOL);
		Assertions.assertEquals(style, style2);
	}


	@Test
	void testGetStylesAndGetStyleCount() {
		SyntaxScheme scheme = new SyntaxScheme(true);
		Assertions.assertEquals(scheme.getStyleCount(), scheme.getStyles().length);
	}


	@Test
	void testGetHashCode() {
		Assertions.assertNotEquals(0, new SyntaxScheme(true).hashCode());
	}


	@Test
	void testChangeBaseFont_differentFont() {

		// Create a syntax scheme with multiple different font families
		Font origBaseFont = new Font(Font.SERIF, Font.PLAIN, 10);
		SyntaxScheme scheme = new SyntaxScheme(origBaseFont);
		scheme.getStyle(TokenTypes.COMMENT_MULTILINE).font = origBaseFont.
			deriveFont(Font.BOLD | Font.ITALIC);
		scheme.getStyle(TokenTypes.COMMENT_EOL).font = new Font(
			Font.SANS_SERIF, Font.PLAIN, 10);

		Font newBaseFont = new Font(Font.MONOSPACED, Font.ITALIC, 12);
		scheme.changeBaseFont(origBaseFont, newBaseFont);

		for (int i = 0; i < scheme.getStyleCount(); i++) {
			Style style = scheme.getStyle(i);

			// Token styles using the old base font's family are updated
			if (i != TokenTypes.COMMENT_EOL && style.font != null) {
				Assertions.assertEquals(newBaseFont.getFamily(), style.font.getFamily());
			}

			// Token styles using other fonts aren't
			if (i == TokenTypes.COMMENT_EOL) {
				Assertions.assertEquals(Font.SANS_SERIF, style.font.getFamily());
			}
		}
	}


	@Test
	void testChangeBaseFont_sameFontDifferentAttributes() {

		// Create a syntax scheme with no fonts with ligature support
		Font origBaseFont = new Font(Font.SERIF, Font.PLAIN, 10);
		SyntaxScheme scheme = new SyntaxScheme(origBaseFont);
		scheme.getStyle(TokenTypes.COMMENT_MULTILINE).font = origBaseFont.
			deriveFont(Font.BOLD | Font.ITALIC);

		// New font is same as old font, but with ligature support
		Map<TextAttribute, Object> extraAttrs = new HashMap<>();
		extraAttrs.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		extraAttrs.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
		Font newBaseFont = origBaseFont.deriveFont(extraAttrs);
		scheme.changeBaseFont(origBaseFont, newBaseFont);

		for (int i = 0; i < scheme.getStyleCount(); i++) {
			Style style = scheme.getStyle(i);
			if (style.font != null) {
				Assertions.assertEquals(newBaseFont.getFamily(), style.font.getFamily());
				Map<TextAttribute, ?> attrs = style.font.getAttributes();
				Assertions.assertEquals(TextAttribute.KERNING_ON, attrs.get(TextAttribute.KERNING));
				Assertions.assertEquals(TextAttribute.LIGATURES_ON, attrs.get(TextAttribute.LIGATURES));
			}
		}
	}


	@Test
	void testCreateAndLoadFromString() {

		SyntaxScheme scheme = new SyntaxScheme(true);
		String string = scheme.toCommaSeparatedString();

		SyntaxScheme scheme2 = SyntaxScheme.loadFromString(string);
		Assertions.assertEquals(scheme, scheme2);
	}


	@Test
	void testEquals_differentSchemes() {
		SyntaxScheme scheme1 = new SyntaxScheme(true);
		SyntaxScheme scheme2 = new SyntaxScheme(false);
		Assertions.assertFalse(scheme1.equals(scheme2));
	}


	@Test
	void testEquals_equivalentSchemes() {
		SyntaxScheme scheme = new SyntaxScheme(true);
		Assertions.assertTrue(scheme.equals(scheme.clone()));
	}


	@Test
	void testEquals_otherNotSyntaxScheme() {
		SyntaxScheme scheme = new SyntaxScheme(true);
		Assertions.assertFalse(scheme.equals(null));
		Assertions.assertNotEquals("hello", scheme);
	}


	@Test
	void testLoad_fontSupplied() throws IOException {

		String xml = "<scheme>\n" +
				"<style token='COMMENT_EOL' fg='$000000' bg='$f0f0f0' bold='true' italic='true' underline='true'/>\n" +
			"</scheme>";

		Font baseFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);

		InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		SyntaxScheme scheme = SyntaxScheme.load(baseFont, in);

		// All tokens use the base font, possibly with a different style
		for (Style style : scheme.getStyles()) {
			if (style.font != null) {
				Assertions.assertEquals(baseFont.getFamily(), style.font.getFamily());
				Assertions.assertEquals(baseFont.getSize(), style.font.getSize());
			}
		}

		// Customizations for EOL comment tokens are right
		Style style = scheme.getStyle(TokenTypes.COMMENT_EOL);
		Assertions.assertEquals(Color.BLACK, style.foreground);
		Assertions.assertEquals(new Color(0xf0f0f0), style.background);
		Assertions.assertEquals(Font.BOLD | Font.ITALIC, style.font.getStyle());
		Assertions.assertTrue(style.underline);
	}


	@Test
	void testLoad_fontSupplied_extraAttributes() throws IOException {

		String xml = "<scheme>\n" +
			"<style token='COMMENT_EOL' fg='$000000' bg='$f0f0f0' bold='true' italic='true' underline='true'/>\n" +
			"</scheme>";

		Font baseFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
		Map<TextAttribute, Object> extraAttrs = new HashMap<>();
		extraAttrs.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		extraAttrs.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
		baseFont = baseFont.deriveFont(extraAttrs);

		InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		SyntaxScheme scheme = SyntaxScheme.load(baseFont, in);

		// All tokens have the base font's extra attributes
		for (Style style : scheme.getStyles()) {
			if (style.font != null) {
				Assertions.assertEquals(baseFont.getFamily(), style.font.getFamily());
				Assertions.assertEquals(baseFont.getSize(), style.font.getSize());
				Map<TextAttribute, ?> attrs = style.font.getAttributes();
				Assertions.assertEquals(TextAttribute.KERNING_ON, attrs.get(TextAttribute.KERNING));
				Assertions.assertEquals(TextAttribute.LIGATURES_ON, attrs.get(TextAttribute.LIGATURES));
			}
		}
	}


	@Test
	void testLoad_noFontSupplied() throws IOException {

		String xml = "<scheme>\n" +
			"<style token='COMMENT_EOL' fg='$000000' bg='$f0f0f0' bold='true' italic='true' underline='true'/>\n" +
			"</scheme>";

		InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		SyntaxScheme scheme = SyntaxScheme.load(null, in);

		Font expectedDefaultFont = RSyntaxTextArea.getDefaultFont();

		// All tokens use the base font, possibly with a different style
		for (Style style : scheme.getStyles()) {
			if (style.font != null) {
				Assertions.assertEquals(expectedDefaultFont.getFamily(), style.font.getFamily());
				Assertions.assertEquals(expectedDefaultFont.getSize(), style.font.getSize());
			}
		}

		// Customizations for EOL comment tokens are right
		Style style = scheme.getStyle(TokenTypes.COMMENT_EOL);
		Assertions.assertEquals(Color.BLACK, style.foreground);
		Assertions.assertEquals(new Color(0xf0f0f0), style.background);
		Assertions.assertEquals(Font.BOLD | Font.ITALIC, style.font.getStyle());
		Assertions.assertTrue(style.underline);
	}
}
