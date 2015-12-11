package com.lucene;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public abstract class LiaTestCase extends TestCase {
//	private String indexDir = System.getProperty("index.dir");
	protected String indexDir = System.getProperty("java.io.tmpdir", "tmp") +
			System.getProperty("file.separator") + "index-dir";
	protected Directory directory;

	@Override
	protected void setUp() throws Exception {
		directory = FSDirectory.getDirectory(indexDir, false);
	}

	@Override
	protected void tearDown() throws Exception {
		directory.close();
	}

	/**
	 * For troubleshooting
	 */
	protected final void dumpHits(Hits hits) throws IOException {
		if (hits.length() == 0) {
			System.out.println("No hits");
		}

		for (int i = 0; i < hits.length(); i++) {
			Document doc = hits.doc(i);
			System.out.println(hits.score(i) + ":" + doc.get("title"));
		}
	}

	protected final void assertHitsIncludeTitle(Hits hits, String title)
			throws IOException {
		for (int i = 0; i < hits.length(); i++) {
			Document doc = hits.doc(i);
			if (title.equals(doc.get("title"))) {
				assertTrue(true);
				return;
			}
		}

		fail("title '" + title + "' not found");
	}

	protected final Date parseDate(String s) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(s);
	}
}
