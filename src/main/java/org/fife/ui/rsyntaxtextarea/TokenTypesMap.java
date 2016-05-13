package org.fife.ui.rsyntaxtextarea;

import java.util.Collections;
import java.util.HashMap;

public class TokenTypesMap extends HashMap<String, Integer> {
	
    public TokenTypesMap() {
            put("NULL", 0);
            put("COMMENT_EOL", 1);
            put("COMMENT_MULTILINE", 2);
            put("COMMENT_DOCUMENTATION", 3);
            put("COMMENT_KEYWORD", 4);
            put("COMMENT_MARKUP", 5);
            put("RESERVED_WORD", 6);
            put("RESERVED_WORD_2", 7);
            put("FUNCTION", 8);
            put("LITERAL_BOOLEAN", 9);
            put("LITERAL_NUMBER_DECIMAL_INT", 10);
            put("LITERAL_NUMBER_FLOAT", 11);
            put("LITERAL_NUMBER_HEXADECIMAL", 12);
            put("LITERAL_STRING_DOUBLE_QUOTE", 13);
            put("LITERAL_CHAR", 14);
            put("LITERAL_BACKQUOTE", 15);
            put("DATA_TYPE", 16);
            put("VARIABLE", 17);
            put("REGEX", 18);
            put("ANNOTATION", 19);
            put("IDENTIFIER", 20);
            put("WHITESPACE", 21);
            put("SEPARATOR", 22);
            put("OPERATOR", 23);
            put("PREPROCESSOR", 24);
            put("MARKUP_TAG_DELIMITER", 25);
            put("MARKUP_TAG_NAME", 26);
            put("MARKUP_TAG_ATTRIBUTE", 27);
            put("MARKUP_TAG_ATTRIBUTE_VALUE", 28);
            put("MARKUP_COMMENT", 29);
            put("MARKUP_DTD", 30);
            put("MARKUP_PROCESSING_INSTRUCTION", 31);
            put("MARKUP_CDATA_DELIMITER", 32);
            put("MARKUP_CDATA", 33);
            put("MARKUP_ENTITY_REFERENCE", 34);
            put("ERROR_IDENTIFIER", 35);
            put("ERROR_NUMBER_FORMAT", 36);
            put("ERROR_STRING_DOUBLE", 37);
            put("ERROR_CHAR", 38);
    }
    
    public int getTypesNum() {
        return Collections.max(values()) + 1;
    }
    
    public String[] toArray() {
    	String[] res = new String[getTypesNum()];
    	for(java.util.Map.Entry<String, Integer> e : entrySet())	{
    		res[e.getValue()] = e.getKey();
    	}
    	return res;
    }
}
