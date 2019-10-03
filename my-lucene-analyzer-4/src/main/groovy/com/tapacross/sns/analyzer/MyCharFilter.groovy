package com.tapacross.sns.analyzer

import groovy.transform.TypeChecked

import java.io.IOException
import java.io.Reader
import java.util.regex.Pattern

import org.apache.lucene.analysis.charfilter.BaseCharFilter

@TypeChecked
class MyCharFilter extends BaseCharFilter {
	public MyCharFilter(Reader reader) {
		super(reader);
	}
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return super.read(cbuf, off, len)
	}
}
