/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Unit tests for the {@link SyntaxScheme} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SyntaxSchemeTest {


	@Test
	void testClone() {

		SyntaxScheme scheme = new SyntaxScheme(true);
		SyntaxScheme scheme2 = (SyntaxScheme)scheme.clone();

		Assertions.assertEquals(scheme, scheme2);
	}


	@Test
	void testGetSetStyle() {

		SyntaxScheme scheme = new SyntaxScheme(true);

		Assertions.assertNotNull(scheme.getStyle(TokenTypes.COMMENT_EOL));
		Style style = new Style(Color.RED, Color.BLUE);

		scheme.setStyle(TokenTypes.COMMENT_EOL, style);
		Style style2 = scheme.getStyle(TokenTypes.COMMENT_EOL);
		Assertions.assertEquals(style, style2);
	}


	@Test
	void testGetStylesAndGetStyleCount() {

		SyntaxScheme scheme = new SyntaxScheme(true);
		Assertions.assertEquals(scheme.getStyleCount(), scheme.getStyles().length);
	}


	@Test
	void testGetHashCode() {
		Assertions.assertNotEquals(0, new SyntaxScheme(true).hashCode());
	}


	@Test
	void testCreateAndLoadFromString() {

		SyntaxScheme scheme = new SyntaxScheme(true);
		String string = scheme.toCommaSeparatedString();

		SyntaxScheme scheme2 = SyntaxScheme.loadFromString(string);
		Assertions.assertEquals(scheme, scheme2);
	}


	@Test
	void testLoad() throws IOException {

		String xml = "<scheme>\n" +
				"<style token='COMMENT_EOL' fg='$000000' bg='$f0f0f0' bold='true' italic='true' underline='true'/>\n" +
			"</scheme>";

		Font baseFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
		InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		SyntaxScheme scheme = SyntaxScheme.load(baseFont, in);
	}
}
