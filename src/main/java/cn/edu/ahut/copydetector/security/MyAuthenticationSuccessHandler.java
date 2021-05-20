/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.security;

import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 13:47
 * @description 认证成功的一些处理
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication)
			throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		String role = userService.selectRoleByUsername(user.getUsername()).getName();

		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){

			 //当前登录用户
			request.getSession().setAttribute("user", user);

			//由前端需要判断是否是重定向
			response.setHeader("REDIRECT", "true");
			/**
			 * 根据角色做页面跳转
			 */
			if (DatabaseConstant.Role.ROLE_ADMIN.getRole().equals(role)) {
				response.setHeader("CONTENTPATH", "/admin/index");
			} else if (DatabaseConstant.Role.ROLE_TEACHER.getRole().equals(role)) {
				response.setHeader("CONTENTPATH", "/teacher/index");
			} else if (DatabaseConstant.Role.ROLE_STUDENT.getRole().equals(role)) {
				response.setHeader("CONTENTPATH", "/student/index");
			}
		} else{
			/**
			 * 否则，则拦截，强制用户登录
			 */
			response.sendRedirect("/public/login");
		}
	}
}
