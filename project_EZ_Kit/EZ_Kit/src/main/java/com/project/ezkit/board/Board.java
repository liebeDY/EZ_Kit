package com.project.ezkit.board;

import java.util.Date;

public class Board {

	private int board_num;
	private String board_title;
	private String board_content;
	private Date board_date;
	private int board_hits;
	private String board_writer_id;
	
	public Board() {
		super();
	}

	public Board(int board_num, String board_title, String board_content, Date board_date, int board_hits,
			String board_writer_id) {
		super();
		this.board_num = board_num;
		this.board_title = board_title;
		this.board_content = board_content;
		this.board_date = board_date;
		this.board_hits = board_hits;
		this.board_writer_id = board_writer_id;
	}

	public int getBoard_num() {
		return board_num;
	}

	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}

	public String getBoard_title() {
		return board_title;
	}

	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}

	public String getBoard_content() {
		return board_content;
	}

	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}

	public Date getBoard_date() {
		return board_date;
	}

	public void setBoard_date(Date board_date) {
		this.board_date = board_date;
	}

	public int getBoard_hits() {
		return board_hits;
	}

	public void setBoard_hits(int board_hits) {
		this.board_hits = board_hits;
	}

	public String getBoard_writer_id() {
		return board_writer_id;
	}

	public void setBoard_writer_id(String board_writer_id) {
		this.board_writer_id = board_writer_id;
	}

	@Override
	public String toString() {
		return "Board [board_num=" + board_num + ", board_title=" + board_title + ", board_content=" + board_content
				+ ", board_date=" + board_date + ", board_hits=" + board_hits + ", board_writer_id=" + board_writer_id
				+ "]";
	}
}
