/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.config;

import cn.edu.ahut.copydetector.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 13:54
 * @description web资源拦截配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration =registry.addInterceptor(new LoginInterceptor());
		registration.addPathPatterns("/**");
		registration.excludePathPatterns(
				"/login",			// 开放登录页面
				"/loginCheck",		// 开放登录接口
				"/X-admin/**"		// 开放样式文件
		);
	}
}
