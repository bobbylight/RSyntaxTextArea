/*
 * 03/06/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rtextarea.RTextAreaEditorKit.DumbCompleteWordAction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link DumbCompleteWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitDumbCompleteWordActionTest {


	@Test
	public void testGetWordStart() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		Assert.assertEquals(0, action.getWordStart(textArea, 0));
		Assert.assertEquals(0, action.getWordStart(textArea, 1));
		Assert.assertEquals(0, action.getWordStart(textArea, 2));
		Assert.assertEquals(3, action.getWordStart(textArea, 3));
		Assert.assertEquals(4, action.getWordStart(textArea, 4));
		Assert.assertEquals(4, action.getWordStart(textArea, 5));
		Assert.assertEquals(6, action.getWordStart(textArea, 6));
		// Multiple adjacent spaces treated as a word
		Assert.assertEquals(6, action.getWordStart(textArea, 7));
		Assert.assertEquals(8, action.getWordStart(textArea, 8));
		Assert.assertEquals(8, action.getWordStart(textArea, 9));
		Assert.assertEquals(8, action.getWordStart(textArea, 10));
		Assert.assertEquals(8, action.getWordStart(textArea, 11));
		Assert.assertEquals(8, action.getWordStart(textArea, 12));
		Assert.assertEquals(8, action.getWordStart(textArea, 13));

		textArea.setText("  for  ");
		Assert.assertEquals(0, action.getWordStart(textArea, 0));
		Assert.assertEquals(0, action.getWordStart(textArea, 1));
		Assert.assertEquals(2, action.getWordStart(textArea, 2));
		Assert.assertEquals(2, action.getWordStart(textArea, 3));
		Assert.assertEquals(2, action.getWordStart(textArea, 4));
		Assert.assertEquals(5, action.getWordStart(textArea, 5));
		Assert.assertEquals(5, action.getWordStart(textArea, 6));
		Assert.assertEquals(5, action.getWordStart(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assert.assertEquals(0, action.getWordStart(textArea, 0));

	}


	@Test
	public void testGetWordEnd() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		Assert.assertEquals(3, action.getWordEnd(textArea, 0));
		Assert.assertEquals(3, action.getWordEnd(textArea, 1));
		Assert.assertEquals(3, action.getWordEnd(textArea, 2));
		Assert.assertEquals(4, action.getWordEnd(textArea, 3));
		Assert.assertEquals(6, action.getWordEnd(textArea, 4));
		Assert.assertEquals(6, action.getWordEnd(textArea, 5));
		Assert.assertEquals(8, action.getWordEnd(textArea, 6));
		Assert.assertEquals(8, action.getWordEnd(textArea, 7));
		Assert.assertEquals(13, action.getWordEnd(textArea, 8));
		Assert.assertEquals(13, action.getWordEnd(textArea, 9));
		Assert.assertEquals(13, action.getWordEnd(textArea, 10));
		Assert.assertEquals(13, action.getWordEnd(textArea, 11));
		Assert.assertEquals(13, action.getWordEnd(textArea, 12));
		Assert.assertEquals(13, action.getWordEnd(textArea, 13));

		textArea.setText("  for  ");
		Assert.assertEquals(2, action.getWordEnd(textArea, 0));
		Assert.assertEquals(2, action.getWordEnd(textArea, 1));
		Assert.assertEquals(5, action.getWordEnd(textArea, 2));
		Assert.assertEquals(5, action.getWordEnd(textArea, 3));
		Assert.assertEquals(5, action.getWordEnd(textArea, 4));
		Assert.assertEquals(7, action.getWordEnd(textArea, 5));
		Assert.assertEquals(7, action.getWordEnd(textArea, 6));
		Assert.assertEquals(7, action.getWordEnd(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assert.assertEquals(0, action.getWordEnd(textArea, 0));

	}


	@Test
	public void testGetPreviousWord() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		// Offset 0 throws an exception
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 2));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 3));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 4));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 5));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 6));
		// Spaces - find word before spaces
		Assert.assertEquals(4, action.getPreviousWord(textArea, 7));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 8));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 9));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 10));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 11));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 12));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 13));

		textArea.setText("  for  ");
		// Offset 0 throws an exception
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
		//Assert.assertEquals(2, action.getPreviousWord(textArea, 2));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 3));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 4));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 5));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 6));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 7));

	}


}