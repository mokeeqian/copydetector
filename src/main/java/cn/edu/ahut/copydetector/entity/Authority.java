/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 14:14
 * @description
 */
@Data
public class Authority implements Serializable {
	private Integer id;
	private String name;
	private String tag;
}
