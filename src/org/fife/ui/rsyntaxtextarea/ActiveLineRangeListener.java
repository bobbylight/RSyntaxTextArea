/*
 * 02/06/2011
 *
 * ActiveLineRangeListener.java - Listens for "active line range" changes
 * in an RSyntaxTextArea.
 * Copyright (C) 2011 Robert Futrell
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

import java.util.EventListener;


/**
 * Listens for "active line range" events from an <code>RSyntaxTextArea</code>.
 * If a text area contains some semantic knowledge of the programming language
 * being edited, it may broadcast {@link ActiveLineRangeEvent}s whenever the
 * caret moves into a new "block" of code.  Listeners can listen for these
 * events and respond accordingly.<p>
 * 
 * See the <code>RSTALanguageSupport</code> project at
 * <a href="http://fifesoft.com">http://fifesoft.com</a> for some
 * <code>LanguageSupport</code> implementations that may broadcast these
 * events.  Note that if an RSTA/LanguageSupport does not support broadcasting
 * these events, the listener will simply never receive any notifications.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ActiveLineRangeListener extends EventListener {


	/**
	 * Called whenever the "active line range" changes.
	 *
	 * @param e Information about the line range change.  If there is no longer
	 *        an "active line range," the "minimum" and "maximum" line values
	 *        should both be <code>-1</code>.
	 */
	public void activeLineRangeChanged(ActiveLineRangeEvent e);


}