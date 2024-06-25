package org.fife.ui.rtextarea.readonly;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;
import javax.swing.text.Segment;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadOnlyContentTest {

	private static GapContent expectedContent;
	private static ReadOnlyContent readOnlyContent;

	@BeforeAll
	static void beforeAll(@TempDir Path tempDir) throws Exception {
		Path filePath = tempDir.resolve("template.txt");
		Charset charset = StandardCharsets.UTF_16;
		FileBuilderTestUtil.writeTestFileWithAsciiChars(filePath, charset, "\n", 128);
		expectedContent = new GapContent();
		String readString = Files.readString(filePath, charset);
		expectedContent.insertString(0, readString);
		ReadOnlyFileStructure fileStructure = new ReadOnlyFileStructureParser(filePath, charset).readStructure();
		readOnlyContent = new ReadOnlyContent(filePath, charset, fileStructure);
	}

	@Test
	void testGetLength() {
		assertEquals(expectedContent.length(), readOnlyContent.length());
	}

	@Test
	void testGetStringWithSingleChar() throws BadLocationException {
		int last = expectedContent.length() - 1;
		for( int i = 0; i < last; i++ ){
			testGetString(i, 1);
		}
	}

	@Test
	void testGetStringWithTwoChars() throws BadLocationException {
		int last = expectedContent.length() - 1;
		for( int i = 0; i < last - 1; i++ ){
			testGetString(i, 2);
		}
	}

	@Test
	void testGetStringThrowsExceptionWhenOffsetExceedLength() {
		int length = expectedContent.length();
		testGetStringThrowsException(length, 1);
		testGetStringThrowsException(length - 1, 2);
	}

	@Test
	void testGetSegmentWithSingleChar() throws BadLocationException {
		int last = expectedContent.length() - 1;
		for( int i = 0; i < last; i++ ){
			testGetSegment(i, 1);
		}
	}

	@Test
	void testGetSegmentWithTwoChars() throws BadLocationException {
		int last = expectedContent.length() - 1;
		for( int i = 0; i < last - 1; i++ ){
			testGetSegment(i, 2);
		}
	}

	private void testGetString(int where, int len) throws BadLocationException {
		assertEquals(expectedContent.getString(where, len), readOnlyContent.getString(where, len), () -> "Error on offset where: " + where + " len: " + len);
	}

	private void testGetStringThrowsException(int where, int len) {
		assertThrows(BadLocationException.class, () -> expectedContent.getString(where, len));
		assertThrows(BadLocationException.class, () -> readOnlyContent.getString(where, len));
	}


	private void testGetSegment(int where, int len) throws BadLocationException {
		Segment testSegment = new Segment();
		Segment readOnlySegment = new Segment();

		testSegment.setPartialReturn(true);
		readOnlySegment.setPartialReturn(true);
		expectedContent.getChars(where, len, testSegment);
		readOnlyContent.getChars(where, len, readOnlySegment);

		assertEquals(testSegment.toString(), readOnlySegment.toString(), () -> "Error on offset where:" + where);
	}
}
