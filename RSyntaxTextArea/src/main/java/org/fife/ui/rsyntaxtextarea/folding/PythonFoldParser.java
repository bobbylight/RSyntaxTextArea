/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * A fold parser for Python.  Analyzes indentation to determine
 * foldable regions.  Lines containing only comments and/or
 * whitespace are ignored.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PythonFoldParser implements FoldParser {


	@Override
	public List<Fold> getFolds(RSyntaxTextArea textArea) {

		List<Fold> folds = new ArrayList<>();

		Fold currentFold = null;
		int lineCount = textArea.getLineCount();
		int tabSize = textArea.getTabSize();
		Stack<Integer> foldStartLeadingWhiteSpaceCounts = new Stack<>();
		int currentNextFoldStart = 0;
		int currentLeadingWhiteSpaceCount = 0;

		try {

			for (int line = 0; line < lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);

				int leadingWhiteSpaceCount = getLeadingWhiteSpaceCount(t, tabSize);
				if (leadingWhiteSpaceCount == -1) {
					continue; // A blank line, or all whitespace, is ignored
				}

				if (leadingWhiteSpaceCount == currentLeadingWhiteSpaceCount) {
					currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;
				}

				else if (leadingWhiteSpaceCount > currentLeadingWhiteSpaceCount) {

					if (currentFold != null) {
						currentFold = currentFold.createChild(FoldType.CODE,
							currentNextFoldStart);
					}
					else {
						currentFold = new Fold(FoldType.CODE, textArea, currentNextFoldStart);
						folds.add(currentFold);
					}
					foldStartLeadingWhiteSpaceCounts.push(currentLeadingWhiteSpaceCount);
					currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;
				}

				else { // leadingWhiteSpaceCount < currentLeadingWhiteSpaceCount

					// Remember the next fold start before we re-call getTokenListForLine(),
					// because of RSTA's token list caching
					currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;

					// The end of the "block" should be the end of the prior line with
					// any non-whitespace content.  Trailing blank lines, say to provide
					// space between functions, should not be part of a code block
					int prevLine = line - 1;
					do {
						t = textArea.getTokenListForLine(prevLine--);
					} while (TokenUtils.isBlankOrAllWhiteSpace(t));
					int endOffs = t.getEndOffset() - 1;

					boolean foundBlock = false;
					while (!foldStartLeadingWhiteSpaceCounts.isEmpty() &&
							foldStartLeadingWhiteSpaceCounts.peek() >= leadingWhiteSpaceCount) {
						// IntelliJ can't tell, but it's not possible for currentFold to be
						// null here
						currentFold.setEndOffset(endOffs);
						currentFold = currentFold.getParent();
						foldStartLeadingWhiteSpaceCounts.pop();
						foundBlock = true;
					}

					// A code block without lines should just be removed
					if (!foundBlock && currentFold != null && !currentFold.removeFromParent()) {
						folds.remove(folds.size()-1);
					}
				}

				currentLeadingWhiteSpaceCount = leadingWhiteSpaceCount;
			}

		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Should never happen
		}

		return folds;
	}

	private static int getLeadingWhiteSpaceCount(Token t, int tabSize) {

		// Lines continuing a multi-line string or char don't count
		// against indentation
		if (t == null || t.getType() == TokenTypes.LITERAL_STRING_DOUBLE_QUOTE ||
				t.getType() == TokenTypes.LITERAL_CHAR) {
			return -1;
		}

		int count = 0;
		while (t != null && t.isPaintable()) {
			if (!t.isWhitespace()) {
				// Note Python doesn't nave multi-line comments so we don't
				// have to worry about MLD's
				return t.getType() == TokenTypes.COMMENT_EOL ? -1 : count;
			}
			count += TokenUtils.getWhiteSpaceTokenLength(t, tabSize, count);
			t = t.getNextToken();
		}

		// All-whitespace lines should be ignored
		return -1;
	}
}
