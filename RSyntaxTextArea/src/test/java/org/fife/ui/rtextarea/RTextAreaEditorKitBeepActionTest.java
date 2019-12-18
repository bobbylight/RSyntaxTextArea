/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeepAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitBeepActionTest {


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.beepAction, new RTextAreaEditorKit.BeepAction().getMacroID());
	}
}
