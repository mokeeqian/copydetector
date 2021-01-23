/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import javax.persistence.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/21 22:58
 * @description 用户角色
 */
@Entity
@Table(name = "tb_role")
public class Role {

    private Integer id;
    private String rolename;
    private String roledetail;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "rolename")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Column(name = "roledetail")
    public String getRoledetail() {
        return roledetail;
    }

    public void setRoledetail(String roledetail) {
        this.roledetail = roledetail;
    }
}
