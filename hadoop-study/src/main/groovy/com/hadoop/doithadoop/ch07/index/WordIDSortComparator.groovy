package com.hadoop.doithadoop.ch07.index

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import com.hadoop.doithadoop.ch07.index.WordID;

class WordIDSortComparator extends WritableComparator {
	protected WordIDSortComparator() {
		super(WordID.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		WordID k1 = w1;
		WordID k2 = w2;
		int result = k1.compareTo(k2)
		return result
	}
}
