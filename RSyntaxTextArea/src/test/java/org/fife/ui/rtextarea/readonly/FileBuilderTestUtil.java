package org.fife.ui.rtextarea.readonly;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBuilderTestUtil {

	public static void writeTestFileWithAsciiChars(Path filePath, Charset charset, String lineSeparator, int rows) {
		writeTestFile(filePath, charset, lineSeparator, "abcdefghi", rows);
	}

	public static void writeTestFileWithDoubleByteChar(Path filePath, Charset charset, String lineSeparator, int rows) {
		writeTestFile(filePath, charset, lineSeparator, "Aݔabcdefg", rows);
	}

	private static void writeTestFile(Path filePath, Charset charset, String lineSeparator, String content, int rows) {
		String text = lineSeparator;
		try( BufferedWriter writer = Files.newBufferedWriter(filePath, charset) ){
			for( int i = 0; i < rows; i++ ){
				text = content + text;
				writer.write(text);
			}

		} catch( IOException e ){
			throw new RuntimeException(e);
		}
	}
}
