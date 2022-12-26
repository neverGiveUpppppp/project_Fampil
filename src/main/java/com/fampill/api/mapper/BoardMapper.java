package com.fampill.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fampill.api.controller.form.PillSaveForm;
import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;
import com.fampill.api.domain.Condition;
import com.fampill.api.domain.Fampill;
import com.fampill.api.domain.FampillTake;
import com.fampill.api.domain.FampillTime;

public interface BoardMapper {
	
	List<Fampill> selectRegPillList(String userId);
	
	List<Fampill> selectSearchList(String searchWord);
	
	void insertPill(PillSaveForm form);
	
	PillSaveForm selectPillTimeInfo(String userId);
	
	void insertTime(FampillTime pillTime);

	String selectPillRegNo(String userId);

	void selectPillTimeInfo(PillSaveForm form);

	Fampill selectDatail(int pillNo);

	void insertPillTake(FampillTake pillTake);
	
	void deletePillTakeHis(FampillTake pillTake);

	int selectPillTake(FampillTake pillTake);

	//삭제시 마스터에서 삭제
	void deletePillMaster(int regNo);
	//삭제시 t_pill_time에서도 같이 삭제
	void deletePillTime(int regNo);
	//삭제시 t_pill_take에서도 같이 삭제
	void deletePillTake(int regNo);
	
	int selectCondition(Map<String, Object> params);
	
	void deleteCondition(Map<String, Object> params);

	void insertCondition(Map<String, Object> params);

	List<PillSaveForm> selectMyList(String userId);
	
	//약섭취리스트가져오기
	List<FampillTime> selectPillTimeList(Map<String, Object> params);
	
	int selectPillcountDay(Map<String, Object> day);

	int selectMyCondition(Map<String, Object> params);
	
	Fampill selectPill(int pillNo);

	/////////////////////////////////////////////
	List<Board> selectBoardList(@Param("boardType") BoardType boardType, 
			@Param("query") String query);
		
	Board selectBoard(int boardSeq);
	
	void insertBoard(Board board);
	
	void deleteBoard(int boardSeq);

	void updateBoard(Board board);

	

	
}
