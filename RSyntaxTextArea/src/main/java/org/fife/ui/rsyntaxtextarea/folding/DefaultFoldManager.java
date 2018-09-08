/*
 * 10/08/2011
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rtextarea.RDocument;


/**
 * The default implementation of a fold manager.  Besides keeping track of
 * folds, this class behaves as follows:
 *
 * <ul>
 *    <li>If text containing a newline is inserted in a collapsed fold,
 *        that fold, and any ancestor folds, are expanded.  This ensures that
 *        modified text is always visible to the user.
 *    <li>If the text area's {@link RSyntaxTextArea#SYNTAX_STYLE_PROPERTY}
 *        changes, the current fold parser is uninstalled, and one appropriate
 *        for the new language, if any, is installed.
 * </ul>
 *
 * The folding strategy to use is retrieved from {@link FoldParserManager}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DefaultFoldManager implements FoldManager {

	private RSyntaxTextArea textArea;
	private Parser rstaParser;
	private FoldParser foldParser;
	private List<Fold> folds;
	private boolean codeFoldingEnabled;
	private PropertyChangeSupport support;
	private Listener l;


	/**
	 * Constructor.
	 *
	 * @param textArea The text area whose folds we are managing.
	 */
	public DefaultFoldManager(RSyntaxTextArea textArea) {
		this.textArea = textArea;
		support = new PropertyChangeSupport(this);
		l = new Listener();
		textArea.getDocument().addDocumentListener(l);
		textArea.addPropertyChangeListener(RSyntaxTextArea.SYNTAX_STYLE_PROPERTY, l);
		textArea.addPropertyChangeListener("document", l);
		folds = new ArrayList<Fold>();
		updateFoldParser();
	}


	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}


	@Override
	public void clear() {
		folds.clear();
	}


	@Override
	public boolean ensureOffsetNotInClosedFold(int offs) {

		boolean foldsOpened = false;
		Fold fold = getDeepestFoldContaining(offs);

		while (fold!=null) {
			if (fold.isCollapsed()) {
				fold.setCollapsed(false);
				foldsOpened = true;
			}
			fold = fold.getParent();
		}

		if (foldsOpened) { // Folds changing state mean gutter is stale
			RSyntaxUtilities.possiblyRepaintGutter(textArea);
		}

		return foldsOpened;

	}


	@Override
	public Fold getDeepestFoldContaining(int offs) {
		Fold deepestFold = null;
		if (offs>-1) {
			for (int i=0; i<folds.size(); i++) {
				Fold fold = getFold(i);
				if (fold.containsOffset(offs)) {
					deepestFold = fold.getDeepestFoldContaining(offs);
					break;
				}
			}
		}
		return deepestFold;
	}


	@Override
	public Fold getDeepestOpenFoldContaining(int offs) {

		Fold deepestFold = null;

		if (offs>-1) {
			for (int i=0; i<folds.size(); i++) {
				Fold fold = getFold(i);
				if (fold.containsOffset(offs)) {
					if (fold.isCollapsed()) {
						return null;
					}
					deepestFold = fold.getDeepestOpenFoldContaining(offs);
					break;
				}
			}
		}

		return deepestFold;

	}


	@Override
	public Fold getFold(int index) {
		return folds.get(index);
	}


	@Override
	public int getFoldCount() {
		return folds.size();
	}


	@Override
	public Fold getFoldForLine(int line) {
		return getFoldForLineImpl(null, folds, line);
	}


