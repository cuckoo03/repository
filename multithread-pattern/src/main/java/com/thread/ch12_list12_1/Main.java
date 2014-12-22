package com.thread.ch12_list12_1;

public class Main {
	public static void main(String[] args) {
		ActiveObject activeObject = ActiveObjectFactory.crateActiveObject(); 	
		new MakerClientThread("A", activeObject).start();
		new MakerClientThread("B", activeObject).start();
		new DisplayClientThread("C", activeObject).start();
	}

}
