package javacan.first;

import akka.actor.UntypedActor;

public class PrintActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		Thread.sleep(500);
		System.out.println(message);
	}
}
