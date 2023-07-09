/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for the {@code PythonFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class PythonFoldParserTest {

	@Test
	void testGetFold_happyPath() {

		String code = "#!/usr/bin/env python\n" +
			"\n" +
			"import getopt, sys, urllib, time\n" +
			"def main():\n" +
			"\n" +
			"    status = 0\n" +
			"\n" +
			"# input arguments\n" +
			"\n" +
			"    try:\n" +
			"    opts, args = getopt.getopt(sys.argv[1:],\"h:iq\",\n" +
			"                   [\"help\",\"invid=\",\"quarter=\"])\n" +
			"    except getopt.GetoptError:\n" +
			"    usage()\n" +
			"    tree = False\n" +
			"    for o, a in opts:\n" +
			"    if o in (\"-h\", \"--help\"):\n" +
			"        usage()\n" +
			"\n" +
			"    kepid, invid, kepmag, mode, start, stop, release = GetMetaData(invid,quarter)\n" +
			"\n" +
			"# convert Gregorian date to Julian date\n" +
			"\n" +
			"def Greg2JD(year, month, day):\n" +
			"\n" +
			"    if (month < 3) \\\\ \n" +
			"and true:\n" +
			"        y = float(year) - 1.0\n" +
			"        m = float(month) + 12.0\n";

		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);

		PythonFoldParser parser = new PythonFoldParser();
		List<Fold> folds = parser.getFolds(textArea);

		Assertions.assertEquals(2, folds.size());

		// First top-level fold is def main()
		Fold topLevelFold = folds.get(0);
		Assertions.assertEquals(FoldType.CODE, topLevelFold.getFoldType());
		Assertions.assertEquals(code.indexOf("def main"), topLevelFold.getStartOffset());
		Assertions.assertEquals(2, topLevelFold.getChildCount());

		// First sub-fold is getopt.getopt() call
		Fold childFold = topLevelFold.getChild(0);
		Assertions.assertEquals(code.indexOf("opts, args"), childFold.getStartOffset());
		Assertions.assertEquals(code.indexOf(" [\"help"), childFold.getEndOffset());

		// Second sub-fold is the if-statement related to "help"
		childFold = topLevelFold.getChild(1);
		int startOffs = childFold.getStartOffset();
		Assertions.assertEquals(code.indexOf("if o in "), startOffs);
		Assertions.assertEquals(code.indexOf(" usage()", startOffs),
			childFold.getEndOffset());

		// Second top-level fold is def Greg2JD
		topLevelFold = folds.get(1);
		Assertions.assertEquals(FoldType.CODE, topLevelFold.getFoldType());
		Assertions.assertEquals(code.indexOf("def Greg2JD"), topLevelFold.getStartOffset());
		Assertions.assertEquals(1, topLevelFold.getChildCount());

	}
}
