package org.fife.ui.rtextarea.readonly;

public class FileCharsCounter {

	int count = 2;

	public void onBufferRead(int readChars) {
		count += readChars;
	}

	public int getCount() {
		return count;
	}
}
