package org.fife.ui.rtextarea.readonly;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;

public class ReadOnlyLeafElement implements Element {

	private final AttributeSet attributeSet = new SimpleAttributeSet();

	private final Document document;
	private final Element parentElement;
	private final ReadOnlyContentInterface content;
	private final int index;

	public ReadOnlyLeafElement(Document document, Element parentElement, ReadOnlyContentInterface content, int index) {
		this.document = document;
		this.parentElement = parentElement;
		this.content = content;
		this.index = index;
	}

	@Override
	public Document getDocument() {
		return document;
	}

	@Override
	public Element getParentElement() {
		return parentElement;
	}

	@Override
	public String getName() {
		return "content";
	}

	@Override
	public AttributeSet getAttributes() {
		return attributeSet;
	}

	@Override
	public int getStartOffset() {
		return content.getStartOffset(index);
	}

	@Override
	public int getEndOffset() {
		return content.getEndOffset(index);
	}

	@Override
	public int getElementIndex(int offset) {
		return 0;
	}

	@Override
	public int getElementCount() {
		return 0;
	}

	@Override
	public Element getElement(int index) {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
