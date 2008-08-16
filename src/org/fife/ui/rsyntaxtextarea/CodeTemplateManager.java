/*
 * 02/21/2005
 *
 * CodeTemplateManager.java - manages code templates.
 * Copyright (C) 2005 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 * Manages "code templates."<p>
 *
 * Note that this class assumes that thread-safety is taken care of at a
 * higher level (which should be the case, as <code>AbstractDocument</code>s
 * only allow text insertions during a write-lock).  You should never have
 * to use this class directly anyway; its only client should be
 * <code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @version 0.1
 */
class CodeTemplateManager {

	private int maxTemplateIDLength;
	private List templates;

	private KeyStroke insertTrigger;
	private String insertTriggerString;
	private Segment s;
	private TemplateComparator comparator;
	private File directory;

	private static final String AFTER_CARET		= "afterCaret";
	private static final String BEFORE_CARET	= "beforeCaret";
	private static final String CODE_TEMPLATE	= "codeTemplate";
	private static final String ENCODING		= "UTF-8";
	private static final String ID			= "id";
	private static final String _VALUE			= "value";


	/**
	 * Constructor.
	 */
	public CodeTemplateManager() {

		// Default insert trigger is a space.
		// FIXME:  See notes in RSyntaxTextAreaDefaultInputMap.
		// Be aware that you may need to use getKeyStroke(' ') when you
		// fix this...
		setInsertTrigger(KeyStroke.getKeyStroke(' '));

		s = new Segment();
		comparator = new TemplateComparator();
		templates = new ArrayList();

	}


	/**
	 * Registers the specified template with this template manager.
	 *
	 * @param template The template to register.
	 */
	public void addTemplate(CodeTemplate template) {
		if (template!=null) {
			templates.add(template);
			sortTemplates();
		}
	}


	/**
	 * Returns the keystroke that is the "insert trigger" for templates;
	 * that is, the character that, when inserted into an instance of
	 * <code>RSyntaxTextArea</code>, triggers the search for
	 * a template matching the token ending at the caret position.
	 *
	 * @return The insert trigger.
	 * @see #getInsertTriggerString
	 * @see #setInsertTrigger
	 */
	/*
	 * FIXME:  This text IS what's inserted if the trigger character is pressed
	 * in a text area but no template matches, but it is NOT the trigger
	 * character used in the text areas.  This is because space (" ") is
	 * hard-coded into RSyntaxTextAreaDefaultInputMap.java.  We need to make
	 * this dynamic somehow.  See RSyntaxTextAreaDefaultInputMap.java.
	 */
	public KeyStroke getInsertTrigger() {
		return insertTrigger;
	}


	/**
	 * Returns the "insert trigger" for templates; that is, the character
	 * that, when inserted into an instance of <code>RSyntaxTextArea</code>,
	 * triggers the search for a template matching the token ending at the
	 * caret position.
	 *
	 * @return The insert trigger character.
	 * @see #getInsertTrigger
	 * @see #setInsertTrigger
	 */
	/*
	 * FIXME:  This text IS what's inserted if the trigger character is pressed
	 * in a text area but no template matches, but it is NOT the trigger
	 * character used in the text areas.  This is because space (" ") is
	 * hard-coded into RSyntaxTextAreaDefaultInputMap.java.  We need to make
	 * this dynamic somehow.  See RSyntaxTextAreaDefaultInputMap.java.
	 */
	public String getInsertTriggerString() {
		return insertTriggerString;
	}


	/**
	 * Returns the template that should be inserted at the current caret
	 * position, assuming the trigger character was pressed.
	 *
	 * @param textArea The text area that's getting text inserted into it.
	 * @return A template that should be inserted, if appropriate, or
	 *         <code>null</code> if no template should be inserted.
	 */
	public CodeTemplate getTemplate(RSyntaxTextArea textArea) {
		int caretPos = textArea.getCaretPosition();
		int charsToGet = Math.min(caretPos, maxTemplateIDLength);
		try {
			Document doc = textArea.getDocument();
			doc.getText(caretPos-charsToGet, charsToGet, s);
			int index = Collections.binarySearch(templates, s, comparator);
			return index>=0 ? (CodeTemplate)templates.get(index) : null;
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			throw new InternalError("Error in CodeTemplateManager");
		}
	}


	/**
	 * Returns the number of templates this manager knows about.
	 *
	 * @return The template count.
	 */
	public int getTemplateCount() {
		return templates.size();
	}


	/**
	 * Returns the templates currently available.  This method exists solely
	 * so <code>TemplateOptionPanel</code> can allow users to
	 * modify/add/remove templates.  You should never call this method.
	 *
	 * @return The templates available.
	 */
	CodeTemplate[] getTemplates() {
		CodeTemplate[] temp = new CodeTemplate[templates.size()];
		return (CodeTemplate[])templates.toArray(temp);
	}


