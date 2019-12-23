/*
 * 09/16/2004
 *
 * Macro.java - A macro as recorded/played back by an RTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.fife.io.UnicodeReader;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * A macro as recorded/played back by an {@link RTextArea}.<p>
 *
 * <code>Macro</code>s are static; when a Macro is loaded, it can be run by any
 * instance of <code>RTextArea</code> in the application.  To activate and play
 * back a macro, use the following methods:
 *
 * <ul>
 *    <li>{@link RTextArea#loadMacro(Macro)}
 *    <li>{@link RTextArea#playbackLastMacro()}
 * </ul>
 *
 * To record and save a new macro, you'd use the following methods:
 *
 * <ul>
 *    <li>{@link RTextArea#beginRecordingMacro()} (this discards the previous
 *        "current" macro, if any)
 *    <li>{@link RTextArea#endRecordingMacro()} (at this point, you could call
 *        <code>playbackLastMacro()</code> to play this macro immediately if
 *        desired)
 *    <li>{@link RTextArea#getCurrentMacro()}.{@link #saveToFile(File)}
 * </ul>
 *
 * As <code>Macro</code>s save themselves as XML files, a common technique is
 * to save all macros in files named "<code>{@link #getName()}.xml</code>", and
 * place them all in a common directory.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public class Macro {

	private String name;
	private ArrayList<MacroRecord> macroRecords;

	private static final String ROOT_ELEMENT			= "macro";
	private static final String MACRO_NAME				= "macroName";
	private static final String ACTION					= "action";
	private static final String ID					= "id";

	private static final String UNTITLED_MACRO_NAME		= "<Untitled>";

	private static final String FILE_ENCODING			= "UTF-8";


	/**
	 * Constructor.
	 */
	public Macro() {
		this(UNTITLED_MACRO_NAME);
	}


	/**
	 * Loads a macro from a file on disk.
	 *
	 * @param file The file from which to load the macro.
	 * @throws IOException If the file does not exist or an I/O exception occurs
	 *         while reading the file.
	 * @see #saveToFile(String)
	 * @see #saveToFile(File)
	 */
	public Macro(File file) throws IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;
		try {
			db = dbf.newDocumentBuilder();
			//InputSource is = new InputSource(new FileReader(file));
			InputSource is = new InputSource(new UnicodeReader(
								new FileInputStream(file), FILE_ENCODING));
			is.setEncoding(FILE_ENCODING);
			doc = db.parse(is);//db.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
			String desc = e.getMessage();
			if (desc==null) {
				desc = e.toString();
			}
			throw new IOException("Error parsing XML: " + desc);
		}

		macroRecords = new ArrayList<>();

		// Traverse the XML tree.
		boolean parsedOK = initializeFromXMLFile(doc.getDocumentElement());
		if (!parsedOK) {
			name = null;
			macroRecords.clear();
			macroRecords = null;
			throw new IOException("Error parsing XML!");
		}

	}


	/**
	 * Constructor.
	 *
	 * @param name The name of the macro.
	 */
	public Macro(String name) {
		this(name, null);
	}


	/**
	 * Constructor.
	 *
	 * @param name The name of the macro.
	 * @param records The initial records of the macro.
	 */
	public Macro(String name, List<MacroRecord> records) {

		this.name = name;

		if (records!=null) {
			macroRecords = new ArrayList<>(records.size());
			macroRecords.addAll(records);
		}
		else {
			macroRecords = new ArrayList<>(10);
		}

	}


	/**
	 * Adds a macro record to this macro.
	 *
	 * @param record The record to add.  If <code>null</code>, nothing happens.
	 * @see #getMacroRecords
	 */
	public void addMacroRecord(MacroRecord record) {
		if (record!=null) {
			macroRecords.add(record);
		}
	}


	/**
	 * Returns the macro records that make up this macro.
	 *
	 * @return The macro records.
	 * @see #addMacroRecord
	 */
	public List<MacroRecord> getMacroRecords() {
		return macroRecords;
	}


	/**
	 * Returns the name of this macro.  A macro's name is simply something to
	 * identify it with in a UI; it has nothing to do with the name of the file
	 * to save the macro to.
	 *
	 * @return The macro's name.
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}


	/**
	 * Used in parsing an XML document containing a macro.  This method
	 * initializes this macro with the data contained in the passed-in node.
	 *
	 * @param root The root node of the parsed XML document.
	 * @return <code>true</code> if the macro initialization went okay;
	 *         <code>false</code> if an error occurred.
	 */
	private boolean initializeFromXMLFile(Element root) {

		/*
		 * This method expects the XML document to be in the following format:
		 *
		 * <?xml version="1.0" encoding="UTF-8" ?>
		 * <macro>
		 *    <macroName>test</macroName>
		 *    <action id="default-typed">abcdefg</action>
		 *    [<action id=...>...</action>]
		 *    ...
		 * </macro>
		 *
		 */

		NodeList childNodes = root.getChildNodes();
		int count = childNodes.getLength();

		for (int i=0; i<count; i++) {

			Node node = childNodes.item(i);
			int type = node.getNodeType();
			switch (type) {

				// Handle element nodes.
				case Node.ELEMENT_NODE:

					String nodeName = node.getNodeName();

					if (nodeName.equals(MACRO_NAME)) {
						NodeList childNodes2 = node.getChildNodes();
						name = UNTITLED_MACRO_NAME;
						if (childNodes2.getLength()>0) {
							node = childNodes2.item(0);
							int type2 = node.getNodeType();
							if (type2!=Node.CDATA_SECTION_NODE &&
									type2!=Node.TEXT_NODE) {
								return false;
							}
							name = node.getNodeValue().trim();
						}
						//System.err.println("Macro name==" + name);
					}

					else if (nodeName.equals(ACTION)) {
						NamedNodeMap attributes = node.getAttributes();
						if (attributes==null || attributes.getLength()!=1) {
							return false;
						}
						Node node2 = attributes.item(0);
						MacroRecord macroRecord = new MacroRecord();
						if (!node2.getNodeName().equals(ID)) {
							return false;
						}
						macroRecord.id = node2.getNodeValue();
						NodeList childNodes2 = node.getChildNodes();
						int length = childNodes2.getLength();
						if (length==0) { // Could be empty "" command.
							//System.err.println("... empty actionCommand");
							macroRecord.actionCommand = "";
							//System.err.println("... adding action: " + macroRecord);
							macroRecords.add(macroRecord);
							break;
						}
						else {
							node = childNodes2.item(0);
							int type2 = node.getNodeType();
							if (type2!=Node.CDATA_SECTION_NODE &&
									type2!=Node.TEXT_NODE) {
								return false;
							}
							macroRecord.actionCommand = node.getNodeValue();
							macroRecords.add(macroRecord);
						}

					}
					break;

				default:
					break; // Skip whitespace nodes, etc.

			}

		}

		// Everything went okay.
		return true;

	}


	/**
	 * Saves this macro to an XML file.  This file can later be read in by the
	 * constructor taking a <code>File</code> parameter; this is the mechanism
	 * for saving macros.
	 *
	 * @param file The file in which to save the macro.
	 * @throws IOException If an error occurs while generating the XML for
	 *         the output file.
	 * @see #saveToFile(String)
	 */
	public void saveToFile(File file) throws IOException {
		saveToFile(file.getAbsolutePath());
	}


	/**
	 * Saves this macro to a  file.  This file can later be read in by the
	 * constructor taking a <code>File</code> parameter; this is the mechanism
	 * for saving macros.
	 *
	 * @param fileName The name of the file in which to save the macro.
	 * @throws IOException If an error occurs while generating the XML for
	 *         the output file.
	 * @see #saveToFile(File)
	 */
	public void saveToFile(String fileName) throws IOException {

		/*
		 * This method writes the XML document in the following format:
		 *
		 * <?xml version="1.0" encoding="UTF-8" ?>
		 * <macro>
		 *    <macroName>test</macroName>
		 *    <action id="default-typed">abcdefg</action>
		 *    [<action id=...>...</action>]
		 *    ...
		 * </macro>
		 *
		 */

		try {

			DocumentBuilder db = DocumentBuilderFactory.newInstance().
											newDocumentBuilder();
			DOMImplementation impl = db.getDOMImplementation();

			Document doc = impl.createDocument(null, ROOT_ELEMENT, null);
			Element rootElement = doc.getDocumentElement();

			// Write the name of the macro.
			Element nameElement = doc.createElement(MACRO_NAME);
			nameElement.appendChild(doc.createCDATASection(name));
			rootElement.appendChild(nameElement);

			// Write all actions (the meat) in the macro.
			for (MacroRecord record : macroRecords) {
				Element actionElement = doc.createElement(ACTION);
				actionElement.setAttribute(ID, record.id);
				if (record.actionCommand!=null &&
						record.actionCommand.length()>0) {
					// Remove illegal characters.  I'm no XML expert, but
					// I'm not sure what I'm doing wrong.  If we don't
					// strip out chars with Unicode value < 32, our
					// generator will insert '&#<value>', which will cause
					// our parser to barf when reading the macro back in
					// (it says "Invalid XML character").  But why doesn't
					// our generator tell us the character is invalid too?
					String command = record.actionCommand;
					for (int j=0; j<command.length(); j++) {
						if (command.charAt(j)<32) {
							command = command.substring(0,j);
							if (j<command.length()-1) {
								command += command.substring(j+1);
							}
						}
					}
					Node n = doc.createCDATASection(command);
					actionElement.appendChild(n);
				}
				rootElement.appendChild(actionElement);
			}

			// Dump the XML out to the file.
			StreamResult result = new StreamResult(new File(fileName));
			DOMSource source = new DOMSource(doc);
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer transformer = transFac.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, FILE_ENCODING);
			transformer.transform(source, result);

		} catch (RuntimeException re) {
			throw re; // Keep FindBugs happy.
		} catch (Exception e) {
			throw new IOException("Error generating XML!");
		}

	}


	/**
	 * Sets the name of this macro.  A macro's name is simply something to
	 * identify it with in a UI; it has nothing to do with the name of the file
	 * to save the macro to.
	 *
	 * @param name The new name for the macro.
	 * @see #getName()
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * A "record" of a macro is a single action in the macro (corresponding to
	 * a key type and some action in the editor, such as a letter inserted into
	 * the document, scrolling one page down, selecting the current line,
	 * etc.).
	 */
	static class MacroRecord {

		String id;
		String actionCommand;

		MacroRecord() {
			this(null, null);
		}

		MacroRecord(String id, String actionCommand) {
			this.id = id;
			this.actionCommand = actionCommand;
		}

	}


}