private Fold getFoldForLineImpl(Fold parent, List<Fold> folds, int line) {

	int low = 0;
	int high = folds.size() - 1;

	while (low <= high) {
		int mid = (low + high) >> 1;
		Fold midFold = folds.get(mid);
		int startLine = midFold.getStartLine();
		if (line==startLine) {
			return midFold;
		}
		else if (line<startLine) {
			high = mid - 1;
		}
		else {
			int endLine = midFold.getEndLine();
			if (line>=endLine) {
				low = mid + 1;
			}
			else { // line>startLine && line<=endLine
				List<Fold> children = midFold.getChildren();
				return children!=null ? getFoldForLineImpl(midFold, children, line) : null;
			}
		}
	}

	return null; // No fold for this line
}


	@Override
	public int getHiddenLineCount() {
		int count = 0;
		for (Fold fold : folds) {
			count += fold.getCollapsedLineCount();
		}
		return count;
	}


	@Override
	public int getHiddenLineCountAbove(int line) {
		return getHiddenLineCountAbove(line, false);
	}


	@Override
	public int getHiddenLineCountAbove(int line, boolean physical) {

		int count = 0;

		for (Fold fold : folds) {
			int comp = physical ? line+count : line;
			if (fold.getStartLine()>=comp) {
				break;
			}
			count += getHiddenLineCountAboveImpl(fold, comp, physical);
		}

		return count;

	}


	/**
	 * Returns the number of lines "hidden" by collapsed folds above the
	 * specified line.
	 *
	 * @param fold The current fold in the recursive algorithm.  It and its
	 *        children are examined.
	 * @param line The line.
	 * @param physical Whether <code>line</code> is the number of a physical
	 *        line (i.e. visible, not code-folded), or a logical one (i.e. any
	 *        line from the model).  If <code>line</code> was determined by a
	 *        raw line calculation (i.e. <code>(visibleTopY / lineHeight)</code>),
	 *        this value should be <code>true</code>.  It should be
	 *        <code>false</code> when it was calculated from an offset in the
	 *        document (for example).
	 * @return The number of lines hidden in folds that are descendants of
	 *         <code>fold</code>, or <code>fold</code> itself, above
	 *         <code>line</code>.
	 */
	private int getHiddenLineCountAboveImpl(Fold fold, int line, boolean physical) {

		int count = 0;

		if (fold.getEndLine()<line ||
				(fold.isCollapsed() && fold.getStartLine()<line)) {
			count = fold.getCollapsedLineCount();
		}
		else {
			int childCount = fold.getChildCount();
			for (int i=0; i<childCount; i++) {
				Fold child = fold.getChild(i);
				int comp = physical ? line+count : line;
				if (child.getStartLine()>=comp) {
					break;
				}
				count += getHiddenLineCountAboveImpl(child, comp, physical);
			}
		}

		return count;

	}


	@Override
	public int getLastVisibleLine() {

		int lastLine = textArea.getLineCount() - 1;

		if (isCodeFoldingSupportedAndEnabled()) {
			int foldCount = getFoldCount();
			if (foldCount>0) {
				Fold lastFold = getFold(foldCount-1);
				if (lastFold.containsLine(lastLine)) {
					if (lastFold.isCollapsed()) {
						lastLine = lastFold.getStartLine();
					}
					else { // Child fold may end on the same line as parent
						while (lastFold.getHasChildFolds()) {
							lastFold = lastFold.getLastChild();
							if (lastFold.containsLine(lastLine)) {
								if (lastFold.isCollapsed()) {
									lastLine = lastFold.getStartLine();
									break;
								}
							}
							else { // Higher up
								break;
							}
						}
					}
				}
			}
		}

		return lastLine;

	}


	@Override
	public int getVisibleLineAbove(int line) {

		if (line<=0 || line>=textArea.getLineCount()) {
			return -1;
		}

		do {
			line--;
		} while (line>=0 && isLineHidden(line));

		return line;

	}


	@Override
	public int getVisibleLineBelow(int line) {

		int lineCount = textArea.getLineCount();
		if (line<0 || line>=lineCount-1) {
			return -1;
		}

		do {
			line++;
		} while (line<lineCount && isLineHidden(line));

		return line==lineCount ? -1 : line;

	}


