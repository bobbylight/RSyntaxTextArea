/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.DefaultKeyTypedAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitDefaultKeyTypedActionTest {


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.defaultKeyTypedAction,
			new RTextAreaEditorKit.DefaultKeyTypedAction().getMacroID());
	}
}
