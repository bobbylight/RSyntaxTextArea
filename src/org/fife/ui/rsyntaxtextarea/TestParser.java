package org.fife.ui.rsyntaxtextarea;

import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;


/**
 * TODO: Delete this class.
 *
 * This is a test parser that highlights all "while" and "if" strings as errors.
 * Actually, not all of them, but enough for testing purposes.
 */
class TestParser implements Parser {

	private java.util.ArrayList noticeList = new java.util.ArrayList(1);


	/**
	 * {@inheritDoc}
	 */
	public void parse(RSyntaxDocument doc, String style) {

		noticeList.clear();
		Segment seg = new Segment();
		int lineCount = doc.getDefaultRootElement().getElementCount();
		int offs = 0;

		try {
			for (int i=0; i<lineCount; i++) {
				Element elem = doc.getDefaultRootElement().getElement(i);
				int start = elem.getStartOffset();
				int end = elem.getEndOffset();
				if (i==lineCount-1) {
					end--;
				}
				int len = end - start;
				doc.getText(start, len, seg);
				char[] buf = seg.array;
				int pos = seg.offset;
				while (pos<seg.offset+seg.count-4) {
					if (buf[pos]=='w') {
						if (buf[pos+1]=='h') {
							if (buf[pos+2]=='i') {
								if (buf[pos+3]=='l') {
									if (buf[pos+4]=='e') {
										noticeList.add(new ParserNotice("Test 'while' notice", offs+pos-seg.offset,5));
										pos += 5;
									}
									else pos += 4;
								}
								else pos += 3;
							}
							else pos += 2;
						}
						else pos += 1;
					}
					else if (buf[pos]=='i') {
						if (buf[pos+1]=='f') {
							noticeList.add(new ParserNotice("Test 'if' notice", offs+pos-seg.offset,2));
							pos += 2;
						}
						else pos += 1;
					}
					else pos += 1;
				}
				offs += end;
			}
		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Never happens
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public List getNotices() {
		return noticeList;
	}


}