package com.thread.ch12_list12_16;

public class Main {
	public static void main(String[] args) {
		ActiveObject activeObject = ActiveObjectFactory.crateActiveObject();

		try {
			new MakerClientThread("A", activeObject).start();
			new MakerClientThread("B", activeObject).start();
			new DisplayClientThread("C", activeObject).start();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally {
			System.out.println("shutdown");
			activeObject.shutdown();
		}
	}
}
