/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;


import lombok.Data;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/21 23:05
 * @description 用户权限映射表
 */
@Data
public class UserRole {
    private Integer id;
    private Integer userid;
    private Integer roleid;

}
