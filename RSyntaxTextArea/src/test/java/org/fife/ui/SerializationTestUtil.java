/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Utility methods for testing serialization.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class SerializationTestUtil {

	private SerializationTestUtil() {
		// Do nothing (comment for Sonar)
	}


	/**
	 * Serializes and then deserializes an object.
	 *
	 * @param <T> The type of the object.
	 * @param obj The object to serialize.
	 * @return The deserialized copy of the object.
	 * @throws ClassNotFoundException Shouldn't happen.
	 * @throws IOException If an IO error occurs.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T roundTrip(T obj)
			throws IOException, ClassNotFoundException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
			out.writeObject(obj);
		}

		try (ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(baos.toByteArray()))) {
			return (T)in.readObject();
		}
	}
}
