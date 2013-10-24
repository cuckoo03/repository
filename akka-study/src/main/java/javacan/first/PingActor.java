package javacan.first;

import akka.actor.UntypedActor;

public class PingActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("hello")) {
			getContext().replyUnsafe("resonse:" + message);
		} else if (message.equals("hi")) {
			if (!getContext().replySafe(message)) {
				System.out.println("fail");
			} else {
				getContext().replyUnsafe("resonse:" + message);
			}
		}
	}

}
