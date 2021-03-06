package com.tapacross.sns.analyzer
import java.io.IOException
import java.io.Reader

import java.util.concurrent.atomic.AtomicInteger

import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.util.BytesRef
import com.tapacross.ise.MorphemeResult
import com.tapacross.service.AdminDataManager

import groovy.json.internal.CharacterSource
import groovy.sql.ResultSetMetaDataWrapper
import groovy.transform.TypeChecked


/**
 * 형태소 토크나이저
 * workflow
 * 최초 객체 생성시:constructor->reset->increment loop->end->close
 * 생성 이후부터:reset->increment loop->end->close
 * @author admin
 *
 */
@TypeChecked
class MyTokenizer extends Tokenizer {
	private int offset, bufferIndex = 0, dataLen = 0, tokenIndex = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private char[] ioBuffer = new char[IO_BUFFER_SIZE];
	
	private AdminDataManager adm = new AdminDataManager();
	private String[] tokens;
	private String[] pos;
//	private String s;
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);
//	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private StringReader input2;
	public MyTokenizer() {
		super();
		System.out.println("MyTokenizer constructor");
		
		adm.setOnlineEngineAddress("broker.ip:2012");
	}
	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		System.out.println("incrementToken");
		String word = "";
//		def word2 = ""
		int length = 0;
		int start = bufferIndex;
		char[] buffer = termAtt.buffer();
		while (true) {
			if (bufferIndex >= dataLen) {
				dataLen = input2.read(ioBuffer);
				if (dataLen == -1) { // end loof 
					dataLen = 0; // so next offset += dataLen won't decrement
									// offset
					if (length > 0)
						break;
					else
						return false;
				}// start loof
				bufferIndex = 0;
			}

			final char c = ioBuffer[bufferIndex++];

			if (isTokenChar(c)) { // if it's a token char
				if (length == 0) // start of token
					start = offset + bufferIndex - 1;
				else if (length == buffer.length)
					buffer = termAtt.resizeBuffer(1 + length);
				if (Character.isWhitespace(c)) {
					continue;
				}
				buffer[length++] = (c); // buffer it, normalized

				// 반복음절로 인해 문장은 남아있지만 형태소토큰이 더이상 없을경우 메서드를 종료한다
				// ex)ㅋㅋㅋ불치병입니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
				// ㅋㅋㅋ/SY불치병/NN입니다/VVㅋㅋㅋ/NNㅋㅋ/NNㅋㅋㅋㅋ/IC
				if (tokenIndex == tokens.size())
					return false
					
				word = word + c
				String token = tokens[tokenIndex]
				if (word == token) {
					word = ""
					setBlankBuffer(buffer)
					pushBufferChar(buffer, token)
					println "buffer:" + buffer.toString()
					break;
				}
				if (length == MAX_WORD_LEN) // buffer overflow!
					break;

			} else if (length > 0) {// at non-Letter w/ chars
				break; // return 'em
			} else {
			}
		}
		termAtt.setEmpty()
		termAtt.setLength(length);
		offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
		typeAtt.setType(pos[tokenIndex]);
		def bytesRef = new BytesRef(pos[tokenIndex].getBytes("UTF-8"));
		payloadAtt.setPayload(bytesRef)
//		posIncrAtt.setPositionIncrement(tokenIndex)
		tokenIndex++
		return true;
	}
	protected boolean isTokenChar(char c) {
//		return !Character.isWhitespace(c);
		return true;
	}
	// reset->increemnttoken->end->close
	@Override
	public final void end() throws IOException {
		super.end()
		System.out.println("end:"+this.toString());
	}
	@Override
	public void reset() throws IOException {
		super.reset();
		
		bufferIndex = 0;
		dataLen = 0;
		tokenIndex = 0
		
		def s = readerToString(input)
		input2 = new StringReader(s.toLowerCase())
		try {
			adm.getMorpheme("reset:"+this.toString());
			
			MorphemeResult result = new MorphemeResult();
			result = adm.getMorpheme(s);
			tokens = result.getToken();
			pos = result.getSynaxTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() throws IOException {
		super.close();
	}
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
	private char normalize1(char c) {
		if (c.isDigit(c))
			return c;
		if (c.isLetter(c))
			return c.toLowerCase();
		
		return c;
	}
	private void setBlankBuffer(char[] buffer) {
		for (def i = 0; i < buffer.size(); i++) {
			buffer[i] = Character.MIN_VALUE
		}
	}
	private void pushBufferChar(char[] buffer, String word) {
		def length = 0
		word.toCharArray().each { char it ->
			def int index = length++
			buffer[index] = it
		}
	}
}
