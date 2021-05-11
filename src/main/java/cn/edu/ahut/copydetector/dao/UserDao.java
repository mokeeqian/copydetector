/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.Role;
import cn.edu.ahut.copydetector.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface UserDao {

	/**
	 * 搜索出数据库里所有账号进行判断操作
	 * @return
	 */
	List<String> checkUsernames();

	/**
	 * 查询最后一个用户的id
	 * @return
	 */
	Integer selectLastId();

	/**
	 * 根据用户名搜索用户
	 * @param username
	 * @return
	 */
	User selectUserByUsername(String username);

	/**
	 * 根据姓名查询用户（模糊查询）
	 * @param realname
	 * @return
	 */
	User selectUserByRealname(String realname);

	/**
	 * 根据系别搜索用户
	 * @param department
	 * @return
	 */
	List<User> selectUsersByDepartment(String department);

	/**
	 * 根据专业搜索用户(教师无专业字段)
	 * @param major
	 * @return
	 */
	List<User> selectUsersByMajor(String major);

	/**
	 * id搜索用户s
	 * @param id
	 * @return
	 */
	List<User> selectByIds(List<Integer> id);

	/**
	 * id搜索用户
	 * @param id
	 * @return User
	 */
	User selectUserById(Integer id);


	/**
	 * 根据角色搜索用户群（用户与角色n:n）
	 * @param param
	 * @return
	 */
	List<User> selectUsersByRole(HashMap<String, Object> param);

	/**
	 * 用户总记录数
	 * @return
	 */
	int countUsers();

	/**
	 * 学生/教师总记录数
	 * @param role
	 * @return
	 */
	int countCurrentUsers(Integer role);

	/**
	 * 批量增加用户
	 * @param users
	 * @return
	 */
	Integer addUsers(List<User> users);

	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 */
	int deleteUsers(List<Integer> ids);

	/**
	 * 批量更新用户
	 * @param users
	 * @return
	 */
	int updateUsers(List<User> users);

	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	int updatePassword(User user);

	/**
	 * 批量添加用户角色中间表记录（用户与角色n:n）
	 * @param userId
	 * @param roleId
	 * @return
	 */
	int addUserRole(@Param("userId") List<Integer> userId, @Param("roleId") Integer roleId);

	/**
	 * 批量删除用户角色中间表记录（用户与角色n:n）
	 * @param userIds
	 * @return
	 */
	int deleteUserRole(List<Integer> userIds);

	/**
	 * 更新用户角色中间表记录（用户与角色n:n）
	 * @param userId
	 * @param roleId
	 * @return
	 */
	int updateUserRole(@Param("userId")Integer userId, @Param("roleId")Integer roleId);
}
