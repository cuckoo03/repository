package com.ds.learningds.ch09

import java.text.Normalizer;

class BinaryTreeMain {

	static main(args) {
		LinkedTree t = new LinkedTree()
		
		TreeNode n7 = t.makeBT(null, 'D', null)
		TreeNode n6 = t.makeBT(null, 'C', null)
		TreeNode n5 = t.makeBT(null, 'B', null)
		TreeNode n4 = t.makeBT(null, 'A', null)
		TreeNode n3 = t.makeBT(n6, '/', n7)
		TreeNode n2 = t.makeBT(n4, '*', n5)
		TreeNode n1 = t.makeBT(n2, '-', n3)
		
		print "\n preorder :"
		t.preorder(n1)
		
		print "\n inorder :"
		t.inorder(n1)
		
		print "\n postorder: "
		t.postorder(n1)
	}
}
class TreeNode {
	Object data
	TreeNode left
	TreeNode right
}
class LinkedTree {
	private TreeNode root
	TreeNode makeBT(TreeNode bt1, Object data, TreeNode bt2) {
		TreeNode root = new TreeNode()
		root.data = data
		root.left = bt1
		root.right = bt2
		return root
	}
	// 루트부터 왼쪽, 오른쪽
	void preorder(TreeNode root) {
		if (root != null) {
			print root.data
			preorder(root.left)
			preorder(root.right)
		}
	}
	// 왼쪽 자식부터
	void inorder(TreeNode root) {
		if (root != null) {
			inorder(root.left)
			print root.data
			inorder(root.right)
		}
	}
	void postorder(TreeNode root) {
		if (root != null) {
			postorder(root.left)
			postorder(root.right)
			print root.data
		}
	}
}