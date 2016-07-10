/*
 * 01/29/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@code SearchContext} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SearchContextTest {


	@Test
	public void testConstructor_2Arg() {
		SearchContext sc = new SearchContext("foo", true);
		Assert.assertEquals("foo", sc.getSearchFor());
		Assert.assertTrue(sc.getMatchCase());
	}


	@Test
	public void testAddPropertyChangeListener() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assert.assertTrue(pcl.called);
	}


	@Test
	public void testClone() {

		String searchFor = "foo";
		boolean matchCase = true;
		boolean markAll = false;
		boolean regex = true;
		String replaceWith = "bar";
		boolean forward = false;
		boolean selectionOnly = false;//true; // "true" not yet supported
		boolean wholeWord = true;

		SearchContext sc = new SearchContext(searchFor, matchCase);
		sc.setMarkAll(markAll);
		sc.setRegularExpression(regex);
		sc.setReplaceWith(replaceWith);
		sc.setSearchForward(forward);
		sc.setSearchSelectionOnly(selectionOnly);
		sc.setWholeWord(wholeWord);

		SearchContext sc2 = sc.clone();
		Assert.assertEquals(sc2.getSearchFor(), sc.getSearchFor());
		Assert.assertEquals(sc2.getMatchCase(), sc.getMatchCase());
		Assert.assertEquals(sc2.getMarkAll(), sc.getMarkAll());
		Assert.assertEquals(sc2.isRegularExpression(), sc.isRegularExpression());
		Assert.assertEquals(sc2.getReplaceWith(), sc.getReplaceWith());
		Assert.assertEquals(sc2.getSearchForward(), sc.getSearchForward());
		Assert.assertEquals(sc2.getSearchSelectionOnly(), sc.getSearchSelectionOnly());
		Assert.assertEquals(sc2.getWholeWord(), sc.getWholeWord());

	}


	@Test
	public void testFirePropertyChange_booleanArgs() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assert.assertTrue(pcl.called);
	}


	@Test
	public void testFirePropertyChange_stringArgs() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", "old", "new");
		Assert.assertTrue(pcl.called);
	}


	@Test
	public void testRemovePropertyChangeListener() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.removePropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assert.assertFalse(pcl.called);
	}


	@Test(expected = UnsupportedOperationException.class)
	public void testSetSearchSelectionOnly_true() {
		SearchContext sc = new SearchContext();
		sc.setSearchSelectionOnly(true);
	}


	@Test
	public void testToString() {
		final String EXPECTED = "[SearchContext: searchFor=null, " +
				"replaceWith=null, matchCase=false, wholeWord=false, " +
				"regex=false, markAll=true]";
		SearchContext sc = new SearchContext();
		Assert.assertEquals(EXPECTED, sc.toString());
	}


	private static class TestPropertyChangeListener
			implements PropertyChangeListener {

		public boolean called;

		public TestPropertyChangeListener() {
			called = false;
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			called = true;
		}

	}


}
