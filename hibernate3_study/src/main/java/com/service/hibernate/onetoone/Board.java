package com.service.hibernate.onetoone;

import java.util.Date;

public class Board {
	private int boardId;
	private String title;
	private String userName;
	private Date writeDate = new Date();
	private BoardDetail boardDetail;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public BoardDetail getBoardDetail() {
		return boardDetail;
	}

	public void setBoardDetail(BoardDetail boardDetail) {
		this.boardDetail = boardDetail;
	}
}
