/*
 * 09/23/2005
 *
 * ParserNotice.java - A notice (i.e, and error or warning) from a parser.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.awt.Color;


/**
 * A notice (e.g., a warning or error) from a parser.<p>
 *
 * Since different parsers have different levels of precision when it comes
 * to identifying errors in code, this class supports marking parser notices
 * on either a per-line basis or arbitrary regions of a document.  For any
 * <code>ParserNotice</code>, {@link #getLine()} is guaranteed to return the
 * (primary) line containing the notice, but {@link #getOffset()} and
 * {@link #getLength()} are allowed to return <code>-1</code> if that
 * particular notice isn't mapped to a specific region of code.  Applications
 * can check whether an instance of this class only has line-level information
 * with the
 *
 * @author Robert Futrell
 * @version 1.0
 * @see DefaultParserNotice
 */
public interface ParserNotice extends Comparable<ParserNotice> {

	/**
	 * Returns whether this parser notice contains the specified location
	 * in the document.
	 *
	 * @param pos The position in the document.
	 * @return Whether the position is contained.  This will always return
	 *         <code>false</code> if {@link #getOffset()} returns
	 *         <code>-1</code>.
	 */
	boolean containsPosition(int pos);


	/**
	 * Returns the color to use when painting this notice.
	 *
	 * @return The color.
	 */
	Color getColor();


	/**
	 * Returns the length of the code the message is concerned with.
	 *
 	 * @return The length of the code the message is concerned with, or
 	 *         <code>-1</code> if unknown.
 	 * @see #getOffset()
 	 * @see #getLine()
	 */
	int getLength();


	/**
	 * Returns the level of this notice.
	 *
	 * @return A value from the {@link Level} enumeration.
	 */
	Level getLevel();


	/**
	 * Returns the line number the notice is about.
	 *
	 * @return The line number.
	 */
	int getLine();


	/**
	 * Returns whether this parser notice has offset and length information
	 * (as opposed to just what line number to mark).
	 *
	 * @return Whether the offset and length of the notice are specified.
	 * @see #getLine()
	 * @see #getOffset()
	 * @see #getLength()
	 */
	boolean getKnowsOffsetAndLength();


	/**
	 * Returns the message from the parser.
	 *
	 * @return The message from the parser.
	 */
	String getMessage();


	/**
	 * Returns the offset of the code the message is concerned with.
	 *
	 * @return The offset, or <code>-1</code> if unknown.
	 * @see #getLength()
	 * @see #getLine()
	 */
	int getOffset();


	/**
	 * Returns the parser that created this message.
	 *
	 * @return The parser.
	 */
	Parser getParser();


	/**
	 * Whether a squiggle underline should be drawn in the editor for this
	 * notice.
	 *
	 * @return Whether a squiggle underline should be drawn.
	 */
	boolean getShowInEditor();


	/**
	 * Returns the tool tip text to display for this notice.
	 *
	 * @return The tool tip text.  If none has been explicitly set, this
	 *         method returns the same text as {@link #getMessage()}.
	 */
	String getToolTipText();


	/**
	 * Denotes the severity of a parser notice.
	 */
	enum Level {

		/**
		 * Indicates an informational notice.
		 */
		INFO(2),

		/**
		 * Indicates a warning notice.
		 */
		WARNING(1),

		/**
		 * Indicates an error notice.
		 */
		ERROR(0);

		private int value;

		Level(int value) {
			this.value = value;
		}

		/**
		 * Returns the value of this notice level, as an integer.
		 *
		 * @return A numeric value for this notice level.
		 */
		public int getNumericValue() {
			return value;
		}

		/**
		 * Returns whether this level is as sever as, or worse than, another
		 * level.
		 *
		 * @param other The other level.
		 * @return Whether this level is equal to or more severe.
		 */
		public boolean isEqualToOrWorseThan(Level other) {
			return value<=other.getNumericValue();
		}

	}


}