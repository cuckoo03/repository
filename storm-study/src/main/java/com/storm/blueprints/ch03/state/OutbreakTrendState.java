package com.storm.blueprints.ch03.state;

import storm.trident.state.map.IBackingMap;
import storm.trident.state.map.NonTransactionalMap;

public class OutbreakTrendState extends NonTransactionalMap<Long> {
	protected OutbreakTrendState(IBackingMap<Long> backing) {
        super(backing);
	}

}
