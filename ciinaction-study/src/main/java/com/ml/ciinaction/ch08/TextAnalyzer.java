package com.ml.ciinaction.ch08;

import java.io.IOException;
import java.util.List;

public interface TextAnalyzer {
	public List<Tag> analyzeText(String text) throws IOException;

	public TagMagnitudeVector createTagMagnitudeVector(String text)
			throws IOException;
}
