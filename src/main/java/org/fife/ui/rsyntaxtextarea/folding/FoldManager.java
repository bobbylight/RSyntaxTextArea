/*
 * 10/08/2011
 *
 * FoldManager.java - Manages code folding in an RSyntaxTextArea instance.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import java.beans.PropertyChangeListener;
import java.util.List;


/**
 * Manages code folding in an instance of RSyntaxTextArea.<p>
 *
 * By default, {@code RSyntaxTextArea} uses a {@link DefaultFoldManager}, which
 * is sufficient for almost all applications.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see DefaultFoldManager
 */
public interface FoldManager {

	/**
	 * Property fired when folds have been updated.
	 */
	String PROPERTY_FOLDS_UPDATED = "FoldsUpdated";


	/**
	 * Adds a property change listener to this fold manager.
	 *
	 * @param l The new listener.
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 */
	void addPropertyChangeListener(PropertyChangeListener l);


	/**
	 * Removes all folds.
	 */
	void clear();


	/**
	 * Ensures that the specified offset is not hidden in a collapsed fold.
	 * Any folds containing this offset that are collapsed will be expanded.
	 *
	 * @param offs The offset.
	 * @return Whether any folds had to be opened.
	 * @see #getDeepestFoldContaining(int)
	 */
	boolean ensureOffsetNotInClosedFold(int offs);


	/**
	 * Returns the "deepest" nested fold containing the specified offset.
	 *
	 * @param offs The offset.
	 * @return The deepest fold containing the offset, or <code>null</code> if
	 *         no fold contains the offset.
	 */
	Fold getDeepestFoldContaining(int offs);


	/**
	 * Returns the "deepest" open fold containing the specified offset.
	 *
	 * @param offs The offset.
	 * @return The fold, or <code>null</code> if no open fold contains the
	 *         offset.
	 */
	Fold getDeepestOpenFoldContaining(int offs);


	/**
	 * Returns a specific top-level fold, which may have child folds.
	 *
	 * @param index The index of the fold.
	 * @return The fold.
	 * @see #getFoldCount()
	 */
	Fold getFold(int index);


	/**
	 * Returns the number of top-level folds.
	 *
	 * @return The number of top-level folds.
	 * @see #getFold(int)
	 */
	int getFoldCount();


	/**
	 * Returns the fold region that starts at the specified line.
	 *
	 * @param line The line number.
	 * @return The fold, or <code>null</code> if the line is not the start
	 *         of a fold region.
	 * @see #isFoldStartLine(int)
	 */
	Fold getFoldForLine(int line);


	/**
	 * Returns the total number of hidden (folded) lines.
	 *
	 * @return The total number of hidden (folded) lines.
	 * @see #getHiddenLineCountAbove(int)
	 */
	int getHiddenLineCount();


	/**
	 * Returns the number of lines "hidden" by collapsed folds above the
	 * specified line.
	 *
	 * @param line The line.  This is the line number for a logical line.
	 *        For the line number of a physical line (i.e. visible, not folded),
	 *        use <code>getHiddenLineCountAbove(int, true)</code>.
	 * @return The number of lines hidden in folds above <code>line</code>.
	 * @see #getHiddenLineCountAbove(int, boolean)
	 */
	int getHiddenLineCountAbove(int line);


	/**
	 * Returns the number of lines "hidden" by collapsed folds above the
	 * specified line.
	 *
	 * @param line The line.
	 * @param physical Whether <code>line</code> is the number of a physical
	 *        line (i.e. visible, not code-folded), or a logical one (i.e. any
	 *        line from the model).  If <code>line</code> was determined by a
	 *        raw line calculation (i.e. <code>(visibleTopY / lineHeight)</code>),
	 *        this value should be <code>true</code>.  It should be
	 *        <code>false</code> when it was calculated from an offset in the
	 *        document (for example).
	 * @return The number of lines hidden in folds above <code>line</code>.
	 */
	int getHiddenLineCountAbove(int line, boolean physical);


	/**
	 * Returns the last visible line in the text area, taking into account
	 * folds.
	 *
	 * @return The last visible line.
	 */
	int getLastVisibleLine();


	int getVisibleLineAbove(int line);


	int getVisibleLineBelow(int line);


    /**
	 * Returns whether code folding is enabled.  Note that only certain
	 * languages support code folding; those that do not will ignore this
	 * property.
	 *
	 * @return Whether code folding is enabled.
	 * @see #setCodeFoldingEnabled(boolean)
	 */
	boolean isCodeFoldingEnabled();


	/**
	 * Returns <code>true</code> if and only if code folding is enabled for
	 * this text area, AND folding is supported for the language it is editing.
	 * Whether or not folding is supported for a language depends on whether
	 * a fold parser is registered for that language with the
	 * <code>FoldParserManager</code>.
	 *
	 * @return Whether folding is supported and enabled for this text area.
	 * @see FoldParserManager
	 */
	boolean isCodeFoldingSupportedAndEnabled();


	/**
	 * Returns whether the specified line contains the start of a fold region.
	 *
	 * @param line The line.
	 * @return Whether the line contains the start of a fold region.
	 * @see #getFoldForLine(int)
	 */
	boolean isFoldStartLine(int line);


	/**
	 * Returns whether a line is hidden in a collapsed fold.
	 *
	 * @param line The line to check.
	 * @return Whether the line is hidden in a collapsed fold.
	 */
	boolean isLineHidden(int line);


	/**
	 * Removes a property change listener from this fold manager.
	 *
	 * @param l The listener to remove.
	 * @see #addPropertyChangeListener(PropertyChangeListener)
	 */
	void removePropertyChangeListener(PropertyChangeListener l);


	/**
	 * Forces an immediate reparsing for folds, if folding is enabled.  This
	 * usually does not need to be called by the programmer, since fold
	 * parsing is done automatically by RSTA.
	 */
	void reparse();


	/**
	 * Sets whether code folding is enabled.  Note that only certain
	 * languages will support code folding out of the box.  Those languages
	 * which do not support folding will ignore this property.
	 *
	 * @param enabled Whether code folding should be enabled.
	 * @see #isCodeFoldingEnabled()
	 */
	void setCodeFoldingEnabled(boolean enabled);


	/**
	 * Sets the folds for this fold manager.
	 *
	 * @param folds The new folds.  This should not be <code>null</code>.
	 */
	void setFolds(List<Fold> folds);


}