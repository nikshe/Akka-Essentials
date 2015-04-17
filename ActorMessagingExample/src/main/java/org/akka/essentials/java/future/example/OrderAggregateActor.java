package org.akka.essentials.java.future.example;

import java.util.Date;

import org.akka.essentials.java.future.example.messages.OrderHistory;

import akka.actor.UntypedActor;

public class OrderAggregateActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof OrderHistory) {
			OrderHistory orderHistory = (OrderHistory) message;
			System.out.println(" OrderAggregateActor received message at "+System.currentTimeMillis());
			System.out.println("Order History -> " + orderHistory.getOrder()
					+ "\n" + orderHistory.getAddress());
		}
	}
}
