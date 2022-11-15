package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

import javax.swing.text.Element;

//This document is useful to read a huge files, like Log Files
public class ReadOnlyDocument extends RSyntaxDocument {

	private ReadOnlyRootElement readOnlyRootElement;

	public ReadOnlyDocument(TokenMakerFactory tmf, String syntaxStyle, ReadOnlyContentInterface content) {
		super(tmf, syntaxStyle, content);
		readOnlyRootElement.setContent(content);
		lastTokensOnLines.clear();
		lastTokensOnLines.insertRange(0, content.getElementCount(), TokenTypes.NULL);
	}

	@Override
	public Element getDefaultRootElement() {
		if( readOnlyRootElement == null ){
			readOnlyRootElement = new ReadOnlyRootElement(this);
		}
		return readOnlyRootElement;
	}

	@Override
	public boolean isEditable() {
		return false;
	}
}
