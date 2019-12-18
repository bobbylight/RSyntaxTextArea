/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitBeginActionTest {


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("begin", new RTextAreaEditorKit.BeginAction("begin", false).getMacroID());
	}
}
