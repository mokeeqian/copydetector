/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AuthorityDao {
	/**
	 * 用户名搜索用户的全部权限，用于登录认证
	 * @param username
	 * @return
	 */
	List<Authority> selectAuthoritiesByUsername(String username);

	/**
	 * 所有权限
	 * @return
	 */
	List<Authority> selectAllAuthotities();
}
