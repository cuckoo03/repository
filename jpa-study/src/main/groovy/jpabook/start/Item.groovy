package jpabook.start

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.Item")
@Table(name = "Item")
class Item {
	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	long id

	String name
	int price
	int stockQuantity

	@ManyToMany(mappedBy = "items")
	List<Category> categories = []
}
