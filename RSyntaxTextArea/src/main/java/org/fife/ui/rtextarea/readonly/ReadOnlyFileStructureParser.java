package org.fife.ui.rtextarea.readonly;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadOnlyFileStructureParser {

	private static final int BUFFER_SIZE = 1024 * 1024;
	private final Path filePath;
	private final Charset charset;

	public ReadOnlyFileStructureParser(Path filePath, Charset charset) {
		this.filePath = filePath;
		this.charset = charset;
	}

	public ReadOnlyFileStructure readStructure() throws IOException {

		try( BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset) ){
			char[] charBuffer = new char[BUFFER_SIZE];
			FileCharsCounter fileCharsCounter = new FileCharsCounter();
			FileOffsetLineDiscover fileOffsetLineDiscover = new FileOffsetLineDiscover();

			int readChars = 0;
			while( readChars >= 0 ){
				readChars = bufferedReader.read(charBuffer);
				fileCharsCounter.onBufferRead(readChars);
				fileOffsetLineDiscover.onBufferRead(charBuffer, readChars);
			}
			return new ReadOnlyFileStructure(fileCharsCounter.getCount(), fileOffsetLineDiscover.getOffsets(), Files.size(filePath));
		}
	}
}
