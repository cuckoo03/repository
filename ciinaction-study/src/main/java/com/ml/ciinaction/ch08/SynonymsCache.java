package com.ml.ciinaction.ch08;

import java.io.IOException;
import java.util.List;

public interface SynonymsCache {
	public List<String> getSynonym(String text) throws IOException;
}
