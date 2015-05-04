package com.holubonpatterns.lifegame

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
}