	/**
	 * Replaces the current set of available templates with the ones
	 * specified.  This method exists solely so
	 * <code>TemplateOptionPanel</code> can allow users to modify/add/remove
	 * templates.  You should never call this method.
	 *
	 * @param newTemplates The new set of templates.  Note that we will
	 *        be taking a shallow copy of these and sorting them.
	 */
	void replaceTemplates(CodeTemplate[] newTemplates) {
		templates.clear();
		if (newTemplates!=null) {
			for (int i=0; i<newTemplates.length; i++) {
				templates.add(newTemplates[i]);
			}
		}
		sortTemplates(); // Also recomputes maxTemplateIDLength.
	}


	/**
	 * Saves all templates as XML files in the current template directory.
	 *
	 * @return Whether or not the save was successful.
	 */
	public boolean saveTemplates() {

		if (templates==null)
			return true;
		if (directory==null || !directory.isDirectory())
			return false;

		// Blow away all old XML files to start anew, as some might be from
		// templates we're removed from the template manager.
		File[] oldXMLFiles = directory.listFiles(new XMLFileFilter());
		if (oldXMLFiles==null)
			return false; // Either an IOException or it isn't a directory.
		int count = oldXMLFiles.length;
		for (int i=0; i<count; i++) {
			/*boolean deleted = */oldXMLFiles[i].delete();
		}

		// Save all current templates as XML.
		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception e) {
			return false;
		}
		DOMImplementation impl = db.getDOMImplementation();
		boolean wasSuccessful = true;
		for (Iterator i=templates.iterator(); i.hasNext(); ) {
			CodeTemplate template = (CodeTemplate)i.next();
			File xmlFile = new File(directory,
								new String(template.getID()) + ".xml");
			try {
				org.w3c.dom.Document doc = impl.createDocument(
										null, CODE_TEMPLATE, null);
				saveToFile(template, doc, xmlFile);
			} catch (Exception e) {
				wasSuccessful = false;
			}
		}

