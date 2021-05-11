/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the {@link SelectRegionLinkGeneratorResult} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class SelectRegionLinkGeneratorResultTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testExecute() {

		RSyntaxTextArea textArea = createTextArea("01234567890");
		SelectRegionLinkGeneratorResult result =
			new SelectRegionLinkGeneratorResult(textArea, 0, 3, 7);
		result.execute();

		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	public void testGetSourceOffset() {

		RSyntaxTextArea textArea = createTextArea("01234567890");
		SelectRegionLinkGeneratorResult result =
			new SelectRegionLinkGeneratorResult(textArea, 9, 3, 7);
		Assert.assertEquals(9, result.getSourceOffset());
	}


}
