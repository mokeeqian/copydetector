/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleDao {
	/**
	 * 用户名查角色，多对多包括权限
	 * @param username
	 * @return
	 */
	List<Role> selectRole(String username);

	/**
	 * 所有角色
	 * @return
	 */
	List<Role> selectAllRole();
}
