
/*
 * Copyright (c) mokeeqian 2021.
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping("/forbiddenError")
    public String forbiddenError(){
        return "forbiddenError";
    }
    @RequestMapping("/serverError")
    public String serverError(){
        return "servererror";
    }
    @RequestMapping("/notFoundError")
    public String notFoundError(){
        return "notFoundError";
    }
    @RequestMapping("/unavailableError")
    public String unavailableError(){
        return "unavailableError";
    }
    @RequestMapping("/norError")
    public String norError(){
        return "error";
    }

    /**
     * 登录接口,权限认证交给后端spring security处理
     * controller这里只做路由映射
     */
    @PostMapping(value = "/loginCheck")
    @ResponseBody
    public void loginCheck() {
    }

}
