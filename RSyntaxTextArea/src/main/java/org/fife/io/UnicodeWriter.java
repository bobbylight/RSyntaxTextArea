/*
 * 09/24/2004
 *
 * UnicodeWriter.java - Writes Unicode output with the proper BOM.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;


/**
 * Writes Unicode text to an output stream.  If the specified encoding is
 * UTF-16 or UTF-32, then the text is preceded by the proper Unicode BOM.
 * If it is any other encoding, this class behaves just like
 * <code>OutputStreamWriter</code>.  This class is here because Java's
 * <code>OutputStreamWriter</code> apparently doesn't believe in writing
 * BOMs.
 * <p>
 *
 * For optimum performance, it is recommended that you wrap all instances of
 * <code>UnicodeWriter</code> with a <code>java.io.BufferedWriter</code>.
 *
 * @author Robert Futrell
 * @version 0.8
 */
public class UnicodeWriter extends Writer {

	/**
	 * If this system property evaluates to "<code>true</code>", ignoring
	 * case, files written out as UTF-8 will have a BOM written for them.
	 * Otherwise, (even if the property is not set), UTF-8 files will not
	 * have a BOM written (which is typical, older builds of Windows Notepad
	 * are the outlier here).
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
	 * @param fileName The file to which to write.
	 * @param charset The character set to use.
	 * @throws IOException If an IO exception occurs.
	 * @see java.nio.charset.StandardCharsets
	 */
	public UnicodeWriter(String fileName, Charset charset) throws IOException {
		this(new FileOutputStream(fileName), charset.name());
	}


	/**
	 * This is a utility constructor since the vast majority of the time, this
	 * class will be used to write Unicode files.
	 *
	 * @param fileName The file to which to write.
	 * @param encoding The encoding to use.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(String fileName, String encoding) throws IOException {
		this(new FileOutputStream(fileName), encoding);
	}


	/**
	 * This is a utility constructor since the vast majority of the time, this
	 * class will be used to write Unicode files.
	 *
	 * @param file The file to which to write.
	 * @param charset The character set to use.
	 * @throws IOException If an IO exception occurs.
	 * @see java.nio.charset.StandardCharsets
	 */
	public UnicodeWriter(File file, Charset charset) throws IOException {
		this(new FileOutputStream(file), charset.name());
	}


	/**
	 * This is a utility constructor since the vast majority of the time, this
	 * class will be used to write Unicode files.
	 *
	 * @param file The file to which to write.
	 * @param encoding The encoding to use.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(File file, String encoding) throws IOException {
		this(new FileOutputStream(file), encoding);
	}


	/**
	 * Creates a new writer.
	 *
	 * @param out The output stream to write.
	 * @param charset The character set to use.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(OutputStream out, Charset charset) throws IOException {
		init(out, charset.name());
	}


	/**
	 * Creates a new writer.
	 *
	 * @param out The output stream to write.
	 * @param encoding The encoding to use.
	 * @throws IOException If an IO exception occurs.
	 */
	public UnicodeWriter(OutputStream out, String encoding) throws IOException {
		init(out, encoding);
	}


	/**
	 * Closes this writer.
	 *
	 * @throws IOException If an IO exception occurs.
	 */
	@Override
	public void close() throws IOException {
		internalOut.close();
	}


	/**
	 * Flushes the stream.
	 *
	 * @throws IOException If an IO exception occurs.
	 */
	@Override
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
	 * @see #setWriteUtf8BOM(boolean)
	 * @see UnicodeWriter
	 */
	public static boolean getWriteUtf8BOM() {
		return Boolean.getBoolean(PROPERTY_WRITE_UTF8_BOM);
	}


	/**
	 * Initializes the internal output stream and writes the BOM if the
	 * specified encoding is a Unicode encoding.
	 *
	 * @param out The output stream we are writing.
	 * @param encoding The encoding in which to write.
	 * @throws IOException If an I/O error occurs while writing a BOM.
	 */
	private void init(OutputStream out, String encoding) throws IOException {

		internalOut = new OutputStreamWriter(out, encoding);

		// Write the proper BOM if they specified a Unicode encoding.
		// NOTE: Creating an OutputStreamWriter with encoding "UTF-16" DOES
		// write out the BOM; "UTF-16LE", "UTF-16BE", "UTF-32", "UTF-32LE"
		// and "UTF-32BE" don't.
		switch (encoding) {
			case "UTF-8":
				if (getWriteUtf8BOM()) {
					out.write(UTF8_BOM, 0, UTF8_BOM.length);
				}
				break;
			case "UTF-16LE":
				out.write(UTF16LE_BOM, 0, UTF16LE_BOM.length);
				break;
			//case "UTF-16": // Already writes the BOM, so we don't
			case "UTF-16BE":
				out.write(UTF16BE_BOM, 0, UTF16BE_BOM.length);
				break;
			case "UTF-32LE":
				out.write(UTF32LE_BOM, 0, UTF32LE_BOM.length);
				break;
			case "UTF-32":
			case "UTF-32BE":
				out.write(UTF32BE_BOM, 0, UTF32BE_BOM.length);
				break;
		}

	}


	/**
	 * Sets whether UTF-8 files should have a BOM written in them.
	 *
	 * @param write Whether to write a BOM.
	 * @see #getWriteUtf8BOM()
	 * @see UnicodeWriter
	 */
	public static void setWriteUtf8BOM(boolean write) {
		System.setProperty(UnicodeWriter.PROPERTY_WRITE_UTF8_BOM,
				Boolean.toString(write));
	}


	/**
	 * Writes a portion of an array of characters.
	 *
	 * @param cbuf The buffer of characters.
	 * @param off The offset from which to start writing characters.
	 * @param len The number of characters to write.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		internalOut.write(cbuf, off, len);
	}


	/**
	 * Writes a single character.
	 *
	 * @param c An integer specifying the character to write.
	 * @throws IOException If an IO error occurs.
	 */
	@Override
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
	@Override
	public void write(String str, int off, int len) throws IOException {
		internalOut.write(str, off, len);
	}


}
