package com.holubonpatterns.lifegame

import java.nio.DirectByteBuffer.Deallocator;

import groovy.transform.TypeChecked

import com.designpattern.lifegame.Distributor
import com.designpattern.lifegame.Listener

@TypeChecked
class Publisher {
	public void subscribe(Listener listener) {
	}
	public void publish(Distributor distributor) {
	}
	public void cancelSubscription(Listener l) {
	}

	private class Node {
		public final Object subscriber
		public final Node next

		private Node(Object subscriber, Node next) {
			this.subscriber = subscriber
			this.next = next
		}

		public Node remove(Object target) {
			if (target == subscriber) {
				return next
			}

			if (next == null) {
				throw new NoSuchElementException(target.toString())
			}
			return new Node(subscriber, next.remove(target))
		}

		public void accept(Distributor deliverAgent) {
			deliverAgent.deliverTo(subscriber)
		}

		private volatile Node subscribers = null

		public void publish(Distributor deliveryAgent) {
			for (Node cursor = subscribers;
			cursor != null;
			cursor = cursor.next) {
				cursor.accept(deliveryAgent)
			}
		}
		
		public void subscribe(Object subscriber) {
			subscribers = new Node(subscriber, subscribers)
		}
		
		public void cancelSubscription(Object subscriber) {
			subscribers = subscribers.remove(subscriber)
		}
	}
}
