package com.fampill.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;

public interface BoardMapper {

	List<Board> selectBoardList(@Param("boardType") BoardType boardType, 
			@Param("query") String query);
		
	Board selectBoard(int boardSeq);
	
	void insertBoard(Board board);
	
	void deleteBoard(int boardSeq);

	void updateBoard(Board board);
	
}
