/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link RSyntaxTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaTest {


	@Test
	public void testAnimateBracketMatching() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAnimateBracketMatching());
		textArea.setAnimateBracketMatching(false);
		Assert.assertFalse(textArea.getAnimateBracketMatching());
	}


	@Test
	public void testAntiAliasingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAntiAliasingEnabled());
		textArea.setAntiAliasingEnabled(false);
		Assert.assertFalse(textArea.getAntiAliasingEnabled());
	}


	@Test
	public void testAutoIndentEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isAutoIndentEnabled());
		textArea.setAutoIndentEnabled(false);
		Assert.assertFalse(textArea.isAutoIndentEnabled());
	}


	@Test
	public void testBracketMatchingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isBracketMatchingEnabled());
		textArea.setBracketMatchingEnabled(false);
		Assert.assertFalse(textArea.isBracketMatchingEnabled());
	}


	@Test
	public void testClearWhitespaceLinesEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
		textArea.setClearWhitespaceLinesEnabled(false);
		Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
	}


	@Test
	public void testCloseCurlyBraces() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseCurlyBraces());
		textArea.setCloseCurlyBraces(false);
		Assert.assertFalse(textArea.getCloseCurlyBraces());
	}


	@Test
	public void testCloseMarkupTags() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseMarkupTags());
		textArea.setCloseMarkupTags(false);
		Assert.assertFalse(textArea.getCloseMarkupTags());
	}


	@Test
	public void testCodeFoldingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isCodeFoldingEnabled());
		textArea.setCodeFoldingEnabled(true);
		Assert.assertTrue(textArea.isCodeFoldingEnabled());
	}


	@Test
	public void testEOLMarkersVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getEOLMarkersVisible());
		textArea.setEOLMarkersVisible(true);
		Assert.assertTrue(textArea.getEOLMarkersVisible());
	}


	@Test
	public void testFractionalFontMetricsEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
		textArea.setFractionalFontMetricsEnabled(true);
		Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
	}


	@Test
	public void testHighlightSecondaryLanguages() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
		textArea.setHighlightSecondaryLanguages(false);
		Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
	}


	@Test
	public void testHyperlinkForeground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setHyperlinkForeground(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
	}


	@Test
	public void testHyperlinksEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHyperlinksEnabled());
		textArea.setHyperlinksEnabled(false);
		Assert.assertFalse(textArea.getHyperlinksEnabled());
	}


	@Test
	public void testMarkOccurrences() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(true);
		Assert.assertTrue(textArea.getMarkOccurrences());
	}


	@Test
	public void testMarkOccurrencesColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
	}


	@Test
	public void testMarkOccurrencesDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesDelay(5432);
		Assert.assertEquals(5432, textArea.getMarkOccurrencesDelay());
	}


	@Test
	public void testMatchedBracketBGColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBGColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
	}


	@Test
	public void testMatchedBracketBorderColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBorderColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
	}


	@Test
	public void testPaintMatchedBracketPair() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMatchedBracketPair());
		textArea.setPaintMatchedBracketPair(true);
		Assert.assertTrue(textArea.getPaintMatchedBracketPair());
	}

	@Test
	public void testPaintMarkOccurrencesBorder() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
		textArea.setPaintMarkOccurrencesBorder(true);
		Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
	}

	@Test
	public void testPaintTabLines() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintTabLines());
		textArea.setPaintTabLines(true);
		Assert.assertTrue(textArea.getPaintTabLines());
	}


	@Test
	public void testSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}


	@Test
	public void testTabLineColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setTabLineColor(Color.blue);
		Assert.assertEquals(Color.blue, textArea.getTabLineColor());
	}


	@Test
	public void testParserDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setParserDelay(6789);
		Assert.assertEquals(6789, textArea.getParserDelay());
	}


	@Test
	public void testUseFocusableTips() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getUseFocusableTips());
		textArea.setUseFocusableTips(false);
		Assert.assertFalse(textArea.getUseFocusableTips());
	}


	@Test
	public void testUseSelectedTextColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getUseSelectedTextColor());
		textArea.setUseSelectedTextColor(true);
		Assert.assertTrue(textArea.getUseSelectedTextColor());
	}


	@Test
	public void testWhitespaceVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isWhitespaceVisible());
		textArea.setWhitespaceVisible(true);
		Assert.assertTrue(textArea.isWhitespaceVisible());
	}

}
