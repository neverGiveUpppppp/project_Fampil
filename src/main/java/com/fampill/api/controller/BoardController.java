package com.fampill.api.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fampill.api.annotation.RequestConfig;
import com.fampill.api.controller.form.BoardSaveForm;
import com.fampill.api.controller.form.PillSaveForm;
import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;
import com.fampill.api.domain.Fampill;
import com.fampill.api.domain.FampillTake;
import com.fampill.api.domain.FampillTime;
import com.fampill.api.domain.PillDate;
import com.fampill.api.exception.DefaultException;
import com.fampill.api.response.FampillResponse;
import com.fampill.api.security.SecurityUserDetails;
import com.fampill.api.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author 82103
 *
 */
/**
 * @author 82103
 *
 */
@Api(tags = { "영양제정보 REST API" })
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	private final BoardService boardService;
	
	/**
	 * 오늘날짜 db에서 가져오기
	 * @return
	 */
	@ApiOperation(value = "게시판메인화면")
	@PostMapping("/main")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> selectToday(@ApiIgnore @AuthenticationPrincipal SecurityUserDetails userDetails) {
		log.debug("selectToday userDetails : {}", userDetails);
		Map<String, Object> result = new HashMap<>();
		
		//토큰을 통해 내 정보 받아온다.
		String userId = userDetails.getUsername();
		//내영양제 리스트가 있는지 확인한다
		List<Fampill> registerList = boardService.selectRegPillList(userId);
		
		//오늘기준 일주일 날짜를 보여준다.
		LocalDate now = LocalDate.now().minusDays(7);
		
		List<PillDate> dates = new ArrayList<>();
		
		dates.add(getDate(now));
		
		for (int i = 1; i <= 7; i++) {
			now = now.plusDays(1);
			PillDate date = getDate(now);
			// 현재일자 표시
			if (i == 7) {
				date.setNow(true);
			}
			
			//---영양제섭취일 dday---//
			String countDayFormat1 = now.format(DateTimeFormatter.ofPattern("YYYY-MM-01")); //2022-12-01
			String countDayFormat2 = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")); //2022-12-14
			Map<String, Object> day = new HashMap<>();
			day.put("countDayFormat1", countDayFormat1);
			day.put("countDayFormat2", countDayFormat2);
			day.put("userId", userId);
			
			int dDay = boardService.selectPillcountDay(day);
			date.setCountDay(dDay);
			//---영양제섭취일 dday---//
			
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("regDate", now);
			params.put("weekLabel", now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

			//---영양제 등록내역이 있을경우---//
			if(registerList.size() > 0) { 

				//약섭취시간을 가져온다.
				List<FampillTime> pillTimeList = boardService.selectPillTimeList(params);
				date.setTimeList(pillTimeList);
			}
			
			//컨디션상태를 가져온다
			
			int myCondition;
			
			if(boardService.selectCondition(params) > 0) {
				myCondition = boardService.selectMyCondition(params);
			} else {
				//디폴트 3(좋음)으로 체크
				myCondition = 3;
			}
			
			date.setCondition(myCondition);
			
			dates.add(date);
		}
		
		result.put("dates", dates); //일주일날짜리턴
		log.debug("result : {}", result);
		return new FampillResponse<>(result);
	}
	
	//날짜포맷맞추기
	private PillDate getDate(LocalDate localDate) {
		PillDate date = new PillDate();
		date.setDateLabel(localDate.format(DateTimeFormatter.ofPattern("M월 dd일")));
		date.setDate(localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
		date.setWeek(localDate.getDayOfWeek().name());
		date.setWeekLabel(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
		return date;
	}
	
	/**
	 * 영양제검색
	 * @return
	 */
	@ApiOperation(value = "영양제검색")
	@PostMapping("/search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "searchWord", type = "String", value = "검색단어"),
	})
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> selectSerch(@RequestParam(value = "searchWord" , required = false) String searchWord){
		
		Map<String, Object> result = new HashMap<>();
		List<Fampill> SearchList = boardService.selectSearchList(searchWord);
		
		//검색결과갯수
		result.put("searchsize", SearchList.size());
		
		//검색결과목록
		result.put("searchWord", SearchList);

		return new FampillResponse<>(result);
	}
	
	/**
	 * 영양제 등록화면
	 * @param pillNo
	 * @return
	 */
	@ApiOperation(value = "영양제 등록화면")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pillNo", type = "int", value = "영양제번호"),
	})
	@GetMapping("/form/{pillNo}")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> selectPill(@PathVariable(required = true) int pillNo) {

		Map<String, Object> result = new HashMap<>();
		result.put("pillInfo", boardService.selectPill(pillNo));
		return new FampillResponse<>(result);
	}

	/**
	 * 영양제디테일화면
	 * @return
	 */
	@ApiOperation(value = "영양제상세화면")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pillNo", type = "int", value = "영양제번호"),
	})	
	@GetMapping("/detail/{pillNo}")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> selectDetail(@PathVariable(required = true) int pillNo){
		
		Map<String, Object> result = new HashMap<>();
		result.put("detail", boardService.selectDetail(pillNo));
		return new FampillResponse<>(result);
	}
	
	/**
	 * 내영양제목록
	 * @return
	 */
	@ApiOperation(value = "내영양제목록")
	@PostMapping("/list")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> selectMyList(@ApiIgnore @AuthenticationPrincipal SecurityUserDetails userDetails){
		
		String userId = userDetails.getUsername();
		List<PillSaveForm> SearchList = boardService.selectMyList(userId);
		
		
		Map<String, Object> result = new HashMap<>();

		//검색결과갯수
		result.put("myListSize", SearchList.size());
		
		//검색결과목록
		result.put("myList", SearchList);

		return new FampillResponse<>(result);
	}
	
	/**
	 * 내영양제등록저장
	 * @return
	 */
	@ApiOperation(value = "내영양제등록저장")
	@PostMapping("/save")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> insertPill(@Validated PillSaveForm form, @ApiIgnore @AuthenticationPrincipal SecurityUserDetails userDetails) {
		
		String userId = userDetails.getUsername();
		
		form.setUserId(userId);
		
		//섭취요일 미선택시
		if(form.isMon() == false 
			&& form.isTue() == false
			&& form.isWed() == false
			&& form.isThu() == false
			&& form.isFri() == false
			&& form.isSat() == false
			&& form.isSun() == false) 
		{
			throw new DefaultException("영양제 섭취 요일을 선택해주세요.");
		}
		//섭취시간 미선택시
		if(form.getHour1() == null
				&& form.getHour2() == null
				&& form.getHour3() == null
				) {
			throw new DefaultException("영양제 섭취 시간을 선택해주세요.");
		}
			
		boardService.insertPill(form);
		
		return new FampillResponse<>(HttpStatus.OK);
	}
	
	
	/**
	 * 내영양제섭취이력등록삭제
	 * @param model
	 * @param boardSeq
	 * @return
	 */
	@ApiOperation(value = "내영양제섭취이력등록삭제-이력이 있으면 삭제/없으면 등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regNo", type = "int", value = "영양제등록번호"),
		@ApiImplicitParam(name = "timeNo", type = "int", value = "영양제섭취시간번호"),
		@ApiImplicitParam(name = "regDate", type = "string", value = "섭취시간등록날짜")
	})	
	@PostMapping("/take")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> updateTake(@RequestParam int regNo,@RequestParam int timeNo, @RequestParam String regDate, @ApiIgnore @AuthenticationPrincipal SecurityUserDetails userDetails) {
		
		String userId = userDetails.getUsername();
		
		FampillTake pillTake = new FampillTake();
		pillTake.setRegNo(regNo);
		pillTake.setTimeNo(timeNo);
		pillTake.setUserId(userId);
		pillTake.setRegDate(regDate);
		
		//먼저 오늘 등록이력이 있는지 가져온다.
		int register = boardService.selectPillTake(pillTake);
		
		//등록이력이 있을경우
		if(register > 0) {
			//이력삭제
			boardService.deletePillTakeHis(pillTake);
		} else {
			//이력이 없을 경우 등록
			boardService.insertPillTake(pillTake);
		}

		return new FampillResponse<>(HttpStatus.OK);
	}


	/**
	 * 컨디션상태등록삭제
	 * @param model
	 * @param boardSeq
	 * @return
	 */
	@ApiOperation(value = "컨디션상태등록삭제-이전등록값이 없으면 등록하고 있으면 삭제 후 재등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "status", type = "int", value = "1나쁨,2보통,3좋음"),
		@ApiImplicitParam(name = "regDate", type = "string", value = "컨디션등록날짜")
	})	
	@PostMapping("/condition")
	@RequestConfig(menu = "BOARD")
	public FampillResponse<?> updateCOndition(@RequestParam int status, @RequestParam String regDate, @ApiIgnore @AuthenticationPrincipal SecurityUserDetails userDetails) {
		
		if(status != 1 && status != 2 && status != 3) 
			{
				throw new DefaultException("컨디션 status 상태를 확인해주세요.");
			}
		
		//먼저 오늘 등록이력이 있는지 가져온다.
		String userId = userDetails.getUsername();
		
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("status", status);
		params.put("regDate", regDate);

		
		int register = boardService.selectCondition(params);

		//등록이력이 있을경우
		if(register > 0) {
			//이력삭제
			boardService.deleteCondition(params);
		} else {
			//이력이 없으면 컨디션등록
			boardService.insertCondition(params);
			
		}

		return new FampillResponse<>(HttpStatus.OK);
	}
	
	
	/**
	 * 내영양제삭제
	 * @return
	 */
	@ApiOperation(value = "내영양제삭제")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regNo", type = "int", value = "영양제등록번호"),
	})
	@PostMapping("/delete")
	@RequestConfig(menu = "BOARD")
 	public FampillResponse<?> deletePill(@RequestParam int regNo) {
		
		boardService.deletePill(regNo);

		return new FampillResponse<>(HttpStatus.OK);
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * 게시물 목록 조회 및 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardType}")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String list(Model model, @PathVariable BoardType boardType, 
			@RequestParam(required = false) String query) {
		// 게시물 목록 조회 후 model에 boardList key로 저장
		model.addAttribute("boardList", boardService.selectBoardList(boardType, query));
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "board/list";
	}
	
	/**
	 * 게시물 상세 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardType}/{boardSeq}")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String detail(Model model, @PathVariable BoardType boardType,
			@PathVariable(required = true) int boardSeq) {
		logger.debug("detail");
		Board board = boardService.selectBoard(boardSeq);
		Assert.notNull(board, "게시글 정보가 없습니다.");
		// 게시물 상세정보 set
		model.addAttribute("board", board);
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "board/detail";
	}
	
	/**
	 * 게시물 등록 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardType}/form")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String form(Model model, @PathVariable BoardType boardType) {
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		return "board/form";
	}
	
	/**
	 * 게시물 등록 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/form-body2")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String formBody(Model model) {
		return "board/form-body";
	}
	
	
	/**
	 * 게시물 수정 화면
	 * @param model
	 * @param boardSeq
	 * @return
	 */
	@GetMapping("/{boardType}/edit/{boardSeq}")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String edit(Model model, @PathVariable BoardType boardType, 
			@PathVariable int boardSeq) {
		// 게시물 상세정보 set
		model.addAttribute("board", boardService.selectBoard(boardSeq));
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "/board/form";
	}
	
	/**
	 * 등록 처리
	 * @param form
	 * @return
	 */
	@PostMapping("/save2")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String save(@Validated BoardSaveForm form,
		Authentication authentication) {
		Board board = boardService.save(form, authentication);
		// 목록 화면으로 이동
		return "redirect:/board/" + form.getBoardType().name() + "/" + board.getBoardSeq();
	}
	
	/**
	 * 업데이트 처리
	 * @param form
	 * @return
	 */
	@PostMapping("/update2")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public String update2(@Validated BoardSaveForm form) {
		boardService.update(form);
		// 상세화면으로 이동
		return "redirect:/board/" + form.getBoardType().name() + "/" + form.getBoardSeq();
	}
	
	/**
	 * 게시물 등록/저장 요청 처리 (Client body에 json으로 받기)
	 * @param model
	 * @return
	 */
	@PostMapping("/save-body2")
	@RequestConfig(menu = "BOARD")
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public HttpEntity<Integer> saveBody(@Validated @RequestBody BoardSaveForm form,
			Authentication authentication) {
		Board selectBoard = null;
		// 게시글 수정으로 요청인경우
		if (form.getBoardSeq() > 0) {
			selectBoard = boardService.selectBoard(form.getBoardSeq());
		}
		// 수정인 경우 업데이트
		if (selectBoard != null) {
			boardService.update(form);
		} else {
			boardService.save(form, authentication);
		}
		// 게시물 목록 화면으로 URL 리다렉트
		return new ResponseEntity<Integer>(form.getBoardSeq(), HttpStatus.OK);
	}
	
	@PostMapping("/delete2")
	@RequestConfig(menu = "BOARD")
	@ResponseBody
	@ApiOperation(value = "게시물 목록 조회 및 화면", hidden = true)
	public HttpEntity<Boolean> delete(@RequestParam int boardSeq) {
		boardService.deleteBoard(boardSeq);
		return ResponseEntity.ok().build();
	}
	
}
