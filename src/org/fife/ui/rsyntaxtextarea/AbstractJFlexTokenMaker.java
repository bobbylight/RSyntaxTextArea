/*
 * 03/23/2005
 *
 * AbstractJFlexTokenMaker.java - Base class for token makers generated from
 * programs such as JFlex.
 * Copyright (C) 2005 Robert Futrell
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

import javax.swing.text.Segment;


/**
 * Base class for JFlex-generated token makers.  This class attempts to factor
 * out all common code from these classes.  Many methods <em>almost</em> could
 * be factored out into this class, but cannot because they reference JFlex
 * variables that we cannot access from this class.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public abstract class AbstractJFlexTokenMaker extends TokenMakerBase {

	protected Segment s;

	protected int start;		// Just for states.
	protected int offsetShift;	// As parser always starts at 0, but our line doesn't.


}