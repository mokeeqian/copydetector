
/*
 * Copyright (c) mokeeqian 2021.
 */

package cn.edu.ahut.copydetector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/public")
public class LoginController {

    /**
     * 公共页面映射
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }


    /**
     * 登录接口
     */
    @RequestMapping("/loginCheck")
    @ResponseBody
    public void loginCheck(String username, String password, HttpSession httpSession) {
        String code;

    }

}
