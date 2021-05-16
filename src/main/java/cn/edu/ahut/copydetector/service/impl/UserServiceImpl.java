/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.constant.BasicConstant;
import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.dao.AuthorityDao;
import cn.edu.ahut.copydetector.dao.RoleDao;
import cn.edu.ahut.copydetector.dao.UserDao;
import cn.edu.ahut.copydetector.entity.Authority;
import cn.edu.ahut.copydetector.entity.PageBean;
import cn.edu.ahut.copydetector.entity.Role;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.FileService;
import cn.edu.ahut.copydetector.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/22 20:00
 * @description
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private AuthorityDao authorityDao;
    private FileService fileService;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao, AuthorityDao authorityDao, FileService fileService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.authorityDao = authorityDao;
        this.fileService = fileService;
    }



    @Override
    public User selectUserBySort(String type, String keyword){
        HashMap<String, Object> pageMap = new HashMap<>(2);
        if (BasicConstant.User.USERNAME.getString().equals(type)){
            User user = userDao.selectUserByUsername(keyword);
            return user;
        }else if (BasicConstant.User.REAL_NAME.getString().equals(type)){
            User user = userDao.selectUserByRealname(keyword);
            return user;
        }else {
            return new User();
        }
    }

    @Override
    public HashMap<String, Object> selectUser(String username) {
        User user = userDao.selectUserByUsername(username);
        List<Role> role = roleDao.selectRole(username);
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public List<User> selectUsersByIds(List<Integer> ids) {
        return userDao.selectByIds(ids);
    }

    @Override
    public User selectUserById(Integer id) {
        return userDao.selectUserById(id);
    }

    @Override
    public HashMap<String, Object> selectUserByRealname(String realname) {
        User user = userDao.selectUserByRealname("%"+realname+"%");
        List<Role> role = roleDao.selectRole(user.getUsername());
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public List<Authority> selectAuthoritiesByUsername(String username) {
        return authorityDao.selectAuthoritiesByUsername(username);
    }

    @Override
    public Role selectRoleByUsername(String username) {
        //用为角色和用户逻辑上是多对多关系，但此系统暂时是一对一，数据库即持久层的设计遵从多对多
        return roleDao.selectRole(username).get(0);
    }

    @Override
    public List<Role> selectAllRole() {
        return roleDao.selectAllRole();
    }

    @Override
    public List<Authority> selectAllAuthority() {
        return authorityDao.selectAllAuthotities();
    }

    @Override
    public PageBean<User> selectUsersByRole(Integer currentPage, Integer pageSize,
                                            Integer roleId, HashMap<String,String> keywordMap) {
        HashMap<String, Object> pageMap = new HashMap<>();
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageMap.put("start", (currentPage - 1) * pageSize);
        pageMap.put("limit", pageBean.getPageSize());
        if (roleId != null) {
            pageMap.put("roleId", roleId);
        }
        if (keywordMap.size() > 0){
            Set<String> set = keywordMap.keySet();
            for (String key : set){
                pageMap.put(key, keywordMap.get(key));
            }
        }
        List<User> users = userDao.selectUsersByRole(pageMap);
        pageBean.setList(users);
        int count = 0;
        if(roleId != null){
            count = userDao.countCurrentUsers(roleId);
            pageBean.setTotalRecord(count);
        }else {
            count = userDao.countUsers();
            pageBean.setTotalRecord(count);
        }
        if (count % pageSize == 0) {
            pageBean.setTotalPage(count / pageSize);
        } else {
            pageBean.setTotalPage((count / pageSize) + 1);
        }
        return pageBean;
    }

    @Override
    public List<User> selectUsersByDepartment(String department) {
        return userDao.selectUsersByDepartment(department);
    }

    @Override
    public List<User> selectUsersByMajor(String major) {
        return userDao.selectUsersByMajor(major);
    }

    @Override
    public List<User> selectAllStudents() {
        return userDao.selectAllStudents();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //先根据用户名查用户
        User user = userDao.selectUserByUsername(username);
        if (user != null) {
            //再根据用户名查角色
            List<Role> roles = roleDao.selectRole(username);
            List<Authority> authorities = new ArrayList<>();
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (Role role : roles) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(grantedAuthority);

                authorities = role.getAuthorityList();
                for (Authority authority : authorities) {
                    grantedAuthority = new SimpleGrantedAuthority(authority.getTag());
                    authorityList.add(grantedAuthority);
                }
            }
            //更新最后登录时间
            user.setLastLoginTime(OtherConstant.DATE_FORMAT.format(new Date()));
            List<User> users = new ArrayList<>();
            users.add(user);
            userDao.updateUsers(users);
            //把该用户的角色和权限放User对象
            user.setAuthorities(authorityList);
            return user;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public int countUsers() {
        return userDao.countUsers();
    }

    @Override
    public HashMap<String, Object> addUsersByExcel(List<User> users, Integer roleId) {
        HashMap<String, Object> res = new HashMap<>(2);
        if (users.size() == 0){
            log.info("用户集合为空,请检查参数是否正确输入");
            return res;
        }else {
            //这里先把待添加的用户做一次遍历，若已有相同用户记录则移至另一用户集合existUsers，后面一起返回
            Iterator<User> userIterator = users.iterator();
            List<String> oldRecord = userDao.checkUsernames();
            List<User> existUsers = new ArrayList<>();
            while (userIterator.hasNext()) {
                User tmp = userIterator.next();
                int isExist = Collections.binarySearch(oldRecord, tmp.getUsername());
                if (isExist >= 0) {
                    existUsers.add(tmp);
                    userIterator.remove();
                }
            }
            //若检查完待添加的用户集合已空，则直接返回existUsers
            if (users.size() == 0) {
                res.put("exist",existUsers);
                return res;
            } else {
                //开始就传进来的角色编号roleId做添加操作
                List<Integer> ids = new ArrayList<>();
                if (roleId == DatabaseConstant.Role.ROLE_TEACHER.ordinal()+1) {
                    //初始化密码和日期
                    for (User user : users) {
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    //用户表添加操作,获得自增长返回的id
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                        // 创建教师用户的同时，新建一个教师文件夹!!!
                        int fileRes = fileService.newTeacherFile(user.getUsername(), user.getRealname(), user.getId());
                        if (fileRes != 1) {
                            log.info("教师-" + user.getRealname() + "-文件夹已存在");
                        }
                    }
                    //中间表添加操作
                    Integer midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    if (existUsers.size() == 0) {
                        return res;
                    } else {
                        res.put("exist", existUsers);
                        for (User user : existUsers) {
                            log.error("教师-" + user.getRealname() + "-注册失败，已有相同记录");
                        }
                        return res;
                    }
                } else if (roleId == DatabaseConstant.Role.ROLE_STUDENT.ordinal()+1) {
                    //初始化密码和日期
                    for (User user : users) {
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    //用户表添加操作,获得自增长返回的id
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                    }
                    //中间表添加操作
                    Integer midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    if (existUsers.size() == 0) {
                        return res;
                    } else {
                        res.put("exist", existUsers);
                        for (User user : existUsers) {
                            log.error("学生-" + user.getRealname() + "-注册失败，已有相同记录");
                        }
                        return res;
                    }
                } else {
                    for (User user : users) {
                        user.setPassword(new BCryptPasswordEncoder().encode("root"));
                        user.setRealname("系统管理员");
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                    }
                    Integer midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    return res;
                }
            }
        }
    }

    @Override
    public int deleteUsers(List<Integer> ids) {
        int delUserRes = userDao.deleteUsers(ids);
        int delRoleRes = userDao.deleteUserRole(ids);
        return (delUserRes + delRoleRes);
    }

    @Override
    public int updateUsers(List<User> users) {
        return userDao.updateUsers(users);
    }

    @Override
    public int updatePassword(String newPassword, String oldPassword, User oldUser) {
        int res = 0;
        String encodedPassword = userDao.selectUserByUsername(oldUser.getUsername()).getPassword();
        if (BCryptPasswordEncoder().matches(oldPassword, encodedPassword)){
            User user = new User();
            user.setId(oldUser.getId());
            user.setPassword(BCryptPasswordEncoder().encode(newPassword));
            res = userDao.updatePassword(user);
        }else {
            res = -1;
        }
        return res;
    }

    private static BCryptPasswordEncoder BCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
