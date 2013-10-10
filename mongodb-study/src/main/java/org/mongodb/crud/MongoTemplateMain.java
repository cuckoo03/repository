package org.mongodb.crud;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoTemplateMain {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/mongodb/crud/mongo-template-context.xml");

		MongoOperations mongoOps = (MongoOperations) context
				.getBean("userMongoTemplate");
		User user = new User("mongo", 31);

		// insert
		mongoOps.save(user);
		System.out.println("insert:" + user);

		// find
		user = mongoOps.findById(user.getId(), User.class);
		System.out.println("find:" + user);

		// update
		Query query = new Query(Criteria.where("name").is("mongo"));
		mongoOps.updateFirst(query, Update.update("age", 32), User.class);
		user = mongoOps.findOne(query, User.class);
		System.out.println("updated:" + user);

		// delete
		mongoOps.remove(user);

		List<User> users = mongoOps.findAll(User.class);
		System.out.println("number of users:" + users.size());

		mongoOps.dropCollection(User.class);

	}
}
