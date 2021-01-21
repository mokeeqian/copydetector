/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import javax.persistence.Entity;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/21 22:27
 * @description
 */
@Entity
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Integer role;


}
