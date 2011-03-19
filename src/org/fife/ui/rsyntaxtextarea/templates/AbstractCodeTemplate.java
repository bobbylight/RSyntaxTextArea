/*
 * 12/01/2008
 *
 * AbstractCodeTemplate.java - Base class for code templates.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea.templates;


/**
 * A base class to build code templates on top of.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractCodeTemplate implements CodeTemplate {

	/**
	 * The ID of this template.
	 */
	private String id;


	/**
	 * This no-arg constructor is required for serialization purposes.
	 */
	public AbstractCodeTemplate() {
	}


	/**
	 * Creates a new template.
	 *
	 * @param id The ID for this template.
	 * @throws IllegalArgumentException If <code>id</code> is <code>null</code>.
	 */
	public AbstractCodeTemplate(String id) {
		setID(id);
	}


	/**
	 * Creates a deep copy of this template.
	 *
	 * @return A deep copy of this template.
	 */
	public Object clone() {
		// This method can't be abstract as compilers don't like concrete
		// subclassses calling super.clone() on  an abstract super.
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(
				"CodeTemplate implementation not Cloneable: " +
							getClass().getName());
		}
	}



	/**
	 * Compares the <code>StaticCodeTemplate</code> to another.
	 *
	 * @param o Another <code>StaticCodeTemplate</code> object.
	 * @return A negative integer, zero, or a positive integer as this
	 *         object is less than, equal-to, or greater than the passed-in
	 *         object.
	 * @throws ClassCastException If <code>o</code> is
	 *         not an instance of <code>CodeTemplate</code>.
	 */
	public int compareTo(Object o) {
		if (!(o instanceof CodeTemplate)) {
			return -1;
		}
		CodeTemplate t2 = (CodeTemplate)o;
		return getID().compareTo(t2.getID());
	}


	/**
	 * Overridden to return "<code>true</code>" iff {@link #compareTo(Object)}
	 * returns <code>0</code>.
	 *
	 * @return Whether this code template is equal to another.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof CodeTemplate) {
			return compareTo(obj)==0;
		}
		return false;
	}


	/**
	 * Returns the ID of this code template.
	 *
	 * @return The template's ID.
	 * @see #setID(String)
	 */
	public String getID() {
		return id;
	}


	/**
	 * Returns the hash code for this template.
	 *
	 * @return The hash code for this template.
	 */
	public int hashCode() {
		return id.hashCode();
	}


	/**
	 * Sets the ID for this template.
	 *
	 * @param id The ID for this template.
	 * @throws IllegalArgumentException If <code>id</code> is <code>null</code>.
	 * @see #getID()
	 */
	public void setID(String id) {
		if (id==null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		this.id = id;
	}


}