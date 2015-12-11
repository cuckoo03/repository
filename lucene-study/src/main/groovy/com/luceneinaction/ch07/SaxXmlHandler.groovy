package com.luceneinaction.ch07

import groovy.transform.TypeChecked

import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.junit.Test
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

@TypeChecked
class SaxXmlHandler extends DefaultHandler implements DocumentHandler {
	private StringBuffer elementBuffer = new StringBuffer()
	private Map<String, String> attributeMap = new HashMap<>()

	private Document doc

	@Override
	public Document getDocument(InputStream is) throws DocumentHandlerException {
		def spf = SAXParserFactory.newInstance()

		try {
			def parser = spf.newSAXParser()
			parser.parse(is, this)
		} catch (IOException e) {
			throw new DocumentHandlerException("cannot parse XML documnet", e)
		}
		catch (ParserConfigurationException e) {
			throw new DocumentHandlerException("cannot parse XML Document", e)
		}
		catch (SAXException e) {
			throw new DocumentHandlerException("cannot parse XML Document", e)
		}
		return doc
	}

	/**
	 * 파싱을 시작할 때 호출
	 */
	@Override
	public void startDocument() {
		this.doc = new Document()
	}

	/**
	 * 새로운 XML 요소 시작
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		elementBuffer.setLength(0)
		attributeMap.clear()
		if (atts.getLength() > 0) {
			attributeMap = new HashMap()
			for (int i = 0; i < atts.getLength(); i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i))
			}
		}
	}

	/**
	 * cdata를 발견했을 때 호출
	 */
	@Override
	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length)
	}

	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		if (qName.equals("address-book")) {
			return
		}
		else if (qName.equals("contact")) {
			def iter = attributeMap.keySet().iterator()
			while (iter.hasNext()) {
				def attName = iter.next()
				def attValue = attributeMap.get(attName)
				doc.add(Field.Keyword(attName, attValue))
			}
		} else {
			doc.add(Field.Keyword(qName, elementBuffer.toString()))
		}
	}

	@Test
	public void test() {
		def handler = new SaxXmlHandler()
		def file = getClass().getClassLoader().getResource("com/luceneinaction/ch07/example.xml").getFile()
		def doc = handler.getDocument(new FileInputStream(file))
		println doc
	}
}