package com.luceneinaction.ch07

import groovy.transform.TypeChecked

import org.apache.commons.digester.Digester
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.junit.Test
import org.xml.sax.SAXException

@TypeChecked
class DigesterXmlHandler implements DocumentHandler {
	private Digester dig
	private static Document doc

	public DigesterXmlHandler() {
		this.dig = new Digester()
		dig.setValidating(false)

		dig.addObjectCreate("address-book", DigesterXmlHandler.class)
		dig.addObjectCreate("address-book/contact", Contact.class)

		// type 속성이 발견됐을때 Contact 인스턴스의 type 속성에 설정한다.
		dig.addSetProperties("address-book/contact", "type", "type")

		// 지정된 메소드를 이용해 Contact 인스턴스의 다른 속성에 설정한다
		dig.addCallMethod("address-book/contact/name", "setName", 0)
		dig.addCallMethod("address-book/contact/address", "setAddress", 0)

		// 다음 번에 address-book/contact패턴이 보이면 populateDocument메서드를 호출한다.
		dig.addSetNext("address-book/contact", "populateDocument")
	}

	@Override
	public Document getDocument(InputStream is) throws DocumentHandlerException {
		try {
			dig.parse(is)
		} catch (IOException e) {
			throw new DocumentHandlerException("Cannot parse XML", e)
		} catch (SAXException e) {
			throw new DocumentHandlerException("Cannot parse XML", e)
		}

		return doc
	}

	private void populateDocument(Contact contact) {
		doc = new Document()
		doc.add(Field.Keyword("type", contact.type))
		doc.add(Field.Keyword("name", contact.name))
	}

	public static class Contact {
		String type
		String name
	}

	@Test
	public void test() {
		def handler = new DigesterXmlHandler()
		def file = getClass().getClassLoader().getResource("com/luceneinaction/ch07/example.xml").getFile()
		def doc = handler.getDocument(new FileInputStream(file))
		println doc
	}
}
