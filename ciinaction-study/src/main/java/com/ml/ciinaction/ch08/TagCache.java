package com.ml.ciinaction.ch08;

import java.io.IOException;

public interface TagCache {
	public Tag getTag(String text) throws IOException;	
}
