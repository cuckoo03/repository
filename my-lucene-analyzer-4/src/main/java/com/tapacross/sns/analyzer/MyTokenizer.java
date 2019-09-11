package com.tapacross.sns.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.tapacross.ise.MorphemeResult;
import com.tapacross.service.AdminDataManager;

/**
 * 메서드 실행순서
 * 입력테스트=버튼 입력
 * -constructor
 * -reset
 * -incrementToken
 * --isTokenChar:버
 * --isTokenChar:튼
 * --isTokenChar:
 * -incrementToken
 * --isTokenChar:입
 * --isTokenChar:력
 * -close
 * 
 * 토큰결과
 * 입력:한글A
 * 출력:
 * 한글 1->3
 * A 3->4
 * @author admin
 *
 */
public class MyTokenizer extends Tokenizer {
	private int offset, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	
	private AdminDataManager adm = new AdminDataManager();
	private String[] tokens;
	private String[] pos;
	private int tokenIndex = 0;
	private String s;
	public MyTokenizer(Reader input) {
		super(input);
	}
	public MyTokenizer(Reader input, String s) {
		super(input);
		this.s = s;
		System.out.println("MyTokenizer constructor");
		
		adm.setOnlineEngineAddress("121.254.177.165:2012");
		try {
			MorphemeResult result = new MorphemeResult();
			result = adm.getMorpheme(s);
			tokens = result.getToken();
			pos = result.getSynaxTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		System.out.println("incrementToken");
		String word = "";
		String tag = "";
		int length = 0;
		int start = bufferIndex;
		char[] buffer = termAtt.buffer();
		while (true) {
			if (bufferIndex >= dataLen) {
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
				if (length == 0) // start of token
					start = offset + bufferIndex - 1;
				else if (length == buffer.length)
					buffer = termAtt.resizeBuffer(1 + length);

				buffer[length++] = c; // buffer it, normalized 

				if (length == MAX_WORD_LEN) // buffer overflow!
					break;

			} else if (length > 0) {// at non-Letter w/ chars
				break; // return 'em
			} else {
			}

			/*
			if (isTokenChar(c)) { // if it's a token char
				String token = tokens[tokenIndex].toLowerCase();
				word += c;
				
				if (word.toLowerCase().equals(token)) {
					length = word.length();
					for (int i = 0; i < word.length(); i++) {
						buffer[i] = word.charAt(i);
					}
					
					word = null;
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
			*/
		}

		termAtt.setLength(length);
//		start = bufferIndex - length;
		offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
//		typeAtt.setType(tag);
		return true;
	}
	
	protected boolean isTokenChar(char c) {
		return !Character.isWhitespace(c);
	}

	@Override
	public final void end() throws IOException {
		System.out.println("end");
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		System.out.println("reset");
		
		bufferIndex = 0;
		dataLen = 0;
		tokenIndex = 0;
		
		adm.setOnlineEngineAddress("121.254.177.165:2012");
		try {
			MorphemeResult result = new MorphemeResult();
			result = adm.getMorpheme(s);
			tokens = result.getToken();
			pos = result.getSynaxTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException {
		super.close();
		System.out.println("close");
	}
}
