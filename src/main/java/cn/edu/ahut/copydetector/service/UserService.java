/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.entity.Authority;
import cn.edu.ahut.copydetector.entity.PageBean;
import cn.edu.ahut.copydetector.entity.Role;
import cn.edu.ahut.copydetector.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * 分页查询用户表
     */
    User selectUserBySort(String type, String keyword);

    /**
     * id搜索用户群
     */
    List<User> selectUsersByIds(List<Integer> ids);

    /**
     * 通过用户名查询，返回用户以及角色信息
     */
    HashMap<String, Object> selectUser(String username);

    /**
     * 通过姓名模糊查询，返回用户以及角色信息
     */
    HashMap<String, Object> selectUserByRealname(String realname);

    /**
     * 返回用户的权限，为框架服务
     */
    List<Authority> selectAuthoritiesByUsername(String username);
    Role selectRoleByUsername(String username);
    List<Role> selectAllRole();
    List<Authority> selectAllAuthority();

    /**
     * 三个如同名字的方法
     */
    PageBean<User> selectUsersByRole(Integer currentPage, Integer pageSize, Integer roleId, HashMap<String,String> keywordMap);
    List<User> selectUsersByDepartment(String department);
    List<User> selectUsersByMajor(String major);

    /**
     * 继承UserDetailsService的方法
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 用户表总记录数
     */
    int countUsers();

    /**
     * 批量录入用户，用户已存在的会不录入并返回，包括账户已存在和教师文件夹，其他人录入
     */
//    HashMap<String, Object> addUsersByExcel(List<User> users, Integer roleId);

    /**
     * 批量删除用户
     */
    int deleteUsers(List<Integer> ids);

    /**
     * 批量更新用户
     */
    int updateUsers(List<User> users);

    /**
     * 修改密码
     */
    int updatePassword(String newPassword, String oldPassword, User oldUser);
}

