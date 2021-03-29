package com.project.ezkit.board;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

	void insertBoard(Board b);
	
	// 게시글 전체 검색
	List<Board> selectBoardAll();
	
	// 게시글 번호로 검색
	List<Board> selectBoardByBoardNum(int board_num);
	
	// 게시글 user_id로 검색
	List<Board> selectBoardByUserId(String board_writer_id);
	
	// 게시글 검색어로 검색
	List<Board>	selectBoardBySearchKeyword(String search_keyword);
	
	// 게시글 수정
	void updateBoard(Board b);
	
	// 조회수
	void boardHits(int board_num);
	
	// 게시글 삭제
	void deleteBoard(Map map);
	
	
}
