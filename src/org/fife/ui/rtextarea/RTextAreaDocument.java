/*
 * 03/24/2005
 *
 * RTextAreaDocument.java - The document used by RTextAreas.
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
package org.fife.ui.rtextarea;

import java.util.*;
import javax.swing.text.*;


/**
 * A replacement for <code>PlainDocument</code> that gets rid of the unncessary
 * composed text checks.  Also, since this class is a subclass of
 * <code>AbstractDocument</code>, we are assured of being able to use the
 * readlock mechanism.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public class RTextAreaDocument extends AbstractDocument {

	/**
	 * Name of the attribute that specifies the tab
	 * size for tabs contained in the content.  The
	 * type for the value is Integer.
	 */
	public static final String tabSizeAttribute = "tabSize";

	private BranchElement root;
	private Vector added = new Vector();     // Vector<Element>
	private Vector removed = new Vector();   // Vector<Element>
	private transient Segment s;


	/**
	 * Constructs a plain text document.  A default model using 
	 * <code>GapContent</code> is constructed and set.
	 */
	public RTextAreaDocument() {
		this(new GapContent());
	}


	/**
	 * Constructs a plain text document.  A default root element is created,
	 * and the tab size set to 5.
	 *
	 * @param c  the container for the content
	 */
	public RTextAreaDocument(Content c) {
		super(c);
		putProperty(tabSizeAttribute, new Integer(5));
		root = createDefaultRoot();
		s = new Segment();
	}


	/**
	 * Gets the default root element for the document model.
	 *
	 * @return the root
	 * @see Document#getDefaultRootElement
	 */
	public Element getDefaultRootElement() {
		return root;
	}


	/**
	 * Creates the root element to be used to represent the
	 * default document structure.
	 *
	 * @return the element base
	 */
	protected BranchElement createDefaultRoot() {
		BranchElement map = (BranchElement)createBranchElement(null, null);
		Element line = createLeafElement(map, null, 0, 1);
		Element[] lines = new Element[1];
		lines[0] = line;
		map.replace(0, 0, lines);
		return map;
	}


	/**
	 * Get the paragraph element containing the given position.  Since this
	 * document only models lines, it returns the line instead.
	 */
	public Element getParagraphElement(int pos) {
        return root.getElement(root.getElementIndex(pos));
	}


	/**
	 * Updates document structure as a result of text insertion.  This
	 * will happen within a write lock.  Since this document simply
	 * maps out lines, we refresh the line map.
	 *
	 * @param chng the change event describing the dit
	 * @param attr the set of attributes for the inserted text
	 */
	protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
		removed.removeAllElements();
		added.removeAllElements();
		int offset = chng.getOffset();
		int length = chng.getLength();
		if (offset > 0) {
			offset -= 1;
			length += 1;
		}
		int index = root.getElementIndex(offset);
		Element rmCandidate = root.getElement(index);
		int rmOffs0 = rmCandidate.getStartOffset();
		int rmOffs1 = rmCandidate.getEndOffset();
		int lastOffset = rmOffs0;
		try {
			getContent().getChars(offset, length, s);
			boolean hasBreaks = false;
			for (int i = 0; i < length; i++) {
				char c = s.array[s.offset + i];
				if (c == '\n') {
					int breakOffset = offset + i + 1;
					added.addElement(createLeafElement(root, null,
										lastOffset, breakOffset));
					lastOffset = breakOffset;
					hasBreaks = true;
				}
			}
			if (hasBreaks) {
				int rmCount = 1;
				removed.addElement(rmCandidate);
				if ((offset + length == rmOffs1) && (lastOffset != rmOffs1) &&
						((index+1) < root.getElementCount())) {
					rmCount += 1;
					Element e = root.getElement(index+1);
					removed.addElement(e);
					rmOffs1 = e.getEndOffset();
				}
				if (lastOffset < rmOffs1) {
					added.addElement(createLeafElement(root, null,
											lastOffset, rmOffs1));
				}
				Element[] aelems = new Element[added.size()];
				added.copyInto(aelems);
				Element[] relems = new Element[removed.size()];
				removed.copyInto(relems);
				ElementEdit ee = new ElementEdit(root, index, relems, aelems);
				chng.addEdit(ee);
				root.replace(index, relems.length, aelems);
			}
		} catch (BadLocationException e) {
			throw new Error("Internal error: " + e.toString());
		}
		super.insertUpdate(chng, attr);
	}


	/**
	 * Updates any document structure as a result of text removal.
	 * This will happen within a write lock. Since the structure
	 * represents a line map, this just checks to see if the 
	 * removal spans lines.  If it does, the two lines outside
	 * of the removal area are joined together.
	 *
	 * @param chng the change event describing the edit
	 */
	protected void removeUpdate(DefaultDocumentEvent chng) {
		removed.removeAllElements();
		int offset = chng.getOffset();
		int length = chng.getLength();
		int line0 = root.getElementIndex(offset);
		int line1 = root.getElementIndex(offset + length);
		if (line0 != line1) {
			// a line was removed
			for (int i = line0; i <= line1; i++)
				removed.addElement(root.getElement(i));
			int p0 = root.getElement(line0).getStartOffset();
			int p1 = root.getElement(line1).getEndOffset();
			Element[] aelems = new Element[1];
			aelems[0] = createLeafElement(root, null, p0, p1);
			Element[] relems = new Element[removed.size()];
			removed.copyInto(relems);
			ElementEdit ee = new ElementEdit(root, line0, relems, aelems);
			chng.addEdit(ee);
			root.replace(line0, relems.length, aelems);
		}
		super.removeUpdate(chng);
	}


}