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
	void testIntArgConstructor_invalidCapacity() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new DynamicIntArray(-1));
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
	void testAdd_2ArgOverload_Array_error_indexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.add(99999, new int[0])
		);
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
	void testAdd_2ArgOverload_2Ints_error_indexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.add(99999, 42)
		);
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
	void testFill() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		for (int i = 0; i < array.getSize(); i++) {
			Assertions.assertEquals(7, array.get(i));
		}
		array.fill(3);
		for (int i = 0; i < array.getSize(); i++) {
			Assertions.assertEquals(3, array.get(i));
		}
	}


	@Test
	void testGet_happyPath() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5 });
		for (int i = 0; i < 5; i++) {
			Assertions.assertEquals(i + 1, array.get(i));
		}
	}


	@Test
	void testGet_error_indexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.get(999)
		);
	}


	@Test
	void testGetUnsafe_happyPath() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5 });
		for (int i = 0; i < 5; i++) {
			Assertions.assertEquals(i + 1, array.getUnsafe(i));
		}
	}


	@Test
	void testGetUnsafe_error_indexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.get(999)
		);
	}


	@Test
	void testIncrement() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		array.increment(2, 4);
		Assertions.assertEquals(7, array.get(0));
		Assertions.assertEquals(7, array.get(1));
		Assertions.assertEquals(8, array.get(2));
		Assertions.assertEquals(8, array.get(3));
		Assertions.assertEquals(7, array.get(4));
	}


	@Test
	void testInsertRange_happyPath_insertingNonZero() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		array.insertRange(2, 3, 4);
		Assertions.assertEquals(7, array.get(0));
		Assertions.assertEquals(7, array.get(1));
		Assertions.assertEquals(4, array.get(2));
		Assertions.assertEquals(4, array.get(3));
		Assertions.assertEquals(4, array.get(4));
		Assertions.assertEquals(7, array.get(5));
		Assertions.assertEquals(7, array.get(6));
		Assertions.assertEquals(7, array.get(7));
	}


	@Test
	void testInsertRange_happyPath_insertingZero() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		array.insertRange(2, 3, 0);
		Assertions.assertEquals(7, array.get(0));
		Assertions.assertEquals(7, array.get(1));
		Assertions.assertEquals(0, array.get(2));
		Assertions.assertEquals(0, array.get(3));
		Assertions.assertEquals(0, array.get(4));
		Assertions.assertEquals(7, array.get(5));
		Assertions.assertEquals(7, array.get(6));
		Assertions.assertEquals(7, array.get(7));
	}


	@Test
	void testInsertRange_error_offsetTooLarge() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 7, 7, 7, 7, 7 });
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.insertRange(9999, 2, 2)
		);
	}


	@Test
	void testIsEmpty() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertTrue(array.isEmpty());
		array.add(42);
		Assertions.assertFalse(array.isEmpty());
		array.remove(0);
		Assertions.assertTrue(array.isEmpty());
	}


	@Test
	void testRemove_happyPath_fromMiddle() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5 });
		array.remove(2);
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(2, array.get(1));
		Assertions.assertEquals(4, array.get(2));
		Assertions.assertEquals(5, array.get(3));
		Assertions.assertEquals(4, array.getSize());
	}


	@Test
	void testRemove_happyPath_fromEnd() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5 });
		array.remove(4);
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(2, array.get(1));
		Assertions.assertEquals(3, array.get(2));
		Assertions.assertEquals(4, array.get(3));
		Assertions.assertEquals(4, array.getSize());
	}


	@Test
	void testGetRemove_error_indexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.remove(999)
		);
	}


	@Test
	void testRemoveRange_happyPath_fromMiddle() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5, 6 });
		array.removeRange(2, 4);
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(2, array.get(1));
		Assertions.assertEquals(5, array.get(2));
		Assertions.assertEquals(6, array.get(3));
		Assertions.assertEquals(4, array.getSize());
	}


	@Test
	void testRemoveRange_happyPath_fromEnd() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3, 4, 5, 6 });
		array.removeRange(4, 6);
		Assertions.assertEquals(1, array.get(0));
		Assertions.assertEquals(2, array.get(1));
		Assertions.assertEquals(3, array.get(2));
		Assertions.assertEquals(4, array.get(3));
		Assertions.assertEquals(4, array.getSize());
	}


	@Test
	void testGetRemoveRange_error_fromIndexTooLarge() {
		DynamicIntArray array = new DynamicIntArray();
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.removeRange(1000, 1002)
		);
	}


	@Test
	void testGetRemoveRange_error_toIndexTooLarge() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.removeRange(1, 8)
		);
	}


	@Test
	void testSet_happyPath() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertEquals(2, array.get(1));
		array.set(1, 7);
		Assertions.assertEquals(7, array.get(1));
	}


	@Test
	void testSet_error_offsetTooLarge() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.set(9, 2)
		);
	}


	@Test
	void testSetUnsafe_happyPath() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertEquals(2, array.get(1));
		array.setUnsafe(1, 7);
		Assertions.assertEquals(7, array.get(1));
	}


	@Test
	void testSetUnsafe_error_offsetTooLarge() {
		DynamicIntArray array = new DynamicIntArray(new int[] { 1, 2, 3 });
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->
			array.setUnsafe(9, 2)
		);
	}
}
