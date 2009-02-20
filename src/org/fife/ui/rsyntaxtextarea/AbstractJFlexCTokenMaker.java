package org.fife.ui.rsyntaxtextarea;



/**
 * Base class for JFlex-based token makers using C-style syntax.  This class
 * knows how to auto-indent after opening braces and parens.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractJFlexCTokenMaker extends AbstractJFlexTokenMaker {


	/**
	 * Returns <code>true</code> always as C-style languages use curly braces
	 * to denote code blocks.
	 *
	 * @return <code>true</code> always.
	 */
	public boolean getCurlyBracesDenoteCodeBlocks() {
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean getShouldIndentNextLineAfter(Token t) {
		if (t!=null && t.textCount==1) {
			char ch = t.text[t.textOffset];
			return ch=='{' || ch=='(';
		}
		return false;
	}


}