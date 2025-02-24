/*
 * 01/29/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@code SearchContext} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SearchContextTest {


	@Test
	void testConstructor_2Arg() {
		SearchContext sc = new SearchContext("foo", true);
		Assertions.assertEquals("foo", sc.getSearchFor());
		Assertions.assertTrue(sc.getMatchCase());
	}


	@Test
	void testAddPropertyChangeListener() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assertions.assertTrue(pcl.called);
	}


	@Test
	void testClone() {

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
		Assertions.assertEquals(sc2.getSearchFor(), sc.getSearchFor());
		Assertions.assertEquals(sc2.getMatchCase(), sc.getMatchCase());
		Assertions.assertEquals(sc2.getMarkAll(), sc.getMarkAll());
		Assertions.assertEquals(sc2.isRegularExpression(), sc.isRegularExpression());
		Assertions.assertEquals(sc2.getReplaceWith(), sc.getReplaceWith());
		Assertions.assertEquals(sc2.getSearchForward(), sc.getSearchForward());
		Assertions.assertEquals(sc2.getSearchSelectionOnly(), sc.getSearchSelectionOnly());
		Assertions.assertEquals(sc2.getWholeWord(), sc.getWholeWord());

	}


	@Test
	void testFirePropertyChange_booleanArgs() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assertions.assertTrue(pcl.called);
	}


	@Test
	void testFirePropertyChange_stringArgs() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", "old", "new");
		Assertions.assertTrue(pcl.called);
	}


	@Test
	void testGetSetMatchCase() {
		SearchContext sc = new SearchContext();
		Assertions.assertFalse(sc.getMatchCase());
		sc.setMatchCase(true);
		Assertions.assertTrue(sc.getMatchCase());
		sc.setMatchCase(true); // conditional testing
		Assertions.assertTrue(sc.getMatchCase());
	}


	@Test
	void testGetSetSearchFor() {
		SearchContext sc = new SearchContext();
		Assertions.assertNull(sc.getSearchFor());
		sc.setSearchFor("foo");
		Assertions.assertEquals("foo", sc.getSearchFor());
		sc.setSearchFor("foo"); // conditional testing
		Assertions.assertEquals("foo", sc.getSearchFor());
	}


	@Test
	void testGetSetSearchSelectionOnly() {
		SearchContext sc = new SearchContext();
		// Can't test any other value as true currently isn't supported
		Assertions.assertFalse(sc.getSearchSelectionOnly());
	}


	@Test
	void testGetSetSearchWrap() {
		SearchContext sc = new SearchContext();
		Assertions.assertFalse(sc.getSearchWrap());
		sc.setSearchWrap(true);
		Assertions.assertTrue(sc.getSearchWrap());
		sc.setSearchWrap(true); // conditional testing
		Assertions.assertTrue(sc.getSearchWrap());
	}


	@Test
	void testRemovePropertyChangeListener() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener();
		SearchContext sc = new SearchContext();
		sc.addPropertyChangeListener(pcl);
		sc.removePropertyChangeListener(pcl);
		sc.firePropertyChange("fooProp", false, true);
		Assertions.assertFalse(pcl.called);
	}


	@Test
	void testSetSearchSelectionOnly_true() {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			SearchContext sc = new SearchContext();
			sc.setSearchSelectionOnly(true);
		});
	}


	@Test
	void testToString() {
		String expected = "[SearchContext: searchFor=null, " +
				"replaceWith=null, matchCase=false, wholeWord=false, " +
				"regex=false, markAll=true]";
		SearchContext sc = new SearchContext();
		Assertions.assertEquals(expected, sc.toString());
	}


	private static class TestPropertyChangeListener
			implements PropertyChangeListener {

		protected boolean called;

		protected TestPropertyChangeListener() {
			called = false;
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			called = true;
		}

	}


}
