/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/21 22:58
 * @description 用户角色
 */
@Data
public class Role implements Serializable {

    private Integer id;
    private String name;
    private String detail;

    private List<Authority> authorityList;
}
