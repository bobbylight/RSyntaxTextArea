package org.fife.ui.rtextarea;

import java.util.EventObject;

public class IconRowEvent extends EventObject {

	protected GutterIconInfo iconInfo;

	protected int line;

	public IconRowEvent(Object source, GutterIconInfo iconInfo, int line) {
		super(source);
		this.iconInfo = iconInfo;
		this.line = line;
	}

	public GutterIconInfo getIconInfo() {
		return iconInfo;
	}

	public int getLine() {
		return line;
	}
}
