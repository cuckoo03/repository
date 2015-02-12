package com.hadoop.log;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

public class LogTuple implements Writable {
	private static final String separator = "\t";
	private static final DateFormat sdf = new SimpleDateFormat(
			"dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

	private String localhost;
	private String identd;
	private String userId;
	private Date requestDate = new Date();
	private String requestInfo;
	private int status;
	private String size;

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
	
	public String getFormattedRequestDate() {
		return sdf.format(requestDate);
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeString(out, localhost);
		WritableUtils.writeString(out, identd);
		WritableUtils.writeString(out, userId);
		WritableUtils.writeVLong(out, requestDate.getTime());
		WritableUtils.writeString(out, requestInfo);
		WritableUtils.writeVInt(out, status);
		WritableUtils.writeString(out, size);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		localhost = WritableUtils.readString(in);
		identd = WritableUtils.readString(in);
		userId = WritableUtils.readString(in);
		requestDate = new Date(WritableUtils.readVLong(in));
		requestInfo = WritableUtils.readString(in);
		status = WritableUtils.readVInt(in);
		size = WritableUtils.readString(in);
	}

	@Override
	public String toString() {
		return localhost + separator + identd + separator + userId + separator
				+ sdf.format(requestDate) + separator + requestInfo + separator
				+ status + separator + size;
	}
}