package com.hadoop.doithadoop.ch07.index;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import com.hadoop.doithadoop.ch07.index.WordID;

/**
 * Sortcomparator는 맵사이드와 리듀스사이드 모두 정렬에 참여한다. 정렬을 위해 전체키를 비교(toString()하는
 * comparator를 구현한다. 리듀스에서 같은 키로 묶인 레코드들에서 밸류들을 소팅할때 쓰는 것
 * 
 * @author cuckoo03
 *
 */
class WordIDSortComparator extends WritableComparator {
	protected WordIDSortComparator() {
		super(WordID.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		WordID k1 = (WordID) w1;
		WordID k2 = (WordID) w2;
		int result = k1.compareTo(k2);
		return result;
	}
}
