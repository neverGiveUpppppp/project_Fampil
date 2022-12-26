package com.fampill.api.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fampill.api.controller.form.BoardSaveForm;
import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;
import com.fampill.api.mapper.BoardMapper;
import com.fampill.api.security.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardMapper boardMapper;
	
	/**
	 * 게시물 목록 조회 후 리턴
	 * @return
	 */
	public List<Board> selectBoardList(BoardType boardType, String query) {
		return boardMapper.selectBoardList(boardType, query);
	}
	
	public Board selectBoard(int boardSeq) {
		return boardMapper.selectBoard(boardSeq);
	}
	
	public Board save(BoardSaveForm form, Authentication authentication) {
		SecurityUserDetails details = (SecurityUserDetails) 
			authentication.getPrincipal();
		Board board = new Board();
		board.setBoardSeq(form.getBoardSeq());	
		board.setBoardType(form.getBoardType());
		board.setTitle(form.getTitle());
		board.setContents(form.getContents());
		board.setUserName(details.getNickname());
		board.setMemberSeq(details.getMemberSeq());
		boardMapper.insertBoard(board);
		return board;
	}
	
	public void update(BoardSaveForm form) {
		Board board = new Board();
		board.setBoardSeq(form.getBoardSeq());	
		board.setBoardType(form.getBoardType());
		board.setTitle(form.getTitle());
		board.setContents(form.getContents());
		boardMapper.updateBoard(board);
	}
	
	public void deleteBoard(int boardSeq) {
		boardMapper.deleteBoard(boardSeq);
	}

}
