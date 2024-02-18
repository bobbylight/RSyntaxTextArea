package org.fife.ui.rtextarea.readonly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileOffsetLineDiscoverTest {

	@Test
	void testOffsetsWithLFCRSeparator(@TempDir Path tempDir) throws IOException {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\n\r", filePath);
	}

	@Test
	void testOffsetsWithCRLFSeparator(@TempDir Path tempDir) throws IOException {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\r\n", filePath);
	}

	@Test
	void testOffsetsWithCRSeparator(@TempDir Path tempDir) throws IOException {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\r", filePath);
	}

	@Test
	void testOffsetsWithLFSeparator(@TempDir Path tempDir) throws IOException {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\n", filePath);
	}

	private void testOffsets(String separator, Path filePath) throws IOException {
		Charset charset = StandardCharsets.UTF_8;
		int rows = 2048;
		FileBuilderTestUtil.writeTestFileWithAsciiChars(filePath, charset, separator, rows);

		FileOffsetLineDiscover fileOffsetLineDiscover = new FileOffsetLineDiscover();
		try( BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset); ){
			char[] charBuffer = new char[1024];
			int readChars = 0;
			while( readChars >= 0 ){
				readChars = bufferedReader.read(charBuffer);
				fileOffsetLineDiscover.onBufferRead(charBuffer, readChars);
			}
		}
		List<Integer> offsets = fileOffsetLineDiscover.getOffsets();


		List<Integer> expectedOffsets = new ArrayList<>();
		int separatorSize = separator.length();
		int last = 0;
		for( int i = 0; i <= rows; i++ ){
			last = (last + separatorSize) + i * 9;
			expectedOffsets.add(last - separatorSize);
		}

		assertEquals(expectedOffsets, offsets);
	}
}
