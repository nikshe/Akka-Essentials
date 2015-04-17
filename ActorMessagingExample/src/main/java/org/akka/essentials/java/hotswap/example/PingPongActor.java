package org.akka.essentials.java.hotswap.example;

import akka.actor.UntypedActor;
import akka.japi.Procedure;

public class PingPongActor extends UntypedActor {

	static String PING = "PING";
	static String PONG = "PONG";
	int count = 0;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (((String) message).matches(PING)) {
				System.out.println("received a message "+message);
				count += 1;
				Thread.sleep(100);
				System.out.println("sleep 0.1's ... and send a meesage to "+getSelf());
				getSelf().tell(PONG,getSelf());
				getContext().become(new Procedure<Object>() {
					public void apply(Object message) {
						if (message instanceof String) {
							if (((String) message).matches(PONG)) {
								System.out.println(count+ " received a message : " + getSelf());
								count += 1;
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									//
								}
								getSelf().tell(PING,getSelf());
								getContext().unbecome();
							}
						}
					}
				});
				if (count > 10)
					getContext().stop(getSelf());
			}
		}
	}
}
