package javacan.first;

import akka.actor.UntypedActor;

public class PingActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("hello")) {
			// replyUnsafe():응답 실패시 예외를 발생시킴
			getContext().replyUnsafe("response:" + message);
		} else if (message.equals("hi")) {
			// replySafe():응답 실패시 false를 리턴시킴
			if (!getContext().replySafe(message)) {
				System.out.println("fail");
			} else {
				getContext().replyUnsafe("resonse:" + message);
			}
		}
	}

}
