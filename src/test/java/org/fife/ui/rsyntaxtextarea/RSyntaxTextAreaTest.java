/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.fife.ui.SwingRunner;


/**
 * Unit tests for the {@link RSyntaxTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaTest {


	@Test
	public void testGetAnimateBracketMatching() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAnimateBracketMatching());
		textArea.setAnimateBracketMatching(false);
		Assert.assertFalse(textArea.getAnimateBracketMatching());
	}


	@Test
	public void testGetAntiAliasingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAntiAliasingEnabled());
		textArea.setAntiAliasingEnabled(false);
		Assert.assertFalse(textArea.getAntiAliasingEnabled());
	}


	@Test
	public void testGetCloseCurlyBraces() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseCurlyBraces());
		textArea.setCloseCurlyBraces(false);
		Assert.assertFalse(textArea.getCloseCurlyBraces());
	}


	@Test
	public void testGetCloseMarkupTags() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseMarkupTags());
		textArea.setCloseMarkupTags(false);
		Assert.assertFalse(textArea.getCloseMarkupTags());
	}


	@Test
	public void testGetEOLMarkersVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getEOLMarkersVisible());
		textArea.setEOLMarkersVisible(true);
		Assert.assertTrue(textArea.getEOLMarkersVisible());
	}


	@Test
	public void testGetFractionalFontMetricsEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
		textArea.setFractionalFontMetricsEnabled(true);
		Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
	}


	@Test
	public void testGetHighlightSecondaryLanguages() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
		textArea.setHighlightSecondaryLanguages(false);
		Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
	}


	@Test
	public void testGetHyperlinkForeground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setHyperlinkForeground(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
	}


	@Test
	public void testGetHyperlinksEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHyperlinksEnabled());
		textArea.setHyperlinksEnabled(false);
		Assert.assertFalse(textArea.getHyperlinksEnabled());
	}


	@Test
	public void testGetMarkOccurrences() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(true);
		Assert.assertTrue(textArea.getMarkOccurrences());
	}


	@Test
	public void testGetMarkOccurrencesColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
	}


	@Test
	public void testGetMatchedBracketBGColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBGColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
	}


	@Test
	public void testGetMatchedBracketBorderColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBorderColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
	}


	@Test
	public void testGetPaintMatchedBracketPair() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMatchedBracketPair());
		textArea.setPaintMatchedBracketPair(true);
		Assert.assertTrue(textArea.getPaintMatchedBracketPair());
	}


	@Test
	public void testGetPaintTabLines() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintTabLines());
		textArea.setPaintTabLines(true);
		Assert.assertTrue(textArea.getPaintTabLines());
	}


	@Test
	public void testGetSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}


	@Test
	public void testGetTabLineColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setTabLineColor(Color.blue);
		Assert.assertEquals(Color.blue, textArea.getTabLineColor());
	}


	@Test
	public void testGetPaintMarkOccurrencesBorder() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
		textArea.setPaintMarkOccurrencesBorder(true);
		Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
	}


	@Test
	public void testGetParserDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setParserDelay(6789);
		Assert.assertEquals(6789, textArea.getParserDelay());
	}


	@Test
	public void testGetUseFocusableTips() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getUseFocusableTips());
		textArea.setUseFocusableTips(false);
		Assert.assertFalse(textArea.getUseFocusableTips());
	}


	@Test
	public void testGetUseSelectedTextColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getUseSelectedTextColor());
		textArea.setUseSelectedTextColor(true);
		Assert.assertTrue(textArea.getUseSelectedTextColor());
	}


	@Test
	public void testIsAutoIndentEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isAutoIndentEnabled());
		textArea.setAutoIndentEnabled(false);
		Assert.assertFalse(textArea.isAutoIndentEnabled());
	}


	@Test
	public void testIsBracketMatchingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isBracketMatchingEnabled());
		textArea.setBracketMatchingEnabled(false);
		Assert.assertFalse(textArea.isBracketMatchingEnabled());
	}


	@Test
	public void testIsClearWhitespaceLinesEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
		textArea.setClearWhitespaceLinesEnabled(false);
		Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
	}


	@Test
	public void testIsCodeFoldingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isCodeFoldingEnabled());
		textArea.setCodeFoldingEnabled(true);
		Assert.assertTrue(textArea.isCodeFoldingEnabled());
	}


	@Test
	public void testIsWhitespaceVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isWhitespaceVisible());
		textArea.setWhitespaceVisible(true);
		Assert.assertTrue(textArea.isWhitespaceVisible());
	}


	@Test
	public void testSetAnimateBracketMatching() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAnimateBracketMatching());
		textArea.setAnimateBracketMatching(false);
		Assert.assertFalse(textArea.getAnimateBracketMatching());
	}


	@Test
	public void testSetAntiAliasingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAntiAliasingEnabled());
		textArea.setAntiAliasingEnabled(false);
		Assert.assertFalse(textArea.getAntiAliasingEnabled());
	}

	@Test
	public void testSetAutoIndentEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isAutoIndentEnabled());
		textArea.setAutoIndentEnabled(false);
		Assert.assertFalse(textArea.isAutoIndentEnabled());
	}

	@Test
	public void testSetBracketMatchingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isBracketMatchingEnabled());
		textArea.setBracketMatchingEnabled(false);
		Assert.assertFalse(textArea.isBracketMatchingEnabled());
	}

	@Test
	public void testSetClearWhitespaceLinesEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
		textArea.setClearWhitespaceLinesEnabled(false);
		Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
	}

	@Test
	public void testSetCloseCurlyBraces() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseCurlyBraces());
		textArea.setCloseCurlyBraces(false);
		Assert.assertFalse(textArea.getCloseCurlyBraces());
	}

	@Test
	public void testSetCloseMarkupTags() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseMarkupTags());
		textArea.setCloseMarkupTags(false);
		Assert.assertFalse(textArea.getCloseMarkupTags());
	}

	@Test
	public void testSetCodeFoldingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isCodeFoldingEnabled());
		textArea.setCodeFoldingEnabled(true);
		Assert.assertTrue(textArea.isCodeFoldingEnabled());
	}

	@Test
	public void testSetEOLMarkersVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getEOLMarkersVisible());
		textArea.setEOLMarkersVisible(true);
		Assert.assertTrue(textArea.getEOLMarkersVisible());
	}

	@Test
	public void testSetFractionalFontMetricsEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
		textArea.setFractionalFontMetricsEnabled(true);
		Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
	}

	@Test
	public void testSetHighlightSecondaryLanguages() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
		textArea.setHighlightSecondaryLanguages(false);
		Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
	}

	@Test
	public void testSetHyperlinkForeground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setHyperlinkForeground(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
	}

	@Test
	public void testSetHyperlinksEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHyperlinksEnabled());
		textArea.setHyperlinksEnabled(false);
		Assert.assertFalse(textArea.getHyperlinksEnabled());
	}


	@Test
	public void testSetMarkOccurrences() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(true);
		Assert.assertTrue(textArea.getMarkOccurrences());
	}

	@Test
	public void testSetMarkOccurrencesColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
	}

	@Test
	public void testSetMatchedBracketBGColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBGColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
	}

	@Test
	public void testSetMatchedBracketBorderColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBorderColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
	}

	@Test
	public void testSetPaintMarkOccurrencesBorder() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
		textArea.setPaintMarkOccurrencesBorder(true);
		Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
	}

	@Test
	public void testSetPaintMatchedBracketPair() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMatchedBracketPair());
		textArea.setPaintMatchedBracketPair(true);
		Assert.assertTrue(textArea.getPaintMatchedBracketPair());
	}

	@Test
	public void testSetPaintTabLines() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintTabLines());
		textArea.setPaintTabLines(true);
		Assert.assertTrue(textArea.getPaintTabLines());
	}

	@Test
	public void testSetParserDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setParserDelay(6789);
		Assert.assertEquals(6789, textArea.getParserDelay());
	}

	@Test
	public void testSetSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}

	@Test
	public void testSetTabLineColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setTabLineColor(Color.blue);
		Assert.assertEquals(Color.blue, textArea.getTabLineColor());
	}

	@Test
	public void testSetUseFocusableTips() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getUseFocusableTips());
		textArea.setUseFocusableTips(false);
		Assert.assertFalse(textArea.getUseFocusableTips());
	}

	@Test
	public void testSetUseSelectedTextColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getUseSelectedTextColor());
		textArea.setUseSelectedTextColor(true);
		Assert.assertTrue(textArea.getUseSelectedTextColor());
	}

	@Test
	public void testSetWhitespaceVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isWhitespaceVisible());
		textArea.setWhitespaceVisible(true);
		Assert.assertTrue(textArea.isWhitespaceVisible());
	}

}
