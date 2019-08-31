package com.tapacross.sns.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class TapacrossTokenizer extends Tokenizer {
	private int offset = 0, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;
	
	private String[] tokens;
	private String[] pos;
	
	protected TapacrossTokenizer(Reader input) {
		super(input);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
	}
	
	public TapacrossTokenizer(Reader input, String[] tokens, String[] pos) {
		super(input);
		this.tokens = tokens;
		this.pos = pos;
		
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
	}

	@Override
	public final boolean incrementToken() throws IOException {
//		System.out.println("incrementToken:");
		clearAttributes();
		int length = 0;
		int start = bufferIndex;
		char[] buffer = termAtt.buffer();
		while (true) {

			if (bufferIndex >= dataLen) {
				offset += dataLen;
				dataLen = input.read(ioBuffer);
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
//				System.out.println("incrementToken else");
			}
				
			
		}

		termAtt.setLength(length);
		start = bufferIndex -length;
		offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
		typeAtt.setType(tag);
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
	public final void end() {
		// set final offset
		int finalOffset = correctOffset(offset);
		offsetAtt.setOffset(finalOffset, finalOffset);
		System.out.println("end");
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		tokenIndex = 0;
		System.out.println("reset:" + input);
	}
	
	public void close() throws IOException {
		super.close();
		System.out.println("close:" + input);
	}
}