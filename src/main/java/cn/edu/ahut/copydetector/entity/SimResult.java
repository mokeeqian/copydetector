/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;


import lombok.Data;

import java.io.Serializable;


@Data
public class SimResult implements Serializable {

    private Integer id;
    private String user1;
    private String user2;
    private Double sim;

    public SimResult(String user1, String user2, Double sim) {
        this.user1 = user1;
        this.user2 = user2;
        this.sim = sim;
    }
}
