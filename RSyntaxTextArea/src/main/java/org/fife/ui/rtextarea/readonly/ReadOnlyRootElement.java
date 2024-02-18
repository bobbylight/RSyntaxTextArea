package org.fife.ui.rtextarea.readonly;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;

public class ReadOnlyRootElement implements Element {

	private final AttributeSet attributeSet = new SimpleAttributeSet();

	private final Document document;

	private ReadOnlyContentInterface content = new NullReadOnlyContent();

	public ReadOnlyRootElement(ReadOnlyDocument document) {
		this.document = document;
	}

	@Override
	public Document getDocument() {
		return document;
	}

	@Override
	public Element getParentElement() {
		return null;
	}

	@Override
	public String getName() {
		return "paragraph";
	}

	@Override
	public AttributeSet getAttributes() {
		return attributeSet;
	}

	@Override
	public int getStartOffset() {
		return 0;
	}

	@Override
	public int getEndOffset() {
		return content.length();
	}

	@Override
	public int getElementIndex(int offset) {
		return content.getElementIndex(offset);
	}

	@Override
	public int getElementCount() {
		return content.getElementCount();
	}

	@Override
	public Element getElement(int index) {
		return new ReadOnlyLeafElement(document, this, content, index);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	public void setContent(ReadOnlyContentInterface content) {
		this.content = content;
	}
}
