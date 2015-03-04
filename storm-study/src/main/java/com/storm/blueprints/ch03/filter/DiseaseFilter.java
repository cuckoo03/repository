package com.storm.blueprints.ch03.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.storm.blueprints.ch03.DiagnosisEvent;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

public class DiseaseFilter extends BaseFilter {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(DiseaseFilter.class);

	@Override
	public boolean isKeep(TridentTuple tuple) {
		DiagnosisEvent diagnosis = (DiagnosisEvent) tuple.getValue(0);
		Integer code = Integer.parseInt(diagnosis.getDiag());
		if (code.intValue() <= 322) {
			LOG.debug("Emitting disease [{}]", diagnosis.getDiag());
			return true;
		} else {
			LOG.debug("Filtering disease [{}", diagnosis.getDiag());
			return false;
		}
	}
}