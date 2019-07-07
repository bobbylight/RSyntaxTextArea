/*
 * 10/28/2004
 *
 * DefaultTokenFactory.java - Default token factory.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.Segment;


/**
 * This class generates tokens for a {@link TokenMaker}.  This class is here
 * because it reuses tokens when they aren't needed anymore to prevent
 * This class doesn't actually create new tokens every time
 * <code>createToken</code> is called.  Instead, it internally keeps a stack of
 * available already-created tokens.  When more tokens are needed to properly
 * display a line, more tokens are added to the available stack.  This saves
 * from needless repetitive memory allocation. However, it makes it IMPERATIVE
 * that users call <code>resetTokenList</code> when creating a new token list so
 * that the token maker can keep an accurate list of available tokens.<p>
 *
 * NOTE:  This class should only be used by {@link TokenMaker}; nobody else
 * needs it!
 *
 * @author Robert Futrell
 * @version 0.1
 */
class DefaultTokenFactory implements TokenFactory {

	private int size;
	private int increment;
	private TokenImpl[] tokenList;
	private int currentFreeToken;

	protected static final int DEFAULT_START_SIZE	= 30;
	protected static final int DEFAULT_INCREMENT		= 10;


	/**
	 * Constructor.
	 */
	DefaultTokenFactory() {
		this(DEFAULT_START_SIZE, DEFAULT_INCREMENT);
	}


	/**
	 * Constructor.
	 *
	 * @param size The initial number of tokens in this factory.
	 * @param increment How many tokens to increment by when the stack gets
	 *        empty.
	 */
	DefaultTokenFactory(int size, int increment) {

		this.size = size;
		this.increment = increment;
		this.currentFreeToken = 0;

		// Give us some tokens to initially work with.
		tokenList = new TokenImpl[size];
		for (int i=0; i<size; i++) {
			tokenList[i] = new TokenImpl();
		}

	}


	/**
	 * Adds tokens to the internal token list.  This is called whenever a
	 * request is made and no more tokens are available.
	 */
	private void augmentTokenList() {
		TokenImpl[] temp = new TokenImpl[size + increment];
		System.arraycopy(tokenList,0, temp,0, size);
		size += increment;
		tokenList = temp;
		for (int i=0; i<increment; i++) {
			tokenList[size-i-1] = new TokenImpl();
		}
		//System.err.println("... size up to: " + size);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenImpl createToken() {
		TokenImpl token = tokenList[currentFreeToken];
		token.text = null;
		token.setType(Token.NULL);
		token.setOffset(-1);
		token.setNextToken(null);
		currentFreeToken++;
		if (currentFreeToken==size) {
			augmentTokenList();
		}
		return token;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenImpl createToken(final Segment line, final int beg,
					final int end, final int startOffset, final int type) {
		return createToken(line.array, beg,end, startOffset, type);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenImpl createToken(final char[] line, final int beg,
					final int end, final int startOffset, final int type) {
		TokenImpl token = tokenList[currentFreeToken];
		token.set(line, beg,end, startOffset, type);
		currentFreeToken++;
		if (currentFreeToken==size) {
			augmentTokenList();
		}
		return token;
	}


	/**
	 * Resets the state of this token maker.  This method should be called
	 * by the <code>TokenMaker</code> every time a token list is generated for
	 * a new line so the tokens can be reused.
	 */
	@Override
	public void resetAllTokens() {
		currentFreeToken = 0;
	}


}
