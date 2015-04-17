package org.akka.essentials.java.future.example;

import java.util.Date;

import org.akka.essentials.java.future.example.messages.Order;

import akka.actor.UntypedActor;

public class OrderActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			// ideally we will get list of orders for given user id
			Order order = new Order(Integer.valueOf(123), Float.valueOf(345),
					Integer.valueOf(5));
			Thread.sleep(3000);
			System.out.println(" OrderActor sending message at "+System.currentTimeMillis());
			getSender().tell(order);
		}
	}
}
