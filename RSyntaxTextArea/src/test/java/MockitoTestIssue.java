import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import javax.swing.text.PlainDocument;

public class MockitoTestIssue {

	@Test
	void testShowingInvalidSpy() {
		PlainDocument doc = new PlainDocument();
		PlainDocument docSpy = spy(doc);
		Assertions.assertEquals(0, docSpy.getLength());
	}
}
