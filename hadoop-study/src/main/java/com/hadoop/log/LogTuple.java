package com.hadoop.log;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

public class LogTuple implements Writable {
	private static final String separator = "\t";
	private static final DateFormat sdf = new SimpleDateFormat(
			"dd/MMM/yyyy:hh:mm:ss");

	private String localhost;
	private String identd;
	private String userId;
	private Date requestDate = new Date();
	private String timezone;
	private String requestInfo;

	public String getLocalhost() {
		return localhost;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public String getIdentd() {
		return identd;
	}

	public void setIdentd(String identd) {
		this.identd = identd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeString(out, localhost);
		WritableUtils.writeString(out, identd);
		WritableUtils.writeString(out, userId);
		WritableUtils.writeVInt(out, requestDate.getDate());
		WritableUtils.writeString(out, timezone);
		WritableUtils.writeString(out, requestInfo);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		localhost = WritableUtils.readString(in);
		identd = WritableUtils.readString(in);
		userId = WritableUtils.readString(in);
		requestDate = new Date(WritableUtils.readVInt(in));
		requestInfo = WritableUtils.readString(in);
	}

	@Override
	public String toString() {
		return localhost + separator + identd + separator + userId + separator
				+ sdf.format(requestDate) + separator + timezone + separator
				+ requestInfo;
	}
}