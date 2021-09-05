/*
 * 06/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link DynamicIntArray} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DynamicIntArrayTest {


	@Test
	void testZeroArgConstructor() {
		new DynamicIntArray();
	}


	@Test
	void testIntArgConstructor() {
		new DynamicIntArray(100);
	}


	@Test
	void testArrayArgConstructor() {
		new DynamicIntArray(new int[] { 0, 1, 2 });
	}


	@Test
	void testAdd_1ArgOverload() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertEquals(0, array.getSize());
		array.add(5);
		Assertions.assertEquals(1, array.getSize());
		array.add(5);
		Assertions.assertEquals(2, array.getSize());
	}


	@Test
	void testAdd_2ArgOverload_Array() {

		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertEquals(0, array.getSize());

		array.add(0, new int[] { 1, 2 });
		Assertions.assertEquals(2, array.getSize());
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(2, array.get(1));

		array.add(1, new int[] { 5, 6 });
		Assertions.assertEquals(4, array.getSize());
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(5, array.get(1));
		Assertions.assertEquals(6, array.get(2));
		Assertions.assertEquals(2, array.get(3));

	}


	@Test
	void testAdd_2ArgOverload_2Ints() {

		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertEquals(0, array.getSize());

		array.add(0, 5);
		Assertions.assertEquals(1, array.getSize());
		Assertions.assertEquals(5, array.get(0));

		array.add(0, 4);
		Assertions.assertEquals(2, array.getSize());
		Assertions.assertEquals(4, array.get(0));
		Assertions.assertEquals(5, array.get(1));

	}


	@Test
	void testClear() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertEquals(3, array.getSize());
		array.clear();
		Assertions.assertEquals(0, array.getSize());
	}


	@Test
	void testContains() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertFalse(array.contains(0));
		Assertions.assertTrue(array.contains(1));
		Assertions.assertTrue(array.contains(2));
		Assertions.assertTrue(array.contains(3));
		Assertions.assertFalse(array.contains(4));
	}


	@Test
	void testDecrement() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		array.decrement(2, 4);
		Assertions.assertEquals(7, array.get(0));
		Assertions.assertEquals(7, array.get(1));
		Assertions.assertEquals(6, array.get(2));
		Assertions.assertEquals(6, array.get(3));
		Assertions.assertEquals(7, array.get(4));
	}


	@Test
	void testfill() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		for (int i = 0; i < array.getSize(); i++) {
			Assertions.assertEquals(7, array.get(i));
		}
		array.fill(3);
		for (int i = 0; i < array.getSize(); i++) {
			Assertions.assertEquals(3, array.get(i));
		}
	}


}
