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
 * @author admin
 *
 */
public class MyTokenizer2 extends Tokenizer {
	Logger log = Logger.getLogger(this.getClass().getCanonicalName());
	
	private int offset = 0, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;
	
	private AdminDataManager adm = new AdminDataManager();
	
	public MyTokenizer2(Reader input) {
		super(input);
		System.out.println("constructor:" + toString());
		
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		
		log.info("MyTokenizer constructor1");
		
//		adm.setOnlineEngineAddress("121.254.177.165:2012");
//		MorphemeResult result = new MorphemeResult();
		try {
//			readerString = readerToString(input);
//			result = adm.getMorpheme(readerString);
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
		}

		termAtt.setLength(length);
		start = bufferIndex - length;
		offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
		return true;
	}
	
	private String word = "";
	private String tag = "";
	protected boolean isTokenChar(char c) {
//		System.out.println("isTokenChar:"+c);
		return !Character.isWhitespace(c);
	}

	protected char normalize(char c) {
		System.out.println("normalizer:"+c);
		return c;
	}

	@Override
	public final void end() throws IOException {
		super.end();
		System.out.println("end");
		// set final offset
//		int finalOffset = correctOffset(offset);
//		offsetAtt.setOffset(finalOffset, finalOffset);
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		System.out.println("reset");
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		
//		this.input = new StringReader(readerString); 
	}
	
	public void close() throws IOException {
//		super.close();
		System.out.println("close");
//		this.input = null;
	}
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}
