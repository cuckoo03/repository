package com.luceneinaction.ch07;

import java.io.File;

import org.apache.lucene.document.Document;

public interface FileHandler {
	Document getDocument(File file) throws FileHandlerException;
}
