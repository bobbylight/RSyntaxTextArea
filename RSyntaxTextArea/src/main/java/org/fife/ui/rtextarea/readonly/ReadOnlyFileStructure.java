package org.fife.ui.rtextarea.readonly;

import java.util.List;

public class ReadOnlyFileStructure {

	private final int charsCount;
	private final List<Integer> offsets;
	private final long fileSize;

	public ReadOnlyFileStructure(int charsCount, List<Integer> offsets, long fileSize) {
		this.charsCount = charsCount;
		this.offsets = offsets;
		this.fileSize = fileSize;
	}

	public int getCharsCount() {
		return charsCount;
	}

	public List<Integer> getOffsets() {
		return offsets;
	}

	public long getFileSize() {
		return fileSize;
	}
}
