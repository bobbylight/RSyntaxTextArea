package org.fife.ui.rsyntaxtextarea;

import java.util.Map;
import java.util.Set;


/**
 * Base class for {@link TokenMakerFactory} implementations.  A
 * <code>java.util.Map</code> maps keys to the names of {@link TokenMaker}
 * classes.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractTokenMakerFactory extends TokenMakerFactory {

	/**
	 * A mapping from keys to the names of {@link TokenMaker} implementation
	 * class names.  When {@link #getTokenMaker(String)} is called with a key
	 * defined in this map, a <code>TokenMaker</code> of the corresponding type
	 * is returned.
	 */
	private Map tokenMakerMap;


	/**
	 * Constructor.
	 */
	protected AbstractTokenMakerFactory() {
		tokenMakerMap = createTokenMakerKeyToClassNameMap();
	}



	/**
	 * Creates and returns a mapping from keys to the names of
	 * {@link TokenMaker} implementation classes.  When
	 * {@link #getTokenMaker(String)} is called with a key defined in this
	 * map, a <code>TokenMaker</code> of the corresponding type is returned.
	 *
	 * @return The map.
	 */
	protected abstract Map createTokenMakerKeyToClassNameMap();


	/**
	 * Returns a {@link TokenMaker} for the specified key.
	 *
	 * @param key The key.
	 * @return The corresponding <code>TokenMaker</code>, or <code>null</code>
	 *         if none matches the specified key.
	 */
	protected TokenMaker getTokenMakerImpl(String key) {
		String clazz = (String)tokenMakerMap.get(key);
		if (clazz!=null) {
			try {
				return (TokenMaker)Class.forName(clazz).newInstance();
			} catch (RuntimeException re) { // FindBugs
				throw re;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	public Set keySet() {
		return tokenMakerMap.keySet();
	}


	/**
	 * Adds a mapping from a key to a <code>TokenMaker</code> implementation
	 * class name.
	 *
	 * @param key The key.
	 * @param className The <code>TokenMaker</code> class name.
	 * @return The previous value for the specified key, or <code>null</code>
	 *         if there was none.
	 */
	public String putMapping(String key, String className) {
		return (String)tokenMakerMap.put(key, className);
	}


}