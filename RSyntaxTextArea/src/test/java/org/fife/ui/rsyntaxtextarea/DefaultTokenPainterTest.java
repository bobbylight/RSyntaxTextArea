/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.TabExpander;
import java.awt.*;


/**
 * Unit tests for the {@link DefaultTokenPainter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class DefaultTokenPainterTest extends AbstractRSyntaxTextAreaTest {

	private RSyntaxTextArea textArea;
	private Graphics2D g2d;
	private TabExpander e;


	@BeforeEach
	void setUp() {
		textArea = createTextArea();
		textArea.addNotify();
		textArea.setAntiAliasingEnabled(false); // Needed to create font metrics cache
		g2d  = createTestGraphics();
		e = (x, tabOffset) -> x + 50;
	}


	@Test
	void testNextX_noTabs() {
		char[] chars = "// this is a comment".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.COMMENT_DOCUMENTATION, 0);
		DefaultTokenPainter dtp = new DefaultTokenPainter();
		float paintX = dtp.paintImpl(t, g2d, 0, 0, textArea, e, 0, false, false);
		float nextXX = dtp.nextX(t, t.length(), 0, textArea, e);

		Assertions.assertEquals(paintX, nextXX, 0.0001);
	}


	@Test
	void testNextX_withTabs_startingAtZeroXOffs() {
		char[] chars = "\t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.WHITESPACE, 0);
		DefaultTokenPainter dtp = new DefaultTokenPainter();
		float paintX = dtp.paintImpl(t, g2d, 0, 0, textArea, e, 0, false, false);
		float nextXX = dtp.nextX(t, t.length(), 0, textArea, e);

		Assertions.assertEquals(paintX, nextXX, 0.0001);
	}


	@Test
	void testNextX_withTabs_startingAtNonZeroXOffs() {
		char[] chars = "\t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.WHITESPACE, 0);
		DefaultTokenPainter dtp = new DefaultTokenPainter();

		for (int xOffs = 0; xOffs < 20; xOffs++) {
			float paintX = dtp.paintImpl(t, g2d, xOffs, 0, textArea, e, 0, false, false);
			float nextXX = dtp.nextX(t, t.length(), xOffs, textArea, e);

			Assertions.assertEquals(paintX, nextXX, 0.0001,
				"unexpected value from nextX for xOffs: " + xOffs);
		}
	}


	@Test
	void testPaint_6args_matches_7args_with_0_clipStart() {
		char[] chars = "\t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.WHITESPACE, 0);
		DefaultTokenPainter dtp = new DefaultTokenPainter();

		float result6Arg = dtp.paint(t, g2d, 0, 0, textArea, e);
		float result7Arg = dtp.paint(t, g2d, 0, 0, textArea, e, 0);
		Assertions.assertEquals(result6Arg, result7Arg);
	}


	@Test
	void testPaint_7args_matches_8args_with_0_clipStart() {
		char[] chars = "\t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.WHITESPACE, 0);
		DefaultTokenPainter dtp = new DefaultTokenPainter();

		float result6Arg = dtp.paint(t, g2d, 0, 0, textArea, e, 0);
		float result7ArgNoBG = dtp.paint(t, g2d, 0, 0, textArea, e, 0, false);
		Assertions.assertEquals(result6Arg, result7ArgNoBG);

		float result7ArgWithBG = dtp.paint(t, g2d, 0, 0, textArea, e, 0, true);
		Assertions.assertEquals(result6Arg, result7ArgWithBG);
	}


	@Test
	void testPaintImpl_notSelected_noSTC() {
		testPaintImplImpl(textArea, g2d, e, false, false);
	}


	@Test
	void testPaintImpl_notSelected_sTC() {
		testPaintImplImpl(textArea, g2d, e, false, true);
	}


	@Test
	void testPaintImpl_selected_noSTC() {
		testPaintImplImpl(textArea, g2d, e, true, false);
	}


	@Test
	void testPaintImpl_selected_sTC() {
		testPaintImplImpl(textArea, g2d, e, true, true);
	}


	private static void testPaintImplImpl(RSyntaxTextArea textArea, Graphics2D g2d,
										  TabExpander e, boolean selected, boolean useSTC) {
		// For running through long code path
		Style style = textArea.getSyntaxScheme().getStyle(TokenTypes.COMMENT_DOCUMENTATION);
		style.background = Color.PINK;
		style.underline = true;

		char[] chars = "\t\tThis is an   unrealistic\ftoken\t\tbut whatever".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.COMMENT_DOCUMENTATION, 0);
		new DefaultTokenPainter().paintImpl(t, g2d, 0, 0, textArea, e, 0,
			selected, useSTC);
	}


	@Test
	void testPaintSelected_noClipStart() {
		char[] chars = "foobar".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		new DefaultTokenPainter().paintSelected(t, g2d, 0, 0, textArea, e, false);
	}


	@Test
	void testPaintSelected_withClipStart() {
		char[] chars = "foobar".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		new DefaultTokenPainter().paintSelected(t, g2d, 0, 0, textArea, e, 0, false);
	}
}
