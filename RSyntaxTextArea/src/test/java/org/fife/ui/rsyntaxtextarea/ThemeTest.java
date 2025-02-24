/*
 * 02/15/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rtextarea.FontUtil;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for {@link Theme}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ThemeTest {


	/**
	 * Asserts that all properties set by a {@link Theme} are different
	 * between one text area/gutter pair and another.
	 */
	private void assertAllThemePropertiesDifferent(RSyntaxTextArea textArea1,
			Gutter gutter1, RSyntaxTextArea textArea2, Gutter gutter2) {

		Assertions.assertNotEquals(textArea1.getFont(), textArea2.getFont());
		Assertions.assertNotEquals(textArea1.getSyntaxScheme(), textArea2.getSyntaxScheme());
		Assertions.assertNotEquals(textArea1.getBackground(), textArea2.getBackground());
		Assertions.assertNotEquals(textArea1.getCaretColor(), textArea2.getCaretColor());
		Assertions.assertNotEquals(textArea1.getUseSelectedTextColor(), textArea2.getUseSelectedTextColor());
		Assertions.assertNotEquals(textArea1.getSelectedTextColor(), textArea2.getSelectedTextColor());
		Assertions.assertNotEquals(textArea1.getSelectionColor(), textArea2.getSelectionColor());
		Assertions.assertNotEquals(textArea1.getRoundedSelectionEdges(), textArea2.getRoundedSelectionEdges());
		Assertions.assertNotEquals(textArea1.getCurrentLineHighlightColor(), textArea2.getCurrentLineHighlightColor());
		Assertions.assertNotEquals(textArea1.getFadeCurrentLineHighlight(), textArea2.getFadeCurrentLineHighlight());
		Assertions.assertNotEquals(textArea1.getTabLineColor(), textArea2.getTabLineColor());
		Assertions.assertNotEquals(textArea1.getMarginLineColor(), textArea2.getMarginLineColor());
		Assertions.assertNotEquals(textArea1.getMarkAllHighlightColor(), textArea2.getMarkAllHighlightColor());
		Assertions.assertNotEquals(textArea1.getMarkOccurrencesColor(), textArea2.getMarkOccurrencesColor());
		Assertions.assertNotEquals(textArea1.getPaintMarkOccurrencesBorder(),
			textArea2.getPaintMarkOccurrencesBorder());
		Assertions.assertNotEquals(textArea1.getMatchedBracketBGColor(), textArea2.getMatchedBracketBGColor());
		Assertions.assertNotEquals(textArea1.getMatchedBracketBorderColor(), textArea2.getMatchedBracketBorderColor());
		Assertions.assertNotEquals(textArea1.getPaintMatchedBracketPair(), textArea2.getPaintMatchedBracketPair());
		Assertions.assertNotEquals(textArea1.getAnimateBracketMatching(), textArea2.getAnimateBracketMatching());
		Assertions.assertNotEquals(textArea1.getHyperlinkForeground(), textArea2.getHyperlinkForeground());
		for (int i=0; i<textArea1.getSecondaryLanguageCount(); i++) {
			Assertions.assertNotEquals(textArea1.getSecondaryLanguageBackground(i+1),
					textArea2.getSecondaryLanguageBackground(i+1));
		}
		Assertions.assertNotEquals(gutter1.getBackground(), gutter2.getBackground());
		Assertions.assertNotEquals(gutter1.getBorderColor(), gutter2.getBorderColor());
		Assertions.assertNotEquals(gutter1.getActiveLineRangeColor(), gutter2.getActiveLineRangeColor());
		Assertions.assertNotEquals(gutter1.getIconRowHeaderInheritsGutterBackground(),
			gutter2.getIconRowHeaderInheritsGutterBackground());
		Assertions.assertNotEquals(gutter1.getLineNumberColor(), gutter2.getLineNumberColor());
		Assertions.assertNotEquals(gutter1.getLineNumberFont(), gutter2.getLineNumberFont());
		Assertions.assertNotEquals(gutter1.getCurrentLineNumberColor(), gutter2.getCurrentLineNumberColor());
		Assertions.assertNotEquals(gutter1.getFoldIndicatorForeground(), gutter2.getFoldIndicatorForeground());
		Assertions.assertNotEquals(gutter1.getFoldBackground(), gutter2.getFoldBackground());

	}


	/**
	 * Asserts whether a text area and gutter match the styles defined in
	 * <code>ThemeTest_theme1.xml</code>.
	 */
	private void assertColorsMatchTheme1(RSyntaxTextArea textArea,
			Gutter gutter) {

		Assertions.assertEquals(Color.red, textArea.getBackground());
		Assertions.assertEquals(Color.red, textArea.getCaretColor());
		Assertions.assertFalse(textArea.getUseSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectionColor());
		Assertions.assertTrue(textArea.getRoundedSelectionEdges());
		Assertions.assertEquals(Color.red, textArea.getCurrentLineHighlightColor());
		Assertions.assertTrue(textArea.getFadeCurrentLineHighlight());
		Assertions.assertEquals(Color.red, textArea.getTabLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarginLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarkAllHighlightColor());
		Assertions.assertEquals(Color.red, textArea.getMarkOccurrencesColor());
		Assertions.assertTrue(textArea.getPaintMarkOccurrencesBorder());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBGColor());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBorderColor());
		Assertions.assertTrue(textArea.getPaintMatchedBracketPair());
		Assertions.assertTrue(textArea.getAnimateBracketMatching());
		Assertions.assertEquals(Color.red, textArea.getHyperlinkForeground());
		for (int i=0; i<textArea.getSecondaryLanguageCount(); i++) {
			Color expected = i==TokenTypes.IDENTIFIER ? Color.blue : Color.red;
			Assertions.assertEquals(expected, textArea.getSecondaryLanguageBackground(i+1));
		}

		Assertions.assertEquals(Color.red, gutter.getBackground());
		Assertions.assertEquals(Color.red, gutter.getBorderColor());
		Assertions.assertEquals(Color.red, gutter.getActiveLineRangeColor());
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());
		Assertions.assertEquals(Color.red, gutter.getLineNumberColor());
		Assertions.assertEquals(Color.blue, gutter.getCurrentLineNumberColor());
		//Assertions.assertEquals("Arial",  gutter.getLineNumberFont().getFamily()); // Arial not on CI build servers
		Assertions.assertEquals(22,        gutter.getLineNumberFont().getSize());
		Assertions.assertEquals(Color.red, gutter.getFoldIndicatorForeground());
		Assertions.assertEquals(Color.red, gutter.getFoldBackground());
		Assertions.assertEquals(Color.green, gutter.getArmedFoldBackground());

	}


	/**
	 * Asserts whether a text area and gutter match the styles defined in
	 * <code>ThemeTest_theme1_noLineNumbers_currentFG.xml</code>.
	 */
	private void assertColorsMatchTheme1_noLineNumbers_currentFG(
			RSyntaxTextArea textArea, Gutter gutter) {

		Assertions.assertEquals(Color.red, textArea.getBackground());
		Assertions.assertEquals(Color.red, textArea.getCaretColor());
		Assertions.assertFalse(textArea.getUseSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectionColor());
		Assertions.assertTrue(textArea.getRoundedSelectionEdges());
		Assertions.assertEquals(Color.red, textArea.getCurrentLineHighlightColor());
		Assertions.assertTrue(textArea.getFadeCurrentLineHighlight());
		Assertions.assertEquals(Color.red, textArea.getTabLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarginLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarkAllHighlightColor());
		Assertions.assertEquals(Color.red, textArea.getMarkOccurrencesColor());
		Assertions.assertTrue(textArea.getPaintMarkOccurrencesBorder());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBGColor());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBorderColor());
		Assertions.assertTrue(textArea.getPaintMatchedBracketPair());
		Assertions.assertTrue(textArea.getAnimateBracketMatching());
		Assertions.assertEquals(Color.red, textArea.getHyperlinkForeground());
		for (int i=0; i<textArea.getSecondaryLanguageCount(); i++) {
			Color expected = i==TokenTypes.IDENTIFIER ? Color.blue : Color.red;
			Assertions.assertEquals(expected, textArea.getSecondaryLanguageBackground(i+1));
		}

		Assertions.assertEquals(Color.red, gutter.getBackground());
		Assertions.assertEquals(Color.red, gutter.getBorderColor());
		Assertions.assertEquals(Color.red, gutter.getActiveLineRangeColor());
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());
		Assertions.assertEquals(Color.red, gutter.getLineNumberColor());
		Assertions.assertNull(gutter.getCurrentLineNumberColor());
		//Assertions.assertEquals("Arial",  gutter.getLineNumberFont().getFamily()); // Arial not on CI build servers
		Assertions.assertEquals(22,        gutter.getLineNumberFont().getSize());
		Assertions.assertEquals(Color.red, gutter.getFoldIndicatorForeground());
		Assertions.assertEquals(Color.red, gutter.getFoldBackground());
		Assertions.assertEquals(Color.green, gutter.getArmedFoldBackground());

	}


	/**
	 * Asserts whether a text area and gutter match the styles defined in
	 * <code>ThemeTest_theme1_noArmedBG.xml</code>.
	 */
	private void assertColorsMatchTheme1_noArmedBG(RSyntaxTextArea textArea,
										 Gutter gutter) {

		Assertions.assertEquals(Color.red, textArea.getBackground());
		Assertions.assertEquals(Color.red, textArea.getCaretColor());
		Assertions.assertFalse(textArea.getUseSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectedTextColor());
		Assertions.assertEquals(Color.red, textArea.getSelectionColor());
		Assertions.assertTrue(textArea.getRoundedSelectionEdges());
		Assertions.assertEquals(Color.red, textArea.getCurrentLineHighlightColor());
		Assertions.assertTrue(textArea.getFadeCurrentLineHighlight());
		Assertions.assertEquals(Color.red, textArea.getTabLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarginLineColor());
		Assertions.assertEquals(Color.red, textArea.getMarkAllHighlightColor());
		Assertions.assertEquals(Color.red, textArea.getMarkOccurrencesColor());
		Assertions.assertTrue(textArea.getPaintMarkOccurrencesBorder());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBGColor());
		Assertions.assertEquals(Color.red, textArea.getMatchedBracketBorderColor());
		Assertions.assertTrue(textArea.getPaintMatchedBracketPair());
		Assertions.assertTrue(textArea.getAnimateBracketMatching());
		Assertions.assertEquals(Color.red, textArea.getHyperlinkForeground());
		for (int i=0; i<textArea.getSecondaryLanguageCount(); i++) {
			Color expected = i==TokenTypes.IDENTIFIER ? Color.blue : Color.red;
			Assertions.assertEquals(expected, textArea.getSecondaryLanguageBackground(i+1));
		}

		Assertions.assertEquals(Color.red, gutter.getBackground());
		Assertions.assertEquals(Color.red, gutter.getBorderColor());
		Assertions.assertEquals(Color.red, gutter.getActiveLineRangeColor());
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());
		Assertions.assertEquals(Color.red, gutter.getLineNumberColor());
		Assertions.assertEquals(Color.blue, gutter.getCurrentLineNumberColor());
		//Assertions.assertEquals("Arial",  gutter.getLineNumberFont().getFamily()); // Arial not on CI build servers
		Assertions.assertEquals(22,        gutter.getLineNumberFont().getSize());
		Assertions.assertEquals(Color.red, gutter.getFoldIndicatorForeground());
		Assertions.assertEquals(Color.red, gutter.getFoldBackground());
		// Armed fold BG defaults to regular fold BG
		Assertions.assertEquals(gutter.getFoldBackground(), gutter.getArmedFoldBackground());

	}


	/**
	 * Asserts that all properties set by a {@link Theme} are equal
	 * between one text area/gutter pair and another.
	 */
	private void assertEqualThemeProperties(RSyntaxTextArea textArea1,
			Gutter gutter1, RSyntaxTextArea textArea2, Gutter gutter2) {

		Assertions.assertEquals(textArea1.getFont(), textArea2.getFont());
		Assertions.assertEquals(textArea1.getSyntaxScheme(), textArea1.getSyntaxScheme());
		Assertions.assertEquals(textArea1.getBackground(), textArea2.getBackground());
		Assertions.assertEquals(textArea1.getCaretColor(), textArea2.getCaretColor());
		Assertions.assertEquals(textArea1.getUseSelectedTextColor(), textArea2.getUseSelectedTextColor());
		Assertions.assertEquals(textArea1.getSelectedTextColor(), textArea2.getSelectedTextColor());
		Assertions.assertEquals(textArea1.getSelectionColor(), textArea2.getSelectionColor());
		Assertions.assertEquals(textArea1.getRoundedSelectionEdges(), textArea2.getRoundedSelectionEdges());
		Assertions.assertEquals(textArea1.getCurrentLineHighlightColor(), textArea2.getCurrentLineHighlightColor());
		Assertions.assertEquals(textArea1.getFadeCurrentLineHighlight(), textArea2.getFadeCurrentLineHighlight());
		Assertions.assertEquals(textArea1.getTabLineColor(), textArea2.getTabLineColor());
		Assertions.assertEquals(textArea1.getMarginLineColor(), textArea2.getMarginLineColor());
		Assertions.assertEquals(textArea1.getMarkAllHighlightColor(), textArea2.getMarkAllHighlightColor());
		Assertions.assertEquals(textArea1.getMarkOccurrencesColor(), textArea2.getMarkOccurrencesColor());
		Assertions.assertEquals(textArea1.getPaintMarkOccurrencesBorder(), textArea2.getPaintMarkOccurrencesBorder());
		Assertions.assertEquals(textArea1.getMatchedBracketBGColor(), textArea2.getMatchedBracketBGColor());
		Assertions.assertEquals(textArea1.getMatchedBracketBorderColor(), textArea2.getMatchedBracketBorderColor());
		Assertions.assertEquals(textArea1.getPaintMatchedBracketPair(), textArea2.getPaintMatchedBracketPair());
		Assertions.assertEquals(textArea1.getAnimateBracketMatching(), textArea2.getAnimateBracketMatching());
		Assertions.assertEquals(textArea1.getHyperlinkForeground(), textArea2.getHyperlinkForeground());

		Assertions.assertEquals(gutter1.getBackground(), gutter2.getBackground());
		Assertions.assertEquals(gutter1.getBorderColor(), gutter2.getBorderColor());
		Assertions.assertEquals(gutter1.getActiveLineRangeColor(), gutter2.getActiveLineRangeColor());
		Assertions.assertEquals(gutter1.getIconRowHeaderInheritsGutterBackground(),
			gutter2.getIconRowHeaderInheritsGutterBackground());
		Assertions.assertEquals(gutter1.getLineNumberColor(), gutter2.getLineNumberColor());
		Assertions.assertEquals(gutter1.getCurrentLineNumberColor(), gutter2.getCurrentLineNumberColor());
		Assertions.assertEquals(gutter1.getLineNumberFont(), gutter2.getLineNumberFont());
		Assertions.assertEquals(gutter1.getFoldIndicatorForeground(), gutter2.getFoldIndicatorForeground());
		Assertions.assertEquals(gutter1.getFoldBackground(), gutter2.getFoldBackground());

	}


	/**
	 * Creates and returns a syntax scheme where all styles have the same
	 * font and foreground color.
	 *
	 * @param font The font for the styles.
	 * @param fg The foreground color for the styles.
	 * @return The syntax scheme.
	 */
	private SyntaxScheme createSyntaxScheme(Font font, Color fg) {
		SyntaxScheme scheme = new SyntaxScheme(true);
		for (int i=0; i<scheme.getStyleCount(); i++) {
			Style style = scheme.getStyle(i);
			if (style != null) {
				style.background = style.foreground = fg;
				style.font = font;
				if (i == 5) {
					style.underline = true;
				}
				else if (i == 6) {
					style.font = font.deriveFont(Font.BOLD, 24f);
				}
				else if (i == 7) {
					style.font = font.deriveFont(Font.ITALIC, 25f);
				}
			}
		}
		return scheme;
	}


	/**
	 * Initializes a text area and gutter pair with non-standard values for
	 * all properties loaded and saved by a <code>Theme</code>.
	 *
	 * @param textArea The text area to manipulate.
	 * @param gutter The gutter to manipulate.
	 */
	private void initWithOddProperties(RSyntaxTextArea textArea,
			Gutter gutter) {

		Font font = new Font(Font.DIALOG, Font.PLAIN, 13);
		textArea.setFont(font);
		textArea.setSyntaxScheme(createSyntaxScheme(font, Color.orange));
		textArea.setBackground(Color.orange);
		textArea.setCaretColor(Color.orange);
		textArea.setUseSelectedTextColor(true);
		textArea.setSelectedTextColor(Color.orange);
		textArea.setSelectionColor(Color.orange);
		textArea.setRoundedSelectionEdges(true);
		textArea.setCurrentLineHighlightColor(Color.orange);
		textArea.setFadeCurrentLineHighlight(true);
		textArea.setTabLineColor(Color.orange);
		textArea.setMarginLineColor(Color.orange);
		textArea.setMarkAllHighlightColor(Color.pink); // orange is the default (!)
		textArea.setMarkOccurrencesColor(Color.orange);
		textArea.setPaintMarkOccurrencesBorder(!textArea.getPaintMarkOccurrencesBorder());
		textArea.setMatchedBracketBGColor(Color.orange);
		textArea.setMatchedBracketBorderColor(Color.orange);
		textArea.setPaintMatchedBracketPair(!textArea.getPaintMatchedBracketPair());
		textArea.setAnimateBracketMatching(!textArea.getAnimateBracketMatching());
		textArea.setHyperlinkForeground(Color.orange);
		for (int i=0; i<textArea.getSecondaryLanguageCount(); i++) {
			textArea.setSecondaryLanguageBackground(i+1, Color.orange);
		}

		gutter.setBackground(Color.orange);
		gutter.setBorderColor(Color.orange);
		gutter.setActiveLineRangeColor(Color.orange);
		gutter.setIconRowHeaderInheritsGutterBackground(!gutter.getIconRowHeaderInheritsGutterBackground());
		gutter.setLineNumberColor(Color.orange);
		gutter.setCurrentLineNumberColor(Color.orange);
		gutter.setLineNumberFont(font);
		gutter.setFoldIndicatorArmedForeground(Color.orange);
		gutter.setFoldIndicatorForeground(Color.orange);
		gutter.setFoldBackground(Color.orange);
		gutter.setArmedFoldBackground(Color.orange);

	}


	@Test
	void testConstructor_textAreaArg() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		Font font = new Font(Font.DIALOG, Font.PLAIN, 13);
		textArea.setFont(font);

		Theme theme = new Theme(textArea);
		Assertions.assertEquals(textArea.getSyntaxScheme(), theme.scheme);
		Assertions.assertEquals(textArea.getBackground(), theme.bgColor);
		Assertions.assertEquals(textArea.getCaretColor(), theme.caretColor);
	}


	@Test
	void testApply() {

		RSyntaxTextArea textArea1 = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp1 = new RTextScrollPane(textArea1);
		Gutter gutter1 = sp1.getGutter();
		initWithOddProperties(textArea1, gutter1);

		RSyntaxTextArea textArea2 = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp2 = new RTextScrollPane(textArea2);
		Gutter gutter2 = sp2.getGutter();

		assertAllThemePropertiesDifferent(textArea1, gutter1, textArea2, gutter2);

		Theme theme = new Theme(textArea1);
		theme.apply(textArea2);
		assertEqualThemeProperties(textArea1, gutter1, textArea2, gutter2);

	}


	@Test
	void testLoad_fromStream_noDefaultFont_withArmedBG() throws Exception {

		InputStream in = getClass().getResourceAsStream("ThemeTest_theme1.xml");
		Theme theme = Theme.load(in);
		in.close();

		RSyntaxTextArea textArea1 = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp1 = new RTextScrollPane(textArea1);
		Gutter gutter1 = sp1.getGutter();
		initWithOddProperties(textArea1, gutter1);

		theme.apply(textArea1);
		assertColorsMatchTheme1(textArea1, gutter1);

	}


	@Test
	void testLoad_fromStream_withDefaultFont_preservesLigatureAttributes() throws Exception {

		Font baseFont = FontUtil.createFont(Font.MONOSPACED, Font.PLAIN, 10);
		Map<TextAttribute, Object> extraAttrs = new HashMap<>();
		extraAttrs.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		extraAttrs.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
		baseFont = baseFont.deriveFont(extraAttrs);

		InputStream in = getClass().getResourceAsStream("ThemeTest_theme1.xml");
		Theme theme = Theme.load(in, baseFont);
		in.close();

		Assertions.assertEquals(baseFont, theme.baseFont);

		// All fonts in the scheme have its ligature properties, even if they're
		// different families
		for (int i = 0; i < theme.scheme.getStyleCount(); i++) {
			Style style = theme.scheme.getStyle(i);
			if (style.font != null) {
				Map<TextAttribute, ?> attrs = style.font.getAttributes();
				Assertions.assertEquals(TextAttribute.KERNING_ON, attrs.get(TextAttribute.KERNING));
				Assertions.assertEquals(TextAttribute.LIGATURES_ON, attrs.get(TextAttribute.LIGATURES));
			}
		}
	}


	@Test
	void testLoad_fromStream_noDefaultFont_noArmedBG() throws Exception {

		InputStream in = getClass().getResourceAsStream("ThemeTest_theme1_noArmedBG.xml");
		Theme theme = Theme.load(in);
		in.close();

		RSyntaxTextArea textArea1 = new RSyntaxTextArea(
			SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp1 = new RTextScrollPane(textArea1);
		Gutter gutter1 = sp1.getGutter();
		initWithOddProperties(textArea1, gutter1);

		theme.apply(textArea1);
		assertColorsMatchTheme1_noArmedBG(textArea1, gutter1);

	}


	@Test
	void testLoad_fromStream_noDefaultFont_noLineNumbers_currenetFG() throws Exception {

		InputStream in = getClass().getResourceAsStream("ThemeTest_theme1_noLineNumbers_currentFG.xml");
		Theme theme = Theme.load(in);
		in.close();

		RSyntaxTextArea textArea1 = new RSyntaxTextArea(
			SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp1 = new RTextScrollPane(textArea1);
		Gutter gutter1 = sp1.getGutter();
		initWithOddProperties(textArea1, gutter1);

		theme.apply(textArea1);
		assertColorsMatchTheme1_noLineNumbers_currentFG(textArea1, gutter1);

	}


	@Test
	void testSave() throws Exception {

		RSyntaxTextArea textArea1 = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp1 = new RTextScrollPane(textArea1);
		Gutter gutter1 = sp1.getGutter();
		initWithOddProperties(textArea1, gutter1);

		RSyntaxTextArea textArea2 = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_PHP);
		RTextScrollPane sp2 = new RTextScrollPane(textArea2);
		Gutter gutter2 = sp2.getGutter();

		assertAllThemePropertiesDifferent(textArea1, gutter1, textArea2, gutter2);

		Theme theme = new Theme(textArea1);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		theme.save(baos);
		String actual = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		baos.close();

		ByteArrayInputStream bin = new ByteArrayInputStream(actual.getBytes(StandardCharsets.UTF_8));
		Theme theme2 = Theme.load(bin);
		bin.close();

		theme2.apply(textArea2);

		assertEqualThemeProperties(textArea1, gutter1, textArea2, gutter2);

	}


}
