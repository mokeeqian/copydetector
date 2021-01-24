/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.security;

import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 14:40
 * @description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private MyAuthenticationSuccessHandler successHandler;
	private MyAuthenticationFailureHandler failureHandler;

	public SecurityConfig(UserService userService,
						  MyAuthenticationSuccessHandler successHandler,
						  MyAuthenticationFailureHandler failureHandler) {
		this.userService = userService;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				/**
				 * 1. 多URL的坑: 规则是按顺序的，如果把"/**"写一开始，那么后面的"/a/**"将不会生效
				 * 2. "?"代表单字符，"*"代表0到多个字符，"**"代表0到多个目录
				 * 3. 关于Role和Authority: 如果要hasRole判断，在loadUserByUsername方法中放入List<GrantedAuthority>
				 *    中就需要"ROLE_xx"; 如果要hasAuthority判断，则不用"ROLE_"前缀； 因为这个写一点血的教训，曾因为数据
				 *    库role表的字段没加"ROLE_"前缀导致一直用不了hasRole方法，反而是permission表的字段没有限制
				 */
				.antMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")
				.antMatchers("/teacher/**").hasAnyRole("TEACHER","ADMIN")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/public/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/public/login")
				.loginProcessingUrl("/public/loginCheck")
				.failureHandler(failureHandler)
				.successHandler(successHandler)
				.permitAll()
				.and()
				.logout()
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/public/login")
				.permitAll()
				.and()
				//开放给前端的页面<iframe>引用
				.headers().frameOptions().sameOrigin()
				.and()
				.rememberMe()       //FIXME: 有点问题，无法记住用户，待维护
				.tokenValiditySeconds(60*60*24)
				.and()
				.csrf().disable()
				.sessionManagement()
				.maximumSessions(1)
				.expiredUrl("/public/login");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/X-admin/css/**",
				"/X-admin/fonts/**",
				"/X-admin/images/**",
				"/X-admin/js/**",
				"/X-admin/lib/**",
				"/layui/**",
				"/pdf/**"
		);
	}
}
