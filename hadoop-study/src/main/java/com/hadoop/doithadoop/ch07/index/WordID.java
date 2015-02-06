package com.hadoop.doithadoop.ch07.index;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

class WordID implements WritableComparable<WordID> {
	private String word;
	private Long docID;

	public WordID() {
	}

	public WordID(String word, long docID) {
		this.word = word;
		this.docID = docID;
	}

	/**
	 * deserialization시에 사용되는 메서드, Writable 인터페이스에서 필요한 메서드
	 */
	@Override
	public void readFields(DataInput input) throws IOException {
		word = WritableUtils.readString(input);
		docID = input.readLong();
	}

	/**
	 * serialization시에 사용되는 메서드, Writable인터페이스에서 필요한 메서드
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		WritableUtils.writeString(output, word);
		output.writeLong(docID);
	}

	/**
	 * Comparable 인터페이스에서 필요한 메서드
	 */
	@Override
	public int compareTo(WordID o) {
		int result = word.compareTo(o.word);
		if (0 == result) {
			result = (int) (docID - o.docID);
		}
		return result;
	}

	@Override
	public String toString() {
		return new StringBuilder().append('{').append(word).append(',')
				.append(docID).append('}').toString();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Long getDocID() {
		return docID;
	}

	public void setDocID(Long docID) {
		this.docID = docID;
	}
}