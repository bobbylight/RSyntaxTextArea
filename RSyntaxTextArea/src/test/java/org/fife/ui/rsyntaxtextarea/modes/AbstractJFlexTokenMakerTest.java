/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;


/**
 * Utility methods for {@code TokenMaker}s built from JFlex.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractJFlexTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	void testConstructor_inputStream() throws Exception {
		String className = this.getClass().getName().replace("Test", "");
		Class<?> clazz = Class.forName(className);
		try {
			Constructor<?> c = clazz.getConstructor(InputStream.class);
			c.newInstance(new EmptyInputStream());
		} catch (NoSuchMethodException nsme) {
			// Do nothing; this is just a subclass of another TokenMaker without that constructor
		}
	}


	@Test
	void testConstructor_reader() throws Exception {
		String className = this.getClass().getName().replace("Test", "");
		Class<?> clazz = Class.forName(className);
		try {
			Constructor<?> c = clazz.getConstructor(Reader.class);
			c.newInstance(new StringReader(""));
		} catch (NoSuchMethodException nsme) {
			// Do nothing; this is just a subclass of another TokenMaker without that constructor
		}
	}


	@Test
	public void testCommon_yycharat() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			Segment segment = createSegment("foo\nbar");
			tm.getTokenList(segment, TokenTypes.NULL, 0);
			// We always parse entire lines so the next token is empty
			Assertions.assertEquals('\n', ((AbstractJFlexTokenMaker)tm).yycharat(0));
		}
	}


	@Test
	void testCommon_yyclose() throws IOException {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			((AbstractJFlexTokenMaker)tm).yyclose();
		}
	}


	@Test
	void testCommon_yylength() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			Segment segment = createSegment("foo");
			tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(0, ((AbstractJFlexTokenMaker)tm).yylength());
		}
	}


	@Test
	void testCommon_yypushback_valid() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			((AbstractJFlexTokenMaker)tm).yypushback(0);
			Assertions.assertTrue(true); // Needed for SpotBugs
		}
	}


	@Test
	void testCommon_yypushback_invalid() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			Assertions.assertThrows(Error.class, () ->
				((AbstractJFlexTokenMaker)tm).yypushback(1)
			);
		}
	}


	@Test
	void testCommon_yystate() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			Segment segment = createSegment("foo");
			tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(0, ((AbstractJFlexTokenMaker)tm).yystate());
		}
	}


	@Test
	void testCommon_yytext() {
		TokenMaker tm = createTokenMaker();
		if (tm instanceof AbstractJFlexTokenMaker) {
			Segment segment = createSegment("foo");
			tm.getTokenList(segment, TokenTypes.NULL, 0);
			// We always parse entire lines so the next token is empty
			Assertions.assertEquals("", ((AbstractJFlexTokenMaker)tm).yytext());
		}
	}

	private static final class EmptyInputStream extends InputStream {
		@Override
		public int read() {
			return -1;
		}
	}
}
