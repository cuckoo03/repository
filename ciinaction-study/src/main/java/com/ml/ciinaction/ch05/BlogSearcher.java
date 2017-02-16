package com.ml.ciinaction.ch05;

import com.alag.ci.blog.search.BlogQueryResult;

public interface BlogSearcher {
	public BlogQueryResult getRelevantBlogs(BlogQueryParameter param);
}
