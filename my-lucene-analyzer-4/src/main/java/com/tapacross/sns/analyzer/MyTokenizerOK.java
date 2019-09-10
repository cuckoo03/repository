package com.tapacross.sns.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.analysis.util.CharacterUtils.CharacterBuffer;

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
public class MyTokenizerOK extends Tokenizer {
	public MyTokenizerOK(Reader in) {
	    super(in);
	    charUtils = CharacterUtils.getInstance();
	  }

	private int offset = 0, bufferIndex = 0, dataLen = 0, finalOffset = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

	private final CharacterUtils charUtils;// lucene 4.6.1 not found this class
	private final CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);

	protected int normalize(int c) {
		return c;
	}

	@Override
	  public final boolean incrementToken() throws IOException {
	    clearAttributes();
	    int length = 0;
	    int start = -1; // this variable is always initialized
	    int end = -1;
	    char[] buffer = termAtt.buffer();
	    while (true) {
	      if (bufferIndex >= dataLen) {
	        offset += dataLen;
	        charUtils.fill(ioBuffer, input); // read supplementary char aware with CharacterUtils
	        if (ioBuffer.getLength() == 0) {
	          dataLen = 0; // so next offset += dataLen won't decrement offset
	          if (length > 0) {
	            break;
	          } else {
	            finalOffset = correctOffset(offset);
	            return false;
	          }
	        }
	        dataLen = ioBuffer.getLength();
	        bufferIndex = 0;
	      }
	      // use CharacterUtils here to support < 3.1 UTF-16 code unit behavior if the char based methods are gone
	      final int c = charUtils.codePointAt(ioBuffer.getBuffer(), bufferIndex, ioBuffer.getLength());
	      final int charCount = Character.charCount(c);
	      bufferIndex += charCount;

	      if (isTokenChar(c)) {               // if it's a token char
	        if (length == 0) {                // start of token
	          assert start == -1;
	          start = offset + bufferIndex - charCount;
	          end = start;
	        } else if (length >= buffer.length-1) { // check if a supplementary could run out of bounds
	          buffer = termAtt.resizeBuffer(2+length); // make sure a supplementary fits in the buffer
	        }
	        end += charCount;
	        length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
	        if (length >= MAX_WORD_LEN) // buffer overflow! make sure to check for >= surrogate pair could break == test
	          break;
	      } else if (length > 0)             // at non-Letter w/ chars
	        break;                           // return 'em
	    }

	    termAtt.setLength(length);
	    assert start != -1;
	    offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(end));
	    return true;
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

		// this.input = new StringReader(readerString);
	}

	public void close() throws IOException {
		super.close();
		System.out.println("close");
	}
	
	  /** Collects only characters which do not satisfy
	   * {@link Character#isWhitespace(int)}.*/
	  protected boolean isTokenChar(int c) {
	    return !Character.isWhitespace(c);
	  }
}
