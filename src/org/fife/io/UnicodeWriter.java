/*
 * 09/24/2004
 *
 * UnicodeWriter.java - Writes Unicode output with the proper BOM.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * Writes Unicode text to an output stream.  If the specified encoding is a
 * Unicode, then the text is preceeded by the proper Unicode BOM.  If it is any
 * other encoding, this class behaves just like <code>OutputStreamWriter</code>.
 * This class is here because Java's <code>OutputStreamWriter</code> apparently
 * doesn't believe in writing BOMs.
 * <p>
 *
 * For optimum performance, it is recommended that you wrap all instances of
 * <code>UnicodeWriter</code> with a <code>java.io.BufferedWriter</code>.
 *
 * @author Robert Futrell
 * @version 0.7
 */
public class UnicodeWriter extends Writer {

	/**
	 * If this system property evaluates to "<code>false</code>", ignoring
	 * case, files written out as UTF-8 will not have a BOM written for them.
	 * Otherwise (even if the property is not set), UTF-8 files will have a
	 * BOM written.
	 */
	public static final String PROPERTY_WRITE_UTF8_BOM	=
												"UnicodeWriter.writeUtf8BOM";


	/**
	 * The writer actually doing the writing.
	 */
	private OutputStreamWriter internalOut;

	private static final byte[] UTF8_BOM = new byte[] {
												(byte)0xEF,
												(byte)0xBB,
												(byte)0xBF
											};

	private static final byte[] UTF16LE_BOM = new byte[] { 
												(byte)0xFF,
												(byte)0xFE
											};

	private static final byte[] UTF16BE_BOM = new byte[] {
												(byte)0xFE,
												(byte)0xFF
											};

	private static final byte[] UTF32LE_BOM = new byte[] {
												(byte)0xFF,
												(byte)0xFE,
												(byte)0x00,
												(byte)0x00
											};

	private static final byte[] UTF32BE_BOM = new byte[] {
												(byte)0x00,
												(byte)0x00,
												(byte)0xFE,
												(byte)0xFF
											};


	/**
	 * This is a utility constructor since the vast majority of the time, this
	 * class will be used to write Unicode files.
	 *
	 * @param fileName The file to which to write the Unicode output.
	 * @param encoding The encoding to use.
	 * @throws UnsupportedEncodingException If the specified encoding is not
	 *         supported.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(String fileName, String encoding)
						throws UnsupportedEncodingException, IOException {
		this(new FileOutputStream(fileName), encoding);
	}



	/**
	 * This is a utility constructor since the vast majority of the time, this
	 * class will be used to write Unicode files.
	 *
	 * @param file The file to which to write the Unicode output.
	 * @param encoding The encoding to use.
	 * @throws UnsupportedEncodingException If the specified encoding is not
	 *         supported.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(File file, String encoding)
						throws UnsupportedEncodingException, IOException {
		this(new FileOutputStream(file), encoding);
	}



	/**
	 * Creates a new writer.
	 *
	 * @param out The output stream to write.
	 * @param encoding The encoding to use.
	 * @throws UnsupportedEncodingException If the specified encoding is not
	 *         supported.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(OutputStream out, String encoding)
						throws UnsupportedEncodingException, IOException {
		init(out, encoding);
	}


	/**
	 * Closes this writer.
	 *
	 * @throws IOException If an IO exception occurs.
	 */
	public void close() throws IOException {
		internalOut.close();
	}


	/**
	 * Flushes the stream.
	 *
	 * @throws IOException If an IO exception occurs.
	 */
	public void flush() throws IOException {
		internalOut.flush();
	}


	/**
	 * Returns the encoding being used to write this output stream (i.e., the
	 * encoding of the file).
	 *
	 * @return The encoding of the stream.
	 */
	public String getEncoding() {
		return internalOut.getEncoding();
	}


	/**
	 * Returns whether UTF-8 files should have a BOM in them when written.
	 *
	 * @return Whether to write a BOM for UTF-8 files.
	 */
	public static boolean getWriteUtf8BOM() {
		String prop = System.getProperty(PROPERTY_WRITE_UTF8_BOM);
		if (prop!=null && Boolean.valueOf(prop).equals(Boolean.FALSE)) {
			return false;
		}
		return true;
	}


	/**
	 * Initializes the internal output stream and writes the BOM if the
	 * specified encoding is a Unicode encoding.
	 *
	 * @param out The output stream we are writing.
	 * @param encoding The encoding in which to write.
	 * @throws UnsupportedEncodingException If the specified encoding isn't
	 *         supported.
	 * @throws IOException If an I/O error occurs while writing a BOM.
	 */
	private void init(OutputStream out, String encoding)
						throws UnsupportedEncodingException, IOException {

		internalOut = new OutputStreamWriter(out, encoding);

		// Write the proper BOM if they specified a Unicode encoding.
		// NOTE: Creating an OutputStreamWriter with encoding "UTF-16" DOES
		// DOES write out the BOM; "UTF-16LE", "UTF-16BE", "UTF-32", "UTF-32LE"
		// and "UTF-32BE" don't.
		if ("UTF-8".equals(encoding)) {
			if (getWriteUtf8BOM()) {
				out.write(UTF8_BOM, 0, UTF8_BOM.length);
			}
		}
		else if ("UTF-16LE".equals(encoding)) {
			out.write(UTF16LE_BOM, 0, UTF16LE_BOM.length);
		}
		else if (/*"UTF-16".equals(encoding) || */"UTF-16BE".equals(encoding)) {
			out.write(UTF16BE_BOM, 0, UTF16BE_BOM.length);
		}
		else if ("UTF-32LE".equals(encoding)) {
			out.write(UTF32LE_BOM, 0, UTF32LE_BOM.length);
		}
		else if ("UTF-32".equals(encoding) || "UTF-32BE".equals(encoding)) {
			out.write(UTF32BE_BOM, 0, UTF32BE_BOM.length);
		}

	}


	/**
	 * Writes a portion of an array of characters.
	 *
	 * @param cbuf The buffer of characters.
	 * @param off The offset from which to start writing characters.
	 * @param len The number of characters to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(char[] cbuf, int off, int len) throws IOException {
		internalOut.write(cbuf, off, len);
	}


	/**
	 * Writes a single character.
	 *
	 * @param c An integer specifying the character to write.
	 * @throws IOException If an IO error occurs.
	 */
	public void write(int c) throws IOException {
		internalOut.write(c);
	}


	/**
	 * Writes a portion of a string.
	 *
	 * @param str The string from which to write.
	 * @param off The offset from which to start writing characters.
	 * @param len The number of characters to write.
	 * @throws IOException If an IO error occurs.
	 */
	public void write(String str, int off, int len) throws IOException {
		internalOut.write(str, off, len);
	}


}