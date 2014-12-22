package com.service.hibernate.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class Version {
	private int idx;
	private String ios_min;
	private String ios_max;
	private Date regdate;
	private String play_min;
	private String play_max;
	private String tstore_min;
	private String tstore_max;
	private String nstore_min;
	private String nstore_max;
	private String new_event;

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getIos_min() {
		return ios_min;
	}

	public void setIos_min(String ios_min) {
		this.ios_min = ios_min;
	}

	public String getIos_max() {
		return ios_max;
	}

	public void setIos_max(String ios_max) {
		this.ios_max = ios_max;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public String getPlay_min() {
		return play_min;
	}

	public void setPlay_min(String play_min) {
		this.play_min = play_min;
	}

	public String getPlay_max() {
		return play_max;
	}

	public void setPlay_max(String play_max) {
		this.play_max = play_max;
	}

	public String getTstore_min() {
		return tstore_min;
	}

	public void setTstore_min(String tstore_min) {
		this.tstore_min = tstore_min;
	}

	public String getTstore_max() {
		return tstore_max;
	}

	public void setTstore_max(String tstore_max) {
		this.tstore_max = tstore_max;
	}

	public String getNstore_min() {
		return nstore_min;
	}

	public void setNstore_min(String nstore_min) {
		this.nstore_min = nstore_min;
	}

	public String getNstore_max() {
		return nstore_max;
	}

	public void setNstore_max(String nstore_max) {
		this.nstore_max = nstore_max;
	}

	public String getNew_event() {
		return new_event;
	}

	public void setNew_event(String new_event) {
		this.new_event = new_event;
	}
}
