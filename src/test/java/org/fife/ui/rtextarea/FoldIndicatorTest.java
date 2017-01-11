/*
 * 12/21/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link FoldIndicator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FoldIndicatorTest {


	@Test
	public void testGetSetFoldIconBackground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconBackground(Color.red);
		Assert.assertEquals(Color.red, fi.getFoldIconBackground());
		fi.setFoldIconBackground(Color.green);
		Assert.assertEquals(Color.green, fi.getFoldIconBackground());
	}


	@Test
	public void testGetSetFoldIconArmedBackground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconArmedBackground(Color.red);
		Assert.assertEquals(Color.red, fi.getFoldIconArmedBackground());
		fi.setFoldIconArmedBackground(Color.green);
		Assert.assertEquals(Color.green, fi.getFoldIconArmedBackground());
	}


}