/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code TokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TokenMakerFactoryTest {

	private TokenMakerFactory origTmf;


	@BeforeEach
	public void setUp() {
		origTmf = TokenMakerFactory.getDefaultInstance();
	}


	@AfterEach
	public void tearDown() {
		TokenMakerFactory.setDefaultInstance(origTmf);
	}


	@Test
	public void testGetDefaultInstance() {
		Assertions.assertNotNull(TokenMakerFactory.getDefaultInstance());
	}


	@Test
	public void testGetTokenMaker_badKey() {
		TokenMakerFactory tmf = TokenMakerFactory.getDefaultInstance();
		Assertions.assertNotNull(tmf.getTokenMaker("invalidKey"));
	}


	@Test
	public void testKeySet() {
		Assertions.assertFalse(TokenMakerFactory.getDefaultInstance().keySet().isEmpty());
	}


	@Test
	public void testSetDefaultInstance() {

		Assertions.assertFalse(TokenMakerFactory.getDefaultInstance() instanceof TestTokenMakerFactory);

		TokenMakerFactory tmf = new TestTokenMakerFactory();
		TokenMakerFactory.setDefaultInstance(tmf);
		Assertions.assertTrue(TokenMakerFactory.getDefaultInstance() instanceof TestTokenMakerFactory);
	}


	@Test
	public void testSetDefaultInstance_passNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> TokenMakerFactory.setDefaultInstance(null));
	}


	private static class TestTokenMakerFactory extends AbstractTokenMakerFactory {

		@Override
		protected void initTokenMakerMap() {
		}
	}
}
