package org.fife.ui.rtextarea.readonly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class FileOffsetLineDiscover {

	private static final Logger LOGGER  = Logger.getLogger(FileOffsetLineDiscover.class.getName());

	private FileOffsetLineDiscover() {
	}

	public static List<Integer> getOffsets(Path filePath, Charset charset) {

		List<Integer> lfOffsets = new ArrayList<>();
		Set<Integer> crOffsets = new HashSet<>();

		List<Integer> lineOffsets = new ArrayList<>();

		lineOffsets.add(0);

		readFile(filePath, charset, lfOffsets, crOffsets);

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


	private static void readFile(Path filePath, Charset fixedSizeCharset, Collection<Integer> newLines, Collection<Integer> carrigeRetrun) {
		try( BufferedReader bufferedReader = Files.newBufferedReader(filePath, fixedSizeCharset); ){
			int offset = 0;
			char character;
			int readValue;
			while( true ){
				readValue = bufferedReader.read();
				if( readValue == -1 ) return;
				offset++;
				character = (char) readValue;
				if( character == '\n' ){
					newLines.add(offset);
				} else if( character == '\r' ){
					carrigeRetrun.add(offset);
				}


			}
		} catch( IOException e ){
			LOGGER.log(Level.WARNING, "Unable to read line offsets from file", e);
		}
	}
}
