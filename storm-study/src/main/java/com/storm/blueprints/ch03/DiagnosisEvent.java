package com.storm.blueprints.ch03;

import java.io.Serializable;

public class DiagnosisEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDiag() {
		return diag;
	}

	public void setDiag(String diag) {
		this.diag = diag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private double lat;
	private double lng;
	private long time;
	private String diag;

	public DiagnosisEvent(double lat, double lng, long time, String diag) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.time = time;
		this.diag = diag;
	}
}