		return wasSuccessful;

	}


	/**
	 * Saves a template to a file.
	 *
	 * @param template The template to save.
	 * @param doc The XML document to write to.
	 * @param xmlFile The file in which to save the template.  If the file
	 *        already exists, it is overwritten.
	 * @throws Exception If an error occurs.
	 */
	private void saveToFile(CodeTemplate template, org.w3c.dom.Document doc,
							File xmlFile) throws Exception {

		Element root = doc.getDocumentElement();

		Element elem = doc.createElement(ID);
		elem.setAttribute(_VALUE, new String(template.getID()));
		root.appendChild(elem);

		elem = doc.createElement(BEFORE_CARET);
		String temp = template.getBeforeCaretText();
		if (temp!=null) {
			elem.appendChild(doc.createCDATASection(temp));
		}
		root.appendChild(elem);

		elem = doc.createElement(AFTER_CARET);
		temp = template.getAfterCaretText();
		if (temp!=null) {
			elem.appendChild(doc.createCDATASection(temp));
		}
		root.appendChild(elem);

		// Dump the XML out to the file.
		StreamResult result = new StreamResult(xmlFile);
		DOMSource source = new DOMSource(doc);
		TransformerFactory transFac = TransformerFactory.newInstance();
		Transformer transformer = transFac.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
		transformer.transform(source, result);

	}


	/**
	 * Sets the "trigger" character for templates.
	 *
	 * @param trigger The trigger character to set for templates.  This means
	 *        that when this character is pressed in an
	 *        <code>RSyntaxTextArea</code>,  the last-typed token is found,
	 *        and is checked against all template ID's to see if a template
	 *        should be inserted.  If a template ID matches, that template is
	 *        inserted; if not, the trigger character is inserted.  If this
	 *        parameter is <code>null</code>, no change is made to the trigger
	 *        character.
	 * @see #getInsertTrigger
	 * @see #getInsertTriggerString
	 */
	/*
	 * FIXME:  The trigger set here IS inserted when no matching template
	 * is found, but a space character (" ") is always used as the "trigger"
	 * to look for templates.  This is because it is hardcoded in
	 * RSyntaxTextArea's input map this way.  We need to change this.
	 * See RSyntaxTextAreaDefaultInputMap.java.
	 */
	public void setInsertTrigger(KeyStroke trigger) {
		if (trigger!=null) {
			insertTrigger = trigger;
			insertTriggerString = trigger.getKeyChar() + "";
		}
	}


	/**
	 * Sets the directory in which to look for templates.  Calling this
	 * method adds any new templates found in the specified directory to
	 * the templates already registered.
	 *
	 * @param dir The new directory in which to look for templates.
	 * @return The new number of templates in this template manager, or
	 *         <code>-1</code> if the specified directory does not exist.
	 */
	public int setTemplateDirectory(File dir) {

		if (dir!=null && dir.isDirectory()) {

			this.directory = dir;

			File[] files = dir.listFiles(new XMLFileFilter());
			int newCount = files==null ? 0 : files.length;
			int oldCount = templates.size();

			if (newCount>0) {

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser parser = null;
				try {
					parser = spf.newSAXParser();
				} catch (Exception e) {
					return -1;
				}
				Handler h = new Handler();

				List temp = new ArrayList(oldCount+newCount);
				temp.addAll(templates);
				for (int i=0; i<newCount; i++) {
					try {
						parser.parse(files[i], h);
						CodeTemplate ct = h.getCodeTemplate();
						if (ct!=null) {
							temp.add(ct);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				templates = temp;
				sortTemplates();

			}

			return getTemplateCount();

		}

		return -1;

	}


	/**
	 * Removes any null entries in the current set of templates (if
	 * any), sorts the remaining templates, and computes the new
	 * maximum template ID length.
	 */
	private final void sortTemplates() {

		// Get the maximum length of a template ID.
		maxTemplateIDLength = 0;

		// Remove any null entries (should only happen because of
		// IOExceptions, etc. when loading from files), and sort
		// the remaining list.
		for (Iterator i=templates.iterator(); i.hasNext(); ) {
			CodeTemplate temp = (CodeTemplate)i.next();
			if (temp==null) {
				i.remove();
			}
			else {
				maxTemplateIDLength = Math.max(maxTemplateIDLength,
										temp.getID().length);
			}
		}

		Collections.sort(templates);

	}


	/**
	 * Loads a template from an XML file via SAX.  This class is reusable.
	 */
	private static class Handler extends DefaultHandler {

		private CodeTemplate template;
		private String id;
		private String beforeCaret;
		private String afterCaret;

		private StringBuffer sb;
		private boolean storeChars;

		public Handler() {
			sb = new StringBuffer();
		}

		public void characters(char[] ch, int start, int length)
											throws SAXException {
			if (storeChars) {
				sb.append(ch, start, length);
			}
		}

		public void endDocument() throws SAXException {
			template = new CodeTemplate(id, beforeCaret, afterCaret);
		}

		public void endElement(String uri, String localName, String qName)
											throws SAXException {
			if (ID.equals(qName)) {
				// Do nothing.
			}
			else if (BEFORE_CARET.equals(qName)) {
				beforeCaret = sb.toString();
			}
			else if (AFTER_CARET.equals(qName)) {
				afterCaret = sb.toString();
			}
		}

		public CodeTemplate getCodeTemplate() {
			return template;
		}

		public void startDocument() throws SAXException {
			sb.setLength(0);
			storeChars = false;
			template = null;
			id = beforeCaret = afterCaret = null;
		}

		public void startElement(String uri, String localName, String qName,
							Attributes attributes) throws SAXException {
			if (ID.equals(qName)) {
				id = attributes.getValue(_VALUE);
			}
			else if (BEFORE_CARET.equals(qName)) {
				sb.setLength(0);
				storeChars = true;
			}
			else if (AFTER_CARET.equals(qName)) {
				sb.setLength(0);
				storeChars = true;
			}
		}

	}


	/**
	 * A comparator that takes a <code>CodeTemplate</code> as its first
	 * parameter and a <code>Segment</code> as its second, and knows
	 * to compare the template's ID to the segment's text.
	 */
	private static class TemplateComparator implements Comparator {

		public TemplateComparator() {
		}

		public int compare(Object template, Object segment) {

			// Get template start index (0) and length.
			CodeTemplate t = (CodeTemplate)template;
			final char[] templateArray = t.getID();
			int i = 0;
			int len1 = templateArray.length;

			// Find "token" part of segment and get its offset and length.
			Segment s = (Segment)segment;
			char[] segArray = s.array;
			int len2 = s.count;
			int j = s.offset + len2 - 1;
			while (j>=s.offset && CodeTemplate.isValidChar(segArray[j]))
				j--;
			j++;
			int segShift = j - s.offset;
			len2 -= segShift;

			int n = Math.min(len1, len2);
			while (n-- != 0) {
				char c1 = templateArray[i++];
				char c2 = segArray[j++];
				if (c1 != c2)
					return c1 - c2;
			}
			return len1 - len2;

		}

	}


	/**
	 * A file filter for File.listFiles() (NOT for JFileChoosers!) that
	 * accepts only XML files.
	 */
	private static class XMLFileFilter implements FileFilter {
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml");
		}
	}


}