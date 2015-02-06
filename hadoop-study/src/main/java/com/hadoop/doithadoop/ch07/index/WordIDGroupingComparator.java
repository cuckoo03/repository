package com.hadoop.doithadoop.ch07.index;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 그룹핑은 리듀스 단계에서 로컬 디스크로부터 맵 출력값 레코드를 스트리밍 할 때 일어난다. 그루핑은 리듀서 호출을 위해 한 개의 논리적
 * 레코드 시퀀스를 형성할 수 있게 레코드 병합방식을 지정하는 단계 즉 같은 리듀서로 모인 레코드들을 소팅하여 같은 키를 갖는 레코드들을
 * 하나의 그룹으로 만들때 사용되는 것 주로 파티셔너에서 비교하던 키를 기준으로 비교가 이루어지도록 한다. 그룹핑 단계에서는 모든 레코드가
 * 2차 정렬 순서로 이미 정렬돼 있으며 그루핑 비교기는 키가 같은 레코드만 합치면 된다.
 * 
 * @author cuckoo03
 *
 */
class WordIDGroupingComparator extends WritableComparator {
	protected WordIDGroupingComparator() {
		super(WordID.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		WordID k1 = (WordID) w1;
		WordID k2 = (WordID) w2;
		return k1.getWord().compareTo(k2.getWord());
	}
}
