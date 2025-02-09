package org.fife.ui.rtextarea;

import java.util.EventListener;

public interface IconRowListener extends EventListener {
	void bookmarkAdded(IconRowEvent e);

	void bookmarkRemoved(IconRowEvent e);

}
