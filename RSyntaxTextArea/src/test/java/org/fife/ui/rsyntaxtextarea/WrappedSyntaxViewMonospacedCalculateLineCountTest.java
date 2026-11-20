package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rsyntaxtextarea.WrappedSyntaxView.WrappedLine;
import org.fife.ui.rtextarea.RTextAreaBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_NONE;
import static org.junit.Assert.assertEquals;


/**
 * Unit tests for the {@link WrappedSyntaxView} class.
 *
 * @author Mike Smith
 * @version 1.0
 */
@RunWith(Parameterized.class)
public class WrappedSyntaxViewMonospacedCalculateLineCountTest
{
	private final int fontSize;

	private RSyntaxDocument document;
	private WrappedSyntaxView view;

	@Parameterized.Parameters (name = "{0}")
	public static Collection<Object[]> data()
	{
		return asList(new Object[][] {{15}, {16}, {18}});
	}

	public WrappedSyntaxViewMonospacedCalculateLineCountTest(int fontSize)
	{
		this.fontSize = fontSize;
	}

	@Before
	public void setUp()
	{
		document = new RSyntaxDocument(SYNTAX_STYLE_NONE);
		RSyntaxTextArea textArea = new RSyntaxTextArea(document);

		Font font = RTextAreaBase.getDefaultFont().deriveFont(Font.PLAIN, fontSize);
		FontMetrics fontMetrics = new JPanel().getFontMetrics(font);
		int charWidth = fontMetrics.charWidth(32);
		textArea.setFont(font);
		textArea.setLineWrap(true);
		textArea.getSyntaxScheme().getStyle(TokenTypes.IDENTIFIER).font = font;
		textArea.getSyntaxScheme().getStyle(TokenTypes.IDENTIFIER).fontMetrics = fontMetrics;
		textArea.getSyntaxScheme().getStyle(TokenTypes.WHITESPACE).font = font;
		textArea.getSyntaxScheme().getStyle(TokenTypes.WHITESPACE).fontMetrics = fontMetrics;
		view = (WrappedSyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		view.setSize(charWidth * 8, 1000);
	}

	@Test
	public void givenALineWithOneTokenWithinViewWidth_thenCalculateLineCountReturns1() throws Exception {
		document.replace(0, document.getLength(), "abcdefgh\n",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(1, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensWithinViewWidth_thenCalculateLineCountReturns1() throws Exception {
		document.replace(0, document.getLength(), "abc de f\n",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(1, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensWithinViewWidthAndContainingTab_thenCalculateLineCountReturns1() throws Exception {
		document.replace(0, document.getLength(), "abc\td f\n",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(1, line.calculateLineCount());
	}

	@Test
	public void givenALineWithSingleTokenWrappingOverTwoPhysicalLines_thenCalculateLineCountReturns2() throws Exception {
		document.replace(0, document.getLength(), "abcdefghijk",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(2, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensWrappingOverTwoPhysicalLinesAndContainingATabOnTheFirstLine_thenCalculateLineCountReturns2() throws Exception {
		document.replace(0, document.getLength(), "abc\tde f",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(2, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensWrappingOverTwoPhysicalLinesAndContainingATabOnTheSecondLine_thenCalculateLineCountReturns2() throws Exception {
		document.replace(0, document.getLength(), "abcdefghij\tkl",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(2, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheFirstLine_thenCalculateLineCountReturns2() throws Exception {
		document.replace(0, document.getLength(), "abc\tde f ghijkl\n",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(2, line.calculateLineCount());
	}

	@Test
	public void givenALineWithMultipleTokensFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheSecondLine_thenCalculateLineCountReturns2() throws Exception {
		document.replace(0, document.getLength(), "abcdefghij\tklm\n",null);
		WrappedLine line = (WrappedLine) view.getView(0);

		assertEquals(2, line.calculateLineCount());
	}
}
