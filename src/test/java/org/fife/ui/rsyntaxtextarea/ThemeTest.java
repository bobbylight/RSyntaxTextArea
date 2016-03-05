/*
 * 02/15/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link Theme}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ThemeTest {


	/**
	 * Asserts that all properties set by a {@link Theme} are different
	 * between one text area/gutter pair and another.
	 */
	private void assertAllThemePropertiesDifferent(RSyntaxTextArea textArea1,
			Gutter gutter1, RSyntaxTextArea textArea2, Gutter gutter2) {

		Assert.assertNotEquals(textArea1.getFont(), textArea2.getFont());
		Assert.assertNotEquals(textArea1.getSyntaxScheme(), textArea2.getSyntaxScheme());
		Assert.assertNotEquals(textArea1.getBackground(), textArea2.getBackground());
		Assert.assertNotEquals(textArea1.getCaretColor(), textArea2.getCaretColor());
		Assert.assertNotEquals(textArea1.getUseSelectedTextColor(), textArea2.getUseSelectedTextColor());
		Assert.assertNotEquals(textArea1.getSelectedTextColor(), textArea2.getSelectedTextColor());
		Assert.assertNotEquals(textArea1.getSelectionColor(), textArea2.getSelectionColor());
		Assert.assertNotEquals(textArea1.getRoundedSelectionEdges(), textArea2.getRoundedSelectionEdges());
		Assert.assertNotEquals(textArea1.getCurrentLineHighlightColor(), textArea2.getCurrentLineHighlightColor());
		Assert.assertNotEquals(textArea1.getFadeCurrentLineHighlight(), textArea2.getFadeCurrentLineHighlight());
		Assert.assertNotEquals(textArea1.getMarginLineColor(), textArea2.getMarginLineColor());
		Assert.assertNotEquals(textArea1.getMarkAllHighlightColor(), textArea2.getMarkAllHighlightColor());
		Assert.assertNotEquals(textArea1.getMarkOccurrencesColor(), textArea2.getMarkOccurrencesColor());
		Assert.assertNotEquals(textArea1.getPaintMarkOccurrencesBorder(), textArea2.getPaintMarkOccurrencesBorder());
		Assert.assertNotEquals(textArea1.getMatchedBracketBGColor(), textArea2.getMatchedBracketBGColor());
		Assert.assertNotEquals(textArea1.getMatchedBracketBorderColor(), textArea2.getMatchedBracketBorderColor());
		Assert.assertNotEquals(textArea1.getPaintMatchedBracketPair(), textArea2.getPaintMatchedBracketPair());
		Assert.assertNotEquals(textArea1.getAnimateBracketMatching(), textArea2.getAnimateBracketMatching());
		Assert.assertNotEquals(textArea1.getHyperlinkForeground(), textArea2.getHyperlinkForeground());
		for (int i=0; i<textArea1.getSecondaryLanguageCount(); i++) {
			Assert.assertNotEquals(textArea1.getSecondaryLanguageBackground(i+1),
					textArea2.getSecondaryLanguageBackground(i+1));
		}
		Assert.assertNotEquals(gutter1.getBackground(), gutter2.getBackground());
		Assert.assertNotEquals(gutter1.getBorderColor(), gutter2.getBorderColor());
		Assert.assertNotEquals(gutter1.getActiveLineRangeColor(), gutter2.getActiveLineRangeColor());
		Assert.assertNotEquals(gutter1.getIconRowHeaderInheritsGutterBackground(), gutter2.getIconRowHeaderInheritsGutterBackground());
		Assert.assertNotEquals(gutter1.getLineNumberColor(), gutter2.getLineNumberColor());
		Assert.assertNotEquals(gutter1.getLineNumberFont(), gutter2.getLineNumberFont());
		Assert.assertNotEquals(gutter1.getFoldIndicatorForeground(), gutter2.getFoldIndicatorForeground());
		Assert.assertNotEquals(gutter1.getFoldBackground(), gutter2.getFoldBackground());

	}


	/**
	 * Asserts whether a text area and gutter match the styles defined in
	 * <code>ThemeTest_theme1.xml</code>.
	 */
	private void assertColorsMatchTheme1(RSyntaxTextArea textArea,
			Gutter gutter) {

		Assert.assertEquals(Color.orange, textArea.getBackground());
		Assert.assertEquals(Color.orange, textArea.getCaretColor());
		Assert.assertEquals(false,        textArea.getUseSelectedTextColor());
		Assert.assertEquals(Color.orange, textArea.getSelectedTextColor());
		Assert.assertEquals(Color.orange, textArea.getSelectionColor());
		Assert.assertEquals(true,         textArea.getRoundedSelectionEdges());
		Assert.assertEquals(Color.orange, textArea.getCurrentLineHighlightColor());
		Assert.assertEquals(true,         textArea.getFadeCurrentLineHighlight());
		Assert.assertEquals(Color.orange, textArea.getMarginLineColor());
		Assert.assertEquals(Color.orange, textArea.getMarkAllHighlightColor());
		Assert.assertEquals(Color.orange, textArea.getMarkOccurrencesColor());
		Assert.assertEquals(true,         textArea.getPaintMarkOccurrencesBorder());
		Assert.assertEquals(Color.orange, textArea.getMatchedBracketBGColor());
		Assert.assertEquals(Color.orange, textArea.getMatchedBracketBorderColor());
		Assert.assertEquals(true,         textArea.getPaintMatchedBracketPair());
		Assert.assertEquals(true,         textArea.getAnimateBracketMatching());
		Assert.assertEquals(Color.orange, textArea.getHyperlinkForeground());
		for (int i=0; i<textArea.getSecondaryLanguageCount(); i++) {
			Color expected = i==TokenTypes.IDENTIFIER ? Color.blue : Color.orange;
			Assert.assertEquals(expected, textArea.getSecondaryLanguageBackground(i+1));
		}

		Assert.assertEquals(Color.orange, gutter.getBackground());
		Assert.assertEquals(Color.orange, gutter.getBorderColor());
		Assert.assertEquals(Color.orange, gutter.getActiveLineRangeColor());
		Assert.assertEquals(true,         gutter.getIconRowHeaderInheritsGutterBackground());
		Assert.assertEquals(Color.orange, gutter.getLineNumberColor());
		//Assert.assertEquals("Arial",      gutter.getLineNumberFont().getFamily()); // Arial not on travis-ci build servers
		Assert.assertEquals(22,           gutter.getLineNumberFont().getSize());
		Assert.assertEquals(Color.orange, gutter.getFoldIndicatorForeground());
		Assert.assertEquals(Color.orange, gutter.getFoldBackground());

	}


	/**
	 * Asserts that all properties set by a {@link Theme} are equal
	 * between one text area/gutter pair and another.
	 */
	private void assertEqualThemeProperties(RSyntaxTextArea textArea1,
			Gutter gutter1, RSyntaxTextArea textArea2, Gutter gutter2) {

		Assert.assertEquals(textArea1.getFont(), textArea2.getFont());
		Assert.assertEquals(textArea1.getSyntaxScheme(), textArea1.getSyntaxScheme());
		Assert.assertEquals(textArea1.getBackground(), textArea2.getBackground());
		Assert.assertEquals(textArea1.getCaretColor(), textArea2.getCaretColor());
		Assert.assertEquals(textArea1.getUseSelectedTextColor(), textArea2.getUseSelectedTextColor());
		Assert.assertEquals(textArea1.getSelectedTextColor(), textArea2.getSelectedTextColor());
		Assert.assertEquals(textArea1.getSelectionColor(), textArea2.getSelectionColor());
		Assert.assertEquals(textArea1.getRoundedSelectionEdges(), textArea2.getRoundedSelectionEdges());
		Assert.assertEquals(textArea1.getCurrentLineHighlightColor(), textArea2.getCurrentLineHighlightColor());
		Assert.assertEquals(textArea1.getFadeCurrentLineHighlight(), textArea2.getFadeCurrentLineHighlight());
		Assert.assertEquals(textArea1.getMarginLineColor(), textArea2.getMarginLineColor());
		Assert.assertEquals(textArea1.getMarkAllHighlightColor(), textArea2.getMarkAllHighlightColor());
		Assert.assertEquals(textArea1.getMarkOccurrencesColor(), textArea2.getMarkOccurrencesColor());
		Assert.assertEquals(textArea1.getPaintMarkOccurrencesBorder(), textArea2.getPaintMarkOccurrencesBorder());
		Assert.assertEquals(textArea1.getMatchedBracketBGColor(), textArea2.getMatchedBracketBGColor());
		Assert.assertEquals(textArea1.getMatchedBracketBorderColor(), textArea2.getMatchedBracketBorderColor());
		Assert.assertEquals(textArea1.getPaintMatchedBracketPair(), textArea2.getPaintMatchedBracketPair());
		Assert.assertEquals(textArea1.getAnimateBracketMatching(), textArea2.getAnimateBracketMatching());
		Assert.assertEquals(textArea1.getHyperlinkForeground(), textArea2.getHyperlinkForeground());

		Assert.assertEquals(gutter1.getBackground(), gutter2.getBackground());
		Assert.assertEquals(gutter1.getBorderColor(), gutter2.getBorderColor());
		Assert.assertEquals(gutter1.getActiveLineRangeColor(), gutter2.getActiveLineRangeColor());
		Assert.assertEquals(gutter1.getIconRowHeaderInheritsGutterBackground(), gutter2.getIconRowHeaderInheritsGutterBackground());
		Assert.assertEquals(gutter1.getLineNumberColor(), gutter2.getLineNumberColor());
		Assert.assertEquals(gutter1.getLineNumberFont(), gutter2.getLineNumberFont());
		Assert.assertEquals(gutter1.getFoldIndicatorForeground(), gutter2.getFoldIndicatorForeground());
		Assert.assertEquals(gutter1.getFoldBackground(), gutter2.getFoldBackground());

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

		Font font = new Font("Dialog", Font.PLAIN, 13);
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
		gutter.setLineNumberFont(font);
		gutter.setFoldIndicatorForeground(Color.orange);
		gutter.setFoldBackground(Color.orange);

	}


/*
	@Test
	public void testConstructor_TextAreaArg() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		Font font = new Font("Dialog", Font.PLAIN, 13);
		textArea.setFont(font);

		Theme theme = new Theme(textArea);

	}
*/


	@Test
	public void testApply() {

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
	public void testLoad_FromStream_NoDefaultFont() throws Exception {

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
	public void testSave() throws Exception {

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
		String actual = new String(baos.toByteArray(), "UTF-8");
		baos.close();

		ByteArrayInputStream bin = new ByteArrayInputStream(actual.getBytes("UTF-8"));
		Theme theme2 = Theme.load(bin);
		bin.close();

		theme2.apply(textArea2);

		assertEqualThemeProperties(textArea1, gutter1, textArea2, gutter2);

	}


}