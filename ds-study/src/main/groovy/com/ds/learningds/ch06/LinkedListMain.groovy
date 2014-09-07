package com.ds.learningds.ch06

class LinkedListMain {

	static main(args) {
		LinkedList L = new LinkedList()
		println "1 add 3 nodes to blank list"
		L.insertLastNode("A")
		L.insertLastNode("C")
		L.insertLastNode("E")
		L.printList()

		println "2 add fri node after wed node"
		ListNode pre = L.searchNode("C")
		if (pre == null) {
			println "search failed. no search data"
		} else {
			L.insertMiddleNode(pre, "D")
			L.printList()
		}

		println "3 change reserve nodes"
		L.reverseList()
		L.printList()

		println "4 delete last node"
		L.deleteLastNode()
		L.printList()
	}
}
class LinkedList {
	private ListNode head = null
	void insertLastNode(String data) {
		ListNode newNode = new ListNode(data)
		if (head == null) {
			this.head = newNode	
		} else {
			ListNode temp = head
			while (temp.link != null) {
				temp = temp.link
			}
			temp.link = newNode
		}
	}
	void printList(){
		ListNode temp = this.head
		print "L=("
		while (temp != null) {
			print temp.getData()
			temp = temp.link
			if (temp != null) {
				print ", "
			}
		}
		println ")"
	}
	void insertMiddleNode(ListNode pre, String data) {
		ListNode newNode = new ListNode(data)
		newNode.link = pre.link
		pre.link = newNode
	}
	void reverseList() {
		ListNode next = head
		ListNode current = null
		ListNode pre = null
		while (next != null) {
			pre = current
			current = next
			next = next.link
			current.link = pre
		}
		head = current
	}
	void deleteLastNode() {
		ListNode pre, temp
		if (head == null) {
			return
		}
		if (head.link == null) {
			head = null
		} else {
			pre = head
			temp = head.link
			while (temp.link != null) {
				pre = temp
				temp = temp.link
			}
			pre.link = null
		}
	}
	ListNode searchNode(String data) {
		ListNode temp = this.head
		while (temp != null) {
			if (data == temp.getData()) {
				return temp
			} else {
				temp = temp.link
			}
		}
		return temp
	}
}
class ListNode {
	private String data = null
	ListNode link = null
	ListNode(String data) {
		this.data = data
		this.link = null
	}
	ListNode(String data, ListNode link) {
		this.data = data
		this.link = link
	}
	String getData() {
		return this.data
	}
}
