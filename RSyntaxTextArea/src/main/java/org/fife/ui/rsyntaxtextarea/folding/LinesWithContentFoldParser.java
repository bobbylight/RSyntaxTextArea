/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenUtils;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;


/**
 * A fold parser that considers any contiguous group of lines without a blank
 * line to be a "block" of code.  Useful for languages that don't have the
 * concept of code blocks, such as assembly languages.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LinesWithContentFoldParser implements FoldParser {


	@Override
	public List<Fold> getFolds(RSyntaxTextArea textArea) {

		List<Fold> folds = new ArrayList<>();

		Fold fold = null;
		int lineCount = textArea.getLineCount();

		try {

			for (int line=0; line < lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);

				if (!TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t)) {

					// A line with content after blank lines
					if (fold == null) {
						fold = new Fold(FoldType.CODE, textArea, t.getOffset());
						folds.add(fold);
					}
				}
				else if (fold != null) {
					// Null tokens have nonsensical offsets for some reason
					fold.setEndOffset(textArea.getLineStartOffset(line) - 1);
					if (fold.isOnSingleLine()) {
						folds.remove(folds.size()-1);
					}
					fold = null;
				}
			}
		} catch (BadLocationException ble) { // Should never happen
			ble.printStackTrace();
		}

		return folds;
	}
}
