package org.fife.ui.rtextarea.readonly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileOffsetLineDiscoverTest {

	@Test
	void testOffsetsWithLFCRSeparator(@TempDir Path tempDir) {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\n\r", filePath);
	}

	@Test
	void testOffsetsWithCRLFSeparator(@TempDir Path tempDir) {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\r\n", filePath);
	}

	@Test
	void testOffsetsWithCRSeparator(@TempDir Path tempDir) {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\r", filePath);
	}

	@Test
	void testOffsetsWithLFSeparator(@TempDir Path tempDir) {
		Path filePath = tempDir.resolve("template.txt");
		testOffsets("\n", filePath);
	}

	private void testOffsets(String separator, Path filePath) {
		Charset charset = StandardCharsets.UTF_8;
		FileBuilderTestUtil.writeTestFileWithAsciiChars(filePath, charset, separator, 1024);

		List<Integer> offsets = FileOffsetLineDiscover.getOffsets(filePath, charset);

		List<Integer> expectedOffsets = new ArrayList<>();
		int separatorSize = separator.length();
		int rows = 1025;
		int last = 0;
		for( int i = 0; i < rows; i++ ){
			last = (last + separatorSize) + i * 9;
			expectedOffsets.add(last - separatorSize);
		}

		assertEquals(expectedOffsets, offsets);
	}
}