//	private static int binaryFindFoldContainingLine(int line) {
//
//List allFolds;
//
//		int low = 0;
//		int high = allFolds.size() - 1;
//
//		while (low <= high) {
//			int mid = (low + high) >> 1;
//			Fold midVal = (Fold)allFolds.get(mid);
//			if (midVal.containsLine(line)) {
//				return mid;
//			}
//			if (line<=midVal.getStartLine()) {
//				high = mid - 1;
//			}
//			else { // line > midVal.getEndLine()
//				low = mid + 1;
//			}
//		}
//
//		return -(low + 1); // key not found
//
//	}


	@Override
	public boolean isCodeFoldingEnabled() {
		return codeFoldingEnabled;
	}


	@Override
	public boolean isCodeFoldingSupportedAndEnabled() {
		return codeFoldingEnabled && foldParser!=null;
	}


	@Override
	public boolean isFoldStartLine(int line) {
		return getFoldForLine(line)!=null;
	}


	@Override
	public boolean isLineHidden(int line) {
		for (Fold fold : folds) {
			if (fold.containsLine(line)) {
				if (fold.isCollapsed()) {
					return true;
				}
				else {
					return isLineHiddenImpl(fold, line);
				}
			}
		}
		return false;
	}


	private boolean isLineHiddenImpl(Fold parent, int line) {
		for (int i=0; i<parent.getChildCount(); i++) {
			Fold child = parent.getChild(i);
			if (child.containsLine(line)) {
				if (child.isCollapsed()) {
					return true;
				}
				else {
					return isLineHiddenImpl(child, line);
				}
			}
		}
		return false;
	}


	private void keepFoldState(Fold newFold, List<Fold> oldFolds) {
		int previousLoc = Collections.binarySearch(oldFolds, newFold);
		//System.out.println(newFold + " => " + previousLoc);
		if (previousLoc>=0) {
			Fold prevFold = oldFolds.get(previousLoc);
			newFold.setCollapsed(prevFold.isCollapsed());
		}
		else {
			//previousLoc = -(insertion point) - 1;
			int insertionPoint = -(previousLoc + 1);
			if (insertionPoint>0) {
				Fold possibleParentFold = oldFolds.get(insertionPoint-1);
				if (possibleParentFold.containsOffset(
						newFold.getStartOffset())) {
					List<Fold> children = possibleParentFold.getChildren();
					if (children!=null) {
						keepFoldState(newFold, children);
					}
				}
			}
		}
	}


	private void keepFoldStates(List<Fold> newFolds, List<Fold> oldFolds) {
		for (Fold newFold : newFolds) {
			keepFoldState(newFold, folds);
			List<Fold> newChildFolds = newFold.getChildren();
			if (newChildFolds!=null) {
				keepFoldStates(newChildFolds, oldFolds);
			}
		}
	}


	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}


	@Override
	public void reparse() {

		if (codeFoldingEnabled && foldParser!=null) {

			// Re-calculate folds.  Keep the fold state of folds that are
			// still around.
			List<Fold> newFolds = foldParser.getFolds(textArea);
			if (newFolds==null) {
				newFolds = Collections.emptyList();
			}
			else {
				keepFoldStates(newFolds, folds);
			}
			folds = newFolds;

			// Let folks (gutter, etc.) know that folds have been updated.
			support.firePropertyChange(PROPERTY_FOLDS_UPDATED, null, folds);
			textArea.repaint();

		}
		else {
			folds.clear();
		}

	}


	@Override
	public void setCodeFoldingEnabled(boolean enabled) {
		if (enabled!=codeFoldingEnabled) {
			codeFoldingEnabled = enabled;
			if (rstaParser!=null) {
				textArea.removeParser(rstaParser);
			}
			if (enabled) {
				rstaParser = new AbstractParser() {
					@Override
					public ParseResult parse(RSyntaxDocument doc, String style) {
						reparse();
						return new DefaultParseResult(this);
					}
				};
				textArea.addParser(rstaParser);
				support.firePropertyChange(PROPERTY_FOLDS_UPDATED, null, null);
				//reparse();
			}
			else {
				folds = Collections.emptyList();
				textArea.repaint();
				support.firePropertyChange(PROPERTY_FOLDS_UPDATED, null, null);
			}
		}
	}


	@Override
	public void setFolds(List<Fold> folds) {
		this.folds = folds;
	}


	/**
	 * Updates the fold parser to be the one appropriate for the language
	 * currently being highlighted.
	 */
	private void updateFoldParser() {
		foldParser = FoldParserManager.get().getFoldParser(
											textArea.getSyntaxEditingStyle());
	}


	/**
	 * Listens for events in the text editor.
	 */
	private class Listener implements DocumentListener, PropertyChangeListener {

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			// Adding text containing a newline to the visible line of a folded
			// Fold causes that Fold to unfold.  Check only start offset of
			// insertion since that's the line that was "modified".
			int startOffs = e.getOffset();
			int endOffs = startOffs + e.getLength();
			Document doc = e.getDocument();
			Element root = doc.getDefaultRootElement();
			int startLine = root.getElementIndex(startOffs);
			int endLine = root.getElementIndex(endOffs);
			if (startLine!=endLine) { // Inserted text covering > 1 line...
				Fold fold = getFoldForLine(startLine);
				if (fold!=null && fold.isCollapsed()) {
					fold.toggleCollapsedState();
				}
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {

			String name = e.getPropertyName();

			if (RSyntaxTextArea.SYNTAX_STYLE_PROPERTY.equals(name)) {
				// Syntax style changed in editor.
				updateFoldParser();
				reparse(); // Even if no fold parser change, highlighting did
			}

			else if ("document".equals(name)) {
				// The document switched out from under us
				RDocument old = (RDocument)e.getOldValue();
				if (old != null) {
					old.removeDocumentListener(this);
				}
				RDocument newDoc = (RDocument)e.getNewValue();
				if (newDoc != null) {
					newDoc.addDocumentListener(this);
				}
				reparse();
			}

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// Removing text from the visible line of a folded Fold causes that
			// Fold to unfold.  We only need to check the removal offset since
			// that's the new caret position.
			int offs = e.getOffset();
			try {
				int lastLineModified = textArea.getLineOfOffset(offs);
				//System.out.println(">>> " + lastLineModified);
				Fold fold = getFoldForLine(lastLineModified);
				//System.out.println("&&& " + fold);
				if (fold!=null && fold.isCollapsed()) {
					fold.toggleCollapsedState();
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Never happens
			}
		}

	}


}