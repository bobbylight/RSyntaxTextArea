package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.RTextAreaBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.swing.JPanel;
import javax.swing.text.Position;
import javax.swing.text.View;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
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
public class WrappedSyntaxViewMonospacedViewToModelTest
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

	public WrappedSyntaxViewMonospacedViewToModelTest(int fontSize)
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
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtTheStartOfTheLine_thenViewToModelReturnsThePositionOfTheFirstCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(0, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(0, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsLeftOfTheCentreOfTheFirstCharacter_thenViewToModelReturnsThePositionOfTheFirstCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth / 2f - 1f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(0, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsRightOfTheCentreOfTheFirstCharacter_thenViewToModelReturnsThePositionOfTheSecondCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth / 2f + 1f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(1, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAtTheStartOfACharacter_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 7f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(7, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsLeftOfTheCentreOfACharacter_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 7.5f - 1f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(7, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsRightOfTheCentreOfACharacter_thenViewToModelReturnsThePositionOfTheNextCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 7.5f + 1f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(8, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAfterTheLastCharacter_thenViewToModelReturnsThePositionOfTheLastCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 70f, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(65, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionIsAfterTheViewWidth_thenViewToModelReturnsThePositionOfTheLastCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(65, offset);
	}

	@Test
	public void givenTwoLinesFittingWithinAllocationWidth_whenPositionIsAfterTheLastCharacterOfTheSecondLine_thenViewToModelReturnsThePositionOfTheLastCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\nShort line.\n",null);
		View line = view.getView(1);

		int offset = line.viewToModel(charWidth * 20f, lineHeight * 1.5f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(77, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionAndAllocAreOffsetInYAxis_andPositionIsLeftOfTheCentreOfTheCharacter_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 10f + 2f, 120.0f + lineHeight / 2f, new Rectangle(0, 120, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(10, offset);
	}

	@Test
	public void givenALineFittingWithinAllocationWidth_whenPositionAndAllocAreOffsetInYAxis_andPositionIsAfterTheViewWidth_thenViewToModelReturnsThePositionOfTheLastCharacter() throws Exception {
		document.replace(0, document.getLength(), "This is a line of text. There are a number of words on this line.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, 120.0f + lineHeight / 2f, new Rectangle(0, 120, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(65, offset);
	}

	@Test
	public void givenALineWithWhitespaceFittingExactlyWithinAllocationWidth_whenPositionIsWithinACharacterOfTheLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 23;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "This is a line of text.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 14f + 2f, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight), new Position.Bias[1]);

		assertEquals(14, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceFittingExactlyWithinAllocationWidth_whenPositionIsWithinACharacterOfTheLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 52;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 14f + 2f, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight), new Position.Bias[1]);

		assertEquals(14, offset);
	}

	@Test
	public void givenALineWithWhitespaceFittingExactlyWithinAllocationWidth_whenPositionIsAfterTheEndOfTheLine_thenViewToModelReturnsThePositionAfterTheLastCharacter() throws Exception {
		int viewWidth = charWidth * 23;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "This is a line of text.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(viewWidth + 100, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight), new Position.Bias[1]);

		assertEquals(23, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceFittingExactlyWithinAllocationWidth_whenPositionIsAfterTheEndOfTheLine_thenViewToModelReturnsThePositionAfterTheLastCharacter() throws Exception {
		int viewWidth = charWidth * 52;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(viewWidth + 100, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight), new Position.Bias[1]);

		assertEquals(52, offset);
	}

	@Test
	public void givenALineWithWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsWithinACharacterOfTheSecondPhysicalLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "This is a line of text which will be wrapped over several lines due to its excessive length.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 14f + 2f, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 4), new Position.Bias[1]);

		assertEquals(37, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsWithinACharacterOfTheSecondPhysicalLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 20;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 14f + 2f, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 4), new Position.Bias[1]);

		assertEquals(34, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedExactlyOverSeveralPhysicalLines_whenPositionIsWithinACharacterOfTheSecondPhysicalLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 14f + 2f, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 4), new Position.Bias[1]);

		assertEquals(40, offset);
	}

	@Test
	public void givenALineWithWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheEndOfTheFirstLine_thenViewToModelReturnsThePositionOfTheFirstCharacterOfTheSecondLine() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 20, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(17, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheEndOfTheSecondLine_thenViewToModelReturnsThePositionAfterTheLastCharacterOfTheSecondLine() throws Exception {
		int viewWidth = charWidth * 35;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 22, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(52, offset);
	}

	@Test
	public void givenALineWithWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheViewWidthOnTheFirstLine_thenViewToModelReturnsThePositionOfTheLastCharacterOnTheLine() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(17, offset);
	}

	@Test
	public void givenALineWithWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheViewWidthOnTheSecondLine_thenViewToModelReturnsThePositionOfTheLastCharacterOnTheLine() throws Exception {
		int viewWidth = charWidth * 25;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "xxxx xx x xxxx xx xxxxxxxxxxx xxxxxx xxxx.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(42, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheViewWidthOnTheFirstLine_thenViewToModelReturnsThePositionOfTheNextToLastCharacterOnTheLine() throws Exception {
		int viewWidth = charWidth * 20;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight / 2f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(19, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedOverSeveralPhysicalLines_whenPositionIsAfterTheViewWidthOnTheSecondLine_thenViewToModelReturnsThePositionOfTheNextToLastCharacterOnTheLine() throws Exception {
		int viewWidth = charWidth * 20;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(39, offset);
	}

	@Test
	public void givenALineWithNoWhitespaceWrappedExactlyOverSeveralPhysicalLines_whenPositionIsAfterTheViewWidthOnTheSecondLine_thenViewToModelReturnsThePositionOfTheLastCharacterOnTheLine() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(VIEW_WIDTH + 100, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(52, offset);
	}

	@Test
	public void givenALineWrappedExactlyOverSeveralPhysicalLines_whenPositionIsWithinAWrappedLine_thenViewToModelReturnsThePositionOfTheCharacter() throws Exception {
		int viewWidth = charWidth * 26;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 10, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(36, offset);
	}

	@Test
	public void givenALineWithATabAtTheBeginningFittingWithinAllocationWidth_whenPositionIsAtTheEndOfTheTab_thenViewToModelReturnsThePositionOfTheLetterAfterTheTab() throws Exception {
		document.replace(0, document.getLength(), "\tword.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 5, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(1, offset);
	}

	@Test
	public void givenALineWithATabNotAtATabStopFittingWithinAllocationWidth_whenPositionIsAfterFirstCharOfTheTab_thenViewToModelReturnsThePositionOfTheTab() throws Exception {
		document.replace(0, document.getLength(), "wo\trd.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 3, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(2, offset);
	}

	@Test
	public void givenALineWithATabNotAtATabStopFittingWithinAllocationWidth_whenPositionIsAfterSecondCharOfTheTab_thenViewToModelReturnsThePositionOfTheLetterAfterTheTab() throws Exception {
		document.replace(0, document.getLength(), "wo\trd.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 4, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(3, offset);
	}

	@Test
	public void givenALineWithATabNotAtATabStopFittingWithinAllocationWidth_whenPositionIsAtTheEndOfTheTab_thenViewToModelReturnsThePositionOfTheLetterAfterTheTab() throws Exception {
		document.replace(0, document.getLength(), "wo\trd.\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 5, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(3, offset);
	}

	@Test
	public void givenALineFittingExactlyWithinAllocationWidthWithATabAtTheEnd_whenPositionIsAfterTheTab_thenViewToModelReturnsThePositionAfterTheTab() throws Exception {
		int viewWidth = charWidth * 10;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdef\t\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 10, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(7, offset);
	}

	@Test
	public void givenALineFittingExactlyWithinAllocationWidthAndContainingATab_whenPositionIsAfterTheTab_thenViewToModelReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abc\tdef\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 6, lineHeight / 2f, new Rectangle(0, 0, VIEW_WIDTH, lineHeight), new Position.Bias[1]);

		assertEquals(5, offset);
	}

	@Test
	public void givenALineFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheFirstLine_whenPositionIsOnTheSecondLine_thenViewToModelReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abc\tde f ghijkl\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 3, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(10, offset);
	}

	@Test
	public void givenALineFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheSecondLine_whenPositionIsOnTheSecondLine_thenViewToModelReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefghij\tklm\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 6, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(12, offset);
	}

	@Test
	public void givenALineWithMultipleTokensFittingExactlyInTwoPhysicalLinesAndContainingATabOnTheSecondLine_whenPositionIsOnTheSecondLine_thenViewToModelReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 8;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abc def gh\tijk\n",null);
		View line = view.getView(0);

		int offset = line.viewToModel(charWidth * 6, lineHeight * 1.5f, new Rectangle(0, 0, viewWidth, lineHeight * 2), new Position.Bias[1]);

		assertEquals(12, offset);
	}

	@Test
	public void givenTwoLinesWhereTheSecondHasASingleTokenOverFourPhysicalLines_whenASpaceIsInsertedInTheSecondLogicalLine_andPositionIsAfterWhereTheSpaceWasInserted_thenViewToModelReturnsThePosition() throws Exception {
		int viewWidth = charWidth * 10;
		view.setSize(viewWidth, VIEW_HEIGHT);
		document.replace(0, document.getLength(), "abcdefg\nabcdefghij1234567890abcdefghij1234567890\n",null);
		document.insertString(27, " ", null);
		View line = view.getView(1);

		int offset = line.viewToModel(charWidth * 8, lineHeight * 3.5f, new Rectangle(0, 0, viewWidth, lineHeight * 4), new Position.Bias[1]);

		assertEquals(46, offset);
	}
}
