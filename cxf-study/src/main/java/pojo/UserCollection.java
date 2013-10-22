package pojo;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userCollection")
public class UserCollection {
	private Collection<User> users;

	/**기본 생성자 없을시 런타임 오류 발생
	 * org.apache.cxf.jaxrs.provider.AbstractJAXBProvider handleJAXBException
	 * WARNING: com.sun.xml.bind.v2.runtime.IllegalAnnotationsException: 1
	 * counts of IllegalAnnotationExceptions pojo.UserCollection does not have a
	 * no-arg default constructor.
	 */
	public UserCollection() {
	}

	public UserCollection(Collection<User> users) {
		this.users = users;
	}

	@XmlElement(name = "user")
	@XmlElementWrapper(name = "users")
	public Collection<User> getUsers() {
		return users;
	}
}
