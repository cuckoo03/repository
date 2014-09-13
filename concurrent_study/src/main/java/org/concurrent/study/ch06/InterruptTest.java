package org.concurrent.study.ch06;

public class InterruptTest {

	public static void main(String[] args) {

		Thread t = new WorkingThread();
		t.start();
		try {
			Thread.sleep(3000);
			System.out.println("occurred interrupt");
			t.interrupt();
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

/**
 * 
 * @author x60
 * 
 */
class InterruptThread extends Thread {
	@Override
	public void run() {
		try {
			System.out.println("sleep start");
			Thread.sleep(5000);
			System.out.println("sleep end");
		} catch (InterruptedException e) {
			System.out.println("interrupt exception");
			e.printStackTrace();
		}
		System.out.println("run end");
	}
}

/**
 * <p>
 * 쓰레드가 interrupted exception 을 통보하지 않을 경우에 반복문 조건식에서
 * 인터럽트 발생 여부를 검사하거나  반복문 안에서 쓰레드를 종료한다.
 * 
 * @author x60
 * 
 */
class WorkingThread extends Thread {
	@Override
	public void run() {
		while (true) {
			try {
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				System.out.println('a');
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
