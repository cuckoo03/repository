package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader

import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.analysis.util.CharTokenizer
import org.apache.lucene.analysis.util.CharacterUtils
import org.apache.lucene.analysis.util.CharacterUtils.CharacterBuffer
import org.apache.lucene.util.Version
import com.tapacross.ise.MorphemeResult
import com.tapacross.service.AdminDataManager
import groovy.transform.TypeChecked

@TypeChecked
class MyCharTokenizer extends Tokenizer {
	private String s;
	/**
	 * Creates a new {@link CharTokenizer} instance
	 *
	 * @param matchVersion
	 *          Lucene version to match
	 * @param input
	 *          the input to split up into tokens
	 */
	def MyCharTokenizer(Version matchVersion, Reader input, s) {
		super(input);
		charUtils = CharacterUtils.getInstance(matchVersion);
		this.s = s
		adm.setOnlineEngineAddress("broker.ip:2012");
	}
	AdminDataManager adm = new AdminDataManager();
	private String[] tokens;
	private String[] pos;

	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}

	private int offset = 0, bufferIndex = 0, dataLen = 0, finalOffset = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	private final CharacterUtils charUtils;
	private final CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);

	/**
	 * Returns true iff a codepoint should be included in a token. This tokenizer
	 * generates as tokens adjacent sequences of codepoints which satisfy this
	 * predicate. Codepoints for which this is false are used to define token
	 * boundaries and are not included in tokens.
	 */
	protected boolean isTokenChar(int c) {
		return !Character.isWhitespace(c);
//				return true;
	}

	/**
	 * Called on each token character to normalize it before it is added to the
	 * token. The default implementation does nothing. Subclasses may use this to,
	 * e.g., lowercase tokens.
	 */
	protected int normalize(int c) {
		return c;
	}

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();

		def word = ""
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

				def chars = Character.toChars(c)[0]
//				if (Character.isWhitespace(chars)) {
//					continue
//				}
				def token = tokens[tokenIndex]
				word += chars
				if (word.equals(token)) {
					word = ""
					break
				}
			} else if (length > 0)             // at non-Letter w/ chars
				break;                           // return 'em
		}

		termAtt.setLength(length);
		assert start != -1;
		offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(end));
		typeAtt.setType(pos[tokenIndex++])
		return true;

	}

	@Override
	public final void end() throws IOException {
		super.end();
		// set final offset
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
	int tokenIndex = 0;
	@Override
	public void reset() throws IOException {
		super.reset();
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		finalOffset = 0;
		ioBuffer.reset(); // make sure to reset the IO buffer!!

		tokenIndex = 0;
		try {
			MorphemeResult result = new MorphemeResult();
			result = adm.getMorpheme(s);
			tokens = result.getToken();
			pos = result.getSynaxTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
