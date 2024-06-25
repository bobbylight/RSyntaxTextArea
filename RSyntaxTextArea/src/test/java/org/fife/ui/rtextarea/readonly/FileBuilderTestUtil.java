package org.fife.ui.rtextarea.readonly;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBuilderTestUtil {

	public static void writeTestFileWithAsciiChars(Path filePath, Charset charset, String lineSeparator, int rows) throws IOException {
		writeTestFile(filePath, charset, lineSeparator, "abcdefghi", rows, true);
	}

	public static void writeTestFileWithDoubleByteChar(Path filePath, Charset charset, String lineSeparator, int rows) throws IOException {
		writeTestFile(filePath, charset, lineSeparator, "Aݔabcdefg", rows, true);
	}

	public static void writeTestFile(Path filePath, Charset charset, String lineSeparator, String content, int rows, boolean includeBom) throws IOException {

		if( includeBom ){
			String text = lineSeparator;
			try( BufferedWriter writer = Files.newBufferedWriter(filePath, charset) ){
				for( int i = 0; i < rows; i++ ){
					text = content + text;
					writer.write(text);
				}
			}
		} else{
			try( OutputStream out = new FileOutputStream(filePath.toFile()) ){
				String text = lineSeparator;
				for( int i = 0; i < rows; i++ ){
					text = content + text;
					out.write(text.getBytes(charset));
				}
			}
		}
	}
}
