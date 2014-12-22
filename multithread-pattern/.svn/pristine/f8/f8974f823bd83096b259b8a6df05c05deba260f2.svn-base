package com.thread.ch12_exam12_2_2;

public class Main {
	public static void main(String[] args) {
		ActiveObject activeObject = ActiveObjectFactory.crateActiveObject();

		try {
			new AddClientThread("A", activeObject).start();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally {
			System.out.println("shutdown");
			activeObject.shutdown();
		}
	}
}
