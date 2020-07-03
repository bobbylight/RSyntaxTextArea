/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@code TokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TokenMakerFactoryTest {

	private TokenMakerFactory origTmf;


	@Before
	public void setUp() {
		origTmf = TokenMakerFactory.getDefaultInstance();
	}


	@After
	public void tearDown() {
		TokenMakerFactory.setDefaultInstance(origTmf);
	}


	@Test
	public void testGetDefaultInstance() {
		Assert.assertNotNull(TokenMakerFactory.getDefaultInstance());
	}


	@Test
	public void testGetTokenMaker_badKey() {
		TokenMakerFactory tmf = TokenMakerFactory.getDefaultInstance();
		Assert.assertNotNull(tmf.getTokenMaker("invalidKey"));
	}


	@Test
	public void testKeySet() {
		Assert.assertFalse(TokenMakerFactory.getDefaultInstance().keySet().isEmpty());
	}


	@Test
	public void testSetDefaultInstance() {

		Assert.assertFalse(TokenMakerFactory.getDefaultInstance() instanceof TestTokenMakerFactory);

		TokenMakerFactory tmf = new TestTokenMakerFactory();
		TokenMakerFactory.setDefaultInstance(tmf);
		Assert.assertTrue(TokenMakerFactory.getDefaultInstance() instanceof TestTokenMakerFactory);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDefaultInstance_passNull() {
		TokenMakerFactory.setDefaultInstance(null);
	}


	private static class TestTokenMakerFactory extends AbstractTokenMakerFactory {

		@Override
		protected void initTokenMakerMap() {
		}
	}
}
