/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.fife.ui.SwingRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.TabExpander;
import java.awt.*;

/**
 * Unit tests for the {@link DefaultTokenPainter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class DefaultTokenPainterTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintImpl_notSelected_noSTC() {
		testPaintImplImpl(false, false);
	}


	@Test
	public void testPaintImpl_notSelected_sTC() {
		testPaintImplImpl(false, true);
	}


	@Test
	public void testPaintImpl_selected_noSTC() {
		testPaintImplImpl(true, false);
	}


	@Test
	public void testPaintImpl_selected_sTC() {
		testPaintImplImpl(true, true);
	}


	private static void testPaintImplImpl(boolean selected, boolean useSTC) {

		RSyntaxTextArea textArea = createTextArea();
		textArea.addNotify();
		textArea.setAntiAliasingEnabled(false); // Needed to create font metrics cache
		Graphics2D g2d  = createTestGraphics();
		TabExpander e = (x, tabOffset) -> x + 5;

		// For running through long code path
		Style style = textArea.getSyntaxScheme().getStyle(TokenTypes.COMMENT_DOCUMENTATION);
		style.background = Color.PINK;
		style.underline = true;

		char[] chars = "\t\tThis is an   unrealistic\ftoken\t\tbut whatever".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.COMMENT_DOCUMENTATION, 0);
		new DefaultTokenPainter().paintImpl(t, g2d, 0, 0, textArea, e, 0,
			selected, useSTC);
	}
}
