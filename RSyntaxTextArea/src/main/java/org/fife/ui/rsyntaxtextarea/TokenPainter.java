/*
 * 03/16/2013
 *
 * TokenPainter - Renders tokens in an instance of RSyntaxTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Graphics2D;

import javax.swing.text.TabExpander;


/**
 * Renders tokens in an instance of {@link RSyntaxTextArea}.  One instance
 * may render tokens "regularly," another may render visible whitespace, for
 * example.
 *
 * @author Robert Futrell
 * @version 1.0
 */
interface TokenPainter {


	/**
	 * Paints this token.
	 *
	 * @param token The token to render.
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	float paint(Token token, Graphics2D g, float x, float y,
						RSyntaxTextArea host, TabExpander e);


	/**
	 * Paints this token.
	 *
	 * @param token The token to render.
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param clipStart The left boundary of the clip rectangle in which we're
	 *        painting.  This optimizes painting by allowing us to not paint
	 *        paint when this token is "to the left" of the clip rectangle.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	float paint(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart);


	/**
	 * Paints this token.
	 *
	 * @param token The token to render.
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param clipStart The left boundary of the clip rectangle in which we're
	 *        painting.  This optimizes painting by allowing us to not paint
	 *        paint when this token is "to the left" of the clip rectangle.
	 * @param paintBG Whether to paint the background.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	float paint(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean paintBG);


	/**
	 * Paints this token as it should appear in a selected region of text
	 * (assuming painting with a selection-foreground color is enabled in the
	 * parent <code>RSyntaxTextArea</code>).
	 *
	 * @param token The token to render.
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param useSTC Whether to use the text area's "selected text color."
	 * @return The x-coordinate representing the end of the painted text.
	 */
	float paintSelected(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, boolean useSTC);


	/**
	 * Paints this token as it should appear in a selected region of text
	 * (assuming painting with a selection-foreground color is enabled in the
	 * parent <code>RSyntaxTextArea</code>).
	 *
	 * @param token The token to render.
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param clipStart The left boundary of the clip rectangle in which we're
	 *        painting.  This optimizes painting by allowing us to not paint
	 *        paint when this token is "to the left" of the clip rectangle.
	 * @param useSTC Whether to use the text area's "selected text color."
	 * @return The x-coordinate representing the end of the painted text.
	 */
	float paintSelected(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean useSTC);


}
