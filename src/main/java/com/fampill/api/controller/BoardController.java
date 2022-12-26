package com.fampill.api.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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

import com.fampill.api.annotation.RequestConfig;
import com.fampill.api.controller.form.BoardSaveForm;
import com.fampill.api.domain.Board;
import com.fampill.api.domain.BoardType;
import com.fampill.api.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	private final BoardService boardService;
	
	/**
	 * 게시물 목록 조회 및 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardType}")
	@RequestConfig(menu = "BOARD")
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
	@GetMapping("/form-body")
	@RequestConfig(menu = "BOARD")
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
	@PostMapping("/save")
	@RequestConfig(menu = "BOARD")
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
	@PostMapping("/update")
	@RequestConfig(menu = "BOARD")
	public String update(@Validated BoardSaveForm form) {
		boardService.update(form);
		// 상세화면으로 이동
		return "redirect:/board/" + form.getBoardType().name() + "/" + form.getBoardSeq();
	}
	
	/**
	 * 게시물 등록/저장 요청 처리 (Client body에 json으로 받기)
	 * @param model
	 * @return
	 */
	@PostMapping("/save-body")
	@RequestConfig(menu = "BOARD")
	@ResponseBody
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
	
	@PostMapping("/delete")
	@RequestConfig(menu = "BOARD")
	@ResponseBody
	public HttpEntity<Boolean> delete(@RequestParam int boardSeq) {
		boardService.deleteBoard(boardSeq);
		return ResponseEntity.ok().build();
	}
	
}
