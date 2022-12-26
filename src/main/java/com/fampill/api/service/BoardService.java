package com.fampill.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fampill.api.controller.form.BoardSaveForm;
import com.fampill.api.controller.form.PillSaveForm;
import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;
import com.fampill.api.domain.Condition;
import com.fampill.api.domain.Fampill;
import com.fampill.api.domain.FampillTake;
import com.fampill.api.domain.FampillTime;
import com.fampill.api.mapper.BoardMapper;
import com.fampill.api.security.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

/**
 * @author 82103
 *
 */
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardMapper boardMapper;
	
	/**
	 * 게시물 목록 조회 후 리턴
	 * @return
	 */
	public List<Fampill> selectRegPillList(String userId) {
		// TODO Auto-generated method stub
		return boardMapper.selectRegPillList(userId);
	}
	
	
	/**
	 * 영양제 검색
	 * @param searchWord
	 * @return
	 */
	public List<Fampill> selectSearchList(String searchWord) {
		return boardMapper.selectSearchList(searchWord);
		
	}
	
	public List<PillSaveForm> selectMyList(String userId) {
		return boardMapper.selectMyList(userId);
	}

	
	/**
	 * 영양제 등록화면
	 * @param pillNo
	 * @return
	 */
	public Fampill selectPill(int pillNo) {
		return boardMapper.selectPill(pillNo);
	}

	
	/**
	 * 영양제 상세화면
	 * @param pillNo
	 * @return
	 */
	public Fampill selectDetail(int pillNo) {
		return boardMapper.selectDatail(pillNo);
	}
	
	
	public void insertPill(PillSaveForm form/*,Authentication authentication*/) {
/*
		 SecurityUserDetails details = (SecurityUserDetails)
		 authentication.getPrincipal();
		 form.setUserId(details.getUserId());
*/
		//master테이블에 먼저 저장
		boardMapper.insertPill(form);
		
		//마스터 저장후 T_PILL_TIME 테이블에 시간만 다시 저장
		PillSaveForm time = boardMapper.selectPillTimeInfo(form.getUserId());
		       
		FampillTime pillTime = new FampillTime();
		pillTime.setRegNo(time.getRegNo());
		pillTime.setUserId(time.getUserId());
		pillTime.setPillNo(time.getPillNo());
		pillTime.setHourtype(time.isHourtype1());
		pillTime.setHour(time.getHour1());
		pillTime.setMinute(time.getMinute1());
		pillTime.setCapsule(time.getCapsule1());
		boardMapper.insertTime(pillTime);
		
		if(time.getHour2() != null && time.getHour2().length() != 0 ) {
			FampillTime pillTime2 = new FampillTime();
			pillTime2.setRegNo(time.getRegNo());
			pillTime2.setUserId(time.getUserId());
			pillTime2.setPillNo(time.getPillNo());
			pillTime2.setHourtype(time.isHourtype2());
			pillTime2.setHour(time.getHour2());
			pillTime2.setMinute(time.getMinute2());
			pillTime2.setCapsule(time.getCapsule2());
			
			boardMapper.insertTime(pillTime2);
		}
		if(time.getHour3() != null && time.getHour3().length() != 0) {
			FampillTime pillTime3 = new FampillTime();
			pillTime3.setRegNo(time.getRegNo());
			pillTime3.setUserId(time.getUserId());
			pillTime3.setPillNo(time.getPillNo());
			pillTime3.setHourtype(time.isHourtype3());
			pillTime3.setHour(time.getHour3());
			pillTime3.setMinute(time.getMinute3());
			pillTime3.setCapsule(time.getCapsule3());
			
			boardMapper.insertTime(pillTime3);
		}
	}

	public void insertPillTake(FampillTake pillTake) {
		boardMapper.insertPillTake(pillTake);
		
	}
	
	public void deletePillTakeHis(FampillTake pillTake) {
		boardMapper.deletePillTakeHis(pillTake);
		
	}

	public int selectPillTake(FampillTake pillTake) {
		// TODO Auto-generated method stub
		return boardMapper.selectPillTake(pillTake);
	}

	public void deletePill(int regNo) {
		boardMapper.deletePillMaster(regNo);
		boardMapper.deletePillTime(regNo);
		boardMapper.deletePillTake(regNo);
	}
	
	public int selectCondition(Map<String, Object> params) {
		return boardMapper.selectCondition(params);
	}
	
	public void deleteCondition(Map<String, Object> params) {
		boardMapper.deleteCondition(params);
	}
	
	public void insertCondition(Map<String, Object> params) {
		boardMapper.insertCondition(params);
		
	}
	public List<FampillTime> selectPillTimeList(Map<String, Object> params) {
		return boardMapper.selectPillTimeList(params);
	}

	public int selectPillcountDay(Map<String, Object> day) {
		// TODO Auto-generated method stub
		return boardMapper.selectPillcountDay(day);
	}
	
	public int selectMyCondition(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return boardMapper.selectMyCondition(params);
	}

	
	/////////////////////////////////////////////////

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
		board.setUserName(details.getUsername());
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
