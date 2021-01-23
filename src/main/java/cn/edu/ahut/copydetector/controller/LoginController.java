
/*
 * Copyright (c) mokeeqian 2021.
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
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


    /**
     * 登录接口
     */
    @RequestMapping(value = "/loginCheck")
    @ResponseBody
    public String loginCheck(String username, String password, HttpSession httpSession) {
        String code;
        User user = userService.getUserByUsername(username);
        if ( null != user && password.equals(user.getPassword()) ) {
            httpSession.setAttribute("user", user);
            code = "1";
        } else {
            code = "0";
        }
//        System.out.println(code);
        return code;
    }

}
