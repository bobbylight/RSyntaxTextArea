package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.text.Element;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadOnlyDocumentTest {

	private static RSyntaxDocument expectedDocument;
	private static ReadOnlyDocument readOnlyDocument;

	@BeforeAll
	static void beforeAll(@TempDir Path tempDir) throws IOException {
		expectedDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);

		Charset charset;
		Path filePath;
		try{
			charset = StandardCharsets.UTF_8;
			filePath = tempDir.resolve("template.txt");
			FileBuilderTestUtil.writeTestFileWithDoubleByteChar(filePath, charset, "\n", 128);

			String readString = Files.readString(filePath, charset);

			expectedDocument.insertString(0, readString, null);
		} catch( Exception e ){
			throw new RuntimeException(e);
		}

		ReadOnlyFileStructure fileStructure = new ReadOnlyFileStructureParser(filePath, charset).readStructure();
		ReadOnlyContent content = new ReadOnlyContent(filePath, charset, fileStructure);
		readOnlyDocument = new ReadOnlyDocument(null, "", content);
	}

	@Test
	void testLengths() {
		assertEquals(expectedDocument.getRootElements().length, readOnlyDocument.getRootElements().length);
		assertEquals(expectedDocument.getLength(), readOnlyDocument.getLength());
		assertEquals(expectedDocument.getDefaultRootElement().getElementCount(), readOnlyDocument.getDefaultRootElement().getElementCount());
	}

	@Test
	void testElementIndex() {
		int length = expectedDocument.getLength();
		for( int i = 0; i < length; i++ ){
			assertEquals(expectedDocument.getDefaultRootElement().getElementIndex(i), readOnlyDocument.getDefaultRootElement().getElementIndex(i), "testElementIndex filed on index: " + i);
		}
	}

	@Test
	void testStartOffsets() {
		Element originalRootElement = expectedDocument.getDefaultRootElement();
		Element readonlyRootElement = readOnlyDocument.getDefaultRootElement();
		int elementCount = originalRootElement.getElementCount();

		for( int i = 0; i < elementCount; i++ ){
			assertEquals(originalRootElement.getElement(i).getStartOffset(), readonlyRootElement.getElement(i).getStartOffset());
		}
	}

	@Test
	void testEndOffsets() {
		Element originalRootElement = expectedDocument.getDefaultRootElement();
		Element readonlyRootElement = readOnlyDocument.getDefaultRootElement();
		int elementCount = originalRootElement.getElementCount();

		for( int i = 0; i < elementCount; i++ ){
			assertEquals(originalRootElement.getElement(i).getEndOffset(), readonlyRootElement.getElement(i).getEndOffset());
		}
	}
}
