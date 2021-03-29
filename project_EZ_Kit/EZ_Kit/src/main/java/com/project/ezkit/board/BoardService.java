package com.project.ezkit.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
	
	@Autowired
	private BoardMapper mapper;
	
	public void insertBoard(Board b) {
		mapper.insertBoard(b);
	}
	
	public List<Board> selectBoardAll() {
		return mapper.selectBoardAll();
	}
	
	public List<Board> selectBoardByBoardNum(int board_num) {
		return mapper.selectBoardByBoardNum(board_num);
	}
	
	public List<Board> selectBoardByUserId(String board_writer_id) {
		return mapper.selectBoardByUserId(board_writer_id);
	}
	
	public List<Board>	selectBoardBySearchKeyword(String search_keyword) {
		return mapper.selectBoardBySearchKeyword(search_keyword);
	}
	
	public void updateBoard(Board b) {
		mapper.updateBoard(b);
	}
	
	public void boardHits(int board_num) {
		mapper.boardHits(board_num);
	}
	
	public void deleteBoard(String board_writer_id, int board_num) {
		Map map = new HashMap();
		map.put("board_writer_id", board_writer_id);
		map.put("board_num", board_num);
		mapper.deleteBoard(map);
	}
}
