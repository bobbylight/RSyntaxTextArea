package org.fife.ui.rtextarea.readonly;

import org.fife.io.UnicodeReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadOnlyFileStructureParser {

	private static final int BUFFER_SIZE = 1024 * 1024;
	private final Path filePath;
	private final Charset charset;
	private final int tailOffset;

	public ReadOnlyFileStructureParser(Path filePath, Charset charset) {
		this(filePath, charset, 0);
	}

	//tailOffset can be used to exclude the header of a file
	public ReadOnlyFileStructureParser(Path filePath, Charset charset, int tailOffset) {
		this.filePath = filePath;
		this.charset = charset;
		this.tailOffset = tailOffset;
	}

	public ReadOnlyFileStructure readStructure() throws IOException {

		try( UnicodeReader bufferedReader = new UnicodeReader(filePath.toFile(), charset) ){
			bufferedReader.skip(tailOffset);
			char[] charBuffer = new char[BUFFER_SIZE];
			FileCharsCounter fileCharsCounter = new FileCharsCounter();
			FileOffsetLineDiscover fileOffsetLineDiscover = new FileOffsetLineDiscover();

			int readChars = 0;
			while( readChars >= 0 ){
				readChars = bufferedReader.read(charBuffer);
				fileCharsCounter.onBufferRead(readChars);
				fileOffsetLineDiscover.onBufferRead(charBuffer, readChars);
			}
			return new ReadOnlyFileStructure(fileCharsCounter.getCount(), fileOffsetLineDiscover.getOffsets(), Files.size(filePath), tailOffset);
		}
	}
}
