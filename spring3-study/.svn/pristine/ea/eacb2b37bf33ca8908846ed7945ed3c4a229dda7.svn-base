package org.jaxb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBClient {
	public static void main(String[] args) throws IOException {
		JAXBContext context = null;
		Marshaller marshaller = null;
		Unmarshaller unmarshaller = null;
		Persons persons = null;

		try {
			context = JAXBContext.newInstance(Persons.class);
			persons = new Persons();
			List<NewPerson> newpersonlist = new ArrayList<NewPerson>();

			NewPerson newperson = new NewPerson();
			newperson.setName("a");
			newpersonlist.add(newperson);
			Family family = new Family();
			family.setDescription("desc");

			newperson = new NewPerson();
			newperson.setName("b");
			newpersonlist.add(newperson);

			persons.setNewPerson(newpersonlist);

			// Marshal 객체를 XML로 변환
			// Unmarshal the objects from XML

			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			OutputStreamWriter os = new OutputStreamWriter(
					new FileOutputStream("mashal.xml", false));
			marshaller.marshal(persons, os);
			os.flush();

			System.out.println("------------------------");

			unmarshaller = context.createUnmarshaller();
			persons = (Persons) unmarshaller.unmarshal(new File("mashal.xml"));

			Iterator<NewPerson> iter = persons.getNewPerson().iterator();
			while (iter.hasNext()) {
				System.out.println(iter.next().getName());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}