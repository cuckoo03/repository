package javacan.first;

import scala.Option;
import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.dispatch.Future;

public class SendMessageMain {

	public static void main(String[] args) {
		/*
		ActorRef actor = Actors.actorOf(PrintActor.class);
		actor.start();
		actor.sendOneWay("send 1");
		actor.sendOneWay("send 2");
		System.out.println("start");
		*/
		
		ActorRef actor2 = Actors.actorOf(PingActor.class);
		actor2.start();
		Object obj2 = actor2.sendRequestReply("hello");
		System.out.println(obj2);
	
		/*
		Future future = actor2.sendRequestReplyFuture("hi");
		future.await();
		if (future.isCompleted()) {
			Option resultOption = future.result();
			if (resultOption.isDefined()) {
				Object result = resultOption.get();
				System.out.println(result);
			}
		}
		*/
		
		Actors.registry().shutdownAll();
	}
}
