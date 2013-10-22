package pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {
	private Integer id;
	private String name;

	/**
	 * 기본 생성자 없을시 런타임 오류 발생
	 * org.apache.cxf.jaxrs.provider.AbstractJAXBProvider handleJAXBException
	 * WARNING: com.sun.xml.bind.v2.runtime.IllegalAnnotationsException: 1
	 * counts of IllegalAnnotationExceptions pojo.User does not have a no-arg
	 * default constructor.
	 */
	public User() {
	}

	public User(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("{id=%s,  name=%s}", id, name);
	}
}
