package com.luceneinaction.ch07;

import java.io.InputStream;

import org.apache.lucene.document.Document;

public interface DocumentHandler {
	Document getDocument(InputStream is) throws DocumentHandlerException;
}
