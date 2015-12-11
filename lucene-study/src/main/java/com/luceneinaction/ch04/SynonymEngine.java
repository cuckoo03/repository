package com.luceneinaction.ch04;

import java.io.IOException;

public interface SynonymEngine {
	public String[] getSynonyms(String termText) throws IOException;
}
