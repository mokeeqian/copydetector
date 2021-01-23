/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 13:51
 * @description 管理员映射
 */
@Controller
public class AdminController {


	@RequestMapping(value = "/admin/index")
	public String index() {
		return "/admin/index";
	}


}
