package com.tapacross.sns.analyzer
import java.io.StringReader

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
import com.tapacross.ise.WordResult
import com.tapacross.service.AdminDataManager

import groovy.json.internal.CharacterSource
import groovy.sql.ResultSetMetaDataWrapper
import groovy.transform.TypeChecked


/**
 * TPO 토크나이저
 * workflow
 * 최초 객체 생성시:constructor->reset->increment loop->end->close
 * 생성 이후부터:reset->increment loop->end->close
 * @author admin
 *
 */
@TypeChecked
class OccasionTokenizer extends Tokenizer {
	private int offset, bufferIndex = 0, dataLen = 0, tokenIndex = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private char[] ioBuffer = new char[IO_BUFFER_SIZE];
	
	private AdminDataManager adm = new AdminDataManager();
	private String[] tokens;
	private String[] termNumbers;
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);
	private StringReader input2;
	
	public OccasionTokenizer() {
		System.out.println("TPOTokenizer constructor");
		
		adm.setOnlineEngineAddress("broker.ip:2012");
	}
	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		System.out.println("incrementToken");
		int length = 0;
		char[] buffer = termAtt.buffer();
		while (true) {
			if (tokenIndex >= tokens.size()) {
				return false;
			}
			String token = tokens[tokenIndex]
			setBlankBuffer(buffer)
			pushBufferChar(buffer, token)
			println "buffer:" + buffer.toString()
			break;
			if (length == MAX_WORD_LEN) // buffer overflow!
				break;
		}
		def wordLength = tokens[tokenIndex].size()
		termAtt.setEmpty()
		termAtt.setLength(wordLength);
		typeAtt.setType(termNumbers[tokenIndex]);
		def bytesRef = new BytesRef(termNumbers[tokenIndex].getBytes("UTF-8"));
		payloadAtt.setPayload(bytesRef)
		tokenIndex++
		return true;
	}
	protected boolean isTokenChar(char c) {
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
		input2 = new StringReader(s)
		try {
			adm.getMorpheme("reset:"+this.toString());
			
			def result = new WordResult();
			result = adm.extractOccasion(s, null, 0);
			tokens = result.getWord()
			termNumbers = result.getTno()
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
			buffer[i] = Character.MIN_VALUE;
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
