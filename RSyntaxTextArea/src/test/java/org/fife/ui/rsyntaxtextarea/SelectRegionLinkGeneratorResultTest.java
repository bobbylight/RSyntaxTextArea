/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Unit tests for the {@link SelectRegionLinkGeneratorResult} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SelectRegionLinkGeneratorResultTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testExecute() {

		RSyntaxTextArea textArea = createTextArea("01234567890");
		SelectRegionLinkGeneratorResult result =
			new SelectRegionLinkGeneratorResult(textArea, 0, 3, 7);
		result.execute();

		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	void testGetSourceOffset() {

		RSyntaxTextArea textArea = createTextArea("01234567890");
		SelectRegionLinkGeneratorResult result =
			new SelectRegionLinkGeneratorResult(textArea, 9, 3, 7);
		Assertions.assertEquals(9, result.getSourceOffset());
	}


}
