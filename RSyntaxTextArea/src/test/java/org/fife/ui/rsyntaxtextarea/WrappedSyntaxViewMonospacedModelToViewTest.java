package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.RTextAreaBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.View;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.Collection;

import static java.util.Arrays.asList;
import static javax.swing.text.Position.Bias.Backward;
import static javax.swing.text.Position.Bias.Forward;
import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_NONE;
import static org.junit.Assert.assertEquals;


/**
 * Unit tests for the {@link WrappedSyntaxView} class.
 *
 * @author Mike Smith
 * @version 1.0
 */
@RunWith(Parameterized.class)
public class WrappedSyntaxViewMonospacedModelToViewTest
{
	private static final int VIEW_WIDTH = 1000;
	private static final int VIEW_HEIGHT = 800;

	private final int fontSize;

	private RSyntaxDocument document;
	private WrappedSyntaxView view;
	private int lineHeight;
	private int charWidth;

	@Parameterized.Parameters (name = "{0}")
	public static Collection<Object[]> data()
	{
		return asList(new Object[][] {{15}, {16}, {18}});
	}

	public WrappedSyntaxViewMonospacedModelToViewTest(int fontSize)
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
		charWidth = fontMetrics.charWidth(32);
		textArea.setFont(font);
		textArea.setLineWrap(true);
		textArea.getSyntaxScheme().getStyle(TokenTypes.IDENTIFIER).font = font;
		textArea.getSyntaxScheme().getStyle(TokenTypes.IDENTIFIER).fontMetrics = fontMetrics;
		textArea.getSyntaxScheme().getStyle(TokenTypes.WHITESPACE).font = font;
		textArea.getSyntaxScheme().getStyle(TokenTypes.WHITESPACE).fontMetrics = fontMetrics;
		lineHeight = textArea.getLineHeight();
		view = (WrappedSyntaxView) textArea.getUI().getRootView(textArea).getView(0);
		view.setSize(VIEW_WIDTH, VIEW_HEIGHT);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtTheFirstCharacter_andBiasForward_thenModelToViewReturnsThePositionOfTheFirstCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(0, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(0, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtTheFistCharacter_andBiasBackward_thenModelToViewReturnsThePositionOfTheFirstCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(0, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(0, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtACharacter_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(7, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 7, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtACharacter_andBiasBackward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(7, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 7, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasForward_thenModelToViewReturnsThePositionOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(64, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 64, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(64, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 64, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsAtTheNewlineCharacter_andBiasForward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(65, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 65, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsAtTheNewlineCharacter_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(65, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 65, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsOnePlaceAfterTheNewlineCharacter_andBiasForward_thenModelToViewThrowsBadLocationException() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		line.modelToView(66, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);
	}

	// TODO: should modelToView do this? Wouldn't it be better to throw an exception as in the forward bias case?
	@Test
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsOnePlaceAfterTheNewlineCharacter_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(66, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 65, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsTwoPlacesAfterTheNewlineCharacter_andBiasForward_thenModelToViewThrowsBadLocationException() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		line.modelToView(67, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineFittingWithinAllocationWidthAndEndingWithANewline_whenPositionIsTwoPlacesAfterTheNewlineCharacter_andBiasBackward_thenModelToViewThrowsBadLocationException() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		line.modelToView(67, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasForward_thenModelToViewReturnsThePositionOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(64, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 64, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(64, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 64, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasForward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(65, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 65, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheLine() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(65, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 65, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsTwoPlacesAfterTheLastCharacterOfTheLine_andBiasForward_thenModelToViewThrowsBadLocationException() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		line.modelToView(66, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineFittingWithinAllocationWidthAndNotEndingWithANewline_whenPositionIsTwoPlacesAfterTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewThrowsBadLocationException() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.",null);
		View line = view.getView(0);

		line.modelToView(66, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Backward);
	}

	@Test
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(25, new Rectangle(0, 0, viewWidth, lineHeight), Forward);

		assertEquals(charWidth * 25, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsAtTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(25, new Rectangle(0, 0, viewWidth, lineHeight), Backward);

		assertEquals(charWidth * 25, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasForward_thenModelToViewReturnsThePositionRightOfTheLastCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(26, new Rectangle(0, 0, viewWidth, lineHeight), Forward);

		assertEquals(charWidth * 26, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheLastCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(26, new Rectangle(0, 0, viewWidth, lineHeight), Backward);

		assertEquals(charWidth * 26, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasForward_thenModelToViewThrowsBadLocationException() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		line.modelToView(27, new Rectangle(0, 0, viewWidth, lineHeight), Forward);
	}

	@Test(expected = BadLocationException.class)
	public void givenALineExactlyTheViewWidthWithNoNewline_whenPositionIsOnePlaceAfterTheLastCharacterOfTheLine_andBiasBackward_thenModelToViewThrowsBadLocationException() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		line.modelToView(27, new Rectangle(0, 0, viewWidth, lineHeight), Backward);
	}

	@Test
	public void givenALineExactlyTwoViewWidthsWithNoNewline_whenPositionIsAtTheLastCharacterOfTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(52, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth * 26, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenATokenLongerThanOnePhysicalLine_whenPositionIsAtACharacterOnTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(38, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth * 12, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtACharacter_andTheAllocatedRegionIsOffsetInTheYAxis_thenModelToViewReturnsThePositionOfTheCharacterOffsetInTheYAxisByTheSameAmount() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(7, new Rectangle(0, 120, VIEW_WIDTH, lineHeight), Backward);

		assertEquals(charWidth * 7, screenPosition.x);
		assertEquals(120, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLines_whenPositionIsAtACharacterOfTheSecondPhysicalLine_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "This is a line of text which will be wrapped over several lines due to its excessive length.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(37, new Rectangle(0, 0, viewWidth, lineHeight * 4), Forward);

		assertEquals(charWidth * 14, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheSpaceBreakingTheFirstPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheSpace() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(17, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth * 17, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheSpaceBreakingTheFirstPhysicalLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheSpace() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(17, new Rectangle(0, 0, viewWidth, lineHeight * 2), Backward);

		assertEquals(charWidth * 17, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheFirstCharacterOfTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(18, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(0, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheFirstCharacterOfTheSecondPhysicalLine_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheSpaceBreakingTheFirstLine() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(18, new Rectangle(0, 0, viewWidth, lineHeight * 2), Backward);

		assertEquals(charWidth * 18, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheSecondCharacterOfTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(19, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesBySpaces_whenPositionIsAtTheSecondCharacterOfTheSecondPhysicalLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(19, new Rectangle(0, 0, viewWidth, lineHeight * 2), Backward);

		assertEquals(charWidth, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesWithoutSpaces_whenPositionIsAtTheFirstCharacterOfTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabc\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(26, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(0, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesWithoutSpaces_whenPositionIsAtTheFirstCharacterOfTheSecondPhysicalLine_andBiasBackward_thenModelToViewReturnsThePositionRightOfTheLastCharacterOfTheFirstLine() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabc\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(26, new Rectangle(0, 0, viewWidth, lineHeight * 2), Backward);

		assertEquals(charWidth * 26, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesWithoutSpaces_whenPositionIsAtTheSecondCharacterOfTheSecondPhysicalLine_andBiasForward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabc\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(27, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWrappedOverSeveralPhysicalLinesWithoutSpaces_whenPositionIsAtTheSecondCharacterOfTheSecondPhysicalLine_andBiasBackward_thenModelToViewReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabc\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(27, new Rectangle(0, 0, viewWidth, lineHeight * 2), Backward);

		assertEquals(charWidth, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineWithATabAtTheBeginningFittingWithinAllocationWidth_whenPositionIsAtTheTab_thenModelToViewReturnsThePositionOfTheTab() throws Exception {
		document.replace(0, document.getLength(), "\tword.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(0, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(0, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWithATabAtTheBeginningFittingWithinAllocationWidth_whenPositionIsOnePlaceAfterTheTab_thenModelToViewReturnsThePositionOfTheCharacterAfterTheTab() throws Exception {
		document.replace(0, document.getLength(), "\tword.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(1, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 5, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWithATabNotAtATabStopFittingWithinAllocationWidth_whenPositionIsAtTheTab_thenModelToViewReturnsThePositionOfTheTab() throws Exception {
		document.replace(0, document.getLength(), "wo\trd.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(2, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 2, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineWithATabNotAtATabStopFittingWithinAllocationWidth_whenPositionIsImmediatelyAfterTheTab_thenModelToViewReturnsThePositionOfTheCharacterAfterTheTab() throws Exception {
		document.replace(0, document.getLength(), "wo\trd.\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(3, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), Forward);

		assertEquals(charWidth * 5, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingExactlyWithinAllocationWidthWithATabAtTheEnd_whenPositionIsImmediatelyAfterTheTab_andBiasForward_thenModelToViewReturnsThePositionOfTheNewlineCharacter() throws Exception {
		int viewWidth = charWidth * 10;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdef\t\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(7, new Rectangle(0, 0, viewWidth, lineHeight), Forward);

		assertEquals(charWidth * 10, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingExactlyWithinAllocationWidthWithATabAtTheEnd_whenPositionIsImmediatelyAfterTheTab_andBiasBackward_thenModelToViewReturnsThePositionOfTheNewlineCharacter() throws Exception {
		int viewWidth = charWidth * 10;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdef\t\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(7, new Rectangle(0, 0, viewWidth, lineHeight), Backward);

		assertEquals(charWidth * 10, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingExactlyWithinAllocationWidthAndContainingATab_whenPositionIsImmediatelyAfterTheTab_thenModelToViewReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abc\tdef\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(4, new Rectangle(0, 0, viewWidth, lineHeight), Forward);

		assertEquals(charWidth * 5, screenPosition.x);
		assertEquals(0, screenPosition.y);
	}

	@Test
	public void givenALineFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheFirstLine_whenPositionIsOnTheSecondLine_thenModelToViewReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abc\tde f ghijkl\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(10, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		assertEquals(charWidth * 3, screenPosition.x);
		assertEquals(lineHeight, screenPosition.y);
	}

	@Test
	public void givenALineFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheSecondLine_whenPositionIsAfterTheTabOnTheSecondLine_thenModelToViewReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghij\tklm\n",null);
		View line = view.getView(0);

		Rectangle screenPosition = (Rectangle) line.modelToView(12, new Rectangle(0, 0, viewWidth, lineHeight * 2), Forward);

		System.err.println("(" + screenPosition.x + ", " + screenPosition.y + ")");
		assertEquals(lineHeight, screenPosition.y);
		assertEquals(charWidth * 6, screenPosition.x);
	}
}
