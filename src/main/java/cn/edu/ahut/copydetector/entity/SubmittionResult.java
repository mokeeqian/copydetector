/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

@Data
public class SubmittionResult {
    private String username;
    private String realname;

    public SubmittionResult(String username, String realname) {
        this.username = username;
        this.realname = realname;
    }
}
