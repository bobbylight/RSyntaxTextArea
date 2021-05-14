/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


/**
 * Unit tests for the {@link NsisFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class NsisFoldParserTest {

	@Test
	public void testGetFolds_happyPath() {

		String code =
			"/*\n" +
			" * Multi-line comment.\n" +
			" */\n" +
			"# set the name of the installer\n" +
			"Outfile \"hello world.exe\"\n" +
			" \n" +
			"# create a default section.\n" +
			"Section\n" +
			" \n" +
			"# create a popup box, with an OK button and the text \"Hello world!\"\n" +
			"MessageBox MB_OK \"Hello world!\"\n" +
			" \n" +
			"SectionEnd\n" +
			"\n" +
			"Function Hello\n" +
			"  DetailPrint \"Hello world\"\n" +
			"FunctionEnd";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NSIS);

		FoldParser parser = new NsisFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assert.assertEquals(3, folds.size());

		Fold firstFold = folds.get(0);
		Assert.assertEquals(FoldType.COMMENT, firstFold.getFoldType());
		Assert.assertEquals(code.indexOf("/*"), firstFold.getStartOffset());
		Assert.assertEquals(code.indexOf("*/") + 1, firstFold.getEndOffset());

		Assert.assertEquals(0, firstFold.getChildCount());

		Fold secondFold = folds.get(1);
		Assert.assertEquals(FoldType.CODE, secondFold.getFoldType());
		Assert.assertEquals(code.indexOf("Section"), secondFold.getStartOffset());
		Assert.assertEquals(code.indexOf("SectionEnd"), secondFold.getEndOffset());

		Assert.assertEquals(0, secondFold.getChildCount());

		Fold thirdFold = folds.get(2);
		Assert.assertEquals(FoldType.CODE, thirdFold.getFoldType());
		Assert.assertEquals(code.indexOf("Function"), thirdFold.getStartOffset());
		Assert.assertEquals(code.indexOf("FunctionEnd"), thirdFold.getEndOffset());

		Assert.assertEquals(0, thirdFold.getChildCount());
	}
}
