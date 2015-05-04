package com.holubonpatterns.lifegame

import groovy.transform.TypeChecked

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import com.designpattern.lifegame.Distributor
import com.designpattern.lifegame.Listener

@TypeChecked
class Clock {
	private Timer clock = new Timer()
	private TimerTask tick = null
	public final int aaa

	private Clock() {
		createMenus()
	}

	private static Clock instance

	public synchronized static Clock instance() {
		if (instance == null) {
			instance = new Clock()
		}
		return instance
	}

	public void startTicking(int millisecondsBetweenTicks) {
		if (tick != null) {
			tick.cancel();
			tick = null
		}

		if (millisecondsBetweenTicks > 0) {
			tick = new TimerTask() {
						@Override
						public void run() {
							tick()
						}
					}
			clock.scheduleAtFixedRate(tick, 0, millisecondsBetweenTicks)
		}
	}

	public void stop() {
		startTicking(0)
	}

	private void createMenus() {
		ActionListener modifier = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String name = ((JMenuItem)e.getSource()).getName()
						char toDo = name.charAt(0)

						if (toDo == 'T') {
							tick()
						} else {
							startTicking(toDo == 'A'?500:
									toDo == 'S' ? 150 :
									toDo == 'M' ? 70 :
									toDo == 'F' ? 30 : 0)
						}
					}
				}

		MenuSite.addLine(this, "", "", modifier)
	}

	private Publisher publisher = new Publisher()

	public void addClockListener(Listener observer) {
		publisher.subscribe(observer)
	}

	public void tick() {
		publisher.publish(new Distributor() {
					@Override
					public void deliverTo(Object subscriber) {
						((Listener)subscriber).tick()
					}
				})
	}
}