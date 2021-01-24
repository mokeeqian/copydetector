
/*
 * Copyright (c) mokeeqian 2021.
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/public")
public class LoginController {

    @Autowired
    UserService userService;

    /**
     * 公共页面映射
     */
    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "hello")
    public String hello() {
        return "认证通过";
    }


    /**
     * 登录接口
     */
    @RequestMapping(value = "/loginCheck")
    public void loginCheck() {
    }

}
