package org.fife.ui.rtextarea.readonly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class FileOffsetLineDiscover {

	int lastOffset = 0;

	private final List<Integer> lfOffsets = new ArrayList<>();
	private final Set<Integer> crOffsets = new HashSet<>();

	public List<Integer> getOffsets() {

		List<Integer> lineOffsets = new ArrayList<>();
		lineOffsets.add(0);

		for( Integer newLineIndex : lfOffsets ){

			//reconize \n\r LFCR
			if( crOffsets.contains(newLineIndex + 1) ){
				int lfcrIndex = newLineIndex + 1;
				lineOffsets.add(lfcrIndex);
				crOffsets.remove(lfcrIndex);
			}

			//recognize \r\n CRLF
			else if( crOffsets.contains(newLineIndex - 1) ){
				int crlfOffset = newLineIndex - 1;
				lineOffsets.add(newLineIndex);
				crOffsets.remove(crlfOffset);
			}

			//add LF
			else lineOffsets.add(newLineIndex);
		}

		lineOffsets.addAll(crOffsets);
		lineOffsets.sort(Integer::compare);
		return lineOffsets;
	}

	public void onBufferRead(char[] charBuffer, int readChars) {
		for( int i = 0; i < readChars; i++ ){
			lastOffset++;
			char character = charBuffer[i];
			if( character == '\n' ){
				lfOffsets.add(lastOffset);
			} else if( character == '\r' ){
				crOffsets.add(lastOffset);
			}
		}
	}
}
