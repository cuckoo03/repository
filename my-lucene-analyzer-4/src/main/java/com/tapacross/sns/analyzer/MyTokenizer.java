package com.tapacross.sns.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.tapacross.ise.MorphemeResult;
import com.tapacross.service.AdminDataManager;
import com.tapacross.service.DictionaryDataManager;

public class MyTokenizer extends Tokenizer {
	Logger log = Logger.getLogger(this.getClass().getCanonicalName());
	
	private int offset = 0, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;
	
	private String[] tokens;
	private String[] pos;
	
	private AdminDataManager adm = new AdminDataManager();
	
	public MyTokenizer(Reader input) {
		super(input);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		
		log.info("MyTokenizer constructor1");
		
		adm.setOnlineEngineAddress("121.254.177.165:2012");
		MorphemeResult result = new MorphemeResult();
		try {
			readerString = readerToString(input);
			result = adm.getMorpheme(readerString);
			this.tokens = result.getToken();
			this.pos = result.getSynaxTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String readerString = null;

	@Override
	public final boolean incrementToken() throws IOException {
		System.out.println("incrementToken");
		clearAttributes();
		int length = 0;
		int start = bufferIndex;
		char[] buffer = termAtt.buffer();
		while (true) {

			if (bufferIndex >= dataLen) {
				offset += dataLen;
				dataLen = input2.read(ioBuffer);
				if (dataLen == -1) {
					dataLen = 0; // so next offset += dataLen won't decrement
									// offset
					if (length > 0)
						break;
					else
						return false;
				}
				bufferIndex = 0;
			}

			final char c = ioBuffer[bufferIndex++];
			if (isTokenChar(c)) { // if it's a token char
				int tokenLength = tokens[tokenIndex].length();
				String token = tokens[tokenIndex].toLowerCase();
				word += c;
				if (word.length() > tokenLength) {
					word = word.substring(1, word.length());
				}
				
				if (word.toLowerCase().equals(token)) {
					length = word.length();
					for (int i = 0; i < word.length(); i++) {
						buffer[i] = word.charAt(i);
					}
					
					word = "";
					tag = pos[tokenIndex];
					tokenIndex++;
					break;
				}

				if (length == MAX_WORD_LEN) // buffer overflow!
					break;

			} else if (length > 0) {// at non-Letter w/ chars
				break; // return 'em
			} else {
			}
		}

		termAtt.setLength(length);
		start = bufferIndex - length;
		offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
		typeAtt.setType(tag);
		
		close();
		return true;
	}
	
	private int tokenIndex;
	private String word = "";
	private String tag = "";
	protected boolean isTokenChar(char c) {
//		System.out.println("isTokenChar:"+c);
		/*
		int tokenLength = tokens[tokenIndex].length();
		String token = tokens[tokenIndex];
		word += c;
		if (word.length() > tokenLength) {
			word = word.substring(1, word.length());
			return false;
		}
		
		if (word.equals(token)) {
			word = "";
			tokenIndex++;
			return true;
		}
		return false;
		*/
		return !Character.isWhitespace(c);
	}

	protected char normalize(char c) {
		System.out.println("normalizer:"+c);
		return c;
	}

	@Override
	public final void end() throws IOException {
		super.end();
		// set final offset
		System.out.println("end");
		int finalOffset = correctOffset(offset);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
	
	private StringReader input2;
	
	@Override
	public void reset() throws IOException {
		super.reset();
		System.out.println("reset");
		input2 = new StringReader(readerString); 
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		tokenIndex = 0;
		
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		System.out.println("close");
	}
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}
