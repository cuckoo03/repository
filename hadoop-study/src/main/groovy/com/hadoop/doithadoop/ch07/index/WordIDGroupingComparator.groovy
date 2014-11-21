package com.hadoop.doithadoop.ch07.index

import org.apache.hadoop.io.WritableComparable
import org.apache.hadoop.io.WritableComparator

class WordIDGroupingComparator extends WritableComparator {
	protected WordIDGroupingComparator() {
		super(WordID.class, true)
	}
	@Override
	public int compare(WritableComparable<WordID> w1,
			WritableComparable<WordID> w2) {
			WordID k1 = w1
			WordID k2 = w2
			return k1.word.compareTo(k2.word)
	}
}
