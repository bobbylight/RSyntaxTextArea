package org.fife.ui.rtextarea.readonly;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadOnlyContent implements ReadOnlyContentInterface {

	private static final Logger LOGGER = Logger.getLogger(ReadOnlyContent.class.getName());

	private static final int MAX_BUFFER_SIZE = 1024 * 1024; //1 MB
	private static final int NOT_INITIALIZED_BUFFER_OFFSET = -1;

	private final File file;
	private final Charset charset;
	private final List<Integer> offsets;
	private final int bufferSize;
	private final int fileSize;
	private final char[] buffer;

	private int charsCount = -1;

	private int startBufferOffset = NOT_INITIALIZED_BUFFER_OFFSET;


	public ReadOnlyContent(File file, Charset charset) {
		this.file = file;
		this.charset = charset;
		fileSize = (int) file.length();
		bufferSize = Math.min(fileSize, MAX_BUFFER_SIZE);
		buffer = new char[bufferSize];
		offsets = FileOffsetLineDiscover.getOffsets(file.toPath(), charset);
	}


	@Override
	public Position createPosition(int offset) {
		return () -> offset;
	}

	@Override
	public int length() {
		if( charsCount == -1 ){
			charsCount = getCharsCount();
		}
		return charsCount;
	}

	@Override
	public UndoableEdit insertString(int where, String str) throws BadLocationException {
		throw new BadLocationException("Unable to insert String. This content is not editable", where);
	}

	@Override
	public UndoableEdit remove(int where, int nitems) throws BadLocationException {
		throw new BadLocationException("Unable to remove String. This content is not editable", where);
	}

	@Override
	public String getString(int charsOffset, int amountCharsToRead) throws BadLocationException {
		int endReadOffset = charsOffset + amountCharsToRead;

		if( endReadOffset >= length() ){
			throw new BadLocationException("Unable to get String. The requested end offset exceed the document length: ", charsOffset + amountCharsToRead);
		}

		int endBuffedOffset = buffer.length + startBufferOffset;

		if( startBufferOffset == NOT_INITIALIZED_BUFFER_OFFSET || charsOffset < startBufferOffset || endReadOffset > endBuffedOffset ){
			reloadBuffer(charsOffset);
		}
		int relativeIndex = Math.min(charsOffset - startBufferOffset, bufferSize - amountCharsToRead);
		try{
			return new String(buffer, relativeIndex, amountCharsToRead);
		} catch( Exception e ){
			throw new ReadOnlyBadLocationException("Unable to read String", charsOffset, e);
		}
	}


	private void reloadBuffer(int startReadOffset) throws BadLocationException {
		if( fileSize < startReadOffset + bufferSize ){
			startReadOffset = Math.max(0, fileSize - bufferSize);
		}
		this.startBufferOffset = startReadOffset;

		read(startReadOffset, bufferSize);
	}

	private void read(int startReadOffset, int charsToRead) throws BadLocationException {
		try( BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), charset); ){
			bufferedReader.skip(startReadOffset);
			int foundCharsCount = bufferedReader.read(buffer, 0, charsToRead);

			if( charsToRead != foundCharsCount ){
				throw new BadLocationException("Not all required chars are available", startReadOffset);
			}
		} catch( Exception e ){
			throw new ReadOnlyBadLocationException("Unable to read file", startReadOffset, e);
		}
	}

	@Override
	public void getChars(int where, int len, Segment chars) throws BadLocationException {
		String content = getString(where, len);

		chars.array = content.toCharArray();
		chars.offset = 0;
		chars.count = len;
	}

	@Override
	public char charAt(int offset) {
		try{
			return getString(offset, 1).charAt(0);
		} catch( BadLocationException e ){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getStartOffset(int row) {
		return offsets.get(row);
	}

	@Override
	public int getEndOffset(int index) {
		if( index < offsets.size() - 1 )
			return offsets.get(index + 1);
		else return offsets.get(offsets.size() - 1) + 1;
	}

	@Override
	public int getElementCount() {
		return offsets.size();
	}

	@Override
	public int getElementIndex(int offset) {
		if (offsets.isEmpty()) return 0;

		for( int i = 0; i < offsets.size(); i++ ){
			if (offset < offsets.get(i+1)) {
				return i;
			}
		}
		return 0;

	}

	private int getCharsCount() {
		try( BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), charset); ){
			char[] charBuffer = new char[1024 * 1024];
			int count = 2;
			int readChars = 0;
			while( readChars >= 0 ){
				readChars = bufferedReader.read(charBuffer);
				count += readChars;
			}
			return count;
		} catch( Exception e ){
			LOGGER.log(Level.WARNING, "Unable to read chars count", e);
			return 0;
		}
	}

	private static class ReadOnlyBadLocationException extends BadLocationException {

		public ReadOnlyBadLocationException(String s, int offs, Exception cause) {
			super(s, offs);
			setStackTrace(cause.getStackTrace());
		}
	}
}
