/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;


/**
 * Unit tests for the {@code FontUtil} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
// TODO: Mock various OS code paths when migrating to Java 11/Mockito 5.x
@ExtendWith(SwingRunnerExtension.class)
class FontUtilTest {


	@Test
	void testCreateFont() {
		String family = Font.MONOSPACED;
		int style = Font.PLAIN;
		int size = 10;
		Font font = FontUtil.createFont(family, style, size);
		Assertions.assertEquals(family, font.getFamily());
		Assertions.assertEquals(style, font.getStyle());
		Assertions.assertEquals(size, font.getSize());
	}


	@Test
	void testDeriveFont_2Arg_newFamily() {
		Font baseFont = FontUtil.createFont(Font.SERIF, Font.ITALIC, 10);
		String newFamily = Font.MONOSPACED;
		Font newFont = FontUtil.deriveFont(baseFont, newFamily);
		Assertions.assertEquals(newFamily, newFont.getFamily());
		Assertions.assertEquals(baseFont.getStyle(), newFont.getStyle());
		Assertions.assertEquals(baseFont.getSize(), newFont.getSize());
	}


	@Test
	void testDeriveFont_2Arg_noNewFamily() {
		Font baseFont = FontUtil.createFont(Font.SERIF, Font.ITALIC, 10);
		Font newFont = FontUtil.deriveFont(baseFont, null);
		Assertions.assertEquals(baseFont.getFamily(), newFont.getFamily());
		Assertions.assertEquals(baseFont.getStyle(), newFont.getStyle());
		Assertions.assertEquals(baseFont.getSize(), newFont.getSize());
	}


	@Test
	void testDeriveFont_4Arg_allNew() {
		Font baseFont = FontUtil.createFont(Font.SERIF, Font.ITALIC, 10);
		String newFamily = Font.MONOSPACED;
		int newStyle = Font.BOLD;
		float newSize = 14f;
		Font newFont = FontUtil.deriveFont(baseFont, newFamily, newStyle, newSize);
		Assertions.assertEquals(newFamily, newFont.getFamily());
		Assertions.assertEquals(newStyle, newFont.getStyle());
		Assertions.assertEquals(newSize, newFont.getSize());
	}


	@Test
	void testDeriveFont_4Arg_noChange() {
		Font baseFont = FontUtil.createFont(Font.SERIF, Font.ITALIC, 10);
		Font newFont = FontUtil.deriveFont(baseFont, null, null, null);
		Assertions.assertEquals(baseFont.getFamily(), newFont.getFamily());
		Assertions.assertEquals(baseFont.getStyle(), newFont.getStyle());
		Assertions.assertEquals(baseFont.getSize(), newFont.getSize());
	}


	@Test
	void testGetDefaultMonospacedFont() {
		// Unfortunately we can't test platform-specific code paths
		// even with setting os.name, since that causes specific
		// sun.awt.*FontManager class implementations to be looked
		// that are platform-specific and not in all JREs.
		Font font = FontUtil.getDefaultMonospacedFont();
		Assertions.assertNotNull(font);
	}
}
