/*
 * 02/24/2004
 *
 * DocumentReader.java - A reader for javax.swing.text.Document
 * objects.
 * Copyright (C) 2004 Robert Futrell
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
package org.fife.io;

import java.io.Reader;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;


/**
 * A <code>Reader</code> for <code>javax.swing.text.Document</code> objects.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DocumentReader extends Reader {

	/**
	 * The stream's position in the document.
	 */
	private long position;
	
	/**
	 * A remembered position in the document.
	 */
	private long mark;
	
	/**
	 * The document we're working on.
	 */
	private Document document;
	
	/**
	 * Used for fast character access.
	 */
	private Segment segment;


	/**
	 * Constructor.
	 *
	 * @param document The document we're 'reading'.
	 */
	public DocumentReader(Document document) {
		position = 0;
		mark = -1;
		this.document = document;
		this.segment = new Segment();
	}


	/**
	 * This currently does nothing...
	 */
	public void close() {
	}


	/**
	 * Marks the present position in the stream.  Subsequent calls to
	 * <code>reset()</code> will reposition the stream to this point.
	 *
	 * @param readAheadLimit Ignored.
	 */
	public void mark(int readAheadLimit) {
		mark = position;
	}


	/**
	 * Tells whether this reader supports the <code>mark</code> operation.
	 * This always returns <code>true</code> for <code>DocumentReader</code>.
	 */
	public boolean markSupported() {
		return true;
	}


	/**
	 * Reads the single character at the current position in the document.
	 */
	public int read() {
		if(position>=document.getLength()) {
			return -1;      // Read past end of document.
		}
		try {
			document.getText((int)position,1, segment);
			position++;
			return segment.array[segment.offset];
		} catch (BadLocationException ble) {
			/* Should never happen?? */
			ble.printStackTrace();
			return -1;
		}
	}


	/**
	 * Read <code>array.length</code> characters from the beginning
	 * of the document into <code>array</code>.
	 *
	 * @param array The array to read characters into.
	 * @return The number of characters read.
	 */
	public int read(char array[]) {
		return read(array, 0, array.length);
	}


	/**
	 * Reads characters into a portion of an array.
	 *
	 * @param cbuf The destination buffer.
	 * @param off Offset at which to start storing characters.
	 * @param len Maximum number of characters to read.
	 * @return The number of characters read, or <code>-1</code> if the
	 *         end of the stream (document) has been reached.
	 */
	public int read(char cbuf[], int off, int len) {
		int k;
		if(position>=document.getLength()) {
			return -1;      // Read past end of document.
		}
		k = len;
		if((position+k)>=document.getLength())
			k = document.getLength() - (int)position;
		if(off + k >= cbuf.length)
			k = cbuf.length - off;
		try {
			document.getText((int)position, k, segment);
			position += k;
			System.arraycopy(segment.array,segment.offset,
							cbuf,off,
							k);
			return k;
		} catch (BadLocationException ble) {
			/* Should never happen ? */
			return -1;
		}
	}


	/**
	 * Tells whether this reader is ready to be read without
	 * blocking for input.  <code>DocumentReader</code> will
	 * always return true.
	 *
	 * @return <code>true</code> if the next read operation will
	 *         return without blocking.
	 */
	public boolean ready() {
		return true;
	}


	/**
	 * Resets the stream.  If the stream has been marked, then attempt to
	 * reposition it at the mark.  If the stream has not been marked, then
	 * move it to the beginning of the document.
	 */
	public void reset() {
		if(mark==-1) {
			position = 0;
		}
		else {
			position = mark;
			mark = -1;
		}
	}


	/**
	 * Skips characters.  This will not 'skip' past the end of the document.
	 *
	 * @param n The number of characters to skip.
	 * @return The number of characters actually skipped.
	 */
	public long skip(long n) {
		if (position+n<=document.getLength()) {
			position += n;
			return n;
		}
		long temp = position;
		position = document.getLength();
		return document.getLength() - temp;
	}


	/**
	 * Move to the specified position in the document.  If <code>pos</code>
	 * is greater than the document's length, the stream's position is moved
	 * to the end of the document.
	 *
	 * @param pos The position in the document to move to.
	 */
	public void seek(long pos) {
		position = Math.min(pos, document.getLength());
	}


}