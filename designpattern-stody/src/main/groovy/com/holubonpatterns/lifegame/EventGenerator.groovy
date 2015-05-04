package com.holubonpatterns.lifegame

import groovy.transform.TypeChecked

import com.designpattern.lifegame.Distributor
import com.designpattern.lifegame.Listener
import com.designpattern.lifegame.NotifyListener

@TypeChecked
class EventGenerator {
	private Publisher publisher = new Publisher()

	public void addEventListener(Listener l) {
		publisher.subscribe(l)
	}

	public void removeEventListener(Listener l) {
		publisher.cancelSubscription(l)
	}

	public void someEventHasHappend(final String reason) {
		publisher.publish(new Distributor() {
					@Override
					public void deliverTo(Object subscriber) {
						((NotifyListener)subscriber).notify(reason)
					}
				})
	}
}
