/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.dao.UserDao;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/22 20:00
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsernameEquals(username);
    }
}
