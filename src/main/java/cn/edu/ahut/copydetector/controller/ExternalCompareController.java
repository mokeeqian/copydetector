/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.service.ExternalCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/4/23 14:37
 * @description 扩展工具查重服务
 */
@Controller
@RequestMapping(value = "externalCompare", method = {RequestMethod.GET, RequestMethod.POST},
produces = "application/json;charset=utf-8")
public class ExternalCompareController {
	private ExternalCompareService externalCompareService;

	@Autowired
	public ExternalCompareController(ExternalCompareService externalCompareService) {
		this.externalCompareService = externalCompareService;
	}

	@GetMapping(value="jplag")
	@ResponseBody
	public String[] jplag(@RequestParam String lang, @RequestParam int simValue,
						  HttpServletRequest httpServletRequest) {
		return externalCompareService.jplag(lang, simValue, httpServletRequest.getSession().getId());
	}

}
