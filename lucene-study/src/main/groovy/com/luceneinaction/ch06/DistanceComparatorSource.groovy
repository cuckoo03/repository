package com.luceneinaction.ch06

import groovy.transform.TypeChecked

import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.Term
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.ScoreDocComparator
import org.apache.lucene.search.SortComparatorSource
import org.apache.lucene.search.SortField

@TypeChecked
class DistanceComparatorSource implements SortComparatorSource {
	private int x
	private int y

	public DistanceComparatorSource(int x, int y) {
		this.x = x
		this.y = y
	}

	@Override
	public ScoreDocComparator newComparator(IndexReader reader,
			String fieldName)
	throws IOException {
		return new DistanceScoreDocLookupComparator(reader, fieldName, x, y)
	}

	private static class DistanceScoreDocLookupComparator
	implements ScoreDocComparator {
		private float[] distances

		public DistanceScoreDocLookupComparator(IndexReader reader,
		String fieldName, int x, int y) {
			final def enumerator = reader.terms(new Term(fieldName, ""))
			distances = new float[reader.maxDoc()]
			if (distances.length > 0) {
				def termDocs = reader.termDocs()
				try {
					if (enumerator.term() == null)
						throw new RuntimeException("no term is field:$fieldName")

					// 모든 텀에 대해 반복
					while (true) {
						def term = enumerator.term()
						if (term.field() != fieldName)
							break

						termDocs.seek(enumerator)
						// 현재 텀을 갖는 모든 문서에 대해 반복
						while (termDocs.next()) {
							def xy = term.text().split(",")
							def deltaX = Integer.parseInt(xy[0]) - x
							def deltaY = Integer.parseInt(xy[1]) - y

							// 계산한 결과를 거리배열에 저장한다.
							distances[termDocs.doc()] =
									(float) Math.sqrt(deltaX * deltaY + deltaX * deltaY)
						}
						if (!enumerator.next()) {
							break
						}
					}
				} finally {
					termDocs.close()
				}
			}
		}

		@Override
		public int compare(ScoreDoc i, ScoreDoc j) {
			if (distances[i.doc] < distances[j.doc])
				return -1
			if (distances[i.doc] > distances[j.doc])
				return 1

			return 0;
		}

		@Override
		public int sortType() {
			return SortField.FLOAT
		}

		@Override
		public Comparable sortValue(ScoreDoc i) {
			return new Float(distances[i.doc])
		}
	}
}
