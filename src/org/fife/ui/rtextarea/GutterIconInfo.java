/*
 * 02/19/2009
 *
 * GutterIconInfo.java - Information about an Icon in a Gutter.
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
package org.fife.ui.rtextarea;

import javax.swing.Icon;


/**
 * Information about an icon displayed in a {@link Gutter}.  Instances of this
 * class are returned by {@link Gutter#addLineTrackingIcon(int, Icon)} and
 * {@link Gutter#addOffsetTrackingIcon(int, Icon)}.  They can later be used
 * in calls to {@link Gutter#removeTrackingIcon(GutterIconInfo)} to be
 * individually removed.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see Gutter
 */
public interface GutterIconInfo {


	/**
	 * Returns the icon being rendered.
	 *
	 * @return The icon being rendered.
	 */
	public Icon getIcon();


	/**
	 * Returns the offset that is being tracked.  The line of this offset is
	 * where the icon is rendered.  This offset may change as the user types
	 * to track the new location of the marked offset.
	 *
	 * @return The offset being tracked.
	 */
	public int getMarkedOffset();

}