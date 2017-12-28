package jpabook.start

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.Category")
@Table(name = "CATEGORY")
class Category {
	@Id
	@GeneratedValue
	@Column(name = "CATEGORY_ID")
	long id

	String name

	@ManyToMany
	@JoinTable(name = "CATEGORY_ITEM",
	joinColumns = @JoinColumn(name = "CATEGORY_ID"),
	inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
	List<Item> items = []
	
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	Category parent
	
	@OneToMany(mappedBy = "parent")
	List<Category> child = []
	
	void addChildCategory(Category child) {
		this.child += child
		child.parent = this
	}
	
	void addItem(Item item) {
		items += item
	}
}