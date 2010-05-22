/*
 * 07/31/2009
 *
 * AbstractParser.java - A base implementation for parsers.
 * Copyright (C) 2009 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.parser;

import java.net.URL;

import org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip;


/**
 * A base class for {@link Parser} implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractParser implements Parser {

	/**
	 * Whether this parser is enabled.  If this is <code>false</code>, then
	 * this parser will not be run.
	 */
	private boolean enabled;

	/**
	 * Listens for events from {@link FocusableTip}s generated from this
	 * parser's notices.
	 */
	private ExtendedHyperlinkListener linkListener;


	/**
	 * Constructor.
	 */
	protected AbstractParser() {
		setEnabled(true);
	}


	/**
	 * {@inheritDoc}
	 */
	public ExtendedHyperlinkListener getHyperlinkListener() {
		return linkListener;
	}


	/**
	 * Returns <code>null</code>.  Parsers that wish to show images in their
	 * tool tips should override this method to return the image base URL.
	 *
	 * @return <code>null</code> always.
	 */
	public URL getImageBase() {
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * Toggles whether this parser is enabled.
	 *
	 * @param enabled Whether this parser is enabled.
	 * @see #isEnabled()
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	/**
	 * Returns the listener for this parser.
	 *
	 * @param listener The new listener.
	 * @see #getHyperlinkListener()
	 */
	public void setHyperlinkListener(ExtendedHyperlinkListener listener) {
		linkListener = listener;
	}


}