/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link DefaultTokenPainterFactory} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultTokenPainterFactoryTest extends AbstractRSyntaxTextAreaTest {

	private RSyntaxTextArea textArea;
	private DefaultTokenPainterFactory factory;


	@BeforeEach
	void setUp() {
		textArea = createTextArea();
		factory = new DefaultTokenPainterFactory();
	}


	@Test
	void testGetTokenPainter_dontShowWhitespace() {
		Assertions.assertEquals(DefaultTokenPainter.class,
			factory.getTokenPainter(textArea).getClass());
	}


	@Test
	void testGetTokenPainter_showWhitespace() {
		textArea.setWhitespaceVisible(true);
		Assertions.assertEquals(VisibleWhitespaceTokenPainter.class,
			factory.getTokenPainter(textArea).getClass());
	}
}
