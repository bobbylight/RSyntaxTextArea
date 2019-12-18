/*
 * 09/20/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;


/**
 * Utility classes for unit tests for <code>TokenMaker</code> implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractTokenMakerTest {


	/**
	 * Creates a <code>Segment</code> from a <code>String</code>.
	 *
	 * @param code The string representing some code.
	 * @return The code, as a <code>Segment</code>.
	 */
	protected Segment createSegment(String code) {
		return new Segment(code.toCharArray(), 0, code.length());
	}


	/**
	 * Verifies whether the line comment delimiters returned by this token maker are
	 * correct for the primary language.
	 * correct for the primary language.
	 */
	public abstract void testGetLineCommentStartAndEnd();
}
