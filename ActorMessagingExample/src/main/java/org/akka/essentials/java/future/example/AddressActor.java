package org.akka.essentials.java.future.example;

import java.util.Date;

import org.akka.essentials.java.future.example.messages.Address;

import akka.actor.UntypedActor;

public class AddressActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			// ideally we will get address for given user id
			Address address = new Address(userId, "Munish Gupta",
					"Sarjapura Road", "Bangalore, India");
			Thread.sleep(2000);
			System.out.println(" AddressActor sending message at "+System.currentTimeMillis());
			getSender().tell(address);
		}
	}
}