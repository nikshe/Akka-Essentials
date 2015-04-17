package org.akka.essentials.java.future.example;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.akka.essentials.java.future.example.messages.Address;
import org.akka.essentials.java.future.example.messages.Order;
import org.akka.essentials.java.future.example.messages.OrderHistory;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;

public class ProcessOrderActor extends UntypedActor {

	final Timeout t = new Timeout(Duration.create(4, TimeUnit.SECONDS));
	final Timeout t1 = new Timeout(Duration.create(3, TimeUnit.SECONDS));
	ActorRef orderActor = getContext().actorOf(new Props(OrderActor.class));
	ActorRef addressActor = getContext().actorOf(new Props(AddressActor.class));
	ActorRef orderAggregateActor = getContext().actorOf(new Props(OrderAggregateActor.class));

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
			// make concurrent calls to actors
			
			futures.add(ask(addressActor, userId, t1));
			futures.add(ask(orderActor, userId, t));

			// set the sequence in which the reply are expected
			System.out.println(" ProcessOrderActor process message at "+ System.currentTimeMillis());
			final Future<Iterable<Object>> aggregate = Futures.sequence(
					futures, getContext().system().dispatcher());
			// once the replies comes back, we loop through the Iterable to
			// get the replies in same order
			// final Future<OrderHistory> aggResult =
			if(aggregate.isCompleted()){
				System.out.println("done.....");
			}
			final Future<OrderHistory> aggResult = aggregate.map(
					new Mapper<Iterable<Object>, OrderHistory>() {
						public OrderHistory apply(Iterable<Object> coll) {
//							Object tmp = it.next();
//							Address address = null;
//							Order order = null;
//							do{
//							if (tmp instanceof Address) {
//								address = (Address) tmp;
//								System.out.println("dealing address...");
//							}else if (tmp instanceof Order){
//								System.out.println("dealing order...");
//								order = (Order) tmp;
//							}else{
//								System.out.println("unknowing ....");
//							}
//							tmp = it.next();
//							}while(it.hasNext());
							
							final Iterator<Object> it = coll.iterator();
							System.out.println("start...");
							final Address address = (Address) it.next();
							final Order order = (Order) it.next();
							return new OrderHistory(order, address);
						}
					}, getContext().system().dispatcher());
			// aggregated result is piped to another actor
			pipe(aggResult, getContext().system().dispatcher()).to(
					orderAggregateActor);
		}
	}
}
