/*
 * 12/12/2008
 *
 * TokenMakerFactory.java - A factory for TokenMakers.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.Map;
import java.util.Set;

import org.fife.ui.rsyntaxtextarea.modes.PlainTextTokenMaker;


/**
 * A factory that maps syntax styles to {@link TokenMaker}s capable of splitting
 * text into tokens for those syntax styles.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class TokenMakerFactory {

	/**
	 * If this system property is set, a custom <code>TokenMakerFactory</code>
	 * of the specified class will be used as the default token maker factory.
	 */
	public static final String PROPERTY_DEFAULT_TOKEN_MAKER_FACTORY	=
														"TokenMakerFactory";

	/**
	 * The singleton default <code>TokenMakerFactory</code> instance.
	 */
	private static TokenMakerFactory DEFAULT_INSTANCE;


	/**
	 * Creates and returns a mapping from keys to the names of
	 * {@link TokenMaker} implementation classes.  When
	 * {@link #getTokenMaker(String)} is called with a key defined in this
	 * map, a <code>TokenMaker</code> of the corresponding type is returned.
	 *
	 * @return The map.
	 */
	protected abstract Map createTokenMakerKeyToClassNameMap();


	/**
	 * Returns the default <code>TokenMakerFactory</code> instance.  This is
	 * the factory used by all {@link RSyntaxDocument}s by default.
	 *
	 * @return The factory.
	 * @see #setDefaultInstance(TokenMakerFactory)
	 */
	public static synchronized TokenMakerFactory getDefaultInstance() {
		if (DEFAULT_INSTANCE==null) {
			String clazz = null;
			try {
				clazz= System.getProperty(PROPERTY_DEFAULT_TOKEN_MAKER_FACTORY);
			} catch (java.security.AccessControlException ace) {
				clazz = null; // We're in an applet; take default.
			}
			if (clazz==null) {
				clazz = "org.fife.ui.rsyntaxtextarea.DefaultTokenMakerFactory";
			}
			try {
				DEFAULT_INSTANCE = (TokenMakerFactory)Class.forName(clazz).
													newInstance();
			} catch (RuntimeException re) { // FindBugs
				throw re;
			} catch (Exception e) {
				e.printStackTrace();
				throw new InternalError("Cannot find TokenMakerFactory: " +
											clazz);
			}
		}
		return DEFAULT_INSTANCE;
	}


	/**
	 * Returns a {@link TokenMaker} for the specified key.
	 *
	 * @param key The key.
	 * @return The corresponding <code>TokenMaker</code>, or
	 *         {@link PlainTextTokenMaker} if none matches the specified key.
	 */
	public final TokenMaker getTokenMaker(String key) {
		TokenMaker tm = getTokenMakerImpl(key);
		if (tm==null) {
			tm = new PlainTextTokenMaker();
		}
		return tm;
	}


	/**
	 * Returns a {@link TokenMaker} for the specified key.
	 *
	 * @param key The key.
	 * @return The corresponding <code>TokenMaker</code>, or <code>null</code>
	 *         if none matches the specified key.
	 */
	protected abstract TokenMaker getTokenMakerImpl(String key);


	/**
	 * Returns the set of keys that this factory maps to token makers.
	 *
	 * return The set of keys.
	 */
	public abstract Set keySet();


	/**
	 * Sets the default <code>TokenMakerFactory</code> instance.  This is
	 * the factory used by all future {@link RSyntaxDocument}s by default.
	 * <code>RSyntaxDocument</code>s that have already been created are not
	 * affected.
	 *
	 * @param tmf The factory.
	 * @throws IllegalArgumentException If <code>tmf</code> is
	 *         <code>null</code>.
	 * @see #getDefaultInstance()
	 */
	public static synchronized void setDefaultInstance(TokenMakerFactory tmf) {
		if (tmf==null) {
			throw new IllegalArgumentException("tmf cannot be null");
		}
		DEFAULT_INSTANCE = tmf;
	}


}