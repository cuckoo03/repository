package com.ds.learningds.ch09.bs

class BinarySearchTreeMain {
	static main(args) {
		BinarySearchTree bst = new BinarySearchTree()
		bst.insertBST('G')
		bst.insertBST('I')
		bst.insertBST('H')
		bst.insertBST('D')
		bst.insertBST('B')
		bst.insertBST('M')
		bst.insertBST('N')
		bst.insertBST('A')
		bst.insertBST('J')
		bst.insertBST('E')
		bst.insertBST('Q')
		
		print "\nbinary tree:"
		bst.printBST()
		
		println "is there 'A'?:"
		TreeNode p1 = bst.searchBST('A')
		if (p1 != null) {
			println "searching successs $p1.data"
		} else {
			println "failed"
		}
		
		print "is there z:"
		TreeNode p2 = bst.searchBST('Z')
		if (p2 != null) {
			println p2.data
		} else {
			println "failed"
		}
	}
}
class TreeNode {
	String data
	TreeNode left
	TreeNode right
}
class BinarySearchTree {
	TreeNode root = new TreeNode()
	TreeNode insertKey(TreeNode root, String x) {
		TreeNode p = root
		TreeNode newNode = new TreeNode()
		newNode.data = x
		newNode.left = null
		newNode.right = null
		if (p == null) {
			return newNode
		} else if (newNode.data < p.data) {
			p.left = insertKey(p.left, x)
			return p
		} else if (newNode.data > p.data) {
			p.right = insertKey(p.right, x)
			return p
		} else {
			return p
		}
	}
	void insertBST(String x) {
		root = insertKey(root, x)
	}
	TreeNode searchBST(String x) {
		TreeNode p = root
		while (p != null) {
			if (x < p.data) {
				p = p.left
			} else if (x > p.data) {
				p = p.right
			} else {
				return p
			}		
		}
		return p
	}
	void inorder(TreeNode root) {
		if (root != null) {
			inorder(root.left)
			print root.data
			inorder(root.right)
		}
	}
	void printBST() {
		inorder(root)
		println ''
	}
}
