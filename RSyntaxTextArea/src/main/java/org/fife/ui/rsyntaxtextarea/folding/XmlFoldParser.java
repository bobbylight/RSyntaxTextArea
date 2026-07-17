/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;


/**
 * Fold parser for XML.  Any tags that span more than one line, as well as
 * comment regions spanning more than one line, are identified as foldable
 * regions.<p>
 *
 * Tags are tracked on a lightweight stack of plain offsets while scanning,
 * and a {@code Fold} (and the {@code Position} it wraps) is only created
 * once a tag is confirmed to span multiple lines.  This avoids a lot of
 * wasted {@code Fold}/{@code Position} churn on XML documents, which tend to
 * have many single-line elements.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlFoldParser implements FoldParser {

	private static final char[] MARKUP_SHORT_TAG_END = { '/', '>' };
	private static final char[] MARKUP_CLOSING_TAG_START = { '<', '/' };
	private static final char[] MLC_END = { '-', '-', '>' };


	@Override
	public List<Fold> getFolds(RSyntaxTextArea textArea) {

		List<Fold> folds = new ArrayList<>();

		List<PendingTag> tagStack = new ArrayList<>();
		int lineCount = textArea.getLineCount();
		boolean inMLC = false;
		int mlcStart = 0;

		try {

			for (int line=0; line<lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {

					if (t.isComment()) {

						// Continuing an MLC from a previous line
						if (inMLC) {
							// Found the end of the MLC starting on a previous line...
							if (t.endsWith(MLC_END)) {
								int mlcEnd = t.getEndOffset() - 1;
								Fold parentFold = materializeTop(tagStack, textArea, folds);
								Fold commentFold;
								if (parentFold==null) {
									commentFold = new Fold(FoldType.COMMENT, textArea, mlcStart);
									folds.add(commentFold);
								}
								else {
									commentFold = parentFold.createChild(FoldType.COMMENT, mlcStart);
								}
								commentFold.setEndOffset(mlcEnd);
								inMLC = false;
								mlcStart = 0;
							}
							// Otherwise, this MLC is continuing on to yet
							// another line.
						}

						else {
							// If we're an MLC that ends on a later line...
							if (t.getType()==TokenTypes.MARKUP_COMMENT && !t.endsWith(MLC_END)) {
								inMLC = true;
								mlcStart = t.getOffset();
							}
						}

					}

					else if (t.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<')) {
						// Cheap - just remember where the tag started.  We don't
						// know yet whether it'll span multiple lines, so don't
						// create a Fold (and its backing Position) until we do.
						tagStack.add(new PendingTag(t.getOffset(), line));
					}

					else if (t.is(TokenTypes.MARKUP_TAG_DELIMITER, MARKUP_SHORT_TAG_END)) {
						if (!tagStack.isEmpty()) {
							// Self-closing tags never become Folds, so just
							// forget about this one; nothing was ever created.
							tagStack.remove(tagStack.size()-1);
						}
					}

					else if (t.is(TokenTypes.MARKUP_TAG_DELIMITER, MARKUP_CLOSING_TAG_START)) {
						if (!tagStack.isEmpty()) {
							int tagIndex = tagStack.size() - 1;
							PendingTag tag = tagStack.get(tagIndex);
							// Don't create fold markers for single-line blocks.
							// Note tag.fold may already be non-null here, if a
							// nested multi-line tag or comment forced it to be
							// materialized early; materialize() handles that.
							if (tag.startLine!=line) {
								Fold newFold = materialize(tagStack, tagIndex, textArea, folds);
								newFold.setEndOffset(t.getOffset());
							}
							// else: tag was entirely on one line, so it's
							// discarded having never had a Fold created for it.
							tagStack.remove(tagIndex);
						}
					}

					t = t.getNextToken();

				}

			}

		} catch (BadLocationException ble) { // Should never happen
			ble.printStackTrace();
		}

		return folds;

	}


	/**
	 * Ensures the tag currently at the top of the tag stack, if any, has
	 * had a {@code Fold} created for it (materializing any of its still-open
	 * ancestors along the way, as needed), and returns that fold.
	 *
	 * @param tagStack The stack of currently-open tags.
	 * @param textArea The text area.
	 * @param folds The top-level list of folds found so far.
	 * @return The fold for the tag at the top of the stack, or {@code null}
	 *         if the stack is empty.
	 */
	private static Fold materializeTop(List<PendingTag> tagStack, RSyntaxTextArea textArea,
			List<Fold> folds) throws BadLocationException {
		return tagStack.isEmpty() ? null :
			materialize(tagStack, tagStack.size() - 1, textArea, folds);
	}


	/**
	 * Materializes the tag at the given index in the tag stack into an
	 * actual {@code Fold}, if it hasn't been already, recursively
	 * materializing its ancestors first as needed.
	 *
	 * @param tagStack The stack of currently-open tags.
	 * @param index The index of the tag to materialize.
	 * @param textArea The text area.
	 * @param folds The top-level list of folds found so far.
	 * @return The (possibly newly-created) fold for the tag.
	 */
	private static Fold materialize(List<PendingTag> tagStack, int index, RSyntaxTextArea textArea,
			List<Fold> folds) throws BadLocationException {

		PendingTag tag = tagStack.get(index);
		if (tag.fold!=null) {
			return tag.fold;
		}

		Fold parentFold = index>0 ? materialize(tagStack, index - 1, textArea, folds) : null;

		Fold newFold;
		if (parentFold==null) {
			newFold = new Fold(FoldType.CODE, textArea, tag.startOffset);
			folds.add(newFold);
		}
		else {
			newFold = parentFold.createChild(FoldType.CODE, tag.startOffset);
		}

		tag.fold = newFold;
		return newFold;

	}


	/**
	 * A tag that has been opened but not yet closed.  This is intentionally
	 * a lightweight record - no {@code Fold} or {@code Position} is created
	 * for a tag unless/until it's confirmed to be foldable.
	 */
	private static final class PendingTag {

		private final int startOffset;
		private final int startLine;
		private Fold fold;

		PendingTag(int startOffset, int startLine) {
			this.startOffset = startOffset;
			this.startLine = startLine;
		}

	}


}
