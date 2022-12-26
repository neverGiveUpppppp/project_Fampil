package com.fampill.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class HomeController {

	@GetMapping("/")
	@ApiOperation(value = "홈", hidden = true)
	public String home() {
		return "redirect:/swagger-ui/index.html";
	}
}
